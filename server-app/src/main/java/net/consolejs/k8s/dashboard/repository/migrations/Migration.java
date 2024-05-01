package net.consolejs.k8s.dashboard.repository.migrations;

import com.mongodb.client.MongoDatabase;

public interface Migration {

    String getName();

    void run(MongoDatabase database);
}
