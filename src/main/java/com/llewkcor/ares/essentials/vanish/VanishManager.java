package com.llewkcor.ares.essentials.vanish;

import com.google.common.collect.Sets;
import com.llewkcor.ares.essentials.Essentials;
import com.llewkcor.ares.essentials.vanish.listener.VanishListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

public final class VanishManager {
    @Getter public final Essentials plugin;
    @Getter public final VanishHandler handler;
    @Getter public final Set<UUID> vanished;

    public VanishManager(Essentials plugin) {
        this.plugin = plugin;
        this.handler = new VanishHandler(this);
        this.vanished = Sets.newConcurrentHashSet();

        Bukkit.getPluginManager().registerEvents(new VanishListener(this), plugin);
    }

    /**
     * Returns true if the provided Bukkit Player is vanished
     * @param player Bukkit Player
     * @return True if vanished
     */
    public boolean isVanished(Player player) {
        return vanished.contains(player.getUniqueId());
    }
}