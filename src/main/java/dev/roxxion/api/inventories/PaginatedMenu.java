package dev.roxxion.api.inventories;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * Represents a Menu with many pages.
 */
public abstract class PaginatedMenu extends Menu {

    protected int page = 0;

    //28 empty slots per page
    protected int maxItemsPerPage = 28;

    protected int index = 0;

    protected ItemStack FILLER_GLASS = createFiller( );

    public PaginatedMenu( IPlayerMenuUtility playerMenuUtility ){
        super( playerMenuUtility );
    }

    public void addMenuBorder(){
        String left = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODY1MmUyYjkzNmNhODAyNmJkMjg2NTFkN2M5ZjI4MTlkMmU5MjM2OTc3MzRkMThkZmRiMTM1NTBmOGZkYWQ1ZiJ9fX0=";
        String right = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmEzYjhmNjgxZGFhZDhiZjQzNmNhZThkYTNmZTgxMzFmNjJhMTYyYWI4MWFmNjM5YzNlMDY0NGFhNmFiYWMyZiJ9fX0=";
        addMenuBorder(left, right);
    }

    public void addMenuBorder(String leftSkull, String rightSkull){

        if (page == 0){
            inventory.setItem(48, FILLER_GLASS);
        } else {
            ItemStack left = createCustomSkull("§a§lLeft", leftSkull);

            inventory.setItem( 48 , left );
        }

        ItemStack right = createCustomSkull("§a§lRight", rightSkull);

        inventory.setItem(50 , right);

        ItemStack close = new ItemStack(Material.BARRIER, 1);
        ItemMeta closemeta = close.getItemMeta();
        closemeta.setDisplayName("§c§lClose");
        close.setItemMeta(closemeta);

        inventory.setItem(49, close);

        for (int i = 0; i < 10; i++){
            if (inventory.getItem(i) == null){
                inventory.setItem(i, FILLER_GLASS);
            }
        }

        inventory.setItem(17 , FILLER_GLASS);
        inventory.setItem(18 , FILLER_GLASS);
        inventory.setItem(26 , FILLER_GLASS);
        inventory.setItem(27 , FILLER_GLASS);
        inventory.setItem(35 , FILLER_GLASS);
        inventory.setItem(36 , FILLER_GLASS);

        for (int i = 44; i < 54; i++){
            if (inventory.getItem(i) == null){
                inventory.setItem(i, FILLER_GLASS);
            }
        }

    }

    public void addDefaultButtonsListener(List<?> list, InventoryClickEvent e){
        if (e.getCurrentItem().getType().equals(Material.BARRIER)){
            e.getWhoClicked().closeInventory();
        } else if (e.getCurrentItem().getType().equals(XMaterial.PLAYER_HEAD.parseMaterial())){
            if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Left")){

                if (page != 0){
                    page = page - 1;
                    super.open();
                }

            } else if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Right" )){
                if (!((index + 1) >= list.size())){
                    page = page + 1;
                    super.open();
                }
            }
        }
    }

    public void addPagedItems(List<?> list, Runnable run){
        if(list.isEmpty()) return;

        for (int i = 0; i < maxItemsPerPage; i++){
            index = maxItemsPerPage * page + i;
            if (index >= list.size()) break;
            if (list.get(index) != null){
                run.run();
            }
        }
    }

    private ItemStack createFiller(){
        assert XMaterial.BLACK_STAINED_GLASS_PANE.parseMaterial() != null;
        ItemStack FILLER_GLASS = new ItemStack(XMaterial.BLACK_STAINED_GLASS_PANE.parseMaterial(), 1, (short) 15);
        ItemMeta fillermeta = FILLER_GLASS.getItemMeta();
        fillermeta.setDisplayName(" ");
        FILLER_GLASS.setItemMeta(fillermeta);

        return FILLER_GLASS;
    }


}
