package net.consolejs.k8s.dashboard.repository;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import net.consolejs.k8s.dashboard.repository.repositories.AbstractRepository;
import net.consolejs.k8s.dashboard.repository.repositories.Repository;
import net.consolejs.k8s.dashboard.repository.repositories.migration.MigrationRepository;
import net.consolejs.k8s.dashboard.repository.repositories.seed.SeedRepository;
import net.consolejs.k8s.dashboard.repository.repositories.user.UserRepository;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Map;
import java.util.logging.Logger;

@Singleton
public class RepositoryFactory {
    private static final Logger LOGGER = Logger.getLogger(RepositoryFactory.class.getName());
    private Map<Class<?>, Repository> myRepositories;

    @ConfigProperty(name = "MONGODB_DATABASE")
    private String myDatabaseName;
    @ConfigProperty(name = "RUN_MIGRATION_SEED", defaultValue = "false")
    private boolean myRunMigrationSeed;

    @Inject
    private MongoClient myMongoClient;

    @PostConstruct
    public void init() {
        try {
            MongoDatabase myDatabase = myMongoClient.getDatabase(myDatabaseName);

            CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
                    MongoClientSettings.getDefaultCodecRegistry(),
                    CodecRegistries.fromProviders(PojoCodecProvider
                                                          .builder()
                                                          .automatic(true)
                                                          .build()));
            myDatabase.withCodecRegistry(pojoCodecRegistry);

            myRepositories = Map.of(
                    UserRepository.class, new UserRepository(myDatabase));

            if (myRunMigrationSeed) {
                new MigrationRepository(myDatabase).run(myDatabase);
                new SeedRepository(myDatabase).run(myDatabase);
            }
        } catch (Exception e) {
            LOGGER.warning(String.format("Database connection failed: %s", e.getMessage()));
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends AbstractRepository<?>> T of(Class<T> clazz) {
        return (T) myRepositories.getOrDefault(clazz, null);
    }
}
