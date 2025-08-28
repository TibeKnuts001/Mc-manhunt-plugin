package me.tibeknuts001;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ManhuntCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 0) return false;
        switch (args[0].toLowerCase()) {
            case "hunters": {
                if (args.length == 1) return false;
                List<String> Hunters = ManhuntSettings.getInstance().data.getStringList("hunters");
                switch (args[1].toLowerCase()) {
                    case "list": {
                        if (args.length != 2) return false;
                        sender.sendMessage(ChatColor.GRAY + "Hunters:");
                        for (String hunter : Hunters) {
                            sender.sendMessage(ChatColor.YELLOW + "-   " + ChatColor.AQUA + hunter);
                        }
                        return true;
                    }
                    case "add": {
                        if (args.length != 3) return false;
                        if (Hunters.stream().anyMatch(s -> s.equalsIgnoreCase(args[2]))) {
                            sender.sendMessage(ChatColor.GRAY + "Hunter " + ChatColor.AQUA + args[2] + ChatColor.GRAY + " is already hunter");
                            return true;
                        }
                        Hunters.add(args[2]);
                        ManhuntSettings.getInstance().data.set("hunters", Hunters);
                        ManhuntSettings.getInstance().save();
                        sender.sendMessage(ChatColor.GRAY + "Added hunter: " + ChatColor.AQUA + args[2]);
                        return true;
                    }
                    case "remove": {
                        if (args.length != 3) return false;
                        if (!Hunters.remove(args[2])) {
                            sender.sendMessage(ChatColor.GRAY + "Hunter " + ChatColor.AQUA + args[2] + ChatColor.GRAY + " was already no hunter");
                            return true;
                        }
                        ManhuntSettings.getInstance().data.set("hunters", Hunters);
                        ManhuntSettings.getInstance().save();
                        sender.sendMessage(ChatColor.GRAY + "Removed hunter: " + ChatColor.AQUA + args[2]);
                        return true;
                    }
                    case "clear": {
                        if (args.length != 2) return false;
                        Hunters.removeAll(Hunters);
                        ManhuntSettings.getInstance().data.set("hunters", Hunters);
                        ManhuntSettings.getInstance().save();
                        sender.sendMessage(ChatColor.GRAY + "Removed all Hunters");
                        return true;
                    }
                }
            }
            case "runners": {
                if (args.length == 1) return false;
                List<String> Runners = ManhuntSettings.getInstance().data.getStringList("runners");
                switch (args[1].toLowerCase()) {
                    case "list": {
                        if (args.length != 2) return false;
                        sender.sendMessage(ChatColor.GRAY + "Runners:");
                        for (String Runner : Runners) {
                            sender.sendMessage(ChatColor.YELLOW + "-   " + ChatColor.AQUA + Runner);
                        }
                        return true;
                    }
                    case "add": {
                        if (args.length != 3) return false;
                        if (Runners.stream().anyMatch(s -> s.equalsIgnoreCase(args[2]))) {
                            sender.sendMessage(ChatColor.GRAY + "Runner " + ChatColor.AQUA + args[2] + ChatColor.GRAY + " is already runner");
                            return true;
                        }
                        Runners.add(args[2]);
                        ManhuntSettings.getInstance().data.set("runners", Runners);
                        ManhuntSettings.getInstance().save();
                        sender.sendMessage(ChatColor.GRAY + "Added Runner: " + ChatColor.AQUA + args[2]);
                        return true;
                    }
                    case "remove": {
                        if (args.length != 3) return false;
                        if (!Runners.remove(args[2])) {
                            sender.sendMessage(ChatColor.GRAY + "Runner " + ChatColor.AQUA + args[2] + ChatColor.GRAY + " was already no runner");
                            return true;
                        }
                        ManhuntSettings.getInstance().data.set("runners", Runners);
                        ManhuntSettings.getInstance().save();
                        sender.sendMessage(ChatColor.GRAY + "Removed Runner: " + ChatColor.AQUA + args[2]);
                        return true;
                    }
                    case "clear": {
                        if (args.length != 2) return false;
                        Runners.removeAll(Runners);
                        ManhuntSettings.getInstance().data.set("runners", Runners);
                        ManhuntSettings.getInstance().save();
                        sender.sendMessage(ChatColor.GRAY + "Removed all Runners");
                        return true;
                    }
                }
            }
            case "stop": {
                if (args.length != 1) return false;
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.kickPlayer(ChatColor.GOLD + "=-=-=-=-=-=-=    DRAW    =-=-=-=-=-=-=" + "\n" + ChatColor.RESET +
                            "\n" +
                            ChatColor.RED + "The Game was " + ChatColor.BOLD + "stopped " + ChatColor.RESET + "" + ChatColor.RED + "by " + ChatColor.AQUA + sender.getName() + "\n" + ChatColor.RESET +
                            "Making it a " + ChatColor.GOLD + "draw" + "\n" + ChatColor.RESET +
                            "\n" +
                            ChatColor.GOLD + "=-=-=-=-=-=-=    DRAW    =-=-=-=-=-=-="
                    );
                }
                ManhuntSettings.getInstance().data.set("manhunt_end\n" +
                        "                if (args.length > 2) return false;\n" +
                        "                if (args.length == 1) {ed", true);
                ManhuntSettings.getInstance().save();
                Bukkit.restart();
            }
            case "resourcepack": {
                if (args.length > 2) return false;
                if ((sender instanceof Player player) && args.length == 1)
                    player.setResourcePack(ManhuntSettings.getInstance().data.getString("resourcepack.link"), ManhuntSettings.getInstance().data.getString("resourcepack.hash"), false, Component.text("This resource pack is to make the compass icon in the actionbar work").compact());
                else if (args.length == 1)
                    sender.sendMessage("You need to be a player to do this. Or add a player as argument");
                else if (Bukkit.getPlayer(args[1]).isOnline())
                    Bukkit.getPlayer(args[1]).setResourcePack(ManhuntSettings.getInstance().data.getString("resourcepack.link"), ManhuntSettings.getInstance().data.getString("resourcepack.hash"), false, Component.text("This resource pack is to make the compass icon in the actionbar work").compact());
                else
                    sender.sendMessage("Player is not online");
            }

        }


        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1) return Arrays.asList("runners", "hunters", "stop");
        if (!(args[0].equalsIgnoreCase("runners") || args[0].equalsIgnoreCase("hunters")))
            return new ArrayList<>();
        if (args.length == 2) return Arrays.asList("list", "add", "remove", "clear");
        if (args.length == 3 && args[1].equalsIgnoreCase("add")) {
            List<String> players = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!(ManhuntSettings.getInstance().data.getStringList("runners").contains(player.getName()) || ManhuntSettings.getInstance().data.getStringList("hunters").contains(player.getName())))
                    players.add(player.getName());
            }
            return players;
        }
        if (args.length == 3 && args[1].equalsIgnoreCase("remove"))
            return ManhuntSettings.getInstance().data.getStringList(args[0].toLowerCase());
        return new ArrayList<>();
    }
}
