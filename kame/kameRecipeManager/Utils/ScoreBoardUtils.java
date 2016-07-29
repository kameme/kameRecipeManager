package kame.kameRecipeManager.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import kame.kameRecipeManager.Main;
import kame.kameRecipeManager.config.recipe.RecipeTextInjector;
import kame.kameRecipeManager.craftevent.CraftingEvent;
import kame.kameRecipeManager.craftevent.View;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ScoreBoardUtils {
	private static Map<Player, Integer> time = new HashMap<Player, Integer>();
	private static Map<Player, View> view = new HashMap<Player, View>();
	public ScoreBoardUtils() {
		new BukkitRunnable() {
			public void run() {
				List<Player> remove = new ArrayList<Player>();
				for(Player player : time.keySet()) {
					if(CraftingEvent.getViewlist().containsKey(player) || RecipeTextInjector.isKeep(player))time.put(player, 40);
					else time.put(player, time.get(player) - 1);
					if(time.get(player) == 0 || (!player.isOnline()))remove.add(player);
				}
				for(Player player : remove) {
					time.remove(player);
					view.remove(player);
					player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
				}
			}
		}.runTaskTimer(Main.getPlugin(), 0L, 1L);
	}
	public void timeput(Player player, int i) {
		time.put(player, i);
	}
	public void viewput(Player player, View views) {
		view.put(player, views);

	}
	public static Set<Entry<Player, View>> getViewer() {
		return view.entrySet();
	}
}
