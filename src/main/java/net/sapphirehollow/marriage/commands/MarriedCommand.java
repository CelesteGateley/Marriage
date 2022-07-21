package net.sapphirehollow.marriage.commands;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.sapphirehollow.marriage.Marriage;
import net.sapphirehollow.marriage.storage.PlayerStorage;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import xyz.fluxinc.fluxcore.command.Command;

import java.util.HashMap;
import java.util.Map;

public class MarriedCommand {

    private static final String COMMAND = "married";
    private static final String[] ALIASES = {};
    private static Command command() { return new Command(COMMAND, ALIASES); }

    private static Command getKissCommand() {
        Command command = command().literal("kiss");
        command.executor((sender, args) -> {
            if (sender instanceof Player) {
                PlayerStorage storage = Marriage.getStorageController().getPlayerStorage((OfflinePlayer) sender);
                for (OfflinePlayer oPlayer : storage.getAllPartners()) {
                    if (oPlayer.isOnline() && oPlayer.getPlayer() != null) {
                        oPlayer.getPlayer().getWorld().spawnParticle(Particle.HEART, oPlayer.getPlayer().getLocation(), 50, 0.5, 1, 0.5);
                        oPlayer.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(generateKissMessage((OfflinePlayer) sender)));
                    }
                }
            }
        });
        return command;
    }

    private static Command getTpCommand() {
        Command command = command().literal("tp").player("player");
        command.executor((sender, args) -> {
            if (sender instanceof Player) {
                Player player = (Player) args[0];
                PlayerStorage storage = Marriage.getStorageController().getPlayerStorage((OfflinePlayer) sender);
                if (!(storage.isMarried(player) || storage.isEngaged(player)))  {
                    sender.sendMessage(Marriage.getLanguageController().generateMessage("tpFail"));
                    return;
                }
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
        });
        return command;
    }

    private static Command getTpToggleCommand() {
        Command command = command().literal("tptoggle");
        command.executor((sender, args) -> {
            if (sender instanceof Player) {
                PlayerStorage storage = Marriage.getStorageController().getPlayerStorage((OfflinePlayer) sender);
                boolean toggleVal = storage.toggleTeleport();
                Marriage.getStorageController().setPlayerStorage((OfflinePlayer) sender, storage);
                sender.sendMessage(generateToggleMessage(toggleVal ? "on" : "off"));
            }
        });
        return command;
    }

    private static Command getColorCommand() {
        Command command = command().literal("color").color("color");
        command.executor((sender, args) -> {
            if (sender instanceof Player) {
                PlayerStorage storage = Marriage.getStorageController().getPlayerStorage((OfflinePlayer) sender);
                ChatColor color = (ChatColor) args[0];
                storage.setPreferredColor("" + color);
                Marriage.getStorageController().setPlayerStorage((OfflinePlayer) sender, storage);
            }
        });
        return command;
    }

    private static Command getChatCommand() {
        Command command = command().literal("chat").greedy("message");
        command.executor((sender, args) -> {
            if (sender instanceof Player) {
                PlayerStorage storage = Marriage.getStorageController().getPlayerStorage((OfflinePlayer) sender);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c[&f" + ((Player) sender).getDisplayName()
                        + "&c] " + args[0]));
                for (Player player : Marriage.instance.getServer().getOnlinePlayers()) {
                    if (sender == player) continue;
                    if (storage.isEngaged(player) || storage.isMarried(player)) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c[&f" + ((Player) sender).getDisplayName()
                                + "&c] " + args[0]));
                        continue;
                    }
                    if (player.hasPermission("marriage.spy")) {
                        PlayerStorage pStore = Marriage.getStorageController().getPlayerStorage(player);
                        if (pStore.marriageSpy()) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&6MS&7]&c[&f" + ((Player) sender).getDisplayName()
                                    + "&c] " + args[0]));
                        }
                    }
                }
            }
        });
        return command;
    }

    public static void register() {
        getKissCommand().register();
        getTpCommand().register();
        getTpToggleCommand().register();
        getColorCommand().register();
        getChatCommand().register();
    }

    public static String generateToggleMessage(String status) {
        Map<String, String> args = new HashMap<>();
        args.put("status",status);
        return Marriage.getLanguageController().generateMessage("tpToggle",args);
    }

    private static String generateKissMessage(OfflinePlayer player) {
        String message = Marriage.getLanguageController().getString("kiss");
        message = message.replaceAll("%player%", player.getName());
        if (player.getPlayer() != null) { message = message.replaceAll("%display%",player.getPlayer().getDisplayName()); }
        else { message = message.replaceAll("%display%", player.getName()); }
        return message;
    }
}
