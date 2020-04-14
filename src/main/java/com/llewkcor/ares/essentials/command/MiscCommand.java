package com.llewkcor.ares.essentials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.llewkcor.ares.commons.logger.Logger;
import com.llewkcor.ares.essentials.Essentials;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

@AllArgsConstructor
public final class MiscCommand extends BaseCommand {
    @Getter public final Essentials plugin;

    @CommandAlias("vote")
    @Description("Receive the voting links for this server")
    public void onVote(Player player) {
        player.sendMessage(ChatColor.RESET + " ");
        player.sendMessage(Essentials.PRIMARY + "Vote using the following links and receive a " + Essentials.SECONDARY + "Vote Crate");

        for (String link : plugin.getVoteManager().getVoteLinks()) {
            player.sendMessage(Essentials.SPECIAL + link);
            player.sendMessage(ChatColor.RESET + " ");
        }
    }

    @CommandAlias("weather")
    @CommandPermission("essentials.weather")
    @Description("Change the weather for the world you're in")
    @Syntax("<clear/rain/thunder>")
    public void onWeather(Player player, @Values("clear|rain|thunder") String weatherName) {
        final World world = player.getWorld();

        if (weatherName.equalsIgnoreCase("clear") || weatherName.equalsIgnoreCase("sun")) {
            world.setStorm(false);
            world.setThundering(false);
            world.setWeatherDuration(Integer.MAX_VALUE);

            player.sendMessage(Essentials.PRIMARY + "Weather state changed to " + Essentials.SECONDARY + "clear");
            Logger.print(player.getName() + " changed the weather of " + world.getName() + " to clear");
        }

        else if (weatherName.equalsIgnoreCase("rain")) {
            world.setStorm(true);
            world.setThundering(false);
            world.setWeatherDuration(Integer.MAX_VALUE);

            player.sendMessage(Essentials.PRIMARY + "Weather state changed to " + Essentials.SECONDARY + "storm");
            Logger.print(player.getName() + " changed the weather of " + world.getName() + " to storm");
        }

        else if (weatherName.equalsIgnoreCase("thunder")) {
            world.setStorm(true);
            world.setThundering(true);
            world.setThunderDuration(Integer.MAX_VALUE);
            world.setWeatherDuration(Integer.MAX_VALUE);

            player.sendMessage(Essentials.PRIMARY + "Weather state changed to " + Essentials.SECONDARY + "thundering");
            Logger.print(player.getName() + " changed the weather of " + world.getName() + " to thunder");
        }

        else {
            player.sendMessage(ChatColor.RED + "Invalid weather state");
        }
    }
}
