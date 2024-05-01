package net.consolejs.k8s.dashboard.repository.migrations;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import net.consolejs.k8s.dashboard.repository.repositories.user.UserRepository;

public class Migration20240430Index implements Migration {

    public String getName() {
        return "migration-20240430-index";
    }

    public void run(MongoDatabase database) {
        database.getCollection("migrations")
                .createIndex(Indexes.ascending("name"), new IndexOptions().unique(true));
        database.getCollection("seeds")
                .createIndex(Indexes.ascending("name"), new IndexOptions().unique(true));

        database.getCollection(UserRepository.getCollection())
                .createIndex(Indexes.ascending("username"), new IndexOptions().unique(true));
    }
}