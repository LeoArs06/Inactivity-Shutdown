package ares06.inactivityshutdown;


import java.util.logging.Logger;
import java.util.Timer;
import java.util.TimerTask;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class Inactivityshutdown extends JavaPlugin {
    private Logger logger;
    private int idleWaitTime;
    private Timer idleTimer;

    private final Listener playerQuitListener = new PlayerQuitListener(this);
    private final Listener playerJoinListener = new PlayerJoinListener(this);

    @Override
    public void onEnable() {
        logger = getLogger();
        saveDefaultConfig();
        idleWaitTime = Math.max(0, getConfig().getInt("idle_wait_time"));
        logger.info("This server is running IdleShutdown: It will stop after " + idleWaitTime + " seconds with no player online.");

        registerEvent(playerQuitListener);

        if (noPlayerOnline()) {
            logger.fine("There are no players online!");
            startIdleTimer();
        }
    }

    @Override
    public void onDisable() {
        cancelIdleTimer();
        unregisterAllListeners();
    }

    public void onTimerExpired() {
        logger.fine("Timer expired!");

        if (noPlayerOnline()) {
            logAndShutdown("No players online, shutting down");
        } else {
            logger.warning("A player has come online and we have not been notified! Something is wrong.");
        }
    }

    void onPlayerQuit() {
        logger.fine("A player has quit!");

        if (lastPlayerOnline()) {
            logger.info("The last player is leaving!");
            startIdleTimer();
        }
    }

    void onPlayerJoin() {
        logger.fine("A player has joined!");

        unregisterAllListeners();
        cancelIdleTimer();
    }

    private void registerEvent(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }

    private boolean lastPlayerOnline() {
        return getServer().getOnlinePlayers().size() <= 1;
    }

    private boolean noPlayerOnline() {
        return getServer().getOnlinePlayers().isEmpty();
    }

    private void cancelIdleTimer() {
        if (idleTimer != null) {
            logger.finer("Cancelling timer...");
            idleTimer.cancel();
        }
    }

    private void unregisterAllListeners() {
        logger.finer("Unregistering listeners...");
        HandlerList.unregisterAll(this);
    }

    private void logAndShutdown(String message) {
        logger.info(message);
        unregisterAllListeners();
        getServer().shutdown();
    }

    private void startIdleTimer() {
        if (idleWaitTime == 0) {
            logger.fine("idle_wait_time is 0, not scheduling timer!");
            onTimerExpired();
        } else {
            registerEvent(playerJoinListener);

            logger.finer("Creating and scheduling timer...");
            idleTimer = new Timer();
            TimerTask idleTimerTask = new TimerTask() {
                @Override
                public void run() {
                    onTimerExpired();
                }
            };
            idleTimer.schedule(idleTimerTask, idleWaitTime * 1000);
        }
    }
}