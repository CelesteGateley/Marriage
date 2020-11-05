package net.sapphirehollow.marriage.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import net.sapphirehollow.marriage.Marriage;
import net.sapphirehollow.marriage.controllers.MarriageController;
import net.sapphirehollow.marriage.storage.ExecutorStorage;
import net.sapphirehollow.marriage.storage.PlayerStorage;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

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
            if (!sender.hasPermission("marriage.priest")) {
                sender.sendMessage(Marriage.getLanguageController().generateMessage("noPermission"));
                return;
            }
            OfflinePlayer player1 = Marriage.getStorageController().getOfflinePlayer((String) args[0]);
            OfflinePlayer player2 = Marriage.getStorageController().getOfflinePlayer((String) args[1]);
            boolean status = MarriageController.marryPlayers(player1, player2);
            if (status) {
                if (player1.isOnline() && player1.getPlayer() != null) {
                    player1.getPlayer().sendMessage(generateMarriageMessage(player2));
                }

                if (player2.isOnline() && player2.getPlayer() != null) {
                    player2.getPlayer().sendMessage(generateMarriageMessage(player1));
                }
                Marriage.instance.getServer().broadcastMessage(generateExtMarriageMessage(player1, player2));
            } else {
                sender.sendMessage(Marriage.getLanguageController().generateMessage("marryFail"));
            }
        }, arguments));

        // Confirm Command
        arguments = new LinkedHashMap<>();
        arguments.put("confirm", new LiteralArgument("confirm"));
        arguments.put("player", new StringArgument());
        returnVal.put("confirm", new ExecutorStorage((sender, args) -> {
            OfflinePlayer player = Marriage.getStorageController().getOfflinePlayer((String) args[0]);
            if (!(sender instanceof OfflinePlayer)) {
                sender.sendMessage(Marriage.getLanguageController().generateMessage("mustBePlayer"));
                return;
            }
            boolean status = MarriageController.confirmEngageRequest((OfflinePlayer) sender, player);
            if (status) {
                if (player.isOnline() && player.getPlayer() != null) {
                    player.getPlayer().sendMessage(generateEngageMessage((OfflinePlayer) sender));
                }
                sender.sendMessage(generateEngageMessage(player));
            } else {
                sender.sendMessage(Marriage.getLanguageController().generateMessage("acceptFail"));
            }
        }, arguments));

        // Deny Command
        arguments = new LinkedHashMap<>();
        arguments.put("deny", new LiteralArgument("deny"));
        arguments.put("player", new StringArgument());
        returnVal.put("deny", new ExecutorStorage((sender, args) -> {
            OfflinePlayer player = Marriage.getStorageController().getOfflinePlayer((String) args[0]);
            if (!(sender instanceof OfflinePlayer)) {
                sender.sendMessage(Marriage.getLanguageController().generateMessage("mustBePlayer"));
                return;
            }
            boolean status = MarriageController.denyEngageRequest((OfflinePlayer) sender, player);
            if (status) {
                if (player.isOnline() && player.getPlayer() != null) {
                    player.getPlayer().sendMessage(generateDenyMessage((OfflinePlayer) sender));
                }
                sender.sendMessage(generateDenyMessage(player));
            } else {
                sender.sendMessage(Marriage.getLanguageController().generateMessage("denyFail"));
            }
        }, arguments));

        // Divorce
        arguments = new LinkedHashMap<>();
        arguments.put("divorce", new LiteralArgument("divorce"));
        arguments.put("player1", new StringArgument());
        arguments.put("player2", new StringArgument());
        returnVal.put("divorce", new ExecutorStorage((sender, args) -> {
            if (!sender.hasPermission("marriage.divorce")) {
                sender.sendMessage(Marriage.getLanguageController().generateMessage("noPermission"));
                return;
            }
            OfflinePlayer player1 = Marriage.getStorageController().getOfflinePlayer((String) args[0]);
            OfflinePlayer player2 = Marriage.getStorageController().getOfflinePlayer((String) args[1]);
            boolean status = MarriageController.divorcePlayers(player1, player2);
            if (status) {
                if (player1.isOnline() && player1.getPlayer() != null) {
                    player1.getPlayer().sendMessage(generateDivorceMessage(player2));
                }

                if (player2.isOnline() && player2.getPlayer() != null) {
                    player2.getPlayer().sendMessage(generateDivorceMessage(player1));
                }
                sender.sendMessage(generateExtDivorceMessage(player1, player2));
            } else {
                sender.sendMessage(Marriage.getLanguageController().generateMessage("noDivorce"));
            }
        }, arguments));

        // Divorce
        arguments = new LinkedHashMap<>();
        arguments.put("spy", new LiteralArgument("spy"));
        returnVal.put("spy", new ExecutorStorage((sender, args) -> {
            if (sender instanceof Player) {
                PlayerStorage storage = Marriage.getStorageController().getPlayerStorage((OfflinePlayer) sender);
                boolean spy = storage.toggleMarriageSpy();
                Marriage.getStorageController().setPlayerStorage((OfflinePlayer) sender, storage);
                sender.sendMessage(generateToggleMessage(spy ? "on" : "off"));
            }
        }, arguments));

        return returnVal;
    }

    public static void registerCommands() {
        Map<String, ExecutorStorage> commands = getCommands();
        for (String key : commands.keySet()) {
            new CommandAPICommand("marriage")
                    .withArguments(commands.get(key).getArguments())
                    .executes(commands.get(key).getExecutor())
                    .register();
        }
    }

    public static String generateToggleMessage(String status) {
        Map<String, String> args = new HashMap<>();
        args.put("status",status);
        return Marriage.getLanguageController().generateMessage("spyToggle",args);
    }

    private static String generateDivorceMessage(OfflinePlayer player) {
        Map<String, String> args = new HashMap<>();
        args.put("player",player.getName());
        if (player.getPlayer() != null) args.put("display",player.getPlayer().getDisplayName());
        else args.put("display",player.getName());
        return Marriage.getLanguageController().generateMessage("divorce",args);
    }

    private static String generateExtDivorceMessage(OfflinePlayer player1, OfflinePlayer player2) {
        Map<String, String> args = new HashMap<>();
        args.put("player1",player1.getName());
        if (player1.isOnline() && player1.getPlayer() != null) { args.put("display1",player1.getPlayer().getDisplayName()); }
        else { args.put("display1",player1.getName()); }
        args.put("player2",player2.getName());
        if (player2.isOnline() && player2.getPlayer() != null) { args.put("display2",player2.getPlayer().getDisplayName()); }
        else { args.put("display2",player2.getName()); }
        return Marriage.getLanguageController().generateMessage("extDivorce",args);
    }

    private static String generateExtMarriageMessage(OfflinePlayer player1, OfflinePlayer player2) {
        Map<String, String> args = new HashMap<>();
        args.put("player1",player1.getName());
        if (player1.isOnline() && player1.getPlayer() != null) { args.put("display1",player1.getPlayer().getDisplayName()); }
        else { args.put("display1",player1.getName()); }
        args.put("player2",player2.getName());
        if (player2.isOnline() && player2.getPlayer() != null) { args.put("display2",player2.getPlayer().getDisplayName()); }
        else { args.put("display2",player2.getName()); }
        return Marriage.getLanguageController().generateMessage("extMarriage",args);
    }

    private static String generateMarriageMessage(OfflinePlayer player) {
        Map<String, String> args = new HashMap<>();
        args.put("player",player.getName());
        if (player.getPlayer() != null) { args.put("display",player.getPlayer().getDisplayName()); }
        else { args.put("display", player.getName()); }
        return Marriage.getLanguageController().generateMessage("married",args);
    }

    private static String generateEngageMessage(OfflinePlayer player) {
        Map<String, String> args = new HashMap<>();
        args.put("player",player.getName());
        if (player.getPlayer() != null) { args.put("display",player.getPlayer().getDisplayName()); }
        else { args.put("display", player.getName()); }
        return Marriage.getLanguageController().generateMessage("engaged",args);
    }

    private static String generateDenyMessage(OfflinePlayer player) {
        Map<String, String> args = new HashMap<>();
        args.put("player",player.getName());
        if (player.getPlayer() != null) { args.put("display",player.getPlayer().getDisplayName()); }
        else { args.put("display", player.getName()); }
        return Marriage.getLanguageController().generateMessage("denyMessage",args);
    }

}
