package dev.roxxion.api;

import dev.roxxion.api.commands.CommandService;
import dev.roxxion.api.inventories.IPlayerMenuUtility;
import dev.roxxion.api.inventories.PlayerMenuUtility;
import org.bukkit.entity.Player;

import java.util.HashMap;

public final class Api {

    private static final HashMap<Player, IPlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();
    private static final Config config = new Config();
    private static final CommandService commandService = new CommandService();

    public static IPlayerMenuUtility getPlayerMenuUtility(Player p){
        IPlayerMenuUtility playerMenuUtility;

        if (playerMenuUtilityMap.containsKey(p))
            return playerMenuUtilityMap.get(p);

        playerMenuUtility = new PlayerMenuUtility(p);
        playerMenuUtilityMap.put(p, playerMenuUtility);
        return playerMenuUtility;
    }

    public static Config getConfig(){
        return config;
    }

    public static CommandService getCommandService(){
        return commandService;
    }
}
