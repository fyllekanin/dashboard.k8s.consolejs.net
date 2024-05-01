package net.consolejs.k8s.dashboard.repository.repositories.user;

import com.google.gson.Gson;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import net.consolejs.k8s.dashboard.entityview.repository.user.UserDocument;
import net.consolejs.k8s.dashboard.repository.repositories.AbstractRepository;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.logging.Logger;

public class UserRepository extends AbstractRepository<UserDocument> {
    private static final String INDEX_USERNAME = "username";
    private static final String COLLECTION = "users";
    private static final Logger LOGGER = Logger.getLogger(UserRepository.class.getName());
    private static final Gson GSON = new Gson();

    private final MongoCollection<Document> myCollection;

    public UserRepository(MongoDatabase database) {
        myCollection = database.getCollection(COLLECTION);
    }

    public static String getCollection() {
        return COLLECTION;
    }

    public void create(UserDocument document) {
        try {
            myCollection.insertOne(getDocument(document));
        } catch (MongoWriteException exception) {
            LOGGER.severe(String.format("Failed to insert user, username: \"%s\"",
                                        document.getUsername()));
            LOGGER.fine(exception.getMessage());
        }
    }

    public void update(UserDocument document) {
        Bson query = Filters.eq("_id", document.getObjectId());
        try {
            myCollection.updateOne(query, new Document("$set", getDocument(document)));
        } catch (MongoWriteException exception) {
            LOGGER.severe(String.format("Failed to update user, username: \"%s\"",
                                        document.getUsername()));
            LOGGER.fine(exception.getMessage());
        }
    }

    public boolean isUserPresent(String username) {
        Bson query = Filters.and(Filters.exists(INDEX_USERNAME), Filters.eq(INDEX_USERNAME, username));

        return myCollection.countDocuments(query) > 0;
    }

    public UserDocument getByUsername(String username) {
        Bson query = Filters.and(Filters.exists(INDEX_USERNAME), Filters.eq(INDEX_USERNAME, username));
        try {
            Document document = myCollection.find(query)
                    .first();
            return getUserDocument(document);
        } catch (MongoWriteException exception) {
            LOGGER.severe(String.format("Failed to get user, username: \"%s\"",
                                        username));
            LOGGER.fine(exception.getMessage());
            return null;
        }
    }

    public UserDocument getByObjectId(ObjectId objectId) {
        Bson query = Filters.and(Filters.eq("_id", objectId));
        try {
            Document document = myCollection.find(query)
                    .first();
            return getUserDocument(document);
        } catch (MongoWriteException exception) {
            LOGGER.severe(String.format("Failed to get user, objectId: \"%s\"",
                                        objectId));
            LOGGER.fine(exception.getMessage());
            return null;
        }
    }

    public void deleteAll() {
        try {
            myCollection.deleteMany(new Document());
        } catch (MongoWriteException exception) {
            LOGGER.severe("Failed to clear the users collection");
            LOGGER.fine(exception.getMessage());
        }
    }

    private UserDocument getUserDocument(Document document) {
        return GSON.fromJson(GSON.toJson(document), UserDocument.class);
    }
}
