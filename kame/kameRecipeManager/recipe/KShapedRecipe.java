/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package kame.kameRecipeManager.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kame.kameRecipeManager.config.recipe.BufferedRecipe;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

public class KShapedRecipe implements KRecipe {
	private String[] shape;
	private Map<Character, ItemStack> map;
	private ShapedRecipe recipe;
	private List<String> option;
	private boolean struct;
	private List<VRecipe> sub;
	
	public KShapedRecipe(ShapedRecipe recipe,  BufferedRecipe buffer) {
		this.recipe = recipe;
		this.shape = recipe.getShape();
		this.map = buffer.getCustomMap();
		this.option = buffer.getOption();
		this.struct = !buffer.isStrictly();
		this.sub = buffer.getVRecipe();
	}
	
	public KShapedRecipe(ShapedRecipe recipe, List<String> option) {
		this.shape = recipe.getShape();
		this.recipe = recipe;
		this.map = recipe.getIngredientMap();
		this.option = option;
		this.struct = false;
		this.sub = new ArrayList<VRecipe>();
	}

	public String[] getShape() {
		return this.shape;
	}

	public Map<Character, ItemStack> getIngredientMap() {
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
		return RecipeType.Shaped;
	}

	public boolean isStrictly() {
		return this.struct;
	}
	
	public List<VRecipe> getProducts() {
		return sub;
	}
}
