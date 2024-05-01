package net.consolejs.k8s.dashboard.repository.repositories;

import com.google.gson.Gson;
import net.consolejs.k8s.dashboard.entityview.repository.AbstractDocument;
import org.bson.Document;

public abstract class AbstractRepository<T extends AbstractDocument> implements Repository {
    protected static final Gson GSON = new Gson();

    protected Document getDocument(T document) {
        Document document1 = Document.parse(GSON.toJson(document));
        document1.remove("_id");
        return document1;
    }
}