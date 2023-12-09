package dev.roxxion.api.inventories;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.UUID;

public abstract class Menu implements InventoryHolder {

    protected Inventory inventory;

    protected IPlayerMenuUtility playerMenuUtility;

    public Menu(IPlayerMenuUtility playerMenuUtility){
        this.playerMenuUtility = playerMenuUtility;
    }

    public abstract String getMenuName();

    public abstract int getSlots();

    public abstract void handleMenu(InventoryClickEvent e);

    public abstract void setMenuItems();

    public boolean overridePlayerInv(){
        return true;
    }

    public boolean interactPlayerInv(){
        return false;
    }

    public boolean cancelInteractions(){
        return true;
    }

    public void open(){
        inventory = Bukkit.createInventory(this , getSlots(), getMenuName());

        this.setMenuItems();

        playerMenuUtility.getOwner().openInventory(inventory);
    }

    @Override
    public Inventory getInventory(){
        return inventory;
    }

    protected ItemStack createCustomSkull(String name, String head){
        return createCustomSkull(name, null, head);
    }

    protected ItemStack createCustomSkull(String name, List<String> lore, String head){
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta itemMeta = (SkullMeta) item.getItemMeta();

        if (itemMeta == null)
            return null;

        PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID(), null);

        URL url;
        try{
            url = new URL("https://textures.minecraft.net/texture/" + head);
        }catch (MalformedURLException e) {
            return null;
        }
        profile.getTextures().setSkin(url);
        itemMeta.setOwnerProfile(profile);

        if (lore != null)
            itemMeta.setLore(lore);

        itemMeta.setDisplayName(name);

        item.setItemMeta(itemMeta);
        return item;
    }
}
