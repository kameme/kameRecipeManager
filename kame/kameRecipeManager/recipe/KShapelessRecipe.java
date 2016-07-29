/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package kame.kameRecipeManager.recipe;

import java.util.ArrayList;
import java.util.List;

import kame.kameRecipeManager.config.recipe.BufferedRecipe;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;

public class KShapelessRecipe implements KRecipe {
	private List<ItemStack> map;
	private ShapelessRecipe recipe;
	private List<String> option;
	private boolean struct;
	private List<VRecipe> sub;

	public KShapelessRecipe(ShapelessRecipe recipe, BufferedRecipe buffer) {
		this.recipe = recipe;
		this.map = buffer.getShapeless();
		this.option = buffer.getOption();
		this.struct = !buffer.isStrictly();
		this.sub = buffer.getVRecipe();
	}

	public KShapelessRecipe(ShapelessRecipe recipe, List<String> option) {
		this.recipe = recipe;
		this.map = recipe.getIngredientList();
		this.option = option;
		this.struct = false;
		this.sub = new ArrayList<VRecipe>();
	}

	public List<ItemStack> getIngredientList() {
		return this.map;
	}

	public Recipe getBukkitRecipe() {
		return this.recipe;
	}

	public ItemStack getResult() {
		return this.recipe.getResult();
	}

	public List<String> getOption() {
		return this.option;
	}

	public RecipeType getType() {
		return RecipeType.Shapeless;
	}

	public boolean isStrictly() {
		return this.struct;
	}

	@Override
	public List<VRecipe> getProducts() {
		return sub;
	}
}
