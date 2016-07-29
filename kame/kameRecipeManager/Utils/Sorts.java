/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package kame.kameRecipeManager.Utils;

import java.util.Comparator;

import org.bukkit.inventory.ItemStack;

public class Sorts implements Comparator<ItemStack> {
	public int compare(ItemStack item1, ItemStack item2) {
		return item1.serialize().toString().compareTo(item2.serialize().toString());
	}
}
