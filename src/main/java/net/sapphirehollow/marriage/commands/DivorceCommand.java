package net.sapphirehollow.marriage.commands;

import net.sapphirehollow.marriage.Marriage;
import net.sapphirehollow.marriage.controllers.MarriageController;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import xyz.fluxinc.fluxcore.command.Command;

import java.util.HashMap;
import java.util.Map;

public class DivorceCommand {

    public static void register() {
        Command command = new Command("divorce").player("target");
        command.executor((sender, args) -> {
            Player player = (Player) args[0];
            if (sender instanceof OfflinePlayer) {
                boolean status = MarriageController.divorcePlayers((OfflinePlayer) sender, player);
                if (status) {
                    if (player.isOnline()) {
                        player.getPlayer().sendMessage(generateDivorceMessage((Player) sender));
                    }

                    sender.sendMessage(generateDivorceMessage(player));
                } else {
                    sender.sendMessage(Marriage.getLanguageController().generateMessage("noDivorce"));
                }
            }
        });
        command.register();
    }

    private static String generateDivorceMessage(Player player) {
        Map<String, String> args = new HashMap<>();
        args.put("player",player.getName());
        args.put("display",player.getDisplayName());
        return Marriage.getLanguageController().generateMessage("divorce",args);
    }
}
