package dev.roxxion.api.commands;

import org.bukkit.command.CommandSender;

public class CommandContext {

    private final CommandSender sender;
    private final CommandInfo command;

    private final String[] args;

    public CommandContext(CommandSender sender, String[] args, CommandInfo command){
        this.sender = sender;
        this.args = args;
        this.command = command;
    }

    public CommandSender getSender(){
        return sender;
    }

    public String[] getArgs(){
        return args;
    }

    public String getArg( int index ){
        if (index > args.length - 1) return null;
        return args[index];
    }

    public CommandInfo getCommand(){
        return command;
    }
}
