package ares06.inactivityshutdown;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Inactivityshutdown extends JavaPlugin {
    private int inactivityTimeout; // in seconds
    private int taskId;

    @Override
    public void onEnable() {
        saveDefaultConfig(); // Salva il file di configurazione predefinito se non esiste

        FileConfiguration config = getConfig();
        inactivityTimeout = config.getInt("inactivityTimeout", 1800); // Prende il valore dal file di configurazione, default 1800 secondi (30 minuti)

        getServer().getPluginManager().registerEvents(new PlayerActivityListener(), this);

        startInactivityCheckTask();
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
            if (PlayerActivityListener.getLastActivityTime() + (inactivityTimeout * 1000) < System.currentTimeMillis()) {
                Bukkit.shutdown(); // Spegni il server se non ci sono state attività nell'intervallo di tempo specificato
            }
        }, 0L, 20L * 60L); // Controlla ogni minuto
    }
}
