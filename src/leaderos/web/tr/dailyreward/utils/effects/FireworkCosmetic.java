package leaderos.web.tr.dailyreward.utils.effects;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import leaderos.web.tr.dailyreward.Main;

import java.util.function.Consumer;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;


public class FireworkCosmetic {

	public static void spawn(Location location, int amount) {
		if (location == null || location.getWorld() == null) {
			return;
		}

		World world = location.getWorld();
		Plugin plugin = Main.instance;

		boolean isFolia = false;
		try {
			Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
			isFolia = true;
		} catch (ClassNotFoundException e) {
			isFolia = false;
		}

		Runnable fireworkSpawnLogic = () -> {
			for (int i = 0; i < amount; i++) {
				Firework firework = (Firework) world.spawnEntity(location, EntityType.FIREWORK);
				FireworkMeta meta = firework.getFireworkMeta();

				meta.setPower(2);
				meta.addEffect(FireworkEffect.builder()
						.withColor(Color.LIME)
						.flicker(true)
						.build()
				);

				firework.setFireworkMeta(meta);
			}
		};

		if (isFolia) {
			Consumer<ScheduledTask> foliaTask = scheduledTask -> fireworkSpawnLogic.run();
			Bukkit.getGlobalRegionScheduler().runDelayed(plugin, foliaTask, 5L);
		} else {
			new BukkitRunnable() {
				@Override
				public void run() {
					fireworkSpawnLogic.run();
				}
			}.runTask(plugin);
		}
	}
}
