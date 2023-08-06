package ares06.inactivityshutdown;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    Inactivityshutdown mainPlugin;

    public PlayerJoinListener(Inactivityshutdown mainPlugin) {
        this.mainPlugin = mainPlugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        mainPlugin.onPlayerJoin();
    }
}
