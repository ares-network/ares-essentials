package com.llewkcor.ares.essentials.warp;

import com.google.common.collect.Sets;
import com.llewkcor.ares.essentials.Essentials;
import com.llewkcor.ares.essentials.warp.data.Warp;
import lombok.Getter;

import java.util.Set;

public final class WarpManager {
    @Getter public final Essentials plugin;
    @Getter public final WarpHandler handler;
    @Getter public final Set<Warp> warps;

    public WarpManager(Essentials plugin) {
        this.plugin = plugin;
        this.handler = new WarpHandler(this);
        this.warps = Sets.newHashSet();
    }

    /**
     * Returns a warp matching the provided name
     * @param name Warp Name
     * @return Warp
     */
    public Warp getWarp(String name) {
        return warps.stream().filter(warp -> warp.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}