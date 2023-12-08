package dev.roxxion.api.commands;

import dev.roxxion.api.Api;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandInfo extends BukkitCommand {

    private final Object object;
    private final Method method;
    private final Method tabCompletion;

    public CommandInfo(Object object, Method method, Command command, Method tabCompletion){
        super(command.name(), command.description(), command.usage(), Arrays.asList(command.aliases()));
        this.object = object;
        this.method = method;
        this.tabCompletion = tabCompletion;
        setPermission(command.permission());
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args){
        try {
            CommandContext context = new CommandContext(sender, args, this);
            if ( !sender.hasPermission( this.getPermission())){
                sender.sendMessage(Api.getConfig().getNoPermissionMsg());
                return false;
            }

            boolean ret = (boolean) method.invoke(object, context);
            if (!ret) sender.sendMessage(Api.getConfig().getNoPermissionMsg());
            return ret;

        } catch (IllegalAccessException | InvocationTargetException e){
            e.printStackTrace( );
            return false;
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args){
        if (tabCompletion == null)
            return new ArrayList<>();

        try {
            CommandContext context = new CommandContext(sender, args, this);
            return (List<String>) tabCompletion.invoke(object, context);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
