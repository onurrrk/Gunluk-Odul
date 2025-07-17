package leaderos.web.tr.dailyreward.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import leaderos.web.tr.dailyreward.Main;

public class Title {

	public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
		if (player == null) return;
		if (title == null) title = "";
		if (subtitle == null) subtitle = "";

		final String finalTitle = title;
		final String finalSubtitle = subtitle;

		if (!Bukkit.isPrimaryThread()) {
			Bukkit.getScheduler().runTask(Main.instance, () ->
					sendTitle(player, finalTitle, finalSubtitle, fadeIn, stay, fadeOut)
			);
			return;
		}

		player.sendTitle(
				Main.color(finalTitle.replace("%player%", player.getName())),
				Main.color(finalSubtitle.replace("%player%", player.getName())),
				fadeIn, stay, fadeOut
		);
	}

	public static void sendTitleSeconds(Player player, String title, String subtitle, int fadeInSeconds, int staySeconds, int fadeOutSeconds) {
		sendTitle(player, title, subtitle, fadeInSeconds * 20, staySeconds * 20, fadeOutSeconds * 20);
	}

}
