package net.sapphirehollow.marriage.storage;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.executors.CommandExecutor;

import java.util.LinkedHashMap;

public class ExecutorStorage {

    private final CommandExecutor executor;
    private final LinkedHashMap<String, Argument> arguments;

    public ExecutorStorage(CommandExecutor executor, LinkedHashMap<String, Argument> arguments) {
        this.executor = executor;
        this.arguments = arguments;
    }

    public CommandExecutor getExecutor() {
        return executor;
    }

    public LinkedHashMap<String, Argument> getArguments() {
        return arguments;
    }
}
