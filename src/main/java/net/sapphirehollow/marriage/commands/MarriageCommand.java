package net.sapphirehollow.marriage.commands;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import net.sapphirehollow.marriage.Marriage;
import net.sapphirehollow.marriage.controllers.MarriageController;
import net.sapphirehollow.marriage.storage.ExecutorStorage;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MarriageCommand {
    private static Map<String, ExecutorStorage> getCommands() {
        Map<String, ExecutorStorage> returnVal = new HashMap<>();
        // Priest Command
        LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
        arguments.put("priest", new LiteralArgument("priest"));
        arguments.put("player1", new StringArgument());
        arguments.put("player2", new StringArgument());
        returnVal.put("priest", new ExecutorStorage((sender, args) -> {
            OfflinePlayer player1 = Marriage.getStorageController().getOfflinePlayer((String) args[0]);
            OfflinePlayer player2 = Marriage.getStorageController().getOfflinePlayer((String) args[1]);
            boolean status = MarriageController.marryPlayers(player1, player2);
            if (status) {
                if (player1.isOnline()) {
                    player1.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&7[&9SH Marriage&7]&b You are now married to " + player2.getName()));
                }

                if (player2.isOnline()) {
                    player2.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&7[&9SH Marriage&7]&b You are now married to " + player1.getName()));
                }
                sender.sendMessage("&7[&9SH Marriage&7]&b " + player1.getName() + " is now married to " + player2.getName());
            } else {
                sender.sendMessage("&7[&9SH Marriage&7]&b Those two players cannot be married");
            }
        }, arguments));

        // Confirm Command
        arguments = new LinkedHashMap<>();
        arguments.put("confirm", new LiteralArgument("confirm"));
        arguments.put("player", new StringArgument());
        returnVal.put("confirm", new ExecutorStorage((sender, args) -> {
            OfflinePlayer player = Marriage.getStorageController().getOfflinePlayer((String) args[0]);
            if (!(sender instanceof OfflinePlayer)) CommandAPI.fail("This command can only be run by players");
            boolean status = MarriageController.confirmEngageRequest((OfflinePlayer) sender, player);
            if (status) {
                if (player.isOnline()) {
                    player.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&7[&9SH Marriage&7]&b You are now engaged to " + sender.getName()));
                }
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&7[&9SH Marriage&7]&b You are now engaged to " + player.getName()));
            } else {
                sender.sendMessage("&7[&9SH Marriage&7]&b You are unable to accept this request");
            }
        }, arguments));

        // Deny Command
        arguments = new LinkedHashMap<>();
        arguments.put("deny", new LiteralArgument("deny"));
        arguments.put("player", new StringArgument());
        returnVal.put("deny", new ExecutorStorage((sender, args) -> {
            OfflinePlayer player = Marriage.getStorageController().getOfflinePlayer((String) args[0]);
            if (!(sender instanceof OfflinePlayer)) CommandAPI.fail("This command can only be run by players");
            boolean status = MarriageController.denyEngageRequest((OfflinePlayer) sender, player);
            if (status) {
                if (player.isOnline()) {
                    player.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&7[&9SH Marriage&7]&b " + sender.getName() + " has denied your engagement request"));
                }
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&7[&9SH Marriage&7]&b You have denied the engage request from " + player.getName()));
            } else {
                sender.sendMessage("&7[&9SH Marriage&7]&b You are unable to deny this request");
            }
        }, arguments));

        arguments = new LinkedHashMap<>();
        arguments.put("divorce", new LiteralArgument("divorce"));
        arguments.put("player1", new StringArgument());
        arguments.put("player2", new StringArgument());
        returnVal.put("divorce", new ExecutorStorage((sender, args) -> {
            OfflinePlayer player1 = Marriage.getStorageController().getOfflinePlayer((String) args[0]);
            OfflinePlayer player2 = Marriage.getStorageController().getOfflinePlayer((String) args[1]);
            boolean status = MarriageController.marryPlayers(player1, player2);
            if (status) {
                if (player1.isOnline()) {
                    player1.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&7[&9SH Marriage&7]&b You are now divorced from " + player2.getName()));
                }

                if (player2.isOnline()) {
                    player2.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&7[&9SH Marriage&7]&b You are now divorced from " + player1.getName()));
                }
                sender.sendMessage("&7[&9SH Marriage&7]&b " + player1.getName() + " is now divorced from " + player2.getName());
            } else {
                sender.sendMessage("&7[&9SH Marriage&7]&b Those two players are not married or engaged");
            }
        }, arguments));

        return returnVal;
    }
}
