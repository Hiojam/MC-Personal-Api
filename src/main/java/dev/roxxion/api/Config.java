package dev.roxxion.api;

public class Config {

    private String noPermission = "You don't have permission to execute this command.";

    public void setNoPermissionMessage(String message){
        this.noPermission = message;
    }

    public String getNoPermissionMsg(){
        return this.noPermission;
    }
}
