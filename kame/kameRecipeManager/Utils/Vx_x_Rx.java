/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package kame.kameRecipeManager.Utils;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;

public abstract class Vx_x_Rx {
	public Vx_x_Rx() {
	}

	public abstract void setStand(ArmorStand stand);
	public abstract void createLight(Location loc, int light);
	public abstract void updateLight(Location loc);
	public abstract Sound getSound(String name);
}
