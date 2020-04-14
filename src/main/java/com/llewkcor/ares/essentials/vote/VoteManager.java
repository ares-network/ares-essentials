package com.llewkcor.ares.essentials.vote;

import com.google.common.collect.Lists;
import com.llewkcor.ares.commons.util.general.Configs;
import com.llewkcor.ares.essentials.Essentials;
import com.llewkcor.ares.essentials.vote.listener.VotifierListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

public final class VoteManager {
    @Getter public final Essentials plugin;
    @Getter public final VoteHandler handler;

    @Getter public final List<String> voteLinks;
    @Getter public final List<String> voteCommands;

    public VoteManager(Essentials plugin) {
        this.plugin = plugin;
        this.handler = new VoteHandler(this);
        this.voteLinks = Lists.newArrayList();
        this.voteCommands = Lists.newArrayList();

        final YamlConfiguration config = Configs.getConfig(plugin, "config");

        voteLinks.addAll(config.getStringList("vote.links"));
        voteCommands.addAll(config.getStringList("vote.issue_commands"));

        Bukkit.getPluginManager().registerEvents(new VotifierListener(this), plugin);
    }
}
