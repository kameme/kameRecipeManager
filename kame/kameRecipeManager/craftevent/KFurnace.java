/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package kame.kameRecipeManager.craftevent;

import java.util.ArrayList;
import java.util.List;

import kame.kameRecipeManager.Main;
import kame.kameRecipeManager.recipe.KFurnaceRecipe;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class KFurnace {
	private float cooking;
	private float cookrate = 1;
	private KFurnaceRecipe recipe;
	List<Player> view = new ArrayList<Player>();

	public KFurnace(KFurnaceRecipe recipe, float cookrate) {
		this.recipe = recipe;
		this.cookrate = cookrate;
	}

	public short burn() {
		cooking += cookrate;
		return (short) (cooking /(recipe.getCookTime() / 200.0F));
	}

	public boolean ismatch(ItemStack in, ItemStack out) {
		if(in == null)return false;
		Main.cast(in + " " + out);
		ItemStack basein = this.recipe.getInput();
		ItemStack burnin = in.clone();
		basein.setAmount(1);
		burnin.setAmount(1);
		if(!basein.toString().equals(burnin.toString()))return false;
		if(out == null)return true;
		ItemStack baseout = this.recipe.getResult();
		ItemStack burnout = out.clone();
		baseout.setAmount(1);
		burnout.setAmount(1);
		return baseout.equals(burnout);
	}

	public static boolean ismatch(ItemStack in1, ItemStack in2, ItemStack out1, ItemStack out2) {
		if ((in1 == null) || (in2 == null))return false;
		in1.setAmount(1);
		in2.setAmount(1);
		if (!in1.toString().equals(in2.toString()))return false;
		if (out2 == null)return true;

		out1 = out1.clone();
		out2 = out2.clone();
		out1.setAmount(1);
		out2.setAmount(1);
		return out1.equals(out2);
	}

	public void resettime() {
		this.cooking = 0;
	}

	public KFurnaceRecipe getRecipe() {
		return this.recipe;
	}

	public ItemStack getResult() {
		return this.recipe.getResult();
	}
}
