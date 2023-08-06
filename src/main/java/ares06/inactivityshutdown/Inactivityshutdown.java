package ares06.inactivityshutdown;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;

public final class Inactivityshutdown extends JavaPlugin {
    private int inactivityTimeout; // in seconds
    private int taskId;
    private boolean pluginEnabled;

    @Override
    public void onEnable() {
        saveDefaultConfig(); // Save the default configuration file if it doesn't exist

        FileConfiguration config = getConfig();
        inactivityTimeout = config.getInt("inactivityTimeout", 1800); // Get value from the configuration file, default 1800 seconds (30 minutes)
        pluginEnabled = config.getBoolean("Enabled", false); // Get the plugin's state from the configuration file, default disabled

        if (pluginEnabled) {
            getServer().getPluginManager().registerEvents(new PlayerActivityListener(), this);
            startInactivityCheckTask();
        }
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTask(taskId);
    }

    public int getInactivityTimeout() {
        return inactivityTimeout;
    }

    private void startInactivityCheckTask() {
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            long currentTime = System.currentTimeMillis();
            if (pluginEnabled && PlayerActivityListener.getLastActivityTime() + (inactivityTimeout * 1000) < currentTime) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getLastPlayed() + (inactivityTimeout * 1000) >= currentTime) {
                        // At least one active player found, do not shutdown the server
                        return;
                    }
                }
                Bukkit.shutdown(); // Shutdown the server only if no active players were found
            }
        }, 0L, 20L * 60L); // Check every minute
    }
}
