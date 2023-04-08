package dev.roxxion.api.inventories;

import org.bukkit.entity.Player;

public class PlayerMenuUtility implements IPlayerMenuUtility {

    private Player owner;
    private Player target;

    public PlayerMenuUtility(Player owner){
        this.owner = owner;
    }

    public Player getOwner(){
        return this.owner;
    }

    public void setOwner(Player owner){
        this.owner = owner;
    }


    public Player getTarget(){
        return target;
    }

    public void setTarget(Player target){
        this.target = target;
    }
}
