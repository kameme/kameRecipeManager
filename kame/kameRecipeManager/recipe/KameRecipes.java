/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package kame.kameRecipeManager.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Recipe;

public class KameRecipes {
	private static Map<String, List<KRecipe>> recipes = new HashMap<String, List<KRecipe>>();
	private static List<Recipe> defaults = new ArrayList<Recipe>();

	public static void addExclude(Recipe recipe) {
		defaults.add(recipe);
	}

	public static boolean containtsExclude(Recipe recipe) {
		return defaults.contains(recipe);
	}

	public static List<KRecipe> getRecipe(String recipename) {
		return (List<KRecipe>) recipes.get(recipename);
	}

	public static List<String> getListMap() {
		return Arrays.asList((String[]) recipes.keySet().toArray(new String[0]));
	}

	public static void addRecipe(String recipename, KRecipe recipe) {
		List<KRecipe> list = (List<KRecipe>) recipes.get(recipename);
		if (list == null)list = new ArrayList<KRecipe>();
		list.add(recipe);
		recipes.put(recipename, list);
	}

	public static void addBukkitRecipe(String recipename, KRecipe recipe) {
		Bukkit.addRecipe(recipe.getBukkitRecipe());
		addRecipe(recipename, recipe);
	}

	public static void removeRecipe(String recipename) {
		recipes.remove(recipename);
	}

	public static void clear() {
		recipes = new HashMap<String, List<KRecipe>>();
		defaults = new ArrayList<Recipe>();
	}
}
