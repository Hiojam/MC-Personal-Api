package dev.roxxion.api.listeners;

import dev.roxxion.api.inventories.Menu;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;

public class MenuListener implements Listener {

    @EventHandler
    public void onMenuClick(InventoryClickEvent e){

        if (e.getClickedInventory() == null)
            return;

        InventoryHolder holder = e.getInventory().getHolder();

        if (holder instanceof Menu){

            Menu menu = (Menu) holder;

            if(menu.cancelInteractions())
                e.setCancelled(true);

            if ( e.getClickedInventory().getType() == InventoryType.PLAYER && !menu.interactPlayerInv())
                return;

            if (e.getCurrentItem() == null)
                return;

            if (e.getCurrentItem().getType() == Material.AIR)
                return;

            menu.handleMenu(e);
        }
    }
}
