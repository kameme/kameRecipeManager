/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package kame.kameRecipeManager.craftevent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kame.kameRecipeManager.Main;
import kame.kameRecipeManager.Utils.ScoreBoardUtils;
import kame.kameRecipeManager.Utils.StandUtils;
import kame.kameRecipeManager.Utils.Utils;
import kame.kameRecipeManager.craftevent.sync.SyncCraft;
import kame.kameRecipeManager.craftevent.sync.SyncFusion;
import kame.kameRecipeManager.craftevent.sync.SyncSmelt;
import kame.kameRecipeManager.recipe.KDummyRecipe;
import kame.kameRecipeManager.recipe.KFurnaceRecipe;
import kame.kameRecipeManager.recipe.KRecipe;
import kame.kameRecipeManager.recipe.KameRecipes;
import kame.kameRecipeManager.recipe.VRecipe;
import kame.kameRecipeManager.recipeUtils.RecipeMatcher;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.util.Vector;

public class CraftingEvent implements Listener {
	public static final int hidename = 1024;
	public static final int inHand = -16;
	private ScoreBoardUtils scores = new ScoreBoardUtils();
	private void furnaceRunnable() {
		new SyncSmelt().runTaskTimer(Main.getPlugin(), 0L, 1L);
	}

	public CraftingEvent(){
		furnaceRunnable();
	}

	private void setResultSync(PrepareItemCraftEvent event){
		Player player = (Player) event.getView().getPlayer();
		SyncCraft task = sync.get(player);
		if(task == null) {
			task = new SyncCraft();
			task.runTask(Main.getPlugin());
		}
		task.update(event, event.getInventory());
		sync.put(player, task);
	}

	public static Map<Player, ArmorStand> getViewlist() {
		return viewlist;
	}


	public static Map<Player, Location> getLast() {
		return last;
	}

