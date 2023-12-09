package dev.roxxion.api.services;

import dev.roxxion.api.Main;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scheduler.BukkitWorker;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class TaskService {

    private final Main plugin;

    public TaskService(Main plugin){
        this.plugin = plugin;
    }

    public int scheduleSyncDelayedTask(Runnable run, long delay){
        return Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, run, delay);
    }

    public int scheduleSyncDelayedTask(Runnable run){
        return Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, run);
    }

    public int scheduleSyncRepeatingTask(Runnable run, long delay, long time){
        return Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, run, delay, time);
    }

    public <T> Future<T> callSyncMethod(Callable<T> call){
        return Bukkit.getScheduler().callSyncMethod(plugin, call);
    }

    public void cancelTask(int id){
        Bukkit.getScheduler().cancelTask(id);
    }

    public void cancelTasks(){
        Bukkit.getScheduler().cancelTasks(plugin);
    }

    public void cancelAllTasks(){
        Bukkit.getScheduler().cancelTasks(plugin);
    }

    public boolean isCurrentlyRunning(int id){
        return Bukkit.getScheduler().isCurrentlyRunning(id);
    }

    public boolean isQueued(int id) {
        return Bukkit.getScheduler().isQueued(id);
    }

    public List<BukkitWorker> getActiveWorkers(){
        return Bukkit.getScheduler().getActiveWorkers();
    }


    public List<BukkitTask> getPendingTasks(){
        return Bukkit.getScheduler().getPendingTasks();
    }

    public BukkitTask runTask(Runnable run) throws IllegalArgumentException{
        return Bukkit.getScheduler().runTask(plugin, run);
    }

    public BukkitTask runTaskAsynchronously(Runnable run) throws IllegalArgumentException {
        return Bukkit.getScheduler().runTaskAsynchronously(plugin, run);
    }

    public BukkitTask runTaskLater(Runnable run, long time) throws IllegalArgumentException {
        return Bukkit.getScheduler().runTaskLater(plugin, run, time);
    }

    public BukkitTask runTaskLaterAsynchronously(Runnable run, long delay) throws IllegalArgumentException {
        return Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, run, delay);
    }

    public BukkitTask runTaskTimer(Runnable run, long delay, long time) throws IllegalArgumentException {
        return Bukkit.getScheduler().runTaskTimer(plugin, run, delay, time);
    }

    public BukkitTask runTaskCertainTimes(Runnable run, long delay, long time, int times) throws IllegalArgumentException {
        return new BukkitRunnable() {

            private int i = 1;
            @Override
            public void run() {
                if(i == times){
                    cancel();
                }
                i++;
                run.run();
            }
        }.runTaskTimer(plugin, delay, time);
    }
    public BukkitTask runTaskTimerAsynchronously(Runnable run, long delay, long time) throws IllegalArgumentException {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, run, delay, time);
    }
}
