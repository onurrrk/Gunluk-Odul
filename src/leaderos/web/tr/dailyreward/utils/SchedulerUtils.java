package leaderos.web.tr.dailyreward.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class SchedulerUtils {

    private static final boolean IS_FOLIA;

    static {
        boolean folia;
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            folia = true;
        } catch (ClassNotFoundException e) {
            folia = false;
        }
        IS_FOLIA = folia;
    }

    public static void runAsync(Plugin plugin, Runnable task) {
        if (IS_FOLIA) {
            Bukkit.getAsyncScheduler().runNow(plugin, ignored -> task.run());
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, task);
        }
    }

    public static void runSync(Plugin plugin, Runnable task) {
        if (IS_FOLIA) {
            Bukkit.getGlobalRegionScheduler().execute(plugin, task);
        } else {
            Bukkit.getScheduler().runTask(plugin, task);
        }
    }

    public static void runSyncForPlayer(Plugin plugin, Player player, Runnable task) {
        if (IS_FOLIA) {
            Bukkit.getRegionScheduler().execute(plugin, player.getLocation(), task);
        } else {
            Bukkit.getScheduler().runTask(plugin, task);
        }
    }
}
