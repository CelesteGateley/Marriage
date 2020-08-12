package net.sapphirehollow.marriage.commands;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import net.sapphirehollow.marriage.Marriage;
import net.sapphirehollow.marriage.storage.ExecutorStorage;
import net.sapphirehollow.marriage.storage.PlayerStorage;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
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
                    if (oPlayer.isOnline()) {
                        oPlayer.getPlayer().getWorld().spawnParticle(Particle.HEART, oPlayer.getPlayer().getLocation(), 50, 0.5, 1, 0.5);
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
                if (Marriage.isVanished(player)) CommandAPI.fail("Player not found");
                if (player.getGameMode() == GameMode.SPECTATOR) CommandAPI.fail("Teleport is disabled");
                if (Marriage.getStorageController().getPlayerStorage(player).allowTeleport() && Marriage.getStorageController().getPlayerStorage((OfflinePlayer) sender).allowTeleport()) {
                    ((Player) sender).teleport(player);
                } else {
                    CommandAPI.fail("Teleport is disabled");
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
                sender.sendMessage("&7[&9SH Marriage&7] You have toggled your teleport to " + (toggleVal ? "on" : "off"));
            }
        }, arguments));

        return returnVal;
    }
}
