package kame.kameRecipeManager.craftevent.sync;

import java.util.List;
import java.util.Map;

import kame.kameRecipeManager.Main;
import kame.kameRecipeManager.Utils.Utils;
import kame.kameRecipeManager.craftevent.CraftingEvent;
import kame.kameRecipeManager.recipe.KameRecipes;
import kame.kameRecipeManager.recipeUtils.RecipeMatcher;
import kame.kameRecipeManager.recipeUtils.RecipeSifter;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.scheduler.BukkitRunnable;

public class SyncCraft extends BukkitRunnable {

	private PrepareItemCraftEvent event;
	private Inventory inv;
	@Override
	public void run() {
		Player player = (Player) event.getView().getPlayer();
		CraftingEvent.sync.remove(player);
		ArmorStand stand = CraftingEvent.getViewlist().get(player);
		InventoryType type = event.getInventory().getType();
		if(stand == null && !type.equals(InventoryType.CRAFTING)) {
			player.closeInventory();
			player.sendMessage("[クラフト] リロードされましたインベントリを開きなおしてください");
			return;
		}
		String name = Utils.getStandName(stand).replaceAll("#default", "クラフト");
		List<String> names = Utils.getInclude(stand, name);
		ItemStack item = null;
		Recipe recipe = event.getRecipe();
		if(names.contains("クラフト") && KameRecipes.containtsExclude(recipe)) {
			Main.cast("§d[鯖たん] §e除外にゃん♪(ฅ•ω•ฅ)\n§d(item)§r" + recipe.getResult());
			if(Utils.hasItemLore(RecipeSifter.sortSift(event.getInventory().getMatrix()))) {
				RecipeMatcher.bufferput(player, recipe.getResult());
				return;
				
			}
		}
		if(recipe instanceof ShapedRecipe) {
			String[] shape = ((ShapedRecipe) recipe).getShape();
			Map<Character, ItemStack> map = RecipeSifter.startSift(event.getInventory().getMatrix(), shape[0].length(), shape.length);
			item = RecipeMatcher.matchShapedItem(shape, map,  CraftingEvent.getLast().get(player), names, player);
		}else if(recipe instanceof ShapelessRecipe) {
			List<ItemStack> items = RecipeSifter.sortSift(event.getInventory().getMatrix());
			Main.cast(((ShapelessRecipe) recipe).getIngredientList() + "");
			if(names.contains("クラフト") && (((ShapelessRecipe) recipe).getIngredientList().size() != items.size()
					|| Main.getExclude().contains(recipe.getResult().getType())) && Utils.hasItemLore(items)) {
				RecipeMatcher.bufferput(player, recipe.getResult());
				return;
			}
			item = RecipeMatcher.matchShapelessItem(items,  CraftingEvent.getLast().get(player), names, player, recipe.getResult());
		}
		inv.setItem(0, item);
		if (item != null)Main.cast("§d[鯖たん] §eこれあげるにゃん♪(ฅ•ω•ฅ)\n§d(item)§r" + item.serialize());
		player.updateInventory();
	}
	public void update(PrepareItemCraftEvent event, CraftingInventory inv) {
		this.event = event;
		this.inv = inv;
	}

}
