package dev.roxxion.api.modules;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class Module{
    private final JavaPlugin instance;

    protected Module(JavaPlugin instance){
        this.instance = instance;
    }

    public abstract void onLoad();
    public abstract void onUnload();
    public abstract String getModuleName();

    protected JavaPlugin getInstance(){
        return instance;
    }
}
