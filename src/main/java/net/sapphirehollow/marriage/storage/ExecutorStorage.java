package net.sapphirehollow.marriage.storage;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.executors.CommandExecutor;

import java.util.LinkedHashMap;
import java.util.List;

public class ExecutorStorage {

    private final CommandExecutor executor;
    private final LinkedHashMap<String, Argument> arguments;

    public ExecutorStorage(CommandExecutor executor, List<Argument> arguments) {
        this.executor = executor;
        this.arguments = arguments;
    }

    public CommandExecutor getExecutor() {
        return executor;
    }

    public List<Argument> getArguments() {
        return arguments;
    }
}
