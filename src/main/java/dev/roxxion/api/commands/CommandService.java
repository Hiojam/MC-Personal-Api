package dev.roxxion.api.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

public class CommandService {

    private CommandMap commandMap;

    public CommandService(){
        Field bukkitCommandMap;
        try {
            bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void registerCommands(Object object){
        Class < ? > klass = object.getClass();
        HashMap<String, Method> tabCompleter = new HashMap<>();

        for (Method method : klass.getMethods()){
            if(!method.isAnnotationPresent(TabCompleter.class)) continue;
            TabCompleter completer = method.getAnnotation(TabCompleter.class);
            tabCompleter.put(completer.command(), method);
        }
        for (Method method : klass.getMethods()){
            if (!method.isAnnotationPresent(Command.class)) continue;
            Command annotation = method.getAnnotation(Command.class);
            Method tabCompletion = null;

            if (tabCompleter.containsKey(annotation.name()))
                tabCompletion = tabCompleter.get(annotation.name());

            CommandInfo commandInfo = new CommandInfo(object, method, annotation, tabCompletion);
            commandMap.register(commandInfo.getName(), commandInfo);
        }
    }
}
