/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package kame.kameRecipeManager.recipe;

import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public abstract interface KRecipe {
	public abstract Recipe getBukkitRecipe();

	public abstract ItemStack getResult();

	public abstract List<String> getOption();

	public abstract RecipeType getType();

	public abstract boolean isStrictly();

	public abstract List<VRecipe> getProducts();
}
