package ares06.inactivityshutdown;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerActivityListener implements Listener {
    private static long lastActivityTime = System.currentTimeMillis();

    public PlayerActivityListener() {

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        lastActivityTime = System.currentTimeMillis();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        lastActivityTime = System.currentTimeMillis();
    }

    public static long getLastActivityTime() {
        return lastActivityTime;
    }
}