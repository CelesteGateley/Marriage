package net.sapphirehollow.marriage.commands;

import net.sapphirehollow.marriage.Marriage;
import net.sapphirehollow.marriage.controllers.MarriageController;
import net.sapphirehollow.marriage.storage.PlayerStorage;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import xyz.fluxinc.fluxcore.command.Command;

import java.util.HashMap;
import java.util.Map;

public class MarriageCommand {

    private static final String COMMAND = "marriage";
    private static final String[] ALIASES = {};

    private static Command getPriestCommand() {
        Command command = new Command(COMMAND, ALIASES).literal("priest").string("player1").string("player2");
        command.executor((sender, args) -> {
            if (!sender.hasPermission("marriage.priest")) {
                sender.sendMessage(Marriage.getLanguageController().generateMessage("noPermission"));
                return;
            }
            OfflinePlayer player1 = Marriage.getStorageController().getOfflinePlayer((String) args.get(0));
            OfflinePlayer player2 = Marriage.getStorageController().getOfflinePlayer((String) args.get(1));
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
        });
        return command;
    }

    private static Command getConfirmCommand() {
        Command command = new Command(COMMAND, ALIASES).literal("confirm").string("player");
        command.executor((sender, args) -> {
            OfflinePlayer player = Marriage.getStorageController().getOfflinePlayer((String) args.get(0));
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
        });
        return command;
    }

    private static Command getDenyCommand() {
        Command command = new Command(COMMAND, ALIASES).literal("deny").string("player");
        command.executor((sender, args) -> {
            OfflinePlayer player = Marriage.getStorageController().getOfflinePlayer((String) args.get(0));
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
        });
        return command;
    }

    private static Command getDivorceCommand() {
        Command command = new Command(COMMAND, ALIASES).literal("divorce").string("player1").string("player2");
        command.executor((sender, args) -> {
            if (!sender.hasPermission("marriage.divorce")) {
                sender.sendMessage(Marriage.getLanguageController().generateMessage("noPermission"));
                return;
            }
            OfflinePlayer player1 = Marriage.getStorageController().getOfflinePlayer((String) args.get(0));
            OfflinePlayer player2 = Marriage.getStorageController().getOfflinePlayer((String) args.get(1));
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
        });
        return command;
    }

    private static Command getSpyCommand() {
        Command command = new Command(COMMAND, ALIASES).literal("spy");
        command.executor((sender, args) -> {
            if (sender instanceof Player) {
                PlayerStorage storage = Marriage.getStorageController().getPlayerStorage((OfflinePlayer) sender);
                boolean spy = storage.toggleMarriageSpy();
                Marriage.getStorageController().setPlayerStorage((OfflinePlayer) sender, storage);
                sender.sendMessage(generateToggleMessage(spy ? "on" : "off"));
            }
        });
        return command;
    }

    public static void register() {
        getPriestCommand().register();
        getConfirmCommand().register();
        getDenyCommand().register();
        getDivorceCommand().register();
        getSpyCommand().register();
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
