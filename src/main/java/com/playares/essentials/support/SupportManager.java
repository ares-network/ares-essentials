package com.playares.essentials.support;

import com.google.common.collect.Lists;
import com.playares.commons.promise.Promise;
import com.playares.commons.util.bukkit.Scheduler;
import com.playares.essentials.EssentialsService;
import com.playares.essentials.support.data.ISupport;
import com.playares.essentials.support.data.SupportDAO;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public final class SupportManager {
    @Getter public final EssentialsService essentials;
    @Getter public final SupportHandler handler;
    @Getter public final List<UUID> ticketCooldowns;

    public SupportManager(EssentialsService essentials) {
        this.essentials = essentials;
        this.handler = new SupportHandler(this);
        this.ticketCooldowns = Collections.synchronizedList(Lists.newArrayList());
    }

    public void getReports(Promise<List<ISupport>> promise) {
        new Scheduler(essentials.getOwner()).async(() -> {

            final List<ISupport> reports = SupportDAO.getReports(essentials);
            new Scheduler(essentials.getOwner()).sync(() -> promise.ready(reports)).run();

        }).run();
    }

    public void getRequests(Promise<List<ISupport>> promise) {
        new Scheduler(essentials.getOwner()).async(() -> {

            final List<ISupport> requests = SupportDAO.getRequests(essentials);
            new Scheduler(essentials.getOwner()).sync(() -> promise.ready(requests)).run();

        }).run();
    }
}