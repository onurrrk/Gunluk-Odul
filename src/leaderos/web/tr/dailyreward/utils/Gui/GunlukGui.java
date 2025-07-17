package leaderos.web.tr.dailyreward.utils.Gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import leaderos.web.tr.dailyreward.Main;
import leaderos.web.tr.dailyreward.utils.Manager;
import leaderos.web.tr.dailyreward.utils.RewardManager;
import leaderos.web.tr.dailyreward.utils.Timer;
import leaderos.web.tr.dailyreward.utils.objects.Rewards;

public class GunlukGui {
	
	@SuppressWarnings("deprecation")
	public static void create(Player player) {
		// Inventory holder
		Inventory gui = Bukkit.getServer().createInventory(player, 27, (Main.color( Manager.getText("Lang", "Gui.name")) ));
		
		List<Rewards> rewards = Main.rewards;
		
		if (!rewards.isEmpty() && rewards != null)
		{
			
			int playerLootID = RewardManager.cache.get(player.getName()).getStreak();
			
			for (int i = 0; i < rewards.size(); i++)
			{
				
				Material itemMaterial = rewards.get(i).getMaterial();
				
				boolean hasGlow = false;
				
				String status = Manager.getText("Lang", "Gui.rewardCannot");
				
				if (playerLootID == i)
				{
					
					if (RewardManager.canPlayerTakeLoot(player.getName()))
						status = Manager.getText("Lang", "Gui.rewardATM");
					
					else
						status = "&c" + Timer.getToTake();
					
				}
				
				else if (playerLootID > i)
				{
					
					status = Manager.getText("Lang", "Gui.rewardTooked");
					
					itemMaterial = rewards.get(i).getTookedMaterial();
					
					hasGlow = true;
					
				}
				
				String name = rewards.get(i).getDisplayName();
				
				List<String> lore = new ArrayList<String>();
				
				for (String loreAdder : rewards.get(i).getLore())
					lore.add(loreAdder.replace("{status}", status));
				
				ItemStack toSet = GuiManager.defaultItem(lore, name, itemMaterial, hasGlow);
				
				gui.setItem(11+i, toSet);
				
			}
			
		}
		
		gui.setItem(9, GuiManager.helpChest());
		
		ItemStack fillItem = new ItemStack(Material.getMaterial(Manager.getText("Lang", "fillItem.material")), 1);
		
		if (Manager.isSet("Lang", "fillItem.damage"))
		fillItem.setDpackage leaderos.web.tr.dailyreward.utils.Gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import leaderos.web.tr.dailyreward.Main;
import leaderos.web.tr.dailyreward.utils.Manager;
import leaderos.web.tr.dailyreward.utils.RewardManager;
import leaderos.web.tr.dailyreward.utils.Timer;
import leaderos.web.tr.dailyreward.utils.Gui.GuiManager;
import leaderos.web.tr.dailyreward.utils.objects.Rewards;

public class GunlukGui {

	@SuppressWarnings("deprecation")
	public static void create(Player player) {
		if (isFolia()) {
			createFolia(player);
		} else {
			createNormal(player);
		}
	}

	private static void createNormal(Player player) {
		Inventory gui = Bukkit.getServer().createInventory(player, 27, Main.color(Manager.getText("Lang", "Gui.name")));

		List<Rewards> rewards = Main.rewards;
		if (rewards == null || rewards.isEmpty()) {
			// Artık mesaj atmıyoruz, GUI açılacak bariyer görünecek.
			rewards = new ArrayList<>();
		}

		int playerLootID = 0;
		if (RewardManager.cache.get(player.getName()) != null) {
			playerLootID = RewardManager.cache.get(player.getName()).getStreak();
		}

		for (int i = 0; i < rewards.size(); i++) {
			Rewards reward = rewards.get(i);

			Material itemMaterial = reward.getMaterial();
			if (itemMaterial == null) {
				itemMaterial = Material.BARRIER;
			}

			Material tookedMaterial = reward.getTookedMaterial();
			if (tookedMaterial == null) {
				tookedMaterial = Material.BARRIER;
			}

			boolean hasGlow = false;
			String status = Manager.getText("Lang", "Gui.rewardCannot");

			if (playerLootID == i) {
				if (RewardManager.canPlayerTakeLoot(player.getName())) {
					status = Manager.getText("Lang", "Gui.rewardATM");
				} else {
					status = "&c" + Timer.getToTake();
				}
			} else if (playerLootID > i) {
				status = Manager.getText("Lang", "Gui.rewardTooked");
				itemMaterial = tookedMaterial;
				hasGlow = true;
			}

			String name = reward.getDisplayName() != null ? reward.getDisplayName() : "Bilinmeyen Ödül";
			List<String> lore = new ArrayList<>();
			if (reward.getLore() != null) {
				for (String loreAdder : reward.getLore()) {
					lore.add(loreAdder.replace("{status}", status));
				}
			}

			ItemStack toSet = GuiManager.defaultItem(lore, name, itemMaterial, hasGlow);
			gui.setItem(11 + i, toSet);
		}

		gui.setItem(9, GuiManager.helpChest());

		Material fillMaterial = Material.getMaterial(Manager.getText("Lang", "fillItem.material"));
		if (fillMaterial == null) fillMaterial = Material.GRAY_STAINED_GLASS_PANE;

		ItemStack fillItem = new ItemStack(fillMaterial, 1);

		if (Manager.isSet("Lang", "fillItem.damage")) {
			fillItem.setDurability((short) Manager.getInt("Lang", "fillItem.damage"));
		}

		for (int i = 0; i <= 18; i++) {
			if (i == 9) {
				gui.setItem(1 + i, fillItem);
			} else if (i < 9) {
				gui.setItem(i, fillItem);
			} else {
				gui.setItem(8 + i, fillItem);
			}
		}

		player.openInventory(gui);
	}

	private static void createFolia(Player player) {
		Inventory gui = Bukkit.createInventory(player, 27, Main.color(Manager.getText("Lang", "Gui.name")));

		List<Rewards> rewards = Main.rewards;
		if (rewards == null || rewards.isEmpty()) {
			rewards = new ArrayList<>();
		}

		int playerLootID = 0;
		if (RewardManager.cache.get(player.getName()) != null) {
			playerLootID = RewardManager.cache.get(player.getName()).getStreak();
		}

		for (int i = 0; i < rewards.size(); i++) {
			Rewards reward = rewards.get(i);

			Material itemMaterial = reward.getMaterial();
			if (itemMaterial == null) {
				itemMaterial = Material.BARRIER;
			}

			Material tookedMaterial = reward.getTookedMaterial();
			if (tookedMaterial == null) {
				tookedMaterial = Material.BARRIER;
			}

			boolean hasGlow = false;
			String status = Manager.getText("Lang", "Gui.rewardCannot");

			if (playerLootID == i) {
				if (RewardManager.canPlayerTakeLoot(player.getName())) {
					status = Manager.getText("Lang", "Gui.rewardATM");
				} else {
					status = "&c" + Timer.getToTake();
				}
			} else if (playerLootID > i) {
				status = Manager.getText("Lang", "Gui.rewardTooked");
				itemMaterial = tookedMaterial;
				hasGlow = true;
			}

			String name = reward.getDisplayName() != null ? reward.getDisplayName() : "Bilinmeyen Ödül";
			List<String> lore = new ArrayList<>();
			if (reward.getLore() != null) {
				for (String loreAdder : reward.getLore()) {
					lore.add(loreAdder.replace("{status}", status));
				}
			}

			ItemStack toSet = GuiManager.defaultItem(lore, name, itemMaterial, hasGlow);
			gui.setItem(11 + i, toSet);
		}

		gui.setItem(9, GuiManager.helpChest());

		Material fillMaterial = Material.getMaterial(Manager.getText("Lang", "fillItem.material"));
		if (fillMaterial == null) fillMaterial = Material.GRAY_STAINED_GLASS_PANE;

		ItemStack fillItem = new ItemStack(fillMaterial, 1);

		if (Manager.isSet("Lang", "fillItem.damage")) {
			fillItem.setDurability((short) Manager.getInt("Lang", "fillItem.damage"));
		}

		for (int i = 0; i <= 18; i++) {
			if (i == 9) {
				gui.setItem(1 + i, fillItem);
			} else if (i < 9) {
				gui.setItem(i, fillItem);
			} else {
				gui.setItem(8 + i, fillItem);
			}
		}

		player.openInventory(gui);
	}

	private static boolean isFolia() {
		try {
			Class.forName("io.papermc.paper.threadedregions.RegionScheduler");
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}
}
urability((short) Manager.getInt("Lang", "fillItem.damage"));
		
		for (int i = 0; i<=18; i++)
		{
			
			if (i == 9)
				gui.setItem(1+i, fillItem);
			
			else if (i < 9)
				gui.setItem(i, fillItem);
			
			else
				gui.setItem(8+i, fillItem);
			
		}
		
		player.openInventory(gui);
		
	}

}
