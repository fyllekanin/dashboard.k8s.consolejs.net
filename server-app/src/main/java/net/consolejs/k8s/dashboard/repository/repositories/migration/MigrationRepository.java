package net.consolejs.k8s.dashboard.repository.repositories.migration;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import net.consolejs.k8s.dashboard.entityview.repository.MigrationDocument;
import net.consolejs.k8s.dashboard.repository.migrations.Migration;
import net.consolejs.k8s.dashboard.repository.migrations.Migration20240430Index;
import net.consolejs.k8s.dashboard.repository.repositories.AbstractRepository;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class MigrationRepository extends AbstractRepository<MigrationDocument> {
    private static final String INDEX_NAME = "name";
    private static final AtomicBoolean myIsDone = new AtomicBoolean(false);
    private static final String COLLECTION = "migrations";
    private static final Logger LOGGER = Logger.getLogger(MigrationRepository.class.getName());

    private final MongoCollection<Document> myCollection;

    public MigrationRepository(MongoDatabase database) {
        myCollection = database.getCollection(COLLECTION);
    }

    private static List<Migration> getMigrations() {
        return List.of(new Migration20240430Index());
    }

    public synchronized void run(MongoDatabase database) {
        if (myIsDone.get()) {
            throw new RuntimeException("Migration is only allowed to be triggered once");
        }
        LOGGER.info("Starting migration");

        for (Migration migration : getMigrations()) {
            if (isMigrationDone(migration.getName())) {
                continue;
            }

            migration.run(database);
            myCollection.insertOne(Document.parse(GSON.toJson(MigrationDocument.newBuilder()
                                                                      .withName(migration.getName())
                                                                      .build())));
        }

        myIsDone.set(true);
        LOGGER.info("Finished migration");
    }

    private boolean isMigrationDone(String name) {
        Bson query = Filters.and(Filters.exists(INDEX_NAME), Filters.eq(INDEX_NAME, name));
        return myCollection.countDocuments(query) > 0;
    }
}
