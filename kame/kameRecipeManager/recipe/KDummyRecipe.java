package kame.kameRecipeManager.recipe;

import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public class KDummyRecipe implements KRecipe {
	private ItemStack item;
	public KDummyRecipe(ItemStack item) {
		this.item = item;
	}
	/**
	 * アイテム以外すべてNullを返すことに注意、除外レシピがこれに相当
	 */
	@Override
	public Recipe getBukkitRecipe() {
		return null;
	}

	@Override
	public ItemStack getResult() {
		return item;
	}

	@Override
	public List<String> getOption() {
		return null;
	}

	@Override
	public RecipeType getType() {
		return null;
	}

	@Override
	public boolean isStrictly() {
		return false;
	}

	@Override
	public List<VRecipe> getProducts() {
		return null;
	}

}
