package kame.kameRecipeManager.craftevent.sync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import kame.kameRecipeManager.Main;
import kame.kameRecipeManager.Utils.ScoreBoardUtils;
import kame.kameRecipeManager.Utils.Utils;
import kame.kameRecipeManager.craftevent.KFurnace;
import kame.kameRecipeManager.craftevent.View;
import kame.kameRecipeManager.recipe.KFurnaceRecipe;
import kame.kameRecipeManager.recipe.KFurnaceRecipe.FurnaceEffect;
import kame.kameRecipeManager.recipe.KRecipe;
import kame.kameRecipeManager.recipe.VRecipe;
import kame.kameRecipeManager.recipeUtils.RecipeMatcher;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Furnace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class SyncSmelt extends BukkitRunnable{
	private static Map<Location, KFurnace> fur = new HashMap<Location, KFurnace>();
	private List<Location> remove = new ArrayList<Location>();

	@Override
	public void run() {
		remove.clear();
		for(Location loc : fur.keySet()) {
			Block block = loc.getBlock();
			if(block.getType().equals(Material.BURNING_FURNACE)) {
				result(loc, (Furnace) block.getState());

			} else {
				remove.add(loc);
			}
		}
		for(Location loc : remove)fur.remove(loc);
	}

	private void result(Location loc, Furnace furnace) {
		for(Entry<Player, View> e : ScoreBoardUtils.getViewer())e.getValue().updateScore(furnace, (Player) e.getKey(), furnace.getCookTime() / 2);
		FurnaceInventory inv = furnace.getInventory();
		ItemStack in = inv.getSmelting();
		if(in == null) {
			fur.put(loc, null);
			return;
		}
		in = in.clone();
		in.setAmount(1);
		KFurnace kf = fur.get(loc);
		if(kf == null || (!kf.ismatch(in, inv.getResult()))) {
			Main.cast("[鯖たん] マッチしなかったのだよ!(ฅ;ω;ฅ)");
			furnace.setCookTime((short) 0);
			ArmorStand entity = Utils.checkStand(furnace.getBlock());
			KFurnaceRecipe recipe = RecipeMatcher.matchFurnaceItem(furnace, in, Utils.getInclude(entity, Utils.getStandName(entity).replaceAll("#default", "かまど")));
			if(recipe != null)fur.put(loc, new KFurnace(recipe, Utils.getCookRate(entity)));
		}else {
			playParticle(loc.clone().add(0.5, 0.5, 0.5), kf.getRecipe().getEffect());
			short burn = kf.burn();
			furnace.setCookTime(burn);
			Main.cast(kf.getResult() + " :" + burn);
			if(burn  <= 200)return;
			ItemStack out = inv.getResult();
			ItemStack i = inv.getSmelting();
			if((inv.getResult() == null) || (inv.getResult().getAmount() + kf.getResult().getAmount() < 65)) {
				if(i.getAmount() == 1)inv.setSmelting(new ItemStack(Material.AIR));
				else i.setAmount(i.getAmount() - 1);
				out.setAmount(out.getAmount()+ kf.getResult().getAmount());
				generateProduct(furnace.getBlock() , kf.getRecipe());
				kf.resettime();
			}
		}
	}

	private void playParticle(Location loc, FurnaceEffect e) {
		if(e != null)loc.getWorld().spigot().playEffect(loc, e.Effect(), e.Id(), e.Data(), e.Offset(), e.Offset(), e.Offset(), e.Speed(), 1, 16);
	}

	public static void generateProduct(Block block, KRecipe recipe) {
		BlockFace face = BlockFace.valueOf(block.getState().getData().toString().split(" ")[2]);
		for(VRecipe v : recipe.getProducts()) {
			ItemStack byp = v.generateItem();
			if(byp == null)continue;
			Item drop = block.getWorld().dropItemNaturally(block.getRelative(face).getLocation().add(0.5, 0.5, 0.5), byp);
			drop.setVelocity(new Vector(0, 0, 0));
			drop.setPickupDelay(0);

		}
	}
	public static void put(Location loc, KFurnace fur) {
		SyncSmelt.fur.put(loc, fur);
	}

	public static KFurnace get(Location loc) {
		return fur.get(loc);
	}

}
