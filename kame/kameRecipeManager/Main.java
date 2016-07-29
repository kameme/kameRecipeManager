/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package kame.kameRecipeManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import kame.kameRecipeManager.Utils.Vx_x_Rx;
import kame.kameRecipeManager.config.recipe.RecipeTextInjector;
import kame.kameRecipeManager.craftevent.BlockingEvent;
import kame.kameRecipeManager.craftevent.CraftingEvent;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	private static Plugin plugin;
	public static final String ver = Bukkit.getServer().getClass().getPackage()
			.getName().replace(".", ",").split(",")[3].substring(1);
	private static FileConfiguration config;
	private static float offset;
	private static Vx_x_Rx clazz;
	public static boolean debug;
	private static List<Material> items = new ArrayList<Material>();
	Logger log;

	public Main() {
	}

	public void onEnable() {
		this.log = getLogger();
		plugin = this;
		loadConfig();
		RecipeTextInjector r = new RecipeTextInjector();
		getCommand("kamerecipe").setExecutor(r);
		getCommand("kamerecipe").setTabCompleter(r);
		try {
			getServer().getPluginManager().registerEvents(new CraftingEvent(), this);
			getServer().getPluginManager().registerEvents(new BlockingEvent(), this);
			clazz = (Vx_x_Rx) Class.forName("kame.kameRecipeManager.Utils.V" + ver).newInstance();
			r.loadRecipe();
		} catch (Exception e) {
			System.err.println("<kameRecipe> Sorry... this version is not supported ");
			getServer().getPluginManager().disablePlugin(this);
		}
	}

	public void onDisable() {
	}

	public static List<Material> getExclude() {
		return items;
	}
	
	public static void loadConfig() {
		plugin.saveDefaultConfig();
		config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "config.yml"));
		try {
			offset = Float.parseFloat(config.getString("offset-y"));
			debug = config.getBoolean("debug");
			items.clear();
			for(String item : config.getStringList("ExcludeItems")) {
				Material material = Material.getMaterial(item);
				if(material != null) items.add(material);
				else System.out.println("<kameRecipe> Error Material. " + item + " is unknown type!");
			}
		} catch (NumberFormatException e) {
			offset = 0.0F;
		}
	}

	public static FileConfiguration getConf() {
		return config;
	}

	public static void cast(Object o) {
		if(debug)Bukkit.broadcastMessage(String.valueOf(o));
	}

	public static float getOffsetY() {
		return offset;
	}

	public static Plugin getPlugin() {
		return plugin;
	}

	public static Vx_x_Rx getUtils() {
		return clazz;
	}
}
