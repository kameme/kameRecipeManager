/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package kame.kameRecipeManager.Utils;

import net.minecraft.server.v1_8_R1.BlockPosition;
import net.minecraft.server.v1_8_R1.Entity;
import net.minecraft.server.v1_8_R1.EnumSkyBlock;
import net.minecraft.server.v1_8_R1.NBTTagCompound;
import net.minecraft.server.v1_8_R1.World;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftEntity;
import org.bukkit.entity.ArmorStand;

public class V1_8_R1 extends Vx_x_Rx {
	@Override
	public void setStand(ArmorStand stand) {
		Entity Istand = ((CraftEntity) stand).getHandle();
		NBTTagCompound tag = new NBTTagCompound();
		Istand.c(tag);
		tag.setInt("DisabledSlots", 2039583);
		Istand.f(tag);
	}
	
	public void createLight(Location loc, byte light) {
		World w  = ((CraftWorld) loc.getWorld()).getHandle();
		BlockPosition bp = new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		w.a(EnumSkyBlock.BLOCK, bp, light);
	}
}
