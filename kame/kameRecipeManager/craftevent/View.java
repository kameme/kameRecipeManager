/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package kame.kameRecipeManager.craftevent;

import org.bukkit.Location;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;

public class View {
	private Location loc;
	private int score;

	public View(Location loc, int s) {
		this.loc = loc;
		this.score = s;
	}

	public void updateScore(Furnace f, Player p, int s) {
		if (f.getLocation().equals(loc))CraftingEvent.updateFurnaceScore(p, f.getInventory().getTitle().replaceAll("container.furnace", "かまど"), s, score);
	}
}
