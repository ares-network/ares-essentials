package com.llewkcor.ares.essentials.punishment.data;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.llewkcor.ares.commons.connect.mongodb.MongoDB;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.List;

public final class PunishmentDAO {
    private static final String NAME = "essentials";
    private static final String COLL = "punishments";

    /**
     * Returns an Immutable Collection of punishments matching the provided Bson filters
     * @param database Database
     * @param playerFilter Player Bson Filter
     * @param punishmentFilter Punishment Bson Filter
     * @return Immutable Collection of Punishment
     */
    public static ImmutableCollection<Punishment> getPunishments(MongoDB database, Bson playerFilter, Bson punishmentFilter) {
        final List<Punishment> result = Lists.newArrayList();
        final MongoCollection<Document> collection = database.getCollection(NAME, COLL);
        final MongoCursor<Document> cursor = collection.find(Filters.and(playerFilter, punishmentFilter)).iterator();

        while (cursor.hasNext()) {
            result.add(new Punishment().fromDocument(cursor.next()));
        }

        return ImmutableList.copyOf(result);
    }

    /**
     * Saves the provided Punishment to the database
     * @param database Database
     * @param punishment Punishment
     */
    public static void savePunishment(MongoDB database, Punishment punishment) {
        final MongoCollection<Document> collection = database.getCollection(NAME, COLL);
        final Document existing = collection.find(Filters.eq("id", punishment.getUniqueId())).first();

        if (existing != null) {
            collection.replaceOne(existing, punishment.toDocument());
        } else {
            collection.insertOne(punishment.toDocument());
        }
    }
}