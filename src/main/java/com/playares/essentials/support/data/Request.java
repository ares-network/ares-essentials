package com.playares.essentials.support.data;

import com.playares.commons.connect.mongodb.MongoDocument;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bson.Document;

import java.util.UUID;

@AllArgsConstructor
public final class Request implements ISupport, MongoDocument<Request> {
    @Getter public UUID ticketId;
    @Getter public UUID creatorUniqueId;
    @Getter public String creatorUsername;
    @Getter public long createTime;
    @Getter public String description;

    public Request() {
        this.ticketId = UUID.randomUUID();
        this.creatorUniqueId = null;
        this.creatorUsername = null;
        this.createTime = 0L;
        this.description = null;
    }

    @Override
    public Request fromDocument(Document document) {
        this.ticketId = (UUID)document.get("id");
        this.creatorUniqueId = (UUID)document.get("creator_id");
        this.creatorUsername = document.getString("creator_username");
        this.createTime = document.getLong("create_time");
        this.description = document.getString("description");

        return this;
    }

    @Override
    public Document toDocument() {
        return new Document()
                .append("id", ticketId)
                .append("creator_id", creatorUniqueId)
                .append("creator_username", creatorUsername)
                .append("create_time", createTime)
                .append("description", description)
                .append("type", "request");
    }
}