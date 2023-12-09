package dev.roxxion.api;

import dev.roxxion.api.commands.CommandService;
import dev.roxxion.api.inventories.IPlayerMenuUtility;
import dev.roxxion.api.inventories.PlayerMenuUtility;
import dev.roxxion.api.services.TaskService;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;

public final class Api {

    private static final HashMap<Player, IPlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();
    @Getter
    private static final Config config = new Config();
    @Getter
    private static final CommandService commandService = new CommandService();
    @Getter
    private static final TaskService taskService;

    static {
        taskService = new TaskService(Main.getPlugin(Main.class));
    }

    public static IPlayerMenuUtility getPlayerMenuUtility(Player p){
        IPlayerMenuUtility playerMenuUtility;

        if (playerMenuUtilityMap.containsKey(p))
            return playerMenuUtilityMap.get(p);

        playerMenuUtility = new PlayerMenuUtility(p);
        playerMenuUtilityMap.put(p, playerMenuUtility);
        return playerMenuUtility;
    }

}
