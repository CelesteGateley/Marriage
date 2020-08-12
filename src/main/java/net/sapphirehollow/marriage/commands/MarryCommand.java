package net.sapphirehollow.marriage.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import net.sapphirehollow.marriage.controllers.MarriageController;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;

public class MarryCommand {

    public static void registerCommands() {
        LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
        arguments.put("player", new PlayerArgument());
        new CommandAPICommand("marry")
                .withArguments(arguments)
                .executes((sender, args) -> {
                    Player player = (Player) args[0];
                    if (sender instanceof Player) {
                        MarriageController.engagePlayers((Player) sender, player);

                    }
                });
    }
}
