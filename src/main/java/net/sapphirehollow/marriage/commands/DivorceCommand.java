package net.sapphirehollow.marriage.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.sapphirehollow.marriage.Marriage;
import net.sapphirehollow.marriage.controllers.MarriageController;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;

public class DivorceCommand {

    public static void registerCommands() {
        new CommandAPICommand("divorce")
                .withArguments(new PlayerArgument("target"))
                .executes((sender, args) -> {
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
                }).register();
    }

    private static String generateDivorceMessage(Player player) {
        Map<String, String> args = new HashMap<>();
        args.put("player",player.getName());
        args.put("display",player.getDisplayName());
        return Marriage.getLanguageController().generateMessage("divorce",args);
    }
}
