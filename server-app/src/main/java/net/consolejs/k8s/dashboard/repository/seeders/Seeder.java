package net.consolejs.k8s.dashboard.repository.seeders;

import com.mongodb.client.MongoDatabase;

public interface Seeder {
    String getName();

    void run(MongoDatabase database);
}
