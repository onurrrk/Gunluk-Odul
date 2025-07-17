package leaderos.web.tr.dailyreward;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import leaderos.web.tr.dailyreward.commands.Commands;
import leaderos.web.tr.dailyreward.database.ConnectionPool;
import leaderos.web.tr.dailyreward.database.DatabaseQueries;
import leaderos.web.tr.dailyreward.utils.Manager;
import leaderos.web.tr.dailyreward.utils.Placeholders;
import leaderos.web.tr.dailyreward.utils.SchedulerUtils;
import leaderos.web.tr.dailyreward.utils.Timer;
import leaderos.web.tr.dailyreward.utils.enums.RequirementType;
import leaderos.web.tr.dailyreward.utils.objects.FireworkObject;
import leaderos.web.tr.dailyreward.utils.objects.Requirement;
import leaderos.web.tr.dailyreward.utils.objects.Rewards;

public class Main extends JavaPlugin {

	public static Main instance;
	public static List<Rewards> rewards = new ArrayList<>();

	@Override
	public void onEnable() {
		instance = this;
		licenseCheck();
	}

	@Override
	public void onDisable() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			DatabaseQueries.savePlayerAllCache(p.getName());
		}
	}

	public static String color(String text) {
		return ChatColor.translateAlternateColorCodes('&', text);
	}

	private void licenseCheck() {
		Bukkit.getConsoleSender().sendMessage(color("&2&l\tGUNLUK ODUL \t&b"));
		Bukkit.getConsoleSender().sendMessage(color("&3Developed by &9Geik"));
		Bukkit.getConsoleSender().sendMessage(color("&3Discord: &9discord.gg/h283guX"));
		Bukkit.getConsoleSender().sendMessage(color("&3Web: &9https://geik.xyz"));
		enableShortcut();
	}

	private static void enableShortcut() {
		instance.saveDefaultConfig();
		Manager.FileChecker("Lang");
		ConnectionPool.initsqlite();
		DatabaseQueries.createTable();
		instance.getCommand("günlüködül").setExecutor(new Commands(instance));
		Bukkit.getPluginManager().registerEvents(new Listeners(instance), instance);

		if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
			new Placeholders().register();
		}

		RewardsInserter();
		Timer.taskAgain();
		new Metrics(instance, 7946);

		if (!Bukkit.getOnlinePlayers().isEmpty()) {
			Listeners listenerInstance = new Listeners(instance);
			SchedulerUtils.runAsync(instance, () -> {
				for (Player p : Bukkit.getOnlinePlayers()) {
					listenerInstance.insertPlayerValues(p);
				}
			});
		}
	}

	public void safeReload() {
		rewards.clear();
		RewardsInserter();
	}

	public static void RewardsInserter() {
		FileConfiguration cfg = instance.getConfig();

		for (String dataName : cfg.getConfigurationSection("Rewards").getKeys(false)) {
			Material material = Material.DIAMOND;
			Material tookedMaterial = Material.MINECART;

			if (Material.getMaterial(cfg.getString("Rewards." + dataName + ".material")) != null)
				material = Material.getMaterial(cfg.getString("Rewards." + dataName + ".material"));

			if (Material.getMaterial(cfg.getString("Rewards." + dataName + ".tookedMaterial")) != null)
				tookedMaterial = Material.getMaterial(cfg.getString("Rewards." + dataName + ".tookedMaterial"));

			FireworkObject firework = new FireworkObject(false, 1);

			if (cfg.isSet("Rewards." + dataName + ".firework.deploy"))
				firework.setEnabled(cfg.getBoolean("Rewards." + dataName + ".firework.deploy"));

			if (cfg.isSet("Rewards." + dataName + ".firework.amount"))
				firework.setAmount(cfg.getInt("Rewards." + dataName + ".firework.amount"));

			String displayName = color(cfg.getString("Rewards." + dataName + ".name"));
			List<String> loreUp = new ArrayList<>();
			for (String loreAdder : cfg.getStringList("Rewards." + dataName + ".lore"))
				loreUp.add(color(loreAdder));

			List<String> commands = cfg.getStringList("Rewards." + dataName + ".commands");
			Requirement requirements = null;

			if (cfg.isSet("Rewards." + dataName + ".requirement")) {
				for (String req : cfg.getConfigurationSection("Rewards." + dataName + ".requirement").getKeys(false)) {
					if (req.equalsIgnoreCase("reward")) continue;

					RequirementType type = req.equalsIgnoreCase("permission") ? RequirementType.PERMISSION : RequirementType.PLACEHOLDER;
					Object value = type.equals(RequirementType.PLACEHOLDER)
							? cfg.getInt("Rewards." + dataName + ".requirement." + req)
							: cfg.getString("Rewards." + dataName + ".requirement." + req);

					List<String> rewardsList = cfg.isSet("Rewards." + dataName + ".requirement.reward")
							? cfg.getStringList("Rewards." + dataName + ".requirement.reward")
							: commands;

					requirements = new Requirement(type, value, req, rewardsList);
				}
			}

			Rewards reward = new Rewards(dataName, displayName, loreUp, requirements, firework, material, tookedMaterial, commands);
			rewards.add(reward);
		}
	}
}
