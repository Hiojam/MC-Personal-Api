package dev.roxxion.api.modules;

public class ModuleInfo {
    private String absolutePath, main;

    public String getMain(){
        return this.main;
    }

    public String getAbsolutePath(){
        return this.absolutePath;
    }
    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }
}
