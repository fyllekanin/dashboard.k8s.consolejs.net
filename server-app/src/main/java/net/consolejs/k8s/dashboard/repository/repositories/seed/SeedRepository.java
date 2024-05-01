package net.consolejs.k8s.dashboard.repository.repositories.seed;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import net.consolejs.k8s.dashboard.entityview.repository.SeedDocument;
import net.consolejs.k8s.dashboard.repository.repositories.AbstractRepository;
import net.consolejs.k8s.dashboard.repository.seeders.Seeder;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class SeedRepository extends AbstractRepository<SeedDocument> {
    private static final String INDEX_NAME = "name";
    private static final AtomicBoolean myIsDone = new AtomicBoolean(false);
    private static final String COLLECTION = "seeds";
    private static final Logger LOGGER = Logger.getLogger(SeedRepository.class.getName());

    private final MongoCollection<Document> myCollection;

    public SeedRepository(MongoDatabase database) {
        myCollection = database.getCollection(COLLECTION);
    }

    private static List<Seeder> getSeeds() {
        return List.of();
    }

    public synchronized void run(MongoDatabase database) {
        if (myIsDone.get()) {
            throw new RuntimeException("Seeding is only allowed to be triggered once");
        }
        LOGGER.info("Starting seeding");

        for (Seeder seeder : getSeeds()) {
            if (isSeedDone(seeder.getName())) {
                continue;
            }

            seeder.run(database);
            myCollection.insertOne(Document.parse(GSON.toJson(SeedDocument.newBuilder()
                                                                      .withName(seeder.getName())
                                                                      .build())));
        }

        myIsDone.set(true);
        LOGGER.info("Finished seeding");
    }

    private boolean isSeedDone(String name) {
        Bson query = Filters.and(Filters.exists(INDEX_NAME), Filters.eq(INDEX_NAME, name));
        return myCollection.countDocuments(query) > 0;
    }
}
