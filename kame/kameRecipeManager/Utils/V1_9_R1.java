/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package kame.kameRecipeManager.Utils;

import net.minecraft.server.v1_9_R1.BlockPosition;
import net.minecraft.server.v1_9_R1.Entity;
import net.minecraft.server.v1_9_R1.EntityPlayer;
import net.minecraft.server.v1_9_R1.EnumDirection;
import net.minecraft.server.v1_9_R1.EnumSkyBlock;
import net.minecraft.server.v1_9_R1.NBTTagCompound;
import net.minecraft.server.v1_9_R1.PacketPlayOutMapChunk;
import net.minecraft.server.v1_9_R1.WorldServer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_9_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

public class V1_9_R1 extends Vx_x_Rx {
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
		PacketPlayOutMapChunk packet = new PacketPlayOutMapChunk(w.getChunkAt(loc.getBlockX(), loc.getBlockZ()),false, 65535);
		for(Player player : Bukkit.getOnlinePlayers()) if(player.getWorld().equals(loc.getWorld())){
			EntityPlayer p = ((CraftPlayer) player).getHandle();
			p.playerConnection.sendPacket(packet);
		}
	}

	public Sound getSound(String name) {
		switch(name) {
		case "LAVA_POP": 			return Sound.BLOCK_LAVA_POP;
		case "FIZZ": 				return Sound.BLOCK_REDSTONE_TORCH_BURNOUT;
		case "WATER": 				return Sound.BLOCK_WATER_AMBIENT;
		case "FIRE_IGNITE": 		return Sound.ITEM_FLINTANDSTEEL_USE;
		case "LEVEL_UP": 			return Sound.ENTITY_PLAYER_LEVELUP;
		case "ITEM_PICKUP": 		return Sound.ENTITY_ITEM_PICKUP;
		case "ENDERDRAGON_WINGS": 	return Sound.ENTITY_ENDERDRAGON_FLAP;
		default:return Sound.UI_BUTTON_CLICK;
		}
	}
}
