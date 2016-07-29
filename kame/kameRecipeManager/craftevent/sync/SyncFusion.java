package kame.kameRecipeManager.craftevent.sync;

import java.util.ArrayList;
import java.util.List;

import kame.kameRecipeManager.Main;
import kame.kameRecipeManager.Utils.Utils;
import kame.kameRecipeManager.recipe.KFusionRecipe;
import kame.kameRecipeManager.recipe.KFusionRecipe.FusionEffect;
import kame.kameRecipeManager.recipe.KFusionRecipe.FusionSound;
import kame.kameRecipeManager.recipe.KRecipe;
import kame.kameRecipeManager.recipe.VRecipe;
import kame.kameRecipeManager.recipeUtils.RecipeMatcher;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Spigot;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.google.common.collect.Lists;

public class SyncFusion extends BukkitRunnable{
	
	private static List<ArmorStand> fusion = Lists.newArrayList();
	private Player player;
	private ArmorStand stand;
	private ArrayList<Item> items;
	private World world;
	private Spigot spigot;
	private List<Item> drops = Lists.newArrayList();
	private List<ItemStack> fusions = Lists.newArrayList();
	int deg = 0;
	public SyncFusion(Player player, ArmorStand stand, ArrayList<Item> items) {
		this.player = player;
		fusion.add(stand);
		this.stand = stand;
		this.items = items;
		this.world = stand.getWorld();
		this.spigot = world.spigot();
	}

	public static boolean isBrewing(ArmorStand stand) {
		return fusion.contains(stand);
	}
	
	@Override
	public void run() {
		if(items.size() > 0 && deg < 3) {
			Item item = items.remove(0);
			ItemStack base = item.getItemStack();
			ItemStack input = base.clone();
			base.setAmount(base.getAmount()-1);
			item.setItemStack(base);
			if(base.getAmount() > 0)items.add(item);
			else item.remove();
			input.setAmount(1);
			fusions.add(input.clone());
			ItemMeta im = input.getItemMeta();
			String name = "";
			if(im.hasDisplayName()) name = im.getDisplayName();
			im.setDisplayName(":kames." + base.hashCode() + ":" + name);
			input.setItemMeta(im);
			Location loc = stand.getLocation();
			switch(deg) {
			case 0:loc.add(0.25, 0.15, 0);
				break;
			case 1:loc.add(-0.18, 0.15, 0.25);
				break;
			case 2:loc.add(-0.18, 0.15, -0.25);
				break;
			}
			Item i = world.dropItem(loc, input);
			i.setPickupDelay(Integer.MAX_VALUE);
			i.setVelocity(new Vector(0, 0, 0));
			drops.add(i);
			deg++;
			
			spigot.playEffect(item.getLocation().add(0, 0.1, 0), Effect.FLAME, 0, 0, 0, 0, 0, 0.01f, 5, 16);
			spigot.playEffect(loc, Effect.EXPLOSION, 0, 0, 0, 0, 0, 0, 1, 16);
			world.playSound(item.getLocation(), Main.getUtils().getSound("ITEM_PICKUP"), 1, 0.5f);
		}else {
			this.cancel();
			Main.cast(fusions);
			KRecipe recipe = RecipeMatcher.matchFusionItem(fusions, stand.getLocation(), Utils.getInclude(stand, Utils.getStandName(stand)), player);
			if(recipe != null)Fusion((KFusionRecipe) recipe);
			else failFusion();
		}
	}
	private void Fusion(KFusionRecipe results) {;
		world.playSound(stand.getEyeLocation(), Main.getUtils().getSound("LEVEL_UP"), 1, 2);
		new BukkitRunnable() {
			int time = 0;
			Location loc = stand.getLocation().add(0, 0.875, 0);
			@Override
			public void run() {
				if(stand.isDead()) {
					fusion.remove(stand);
					this.cancel();
					for(Item item : drops)item.remove();
					for(ItemStack items : fusions)world.dropItem(loc, items);
					return;
				}

				if(time == (int)(results.getCookTime()*0.8)) {
					spigot.playEffect(loc.clone().add(0, -0.375, 0), Effect.MAGIC_CRIT, 0, 0, 0.2f, 0.2f, 0.2f, 0.1f, 50, 16);
					world.playSound(loc, Main.getUtils().getSound("FIRE_IGNITE"), 1, 0.7f);
					
				}
				if(time > results.getCookTime()) {
					for(Item item : drops)item.remove();
					drop(loc, results.getResult());
					for(VRecipe vr : results.getProducts()) {
						ItemStack item = vr.generateItem();
						if(item != null)drop(loc, item);
					}
					fusion.remove(stand);
					this.cancel();
				}else {
				if(time%20 == 0)world.playSound(loc, Main.getUtils().getSound("WATER"), 1, (float) (Math.random()+0.5)); 
					spigot.playEffect(loc, Effect.FLYING_GLYPH, 0, 0, 0, 0, 0, 1, 1, 16);
					for(Item item : drops) {
						item.setTicksLived(1);
					}
					time++;
				}
			}
			private void drop(Location loc, ItemStack item) {
				world.dropItem(loc, item).setVelocity(new Vector(0, 0, 0));
				FusionEffect e = results.getEffect();
				if(e != null)spigot.playEffect(loc, e.Effect(), e.Id(), e.Data(), 0.1f, 0.1f, 0.1f, e.Speed(), 10, 16);
				else spigot.playEffect(loc, Effect.CLOUD, 0, 0, 0.1f, 0.1f, 0.1f, 0, 10, 16);
				FusionSound s = results.getSound();
				if(s != null)world.playSound(loc, s.Sound(), s.Volume(), s.Pitch());
				else world.playSound(loc, Main.getUtils().getSound("FIZZ"), 1, 0.7f);
			}
			
		}.runTaskTimer(Main.getPlugin(), 0, 1);
		
	}
	
	private void failFusion() {
		spigot.playEffect(stand.getEyeLocation().add(0, 1, 0), Effect.COLOURED_DUST, 0, 0, 0.1f, 0.1f, 0.1f, 0, 10, 16);
		world.playSound(stand.getLocation(), Main.getUtils().getSound("LAVA_POP"), 1, 0.5f);
		new BukkitRunnable() {
			@Override
			public void run() {
				fusion.remove(stand);
				world.playSound(stand.getLocation(), Main.getUtils().getSound("FIZZ"), 1, 0.5f);
				for(Item item : drops)item.remove();
				for(ItemStack items : fusions) {
					Item item = world.dropItem(stand.getLocation().add(0, 0.875, 0), items);
					item.setVelocity(new Vector(0, 0, 0));
					spigot.playEffect(item.getLocation(), Effect.EXPLOSION, 0, 0, 0, 0, 0, 0, 1, 16);
				}
				
			}
			
		}.runTaskLater(Main.getPlugin(), 20);
		
	}

}
