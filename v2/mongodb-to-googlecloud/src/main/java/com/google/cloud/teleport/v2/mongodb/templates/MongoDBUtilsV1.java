package com.google.cloud.teleport.v2.mongodb.templates;

import com.google.api.services.bigquery.model.TableFieldSchema;
import com.google.api.services.bigquery.model.TableSchema;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MongoDBUtilsV1 {

    private static final Logger LOG = LoggerFactory.getLogger(MongoDBUtilsV1.class);

    /** Maps and Returns the Datatype form MongoDb To BigQuery. */
    public static String getTableSchemaDataType(Object value, String s) {
        switch (s) {
            case "java.lang.Integer":
                return "INTEGER";
            case "java.lang.Boolean":
                return "BOOLEAN";
            case "org.bson.Document":
                return "STRUCT";
            case "java.lang.Double":
            case "org.bson.types.Decimal128":
                return "FLOAT";
            case "java.util.ArrayList":
                ArrayList ls = (ArrayList)value;
                if(ls.size() > 0) {
                    Object val = ls.get(0);
                    if (val instanceof String) {
                        return "STRING";
                    }
                    if (val instanceof Float) {
                        return "FLOAT";
                    }
                    if (val instanceof Integer) {
                        return "INTEGER";
                    }
                }
                return "STRUCT";
            case "org.bson.types.ObjectId":
                return "STRING";
        }
        return "STRING";
    }

    public static String getModeForTableColumn(String s) {
        switch (s) {
            case "java.util.ArrayList":
                return "REPEATED";
            default:
                return "NULLABLE";
        }
    }

    /** Get a Document from MongoDB to generate the schema for BigQuery. */
    public static Document getMongoDbDocument(String uri, String dbName, String collName) {
        MongoClient mongoClient = MongoClients.create(uri);
        MongoDatabase database = mongoClient.getDatabase(dbName);
        MongoCollection<Document> collection = database.getCollection(collName);
        Document doc = collection.find().first();
        return doc;
    }

    public static void getTableFieldSchemaComplex(TableFieldSchema outerSchema, Object value){
        List<TableFieldSchema> ls = new ArrayList<>();
        if(value instanceof Document)
        {
            Document internalDoc = (Document) value;
            prepareListOfTableFieldSchFromBson(internalDoc,ls);
        }
        else if(value instanceof ArrayList){
            List valueList = (ArrayList)value;
            if(valueList.size() > 0) {
                if(valueList.get(0) instanceof Document) {
                    Document obj = (Document) valueList.get(0);
                    prepareListOfTableFieldSchFromBson(obj, ls);
                }
            }
        }
        outerSchema.setFields(ls);
    }

    public static void prepareListOfTableFieldSchFromBson(Document internalDoc, List<TableFieldSchema> ls){
        Set<String> keySet = internalDoc.keySet();
        Iterator<String> itr = keySet.iterator();
        while(itr.hasNext()) {
            String key = itr.next();
            LOG.info(":Key is:" + key);
            Object internalVal = internalDoc.get(key);
            LOG.info(":Value is:" + internalVal.getClass().getName());
            String type = getTableSchemaDataType(internalVal, internalVal.getClass().getName());
            if(!"java.util.ArrayList".equals(internalVal.getClass().getName()) && !"org.bson.Document".equals(internalVal.getClass().getName()))
                ls.add(new TableFieldSchema().setType(type).setName(key).setMode(getModeForTableColumn(type)));
            else {
                TableFieldSchema outerSchema = new TableFieldSchema();
                outerSchema.setType(type);
                outerSchema.setName(key);
                if("java.util.ArrayList".equals(internalVal.getClass().getName()))
                    outerSchema.setMode(getModeForTableColumn("java.util.ArrayList"));
                ls.add(outerSchema);
            }
        }
    }

    public static TableSchema getTableFieldSchema(
            String uri, String database, String collection) {
        List<TableFieldSchema> bigquerySchemaFields = new ArrayList<>();
        Document document = getMongoDbDocument(uri, database, collection);

        document.forEach(
                (key, value) -> {
                    LOG.info("Key is:"+key+":Value is:"+value);
                    if(value instanceof Document) {
                        TableFieldSchema outerSchema = new TableFieldSchema();
                        outerSchema.setType("STRUCT");
                        outerSchema.setName(key);
                        getTableFieldSchemaComplex(outerSchema, value);
                        bigquerySchemaFields.add(outerSchema);
                    }
                    else if(value instanceof ArrayList){
                        TableFieldSchema outerSchema = new TableFieldSchema();
                        outerSchema.setType(getTableSchemaDataType(value, value.getClass().getName()));
                        outerSchema.setMode("REPEATED");
                        outerSchema.setName(key);
                        getTableFieldSchemaComplex(outerSchema, value);
                        bigquerySchemaFields.add(outerSchema);
                    }
                    else {
                        LOG.info("Type is:"+value.getClass().getName());
                        bigquerySchemaFields.add(
                                new TableFieldSchema()
                                        .setName(key)
                                        .setType(getTableSchemaDataType(value, value.getClass().getName())).setMode(getModeForTableColumn(value.getClass().getName())));
                    }
                });
        LOG.info("bigquerySchemaFields is:"+bigquerySchemaFields);
        TableSchema bigquerySchema = new TableSchema().setFields(bigquerySchemaFields);
        return bigquerySchema;
    }

}