	public static Map<Player, SyncCraft> sync = new HashMap<Player, SyncCraft>();
	private static Map<Player, ArmorStand> viewlist = new HashMap<Player, ArmorStand>();
	private static Map<Player, Location> last = new HashMap<Player, Location>();

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onInteract(PlayerInteractEvent event) {
		if (event.isCancelled())return;
		Action action = event.getAction();
		Player player = event.getPlayer();
		if ((action.equals(Action.RIGHT_CLICK_AIR)) || (action.equals(Action.RIGHT_CLICK_BLOCK))) {
			ItemStack item = player.getItemInHand();
			if ((item.getItemMeta() != null) && (Utils.hasItemLore(item.getItemMeta(), "@inHand")) && (Utils.hasItemLore(item.getItemMeta(), "@craft")) && (player.getLocation().getBlockY() > 0)) {
				ArmorStand entity = StandUtils.createStand(player.getLocation().zero().add(0.0D, inHand, 0.0D), item);
				if (item.getItemMeta().hasDisplayName())entity.setCustomName(item.getItemMeta().getDisplayName());
				else entity.setCustomName("クラフト");
				event.setCancelled(true);
				getLast().put(player, player.getLocation());
				player.openWorkbench(player.getLocation(), true);
				openScore(player, entity);
				getViewlist().put(player, entity);
				Main.cast("§d[鯖たん] §e手持ち作業台にゃん♪(ฅ•ω•ฅ)");
				return;
			}
		}
		if (!action.equals(Action.RIGHT_CLICK_BLOCK))return;
		Block block = event.getClickedBlock();
		Location loc = block.getLocation().add(0.5D, 0.5D, 0.5D);
		if ((player.isSneaking()) && (!player.getItemInHand().getType().equals(Material.AIR)))return;
		ArmorStand entity = Utils.checkStand(block);
		if ((block.getType().equals(Material.FURNACE)) || (block.getType().equals(Material.BURNING_FURNACE))) {
			openScore(player, entity, (Furnace) block.getState());
			getViewlist().put(player, entity);
			return;
		}
		if (entity == null) {
			if (!block.getType().equals(Material.WORKBENCH))return;
			entity = StandUtils.createStand(player.getLocation().zero().add(0, inHand, 0), player.getItemInHand());
			entity.setCustomName("クラフト");
		}
		event.setCancelled(true);
		if(entity != null && block.getType().equals(Material.BREWING_STAND)) {
			onBrew(entity, event.getPlayer());
			return;
		}
		player.openWorkbench(loc, true);
		openScore(player, entity);
		getViewlist().put(player, entity);
		getLast().put(player, loc);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onInteract(PlayerInteractAtEntityEvent event) {
		Bukkit.broadcastMessage("");
		if (event.isCancelled())return;
		Location loc = event.getRightClicked().getLocation().subtract(0, Main.getOffsetY(), 0);
		ArmorStand entity = Utils.checkStand(loc.getBlock());
		if(entity == null)return;
		Player player = event.getPlayer();
		if(entity.getChestplate().getItemMeta().getLore().contains("@furnace")) {
			Block block = entity.getLocation().getBlock();
			if(block.getType().equals(Material.FURNACE) || block.getType().equals(Material.BURNING_FURNACE)) {
				player.openInventory(((Furnace) entity.getLocation().getBlock().getState()).getInventory());
				openScore(player, entity, (Furnace) block.getState());
				event.setCancelled(true);
			}
			return;
		}
		event.setCancelled(true);
		if(entity != null && loc.getBlock().getType().equals(Material.BREWING_STAND)) {
			onBrew(entity, event.getPlayer());
			return;
		}
		player.openWorkbench(loc, true);

		getLast().put(player, loc.add(0, 0.5, 0));
		openScore(player, entity);
		getViewlist().put(player, entity);
	}

	private void onBrew(ArmorStand stand, Player player) {
		if(SyncFusion.isBrewing(stand))return;
		ArrayList<Item> items = new ArrayList<Item>();
		for(Entity e : stand.getWorld().getNearbyEntities(stand.getLocation().add(0, 0.25, 0), 0.5, 0.25, 0.5))if(e instanceof Item){
			if(((Item) e).getPickupDelay() > 1000) {
				e.remove();
				continue;
			}
			((Item) e).setPickupDelay(80);
			items.add((Item) e);
		}
		World world = stand.getWorld();
		if(items.size() == 0) {
			world.playSound(stand.getEyeLocation(), Main.getUtils().getSound("FIZZ"), 1, 2f);
			return;
		}
		world.playSound(stand.getEyeLocation(), Main.getUtils().getSound("ENDERDRAGON_WINGS"), 1, 0.2f);

		new SyncFusion(player, stand, items).runTaskTimer(Main.getPlugin(), 20L, 20L);

	}
	@EventHandler
	private void onClick(InventoryClickEvent event) {
		if ((event.getClickedInventory() != null) && (event.getClickedInventory().getType().equals(InventoryType.ANVIL))) {
			ItemStack item = event.getClickedInventory().getContents()[0];
			if ((event.getSlot() == 2) && (item != null) && (item.getItemMeta().hasLore())) {
				event.getWhoClicked().sendMessage("[クラフト] 説明文の付いたアイテムの変更はできません。");
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	private void onBurn(FurnaceBurnEvent event) {
		Furnace f = (Furnace) event.getBlock().getState();
		FurnaceInventory inv = f.getInventory();
		ArmorStand entity = Utils.checkStand(f.getBlock());
		String name = Utils.getStandName(entity).replaceAll("#default", "かまど");
		ItemStack in = inv.getSmelting().clone();
		in.setAmount(1);
		KFurnaceRecipe recipe = RecipeMatcher.matchFurnaceItem(f, in, Utils.getInclude(entity, name));
		if (recipe == null || !KFurnace.ismatch(inv.getSmelting().clone(), recipe.getInput().clone(), recipe.getResult(), inv.getResult())) {
			event.setCancelled(true);
			return;
		}
		Main.cast(recipe.getResult());
		int burntime = Utils.getBurnTime(event.getFuel(), event.getBurnTime());
		burntime = Utils.getUsing(event.getFuel(), name, burntime);
		event.setBurnTime((int) (burntime * Utils.getBurnRate(entity)));
		SyncSmelt.put(f.getLocation(), new KFurnace(recipe, Utils.getCookRate(entity)));
	}

	@EventHandler
	private void onBurn(FurnaceSmeltEvent event) {
		Main.cast("にょん");
		Block block = event.getBlock();
		KFurnace item = SyncSmelt.get(block.getLocation());
		if(item != null) {
			event.setResult(item.getResult());
			item.resettime();
			SyncSmelt.generateProduct(block , item.getRecipe());
		}else {
			ArmorStand entity = Utils.checkStand(block);
			ItemStack input = event.getSource().clone();
			input.setAmount(1);
			KFurnaceRecipe recipe = RecipeMatcher.matchFurnaceItem((Furnace) block.getState(), input, Utils.getInclude(entity, Utils.getStandName(entity).replaceAll("#default", "かまど")));
			if(recipe == null)return;
			SyncSmelt.put(block.getLocation(), new KFurnace(recipe, Utils.getCookRate(entity)));
			event.setResult(recipe.getResult());
			SyncSmelt.generateProduct(block , recipe);
		}
	}

	@EventHandler
	private void onClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		Entity e = (Entity) getViewlist().remove(player);
		getLast().remove(player);
		if ((e != null) && (e.getLocation().equals(e.getLocation().zero().add(0, inHand, 0))))e.remove();
		RecipeMatcher.clearMem(player);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	private void onCraft(PrepareItemCraftEvent event) {
		if(event.isRepair())return;
		setResultSync(event);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	private void onCrafting(CraftItemEvent event) {
		Player player = (Player) event.getView().getPlayer();
		if (!getViewlist().containsKey(player))return;
		Entity e = (Entity) getViewlist().get(player);
		if (e == null) {
			event.setCancelled(true);
			player.closeInventory();
			player.sendMessage("[クラフト] リロードされましたインベントリを開きなおしてください");
			return;
		}
		if(event.isShiftClick() && RecipeMatcher.isAny(player)) {
			event.setCancelled(true);
			player.sendMessage("[クラフト] このレシピではシフトクリックは許可されていません");
			return;
		}
		KRecipe recipe = RecipeMatcher.matchItem(getLast().get(player), player);
		int button = event.getHotbarButton();
		if(recipe == null ||(button > -1 && player.getInventory().getItem(button) != null)) {
			event.setCancelled(true);
			return;
		}
		if(recipe instanceof KDummyRecipe)return;

		for(VRecipe rec : recipe.getProducts()) {
			if(event.isShiftClick()) {
				event.setCancelled(true);
				player.sendMessage("[クラフト] このレシピではシフトクリックは許可されていません");
				return;
			}
			ItemStack item = rec.generateItem();
			if(item == null)continue;
			Item drop = player.getWorld().dropItemNaturally(player.getEyeLocation(), item);
			drop.setVelocity(new Vector(0, 0, 0));
			drop.setPickupDelay(0);
		}
	}

	private void openScore(Player player, ArmorStand entity) {
		int i = 0;
		String name = Utils.getStandName(entity).replaceAll("#default", "クラフト");
		for(String list : Utils.getInclude(entity, name)) {
			List<KRecipe> l1 = KameRecipes.getRecipe("$CRAFT " + list);
			List<KRecipe> l2 = KameRecipes.getRecipe("$COMBINE " + list);
			if (l1 != null)i += l1.size();
			if (l2 != null)i += l2.size();
		}
		if (name.length() > 16)name = name.substring(0, 16);
		player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		Objective obj = player.getScoreboard().registerNewObjective(name, "");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.getScore("§b§lRecipeList").setScore(i);
		scores.timeput(player, 30);
	}

	public static void updateFurnaceScore(Player p, String title, int per, int recipes) {
		p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		title = title.replaceAll("container.furnace", "かまど");
		if (title.length() > 9)title = title.substring(0, 9);
		Objective obj = p.getScoreboard().registerNewObjective(title + " " + ChatColor.RED + per + ChatColor.RESET + "%", "");
		obj.getScore("§b§lRecipeList").setScore(recipes);
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
	}

	private void openScore(Player player, ArmorStand entity, Furnace block) {
		int i = 0;
		String name = Utils.getStandName(entity).replaceAll("#default", "かまど");
		for(String list : Utils.getInclude(entity, name)) {
			List<KRecipe> l2 = KameRecipes.getRecipe("$SMELT " + list);
			if (l2 != null)i += l2.size();
		}
		updateFurnaceScore(player, name, 0, i);
		scores.viewput(player, new View(block.getLocation(), i));
		scores.timeput(player, 30);
	}

}
