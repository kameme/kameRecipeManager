/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package kame.kameRecipeManager.recipeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kame.kameRecipeManager.Main;
import kame.kameRecipeManager.Utils.Sorts;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class RecipeSifter {

	public static Map<Character, ItemStack> startSift(ItemStack[] craft, int... i) {
		if (craft.length == 5)return startSiftMini(craft, i);
		if ((i[1] < 3) && ((notAir(craft[6])) || (notAir(craft[7])) || (notAir(craft[8]))))craft = siftUp(craft);
		if ((i[0] < 3) && ((notAir(craft[2])) || (notAir(craft[5])) || (notAir(craft[8]))))craft = siftLeft(craft);
		if ((i[1] < 2) && ((notAir(craft[3])) || (notAir(craft[4])) || (notAir(craft[5]))))craft = siftUp(craft);
		if ((i[0] < 2) && ((notAir(craft[1])) || (notAir(craft[4])) || (notAir(craft[7]))))craft = siftLeft(craft);
		return sortSift(craft, i);
	}

	public static Map<Character, ItemStack> startSiftMini(ItemStack[] craft, int... i) {
		if ((i[1] < 2) && (notAir(craft[2]) || notAir(craft[3])))craft = siftUpMini(craft);
		if ((i[0] < 2) && (notAir(craft[1]) || notAir(craft[3])))craft = siftLeftMini(craft);
		return sortSiftMini(craft, i);
	}

	private static boolean notAir(ItemStack item) {
		return !item.getType().equals(Material.AIR);
	}

	private static ItemStack[] siftUp(ItemStack[] craft){
		ItemStack a = craft[0];
		for (int i = 0;i<6;i++)craft[i] = craft[i+3];
		craft[8] = craft[7] = craft[6] = a;
		return craft;
	}

	private static ItemStack[] siftLeft(ItemStack[] craft) {
		ItemStack a = craft[0];
		for (int i = 0;i<7;i+=3)craft[i] = craft[i+1];
		for (int i = 1;i<8;i+=3)craft[i] = craft[i+1];
		craft[8] = craft[5] = craft[2] = a;
		return craft;
	  }

	private static ItemStack[] siftUpMini(ItemStack[] craft) {
		ItemStack a = craft[0];
		craft[0] = craft[2];
		craft[1] = craft[3];
		craft[3] = a;
		craft[2] = a;
		return craft;
	}

	private static ItemStack[] siftLeftMini(ItemStack[] craft) {
		ItemStack a = craft[0];
		craft[0] = craft[1];
		craft[2] = craft[3];
		craft[3] = a;
		craft[1] = a;
		return craft;
	}

	public static List<ItemStack> sortSift(ItemStack[] item) {
		List<ItemStack> items = new ArrayList<ItemStack>();
		for(ItemStack sort : item) {
			if (sort != null && !sort.getType().equals(Material.AIR)) {
				sort = sort.clone();
				sort.setAmount(1);
				items.add(sort);
			}
		}
		Collections.sort(items, new Sorts());
		return items;
	}

	private static Map<Character, ItemStack> sortSift(ItemStack[] craft, int[] i) {
		Map<Character, ItemStack> map = new HashMap<Character, ItemStack>();
		char ch = 'a';
		for (int c = 0; c < 9; c++)
			if(c%3 < i[0] && c/3 < i[1]) {
			craft[c] = craft[c].clone();
			craft[c].setAmount(1);
			if (notAir(craft[c]))map.put(ch, craft[c]);
			ch++;
		}
		Main.cast(map);
		return map;
	}

	private static Map<Character, ItemStack> sortSiftMini(ItemStack[] craft, int[] i) {
		Map<Character, ItemStack> map = new HashMap<Character, ItemStack>();
		char ch = 'a';
		for (int c = 0; c < 4; c++)
			if(c%2 < i[0] && c/2 < i[1]) {
			craft[c] = craft[c].clone();
			craft[c].setAmount(1);
			if (notAir(craft[c]))map.put(ch, craft[c]);
			ch++;
		}
		Main.cast(map);
		return map;
	}
}
