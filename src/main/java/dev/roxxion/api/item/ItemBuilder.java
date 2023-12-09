package dev.roxxion.api.item;

import com.cryptomorin.xseries.XMaterial;
import lombok.Getter;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemBuilder {

    private final ItemStack itemStack;
    private String name;
    private List<String> lore;
    private boolean unbreakable = false;
    private int amount;
    @Getter
    private int customModelData;
    private List<ItemFlag> flags = new ArrayList<>();

    public ItemBuilder(XMaterial material){
        this.itemStack = material.parseItem();
    }

    public ItemBuilder(ItemStack item){
        this.itemStack = item;

        if(item.hasItemMeta()){
            ItemMeta meta = item.getItemMeta();
            assert meta != null;
            this.name = meta.getDisplayName();
            this.lore = meta.getLore();
            this.unbreakable = meta.isUnbreakable();
            this.flags = new ArrayList<>(meta.getItemFlags());
        }
    }

    public ItemBuilder setName(String name){
        this.name = name;
        return this;
    }

    public ItemBuilder setLore(List<String> lore){
        this.lore = lore;
        return this;
    }

    public ItemBuilder addLoreLine(String line){
        if(this.lore == null) this.lore = new ArrayList<>();
        this.lore.add(line);
        return this;
    }

    public ItemBuilder removeLoreLine(int line){
        this.lore.remove(line);
        return this;
    }

    public ItemBuilder setAmount(int amount){
        this.amount = amount;
        return this;
    }

    public ItemBuilder setUnbreakable(boolean value){
        this.unbreakable = value;
        return this;
    }

    public ItemBuilder addFlag(ItemFlag flag){
        this.flags.add(flag);
        return this;
    }

    public ItemBuilder addFlags(ItemFlag... flag){
        this.flags.addAll(Arrays.asList(flag));
        return this;
    }

    public ItemBuilder removeFlag(ItemFlag flag){
        this.flags.remove(flag);
        return this;
    }

    public ItemBuilder setCustomModelData(int id){
        customModelData = id;
        return this;
    }

    public ItemStack build(){
        ItemStack item = this.itemStack;
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        if(this.name != null) meta.setDisplayName(this.name);
        if(this.lore != null) meta.setLore(this.lore);
        if(this.amount != 0) item.setAmount(this.amount);
        meta.setUnbreakable(this.unbreakable);
        if(!this.flags.isEmpty()) flags.forEach(meta::addItemFlags);

        meta.setCustomModelData(customModelData);
        item.setItemMeta(meta);
        return item;
    }
}
