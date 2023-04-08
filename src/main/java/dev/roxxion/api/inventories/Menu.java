package dev.roxxion.api.inventories;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
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

    protected ItemStack createCustomSkull(String name, String head ){
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal());
        SkullMeta itemmeta = (SkullMeta) item.getItemMeta();

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", head));
        Field field;
        try {
            field = itemmeta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(itemmeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException x){
            x.printStackTrace();
        }
        itemmeta.setDisplayName(name);

        item.setItemMeta(itemmeta);
        return item;
    }

    protected ItemStack createCustomSkull(String name, List<String> lore, String head){
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal());
        SkullMeta itemmeta = (SkullMeta) item.getItemMeta();

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", head));
        Field field;
        try {
            field = itemmeta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(itemmeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException x){
            x.printStackTrace();
        }
        itemmeta.setLore(lore);
        itemmeta.setDisplayName(name);

        item.setItemMeta(itemmeta);
        return item;
    }
}
