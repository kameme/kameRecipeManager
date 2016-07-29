package kame.kameRecipeManager.craftevent;

import java.util.ArrayList;
import java.util.List;

import kame.kameRecipeManager.Main;
import kame.kameRecipeManager.Utils.StandUtils;
import kame.kameRecipeManager.Utils.Utils;
import kame.kameRecipeManager.useAPI.LoadLight;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BlockingEvent implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	private void onBlockPlace(BlockPlaceEvent event) {
		if (event.isCancelled())return;
		ItemStack item = event.getItemInHand().clone();
		item.setAmount(1);
		ItemStack check = new ItemStack(item.getType(), item.getAmount(), item.getDurability());
		if (item.equals(check))return;
		ItemMeta meta = item.getItemMeta();
		if(meta.hasDisplayName()) {
			Material mate = item.getType();
			String name = meta.getDisplayName();
			if((mate.equals(Material.TORCH) || mate.equals(Material.BARRIER)) && name.matches("[0-9]+")) {
				LoadLight.createLight(event.getBlock().getLocation(), Integer.parseInt(name), mate);
				return;
			}
		}
		Location loc = event.getBlock().getLocation().add(0.5, Main.getOffsetY(), 0.5);
		ArmorStand entity = null;
		if ((meta.hasLore()) && (meta.hasDisplayName())) {
			if (meta.getDisplayName().equals("@hologram")) {
				StandUtils.createStand(loc, meta.getLore());
				return;
			}
			if(meta.getDisplayName().matches("[0-9]+")) {
			}
			List<String> lore = meta.getLore();
			if ((lore.contains("@craft")) || (lore.contains("@furnace") || lore.contains("@fusion"))) {
				if(lore.contains("@hide"))loc.add(0, CraftingEvent.hidename, 0);
				entity = StandUtils.createStand(loc, item);
				entity.setCustomName(meta.getDisplayName());
				if(!lore.contains("@hide"))entity.setCustomNameVisible(true);
				return;
			}
		}
		loc.add(0, CraftingEvent.hidename, 0);
		entity = StandUtils.createStand(loc, item);
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	private void onBlockChange(EntityChangeBlockEvent event){
		ArmorStand stand = Utils.checkStandNature(event.getBlock());
		if(stand != null) {
			event.setCancelled(true);
			event.getBlock().getState().update();
		}
	}
	@EventHandler(priority = EventPriority.MONITOR)
	private void onBlockPiston(BlockPistonExtendEvent event) {
		onPistonMove(event, event.getBlocks());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	private void onBlockPiston(BlockPistonRetractEvent event) {
		onPistonMove(event, event.getBlocks());
	}

	private void onPistonMove(BlockPistonEvent event, List<Block> blocks) {
		if (event.isCancelled())return;
		List<ArmorStand> list = new ArrayList<ArmorStand>();
		BlockFace b = event.getDirection();
		for (Block block : blocks) {
			ArmorStand stand = Utils.checkStandNature(block);
			if (stand != null) {
				if (block.getPistonMoveReaction().equals(PistonMoveReaction.MOVE))list.add(stand);
				else if (block.getPistonMoveReaction().equals(PistonMoveReaction.BREAK)) {
					blockremove(block, stand);
				}
			}
		}
		for (Entity e : list) {
			e.teleport(e.getLocation().add(b.getModX(), b.getModY(), b.getModZ()));
			Main.cast(b + " " + b.getModX() + " " + b.getModY() + " " + b.getModZ());
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	private void onBlockBreak(EntityExplodeEvent event) {
		if (event.isCancelled())return;
		for (Block block : event.blockList()) {
			ArmorStand stand = Utils.checkStandNature(block);
			if (stand != null) {
				blockremove(block, stand);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onBlockBreak(BlockBreakEvent event) {
		if (event.isCancelled())return;
		Block block = event.getBlock();
		ArmorStand entity = Utils.checkStandNature(block);
		if (entity == null)return;
		Location loc = block.getLocation();
		if (!event.getPlayer().getGameMode().equals(GameMode.CREATIVE) && entity.getChestplate() != null && block.getDrops(event.getPlayer().getItemInHand()).size() > 0) {
			loc.getWorld().dropItemNaturally(loc.add(0.5, 0, 0.5), entity.getChestplate());
		}
		block.setType(Material.AIR);
		entity.remove();
	}

	private void blockremove(Block block, ArmorStand stand) {
		if (block.getWorld().getGameRuleValue("doTileDrops").equals("true") && block.getDrops().size() > 0) {
			block.getWorld().dropItemNaturally(block.getLocation().add(0.5, 0, 0.5), stand.getChestplate());
		}
		block.getDrops().clear();
		stand.remove();
	}
}
