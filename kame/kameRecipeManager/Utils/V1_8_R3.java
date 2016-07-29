/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package kame.kameRecipeManager.Utils;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EnumDirection;
import net.minecraft.server.v1_8_R3.EnumSkyBlock;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.PacketPlayOutMapChunk;
import net.minecraft.server.v1_8_R3.WorldServer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

public class V1_8_R3 extends Vx_x_Rx {
	@Override
	public void setStand(ArmorStand stand) {
		Entity Istand = ((CraftEntity) stand).getHandle();
		NBTTagCompound tag = new NBTTagCompound();
		Istand.c(tag);
		tag.setInt("DisabledSlots", 2039583);
		Istand.f(tag);
	}

	@Override
	public void createLight(Location loc, int light) {
		WorldServer w  = ((CraftWorld) loc.getWorld()).getHandle();
		BlockPosition bp = new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		w.a(EnumSkyBlock.BLOCK, bp, light);
		for(EnumDirection dir : EnumDirection.values())if(!w.c(bp.shift(dir)).getMaterial().blocksLight())w.c(EnumSkyBlock.BLOCK, bp.shift(dir));
	}

	@Override
	public void updateLight(Location loc) {
		WorldServer w  = ((CraftWorld) loc.getWorld()).getHandle();
		w.getChunkAt(loc.getBlockX(), loc.getBlockZ());
		PacketPlayOutMapChunk packet = new PacketPlayOutMapChunk(w.getChunkAt(loc.getBlockX(), loc.getBlockZ()), false, 65535);
		for(Player player : Bukkit.getOnlinePlayers()) if(player.getWorld().equals(loc.getWorld())){
			EntityPlayer p = ((CraftPlayer) player).getHandle();
			p.playerConnection.sendPacket(packet);
		}
	}

	public Sound getSound(String name) {
		return Sound.valueOf(name);
	}
}
