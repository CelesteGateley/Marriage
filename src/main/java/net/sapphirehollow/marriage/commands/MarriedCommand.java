package net.sapphirehollow.marriage.commands;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.ChatColorArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import net.sapphirehollow.marriage.Marriage;
import net.sapphirehollow.marriage.storage.ExecutorStorage;
import net.sapphirehollow.marriage.storage.PlayerStorage;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.*;

public class MarriedCommand {

    private static Map<String, ExecutorStorage> getCommands() {
        Map<String, ExecutorStorage> returnVal = new HashMap<>();
        // Priest Command
        LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
        arguments.put("kiss", new LiteralArgument("kiss"));
        returnVal.put("kiss", new ExecutorStorage((sender, args) -> {
            if (sender instanceof Player) {
                PlayerStorage storage = Marriage.getStorageController().getPlayerStorage((OfflinePlayer) sender);
                for (OfflinePlayer oPlayer : storage.getAllPartners()) {
                    if (oPlayer.isOnline() && oPlayer.getPlayer() != null) {
                        oPlayer.getPlayer().getWorld().spawnParticle(Particle.HEART, oPlayer.getPlayer().getLocation(), 50, 0.5, 1, 0.5);
                        oPlayer.getPlayer().sendMessage(generateKissMessage((OfflinePlayer) sender));
                    }
                }
            }
        }, arguments));

        arguments = new LinkedHashMap<>();
        arguments.put("tp", new LiteralArgument("tp"));
        arguments.put("player", new PlayerArgument());
        returnVal.put("tp", new ExecutorStorage((sender, args) -> {
            if (sender instanceof Player) {
                Player player = (Player) args[0];
                if (Marriage.isVanished(player)) {
                    sender.sendMessage(Marriage.getLanguageController().generateMessage("playerNotFound"));
                    return;
                }
                if (player.getGameMode() == GameMode.SPECTATOR) {
                    sender.sendMessage(Marriage.getLanguageController().generateMessage("teleportDisabled"));
                    return;
                }
                if (Marriage.getStorageController().getPlayerStorage(player).allowTeleport()
                        && Marriage.getStorageController().getPlayerStorage((OfflinePlayer) sender).allowTeleport()) {
                    ((Player) sender).teleport(player);
                } else {
                    sender.sendMessage(Marriage.getLanguageController().generateMessage("teleportDisabled"));
                }
            }
        }, arguments));

        arguments = new LinkedHashMap<>();
        arguments.put("tptoggle", new LiteralArgument("tptoggle"));
        returnVal.put("tptoggle", new ExecutorStorage((sender, args) -> {
            if (sender instanceof Player) {
                PlayerStorage storage = Marriage.getStorageController().getPlayerStorage((OfflinePlayer) sender);
                boolean toggleVal = storage.toggleTeleport();
                Marriage.getStorageController().setPlayerStorage((OfflinePlayer) sender, storage);
                sender.sendMessage(generateToggleMessage(toggleVal ? "on" : "off"));
            }
        }, arguments));

        arguments = new LinkedHashMap<>();
        arguments.put("heart", new LiteralArgument("heart"));
        arguments.put("color", new ChatColorArgument());
        returnVal.put("heart", new ExecutorStorage((sender, args) -> {
            if (sender instanceof Player) {
                PlayerStorage storage = Marriage.getStorageController().getPlayerStorage((OfflinePlayer) sender);
                ChatColor color = (ChatColor) args[0];
                storage.setPreferredColor("" + color);
                Marriage.getStorageController().setPlayerStorage((OfflinePlayer) sender, storage);
            }
        }, arguments));

        return returnVal;
    }

    public static void registerCommands() {
        Map<String, ExecutorStorage> commands = getCommands();
        for (String key : commands.keySet()) {
            new CommandAPICommand("married")
                    .withAliases("m")
                    .withArguments(commands.get(key).getArguments())
                    .executes(commands.get(key).getExecutor())
                    .register();
        }
    }

    public static String generateToggleMessage(String status) {
        Map<String, String> args = new HashMap<>();
        args.put("status",status);
        return Marriage.getLanguageController().generateMessage("tpToggle",args);
    }

    private static String generateKissMessage(OfflinePlayer player) {
        Map<String, String> args = new HashMap<>();
        args.put("player",player.getName());
        if (player.getPlayer() != null) { args.put("display",player.getPlayer().getDisplayName()); }
        else { args.put("display", player.getName()); }
        return Marriage.getLanguageController().generateMessage("kiss",args);
    }
}
