package com.llewkcor.ares.essentials.broadcast;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.llewkcor.ares.commons.logger.Logger;
import com.llewkcor.ares.commons.util.bukkit.Scheduler;
import com.llewkcor.ares.commons.util.general.Configs;
import com.llewkcor.ares.core.player.data.account.AresAccount;
import com.llewkcor.ares.essentials.Essentials;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.Queue;

public final class BroadcastManager {
    @Getter public final Essentials plugin;
    @Getter public final int interval;
    @Getter public final String prefix;
    @Getter public final List<String> messages;
    @Getter public final Queue<String> queue;
    @Getter public final BukkitTask task;

    public BroadcastManager(Essentials plugin) {
        final YamlConfiguration config = Configs.getConfig(plugin, "broadcasts");

        this.plugin = plugin;
        this.interval = config.getInt("settings.interval");
        this.prefix = ChatColor.translateAlternateColorCodes('&', config.getString("settings.prefix"));
        this.messages = Lists.newArrayList();
        this.queue = Queues.newConcurrentLinkedQueue();

        for (String message : config.getStringList("messages")) {
            messages.add(ChatColor.translateAlternateColorCodes('&', message));
        }

        Logger.print("Loaded " + messages.size() + " Broadcast Messages");

        this.task = new Scheduler(plugin).sync(() -> {
            final String message = pullMessage();

            for (Player player : Bukkit.getOnlinePlayers()) {
                final AresAccount account = plugin.getCore().getPlayerManager().getAccountByBukkitID(player.getUniqueId());

                if (account == null || !account.getSettings().isBroadcastsEnabled()) {
                    continue;
                }

                player.sendMessage(prefix + message);
            }

        }).repeat(interval * 20L, interval * 20L).run();
    }

    /**
     * Pulls a new message from the broadcast queue
     * @return Message
     */
    private String pullMessage() {
        if (queue.isEmpty()) {
            queue.addAll(messages);
        }

        return queue.remove();
    }
}
