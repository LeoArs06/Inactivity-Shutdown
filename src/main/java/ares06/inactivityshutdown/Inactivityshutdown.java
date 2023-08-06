package ares06.inactivityshutdown;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;


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
            long currentTime = System.currentTimeMillis();
            if (PlayerActivityListener.getLastActivityTime() + (inactivityTimeout * 1000) < currentTime) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getLastPlayed() + (inactivityTimeout * 1000) >= currentTime) {
                        // Almeno un giocatore attivo trovato, non spegnere il server
                        return;
                    }
                }
                Bukkit.shutdown(); // Spegni il server solo se nessun giocatore attivo è stato trovato
            }
        }, 0L, 20L * 60L); // Controlla ogni minuto
    }
}
