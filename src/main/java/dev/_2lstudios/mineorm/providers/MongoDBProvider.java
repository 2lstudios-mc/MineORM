package dev._2lstudios.mineorm.providers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mongodb.BasicDBObject;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;

import org.bson.Document;
import org.bson.types.ObjectId;

import dev._2lstudios.mineorm.repository.FindOption;
import dev._2lstudios.mineorm.utils.MapFactory;

public class MongoDBProvider implements IProvider {

    private MongoClient client;
    private MongoDatabase database;

    /* Database management */
    @Override
    public IProvider connect(String connectionURI) {
        final ConnectionString connectionString = new ConnectionString(connectionURI);
        final MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connectionString)
                .retryWrites(true).build();
        this.client = MongoClients.create(settings);
        this.database = this.client.getDatabase(connectionString.getDatabase());
        return this;
    }

    @Override
    public void disconnect() {
        if (this.client != null) {
            this.client.close();
            this.client = null;
        }
    }

    /* Create */
    @Override
    public String create(String entity, Map<String, Object> props) {
        final MongoCollection<Document> documents = database.getCollection(entity);
        final Document doc = new Document(props);
        final InsertOneResult result = documents.insertOne(doc);
        return result.getInsertedId().asObjectId().getValue().toHexString();
    }

    /* Find */
    @Override
    public List<Map<String, Object>> findMany(final String entity, final Map<String, Object> filter,
            final FindOption options) {
        final MongoCollection<Document> documents = database.getCollection(entity);
        FindIterable<Document> docsIterator = documents.find(new Document(filter));

        if (options.getSortKey() != null) {
            docsIterator = docsIterator.sort(new BasicDBObject(options.getSortKey(), options.getSortOrder()));
        }

        final List<Map<String, Object>> objects = new ArrayList<>();

        docsIterator.forEach((Document doc) -> {
            objects.add(doc);
        });

        return objects;
    }

    @Override
    public Map<String, Object> findOne(String entity, Map<String, Object> filter) {
        final MongoCollection<Document> documents = database.getCollection(entity);
        final Document doc = documents.find(new Document(filter)).first();
        return doc;
    }

    @Override
    public Map<String, Object> findByID(String entity, String id) {
        return this.findOne(entity, MapFactory.create("_id", new ObjectId(id)));
    }

    /* Update */
    @Override
    public long updateMany(final String entity, final Map<String, Object> filter, final Map<String, Object> update) {
        final MongoCollection<Document> documents = database.getCollection(entity);
        final UpdateResult result = documents.updateMany(new Document(filter),
                new Document("$set", new Document(update)));
        return result.getModifiedCount();
    }

    @Override
    public boolean updateOne(String entity, Map<String, Object> filter, Map<String, Object> update) {
        final MongoCollection<Document> documents = database.getCollection(entity);
        final UpdateResult result = documents.updateOne(new Document(filter),
                new Document("$set", new Document(update)));
        return result.getModifiedCount() >= 1;
    }

    @Override
    public boolean updateByID(String entity, String id, Map<String, Object> update) {
        return updateOne(entity, MapFactory.create("_id", new ObjectId(id)), update);
    }

    /* Delete */
    @Override
    public long deleteMany(final String entity, final Map<String, Object> filter) {
        final MongoCollection<Document> documents = database.getCollection(entity);
        final DeleteResult result = documents.deleteMany(new Document(filter));
        return result.getDeletedCount();
    }

    @Override
    public boolean deleteOne(String entity, Map<String, Object> filter) {
        final MongoCollection<Document> documents = database.getCollection(entity);
        final DeleteResult result = documents.deleteOne(new Document(filter));
        return result.getDeletedCount() >= 1;
    }

    @Override
    public boolean deleteByID(String entity, String id) {
        return deleteOne(entity, MapFactory.create("_id", new ObjectId(id)));
    }
}
