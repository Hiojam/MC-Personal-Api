package dev.roxxion.api.inventories;

import org.bukkit.entity.Player;

public interface IPlayerMenuUtility {

    Player getOwner();

    void setOwner(Player owner);

    Player getTarget();

    void setTarget(Player target);

}
