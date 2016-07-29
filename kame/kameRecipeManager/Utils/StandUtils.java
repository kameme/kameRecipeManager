package kame.kameRecipeManager.Utils;

import java.util.List;

import kame.kameRecipeManager.Main;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
public class StandUtils {
	private static final ItemStack kames;
	static {
		ItemStack item = new ItemStack(Material.COOKED_CHICKEN);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName("[kameRecipeManager]");
		item.setItemMeta(im);
		kames = item;
	}
	public static void createStand(Location loc, List<String> lore) {
		loc.add(0.0D, lore.size() * 0.25D - 0.001D, 0.0D);
		for (String name : lore) {
			loc.add(0.0D, -0.25D, 0.0D);
			ArmorStand entity = (ArmorStand) loc.getWorld().spawn(loc, ArmorStand.class);
			setStand(entity);
			entity.setCustomName(name);
			entity.setCustomNameVisible(true);
		}
	}

	public static ArmorStand createStand(Location loc, ItemStack item) {
		ArmorStand entity = (ArmorStand) loc.getWorld().spawn(loc,ArmorStand.class);
		setStand(entity);
		entity.setChestplate(item);
		return entity;
	}

	private static void setStand(ArmorStand entity) {
		entity.setItemInHand(new ItemStack(Material.AIR));
		entity.setBoots(kames);
		entity.setVisible(false);
		//entity.setSmall(true);
		entity.setArms(false);
		entity.setGravity(false);
		entity.setBasePlate(true);
		Main.getUtils().setStand(entity);
	}

	public static boolean isKameStand(ArmorStand stand) {
		ItemStack item = stand.getBoots();
		if(item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
			if(item.getItemMeta().getDisplayName().equals("[kameRecipeManager]"))return true;
		}
		return false;
	}
	
	public static ArmorStand createBrewStand(Location loc, ItemStack item) {
		ArmorStand entity = (ArmorStand) loc.getWorld().spawn(loc,ArmorStand.class);
		setStand(entity);
		item.setAmount(1);
		entity.setItemInHand(item);
		return entity;
	}
}
