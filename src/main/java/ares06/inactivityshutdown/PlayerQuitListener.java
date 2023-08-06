package ares06.inactivityshutdown;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    Inactivityshutdown mainPlugin;

    public PlayerQuitListener(Inactivityshutdown mainPlugin) {
        this.mainPlugin = mainPlugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        mainPlugin.onPlayerQuit();
    }
}
