/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package kame.kameRecipeManager.recipeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import kame.kameRecipeManager.Main;
import kame.kameRecipeManager.Utils.Buffer;
import kame.kameRecipeManager.Utils.Sorts;
import kame.kameRecipeManager.Utils.Utils;
import kame.kameRecipeManager.recipe.KDummyRecipe;
import kame.kameRecipeManager.recipe.KFurnaceRecipe;
import kame.kameRecipeManager.recipe.KFusionRecipe;
import kame.kameRecipeManager.recipe.KRecipe;
import kame.kameRecipeManager.recipe.KShapedRecipe;
import kame.kameRecipeManager.recipe.KShapelessRecipe;
import kame.kameRecipeManager.recipe.KameRecipes;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RecipeMatcher {
	private static Map<UUID, List<String>> result = new HashMap<UUID, List<String>>();
	private static Map<UUID, List<Buffer>> buffer = new HashMap<UUID, List<Buffer>>();
	private static Map<UUID, ItemStack> items = new HashMap<UUID, ItemStack>();

	public static void bufferput(Player player, ItemStack item) {
		List<Buffer> list = new ArrayList<Buffer>();
		list.add(new Buffer(player, item, new KDummyRecipe(item)));
		buffer.put(player.getUniqueId(), list);
	}

	public static ItemStack matchShapedItem(String[] shape, Map<Character, ItemStack> map, Location loc, List<String> names, Player player) {
		Main.cast("§d[鯖たん] §e定型レシピにゃん♪(ฅ•ω•ฅ)");
		clearMem(player);
		for(String name : names) {
			List<KRecipe> m = KameRecipes.getRecipe("$CRAFT " + name);
			if(m == null)return null;
			for(KRecipe list : m) {
				if(list instanceof KShapedRecipe && Arrays.equals(((KShapedRecipe) list).getShape(), shape)) {
					Map<Character, ItemStack> mp = ((KShapedRecipe) list).getIngredientMap();
					for(char c = 'a'; c < 'j';c++)mp.remove(Character.valueOf(c), null);
					if(mp.size() != map.size())continue;
					if(Utils.itemCheck(mp,map) || (!list.isStrictly() && Utils.itemCheckNature(mp, map, shape))) {
						Main.cast("§d[鯖たん] §eレシピが一致したにゃん♪(ฅ•ω•ฅ)");
						Main.cast("[鯖たん] " + list.getResult());
						result.put(player.getUniqueId(), list.getOption());
						items.put(player.getUniqueId(), list.getResult());
						optionBuild(list.getOption(), player, list.getResult(), loc, list);
					}
				}
			}
		}
		return result(player, loc);
	}

	public static ItemStack matchShapelessItem(List<ItemStack> item, Location loc, List<String> names, Player player, ItemStack res) {
		clearMem(player);
		Main.cast("§d[鯖たん] §e不定型レシピにゃん♪(ฅ•ω•ฅ)");
		Collections.sort(item, new Sorts());
		for(String name : names) {
			List<KRecipe> m = KameRecipes.getRecipe("$COMBINE " + name);
			if(m == null)continue;
			for(KRecipe list : m) {
				if(list instanceof KShapelessRecipe) {
					List<ItemStack> im = ((KShapelessRecipe) list).getIngredientList();
					if(item.size() != im.size())continue;
					Collections.sort(im, new Sorts());
					if(Utils.itemCheck(im, item) || (!list.isStrictly() && Utils.itemCheckNature(im, item))) {
						Main.cast("§d[鯖たん] §eレシピが一致したにゃん♪(ฅ•ω•ฅ)");
						Main.cast("[鯖たん] " + list.getResult());
						result.put(player.getUniqueId(), list.getOption());
						items.put(player.getUniqueId(), list.getResult());
						optionBuild(list.getOption(), player, list.getResult(), loc, list);
					}
				}
			}
		}

		return result(player, loc);
	}

	public static KFurnaceRecipe matchFurnaceItem(Furnace block, ItemStack input, List<String> names) {
		Main.cast("§d[鯖たん] §eかまどレシピにゃん♪(ฅ•ω•ฅ)");
		for(String name : names) {
			List<KRecipe> m = KameRecipes.getRecipe("$SMELT " + name);
			if(m == null)continue;
			for(KRecipe list : m)
				if((list instanceof KFurnaceRecipe) && Utils.itemCheck(((KFurnaceRecipe) list).getInput(), input)){
				Main.cast("§d[鯖たん] §eレシピが一致したにゃん♪(ฅ•ω•ฅ)");
				return (KFurnaceRecipe) list;
			}
		}
		Main.cast("§d[鯖たん] §eレシピが一致しなかったにゃん…♪(ฅ;ω;ฅ)");
		return null;
	}
	public static KRecipe matchFusionItem(List<ItemStack> item, Location loc, List<String> names, Player player) {
		clearMem(player);
		Main.cast("§d[鯖たん] §e融合レシピにゃん♪(ฅ•ω•ฅ)");
		for(String name : names) {
			List<KRecipe> m = KameRecipes.getRecipe("$FUSION " + name);
			if(m == null)continue;
			for(KRecipe list : m) {
				Main.cast(name + list.getResult());
				if(list instanceof KFusionRecipe) {
					List<ItemStack> im = ((KFusionRecipe) list).getInput();
					Main.cast(im);
					if(item.size() == im.size() && (Utils.itemCheck(item, im) || (!list.isStrictly() && Utils.itemCheckNature(item, im)))) {
						Main.cast("§d[鯖たん] §eレシピが一致したにゃん♪(ฅ•ω•ฅ)");
						Main.cast("[鯖たん] " + list.getResult());
						result.put(player.getUniqueId(), list.getOption());
						items.put(player.getUniqueId(), list.getResult());
						optionBuild(list.getOption(), player, list.getResult(), loc, list);
					}
				}
			}
		}
		return optionCreate(player, loc);
	}
	private static ItemStack result(Player player, Location loc) {
		List<Buffer> buflist = buffer.get(player.getUniqueId());
		if(buflist == null) {
			Main.cast("§d[鯖たん] §eレシピが一致しなかったにゃん…♪(ฅ;ω;ฅ)");
			return null;
		}else
		if(buflist.size() > 1) {
			for(Buffer buf : buflist) {
				Main.cast("§d[鯖たん] §e" + buf.getResult() + " " + buf.result(player, loc.getBlock()));
				if(buf.result(player, loc.getBlock()))return buf.getResult();
			}
			ItemStack item = new ItemStack(Material.BARRIER);
			List<String> lore = new ArrayList<String>();
			lore.add("クラフトに失敗しました");
			ItemMeta im = item.getItemMeta();
			im.setLore(lore);
			item.setItemMeta(im);
			return item;
		}else if(buflist.size() == 1)return buflist.get(0).getResult();
		return null;


	}

	public static KRecipe matchItem(Location loc, Player player) {
		return optionCreate(player, loc);
	}

	public static boolean isAny(Player player) {
		return buffer.get(player.getUniqueId()).size() > 1;
	}
	public static void clearMem(Player player) {
		result.remove(player.getUniqueId());
		buffer.remove(player.getUniqueId());
		items.remove(player.getUniqueId());
	}

	private static void optionBuild(List<String> options, Player player, ItemStack item, Location loc, KRecipe recipe) {
		List<Buffer> list = buffer.get(player.getUniqueId());
		if(list == null) list = new ArrayList<Buffer>();
		list.add(OptionBuilder.optionBuild(options, player, item, loc, recipe));
		buffer.put(player.getUniqueId(), list);
	}

	private static KRecipe optionCreate(Player player, Location loc) {
		List<Buffer> buff = buffer.get(player.getUniqueId());
		if(buff == null)return null;
		for(Buffer buf : buff) {
			if(buf == null)continue;
			boolean bool = buf.resultCraft(player, loc.getBlock());
			if(bool) {
				buf.playe(loc, true);
				buf.plays(loc, true);
				buf.playm(true);
				buf.playo(loc, true, player);
				return buf.getRecipe();
			}
		}
		for(Buffer buf : buff) {
			if(buf == null)continue;
			buf.playe(loc, false);
			buf.plays(loc, false);
			buf.playm(false);
			buf.playo(loc, false, player);
		}
		return null;
	}
}
