/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package kame.kameRecipeManager.recipe;

import java.util.ArrayList;
import java.util.List;

import kame.kameRecipeManager.config.recipe.BufferedRecipe;

import org.bukkit.Effect;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public class KFurnaceRecipe implements KRecipe {
	public class FurnaceEffect {
		private Effect effect;
		private int id;
		private int data;
		private float offset;
		private float speed;

		private FurnaceEffect(Effect e, String... s) {
			effect = e;
			id = (int) parse(s[1]);
			data = (int) parse(s[2]);
			speed = parse(s[3]);
			offset = parse(s[4]);
		}
		public Effect Effect() {
			return effect;
		}
		public int Id() {
			return id;
		}
		public int Data() {
			return data;
		}
		public float Speed() {
			return speed;
		}
		public float Offset() {
			return offset;
		}
		private float parse(String s) {
			return Float.parseFloat(s);
		}

	}

	private ItemStack input;
	private FurnaceRecipe recipe;
	private List<String> option;
	private int time;
	private boolean struct;
	private FurnaceEffect effect;
	private List<VRecipe> sub;

	public KFurnaceRecipe(FurnaceRecipe recipe, BufferedRecipe buffer) {
		this.recipe = recipe;
		this.input = buffer.getInput();
		this.time = buffer.getTime();
		this.option = buffer.getOption();
		this.struct = !buffer.isStrictly();
		String[] E = buffer.getFurnaceEffect();
		this.sub = buffer.getVRecipe();
		if(!E[0].equals("")) {
			Effect e = getEffect(E[0]);
			if(e != null && E.length == 5)effect = new FurnaceEffect(e, E);
		}
	}

	private Effect getEffect(String s) {
		try {
			return Effect.valueOf(s.toUpperCase());
		}catch(IllegalArgumentException t) {
			for(Effect e : Effect.values())if(s.equals(e.ordinal() + ""))return e;
		}
		return null;
	}

	public KFurnaceRecipe(FurnaceRecipe recipe, List<String> option) {
		this.recipe = recipe;
		this.input = recipe.getInput();
		this.time = 200;
		this.option = option;
		this.struct = false;
		this.sub = new ArrayList<VRecipe>();
	}

	public ItemStack getInput() {
		return this.input;
	}

	public int getCookTime() {
		return this.time;
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
		return RecipeType.Furnace;
	}

	public FurnaceEffect getEffect() {
		return effect;
	}

	public boolean isStrictly() {
		return this.struct;
	}

	public List<VRecipe> getProducts() {
		return sub;
	}
}
