package kame.kameRecipeManager.recipe;

import java.util.Random;

import org.bukkit.inventory.ItemStack;

public class VRecipe {
	private ItemStack viceitem;
	private int per;
	private static Random rnd = new Random();
	/**
	 * 副産物れしぴ
	 * @param item
	 * @param persent
	 */
	public VRecipe(ItemStack item, int persent) {
		viceitem = item;
		per = persent;
	}
	/**
	 * 確率で副産物を生成
	 * @return ランダム ItemStack : null
	 */
	public ItemStack generateItem() {
		return rnd.nextFloat()*100 < per ? viceitem : null;
	}
}
