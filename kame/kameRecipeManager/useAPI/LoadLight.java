package kame.kameRecipeManager.useAPI;

import kame.kameRecipeManager.Main;
import kame.kameRecipeManager.Utils.Utils;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;
public class LoadLight {
	public static void createLight(Location loc, int i, Material material) {
		if(!material.equals(Material.BARRIER))loc.getBlock().setType(Material.AIR);
		for(int j=0;j<10;j++)loc.getWorld().playEffect(loc, Effect.ENDER_SIGNAL, 10);
		new BukkitRunnable() {
			public void run() {
				Main.getUtils().createLight(loc, i);
				new BukkitRunnable() {
					public void run() {
						for(Location loc : Utils.Chunks(loc)) {
							Main.getUtils().updateLight(loc);
						}
					}
				}.runTask(Main.getPlugin());
			}
		}.runTaskLater(Main.getPlugin(), 10);
	}
}
