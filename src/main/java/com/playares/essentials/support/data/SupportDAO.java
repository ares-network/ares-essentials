package com.playares.essentials.support.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.playares.commons.connect.mongodb.MongoDB;
import com.playares.essentials.EssentialsService;
import org.bson.Document;

import java.util.List;

public final class SupportDAO {
    /**
     * Returns an ImmutableList of report tickets
     * @param service Essentials
     * @return Immutable List of Reports
     */
    public static ImmutableList<ISupport> getReports(EssentialsService service) {
        final MongoDB database = (MongoDB)service.getOwner().getDatabaseInstance(MongoDB.class);
        final MongoCollection<Document> collection = database.getCollection(service.getDatabaseName(), "tickets");
        final MongoCursor<Document> cursor = collection.find().cursor();
        final List<Report> result = Lists.newArrayList();

        while (cursor.hasNext()) {
            final Document document = cursor.next();

            if (document.getString("type").equals("report")) {
                final Report report = new Report().fromDocument(document);
                result.add(report);
            }
        }

        cursor.close();

        return ImmutableList.copyOf(result);
    }

    /**
     * Returns an ImmutableList of request tickets
     * @param service Essentials
     * @return Immutable List of Requests
     */
    public static ImmutableList<ISupport> getRequests(EssentialsService service) {
        final MongoDB database = (MongoDB)service.getOwner().getDatabaseInstance(MongoDB.class);
        final MongoCollection<Document> collection = database.getCollection(service.getDatabaseName(), "tickets");
        final MongoCursor<Document> cursor = collection.find().cursor();
        final List<Request> result = Lists.newArrayList();

        while (cursor.hasNext()) {
            final Document document = cursor.next();

            if (document.getString("type").equals("request")) {
                final Request request = new Request().fromDocument(document);
                result.add(request);
            }
        }

        cursor.close();

        return ImmutableList.copyOf(result);
    }

    /**
     * Handles saving a provided ticket to the database
     * @param service Essentials
     * @param ticket Ticket
     */
    public static void setTicket(EssentialsService service, ISupport ticket) {
        final MongoDB database = (MongoDB)service.getOwner().getDatabaseInstance(MongoDB.class);
        final MongoCollection<Document> collection = database.getCollection(service.getDatabaseName(), "tickets");
        final Document existing = collection.find(Filters.eq("id", ticket.getTicketId())).first();

        if (existing != null) {
            if (ticket instanceof Report) {
                final Report report = (Report)ticket;
                collection.replaceOne(existing, report.toDocument());
            } else if (ticket instanceof Request) {
                final Request request = (Request)ticket;
                collection.replaceOne(existing, request.toDocument());
            }
        }

        else if (ticket instanceof Report) {
            final Report report = (Report) ticket;
            collection.insertOne(report.toDocument());
        } else if (ticket instanceof Request) {
            final Request request = (Request) ticket;
            collection.insertOne(request.toDocument());
        }
    }

    /**
     * Handles deleting a ticket from the database
     * @param service Essentials
     * @param ticket Ticket
     */
    public static void deleteTicket(EssentialsService service, ISupport ticket) {
        final MongoDB database = (MongoDB)service.getOwner().getDatabaseInstance(MongoDB.class);
        final MongoCollection<Document> collection = database.getCollection(service.getDatabaseName(), "tickets");
        final Document existing = collection.find(Filters.eq("id", ticket.getTicketId())).first();

        if (existing != null) {
            collection.deleteOne(existing);
        }
    }
}
