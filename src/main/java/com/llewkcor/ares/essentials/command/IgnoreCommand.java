package com.llewkcor.ares.essentials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Syntax;
import com.llewkcor.ares.commons.logger.Logger;
import com.llewkcor.ares.commons.promise.FailablePromise;
import com.llewkcor.ares.core.player.data.account.AresAccount;
import com.llewkcor.ares.essentials.Essentials;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@AllArgsConstructor
public final class IgnoreCommand extends BaseCommand {
    @Getter public final Essentials plugin;

    @CommandAlias("ignore|block")
    @Description("Hide messages from a player")
    @Syntax("<username>")
    public void onIgnore(Player player, String username) {
        final AresAccount account = plugin.getCore().getPlayerManager().getAccountByBukkitID(player.getUniqueId());

        if (account == null) {
            player.sendMessage(ChatColor.RED + "Failed to obtain your account");
            return;
        }

        plugin.getCore().getPlayerManager().getAccountByUsername(username, new FailablePromise<AresAccount>() {
            @Override
            public void success(AresAccount aresAccount) {
                if (aresAccount == null) {
                    player.sendMessage(ChatColor.RED + "Player not found");
                    return;
                }

                if (account.getSettings().isIgnoring(aresAccount.getBukkitId())) {
                    player.sendMessage(ChatColor.RED + "You are already ignoring " + aresAccount.getUsername());
                    return;
                }

                account.getSettings().getIgnoredPlayers().add(aresAccount.getBukkitId());
                player.sendMessage(ChatColor.GREEN + "You are now ignoring " + aresAccount.getUsername());

                Logger.print(player.getName() + " started ignoring " + aresAccount.getUsername());
            }

            @Override
            public void fail(String s) {
                player.sendMessage(ChatColor.RED + "Could not ignore provided username: " + s);
            }
        });
    }

    @CommandAlias("unignore|unblock")
    @Description("Show messages from a player you've previously blocked")
    @Syntax("<username>")
    public void onUnignore(Player player, String username) {
        final AresAccount account = plugin.getCore().getPlayerManager().getAccountByBukkitID(player.getUniqueId());

        if (account == null) {
            player.sendMessage(ChatColor.RED + "Failed to obtain your account");
            return;
        }

        plugin.getCore().getPlayerManager().getAccountByUsername(username, new FailablePromise<AresAccount>() {
            @Override
            public void success(AresAccount aresAccount) {
                if (aresAccount == null) {
                    player.sendMessage(ChatColor.RED + "Player not found");
                    return;
                }

                if (!account.getSettings().isIgnoring(aresAccount.getBukkitId())) {
                    player.sendMessage(ChatColor.RED + "You are not ignoring " + aresAccount.getUsername());
                    return;
                }

                account.getSettings().getIgnoredPlayers().remove(aresAccount.getBukkitId());
                player.sendMessage(ChatColor.GREEN + "You are no longer ignoring " + aresAccount.getUsername());

                Logger.print(player.getName() + " stopped ignoring " + aresAccount.getUsername());
            }

            @Override
            public void fail(String s) {
                player.sendMessage(ChatColor.RED + "Could not unignore provided username: " + s);
            }
        });
    }
}
