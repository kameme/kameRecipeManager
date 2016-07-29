/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package kame.kameRecipeManager.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import kame.kameRecipeManager.Main;
import kame.kameRecipeManager.craftevent.CraftingEvent;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Utils {

	private static final String[] str = {
		"#英語で書くのが面倒なのです",
		"#説明書",
		"#",
		"#このプラグインの使い方",
		"#通常の作業台にはあまり手を加えない仕様にしております",
		"#なので通常の作業台では特殊アイテムを普通のアイテムとしてのクラフトも許してしまいます",
		"#(名前が変わったアイテム、エンチャのついたアイテムがすべてクラフト不可になるからです)",
		"#アイテムの説明文の部分に@craftとついているブロックが特殊作業台対象です",
		"#アイテムの説明文の部分に@furnaceとついているブロックが特殊かまど対象です",
		"#作業台以外にも置けるブロックならすべて対応していると思います",
		"#また@hideとつけると置いたときにネームタグを表示しなかったり@inHandで手持ち作業台になります",
		"#",
		"#",
		"#おてほん",
		"#CRAFT [作業台の名前]無しなら普通にやつ",
		"#1",
		"#1",
		"#1",
		"#=== 280 @name:名前 @lore:出来たやつのLore文",
		"#1 = STONE",
		"#",
		"#CRAFT 名前付ければ特殊加工台",
		"#1 + 4 + 1",
		"#1 + 3 + 2",
		"#1 + 3 + 2",
		"#=== 278 @name:§b黒曜石つるはし @tag:{display:{Name:\"\",Lore:[\"\",\"\",\"\"]},ench:[{id:1,lvl:0}]}",
		"# シャープ入れてなくてもコメントできるけど念のため入れることおすすめ",
		"#1 = 49",
		"#4 = DIAMOND",
		"#3 = 280 @name:名前 @lore:出来たやつのLore文",
		"#",
		"#オプションのリスト",
		"#@name:<name>",
		"#@lore:<lore>",
		"#@ench:<ench>:<power>",
		"#@tag:{name:...}",
		"#",
		"#@level:<min-max>",
		"#@levelcost:<cost>",
		"#@xp:<min-max>",
		"#@xpcost:<cost>",
		"#@info:<message>",
		"#@pathinfo:<message>",
		"#@failinfo:<message>",
		"#@broadcast:<message>",
		"#@playsound:<sound>:<volume>:<pitch>",
		"#@playeffect:<effect>:<id>",
		"#@failsound:<sound>:<volume>:<pitch>",
		"#@faileffect:<effect>:<id>",
		"#@pathsound:<sound>:<volume>:<pitch>",
		"#@patheffect:<effect>:<id>",
		"#@pathexpand:<power>:<true|false>",
		"#@pathexpand:<power>:<true|false>",
		"#",
		"#今のところこれくらいだよ追加していく予定だからその時はよろしくね",
		"#",
		"",
		"石の作業台作るよ",
		"CRAFT",
		"1 + 2 + 1",
		"2 + 3 + 2",
		"1 + 2 + 1",
		"=== STONE @name:石の作業台 @lore:@craft",
		"2 = STONE",
		"3 = CRAFTING_TABLE",
		"$option @level:10-20 @playsound:WOLF_BARK @info:[作業台] わん！ @failinfo:このクラフトはレベル10~20である必要があります",
		"",
		"COMBINE 石の作業台",
		"I8= STONE",
		"=== STONE @name:8倍圧縮ブロック @lore:§bずっしり",
		"",
		"COMBINE 石の作業台",
		"I8= STONE @name:8倍圧縮ブロック @lore:§bずっしり",
		"=== STONE @name:64倍圧縮ブロック @lore:§bさっくり",
		"",
		"COMBINE 石の作業台",
		"I = STONE @name:64倍圧縮ブロック @lore:§bさっくり",
		"=== STONE:0:8 @name:8倍圧縮ブロック @lore:§bずっしり",
		"",
		"COMBINE 石の作業台",
		"I = STONE @name:8倍圧縮ブロック @lore:§bずっしり",
		"=== STONE:0:8",
		"",
		"CRAFT",
		"1 + 1 + 1",
		"2 + 3 + 2",
		"4 + 4 + 4",
		"=== FURNACE @name:粉砕機 @lore:@furnace @lore:金属を砕くのに使用する",
		"1 = DIAMOND_SWORD",
		"2 = FLINT",
		"3 = FURNACE",
		"4 = STONE",
		"",
		"#ステンレス 素材",
		"",
		"SMELT 粉砕機",
		"I = IRON_INGOT",
		"=== 289 @name:鉄粉 @lore:引火注意",
		"$times:400",
		"",
		"",
		"",
		"SMELT 粉砕機",
		"I = GOLD_INGOT",
		"=== 289 @name:金粉 @lore:吸引注意",
		"$times:400",
		"",
		"",
		"COMBINE",
		"I4= GUNPOWDER @name:金粉 @lore:吸引注意",
		"I4= GUNPOWDER @name:鉄粉 @lore:引火注意",
		"I = REDSTONE",
		"=== QUARTZ @name:ステンレス粉",
		"",};

	public String[] getDefault() {
		return str;
	}

	public static ArmorStand checkStand(Block block) {
		Location loc = block.getLocation().add(0.5D, Main.getOffsetY(), 0.5);
		for(Entity entity : loc.getChunk().getEntities())if(entity instanceof ArmorStand) {
			if(inBlock(entity.getLocation(), loc) && StandUtils.isKameStand((ArmorStand) entity) && (
					hasBodyLore(entity, "@craft") ||
					hasBodyLore(entity, "@furnace") ||
					hasBodyLore(entity, "@fusion"))) {
				return (ArmorStand) entity;
			}
		}
		return null;
	}

	public static ArmorStand checkStandNature(Block block) {
		Location loc = block.getLocation().add(0.5D, Main.getOffsetY(), 0.5);
		for(Entity entity : loc.getChunk().getEntities())if(entity instanceof ArmorStand) {
			if(inBlock(entity.getLocation(), loc) && StandUtils.isKameStand((ArmorStand) entity)) {
				return (ArmorStand) entity;
			}
		}
		return null;
	}

	private static boolean inBlock(Location stand, Location loc) {
		return (stand.equals(loc)) || (stand.equals(loc.clone().add(0, CraftingEvent.hidename, 0)));
	}

	public static void loc(Location loc) {
		Bukkit.broadcastMessage(loc.getX() + " " + loc.getY() + " " + loc.getZ());
	}

	private static boolean hasBodyLore(Entity entity, String lore) {
		ItemStack item = ((ArmorStand) entity).getChestplate();
		return hasItemLore(item.getItemMeta(), lore);
	}

	public static boolean hasItemLore(ItemMeta im, String lore) {
		return (im.hasLore()) && (im.getLore().contains(lore));
	}

	public static String getStandName(Entity entity) {
		if(entity == null)return "#default";
		return entity.getCustomName();
	}

	public static Material parseMaterial(String name) {
		String[] item = name.split(":");
		if(item[0].equals("AIR") || name.contains("minecraft:air"))return Material.AIR;
		if(item.length > 1)name = item[0].concat(":").concat(item[1]);
		Material material = Bukkit.getUnsafe().getMaterialFromInternalName(name.toLowerCase());
		if(material.equals(Material.AIR))material = Material.matchMaterial(item[0]);
		return material;
	}

	public static boolean itemCheckNature(Map<Character, ItemStack> map, Map<Character, ItemStack> map2, String[] shape) {
		boolean bool = itemCheckNature(map, map2);
		if(!bool && shape[0].length() > 1) {
			for(String buf : shape)sort(map2, buf.charAt(0), buf.charAt(buf.length()-1));
			bool = itemCheckNature(map, map2);
		}
		return bool;
	}

	private static boolean itemCheckNature(Map<Character, ItemStack> map, Map<Character, ItemStack> map2){
		for(char c = 'a'; c < 'j'; c++)map2.remove(c, null);
		for(Map.Entry<Character, ItemStack> set : map.entrySet())if(!itemCheckNature(set.getValue(),map2.get(set.getKey())))return false;
		return true;

	}

	private static void sort(Map<Character, ItemStack> map, char l, char r) {
		ItemStack bl = map.get(l);
		map.put(l, map.get(r));map.put(r, bl);

	}
	public static boolean itemCheckNature(List<ItemStack> list1, List<ItemStack> list2) {
		Iterator<ItemStack> it1 = list1.iterator();
		Iterator<ItemStack> it2 = list2.iterator();
		while (it1.hasNext()) {
			if(!itemCheckNature(it1.next(), it2.next()))return false;
		}
		return true;
	}

	public static boolean itemCheck(Map<Character, ItemStack> map1, Map<Character, ItemStack> map2) {
		for(Entry<Character, ItemStack> entry : map1.entrySet()) {
			if(!itemCheck(entry.getValue(), map2.get(entry.getKey())))return false;
		}
		return true;
	}

	public static boolean itemCheck(List<ItemStack> list1, List<ItemStack> list2) {
		Iterator<ItemStack> it1 = list1.iterator();
		Iterator<ItemStack> it2 = list2.iterator();
		while (it1.hasNext()) {
			ItemStack item1 = it1.next();
			ItemStack item2 = it2.next();
			if(!itemCheck(item1, item2))return false;
		}
		return true;
	}

	public static boolean itemCheck(ItemStack base, ItemStack item) {
		if(item == null || base == null)return false;
		if(!base.getType().equals(item.getType()))return false;
		if(base.getData().getData() == -1)base.setDurability(item.getDurability());
		if(Main.debug)Main.cast(base.getData() + " ||| " + item.getData());
		if(!base.equals(item))return false;
		return true;
	}

	public static void playEffect(World w, Block b, Player p) {
		for(Entity e : w.getEntities())if(e instanceof Player && !e.equals(p)) {
			((Player) e).playEffect(b.getLocation(), Effect.STEP_SOUND, b.getType().ordinal());
		}
	}

	public static boolean itemCheckNature(ItemStack base, ItemStack item) {
		if(item == null)return false;
		if(!base.getType().equals(item.getType()))return false;
		ItemMeta imbase = base.getItemMeta();
		ItemMeta imitem = item.getItemMeta();
		if(base.getData().getData() == -1)base.setDurability(item.getDurability());
		if(!base.getData().equals(item.getData()))return false;
		Main.cast("ふにゅう！(ฅ>ω<ฅ)");

		if(imbase.hasDisplayName() && (!imitem.hasDisplayName()|| !imbase.getDisplayName().equals(imitem.getDisplayName())))return false;

		if(imbase.hasLore() || imitem.hasLore()) {
			if(!imbase.hasLore() || !imitem.hasLore())return false;
			if(!imbase.getLore().equals(imitem.getLore()))return false;
		}

		if(imbase.hasEnchants() && (!imitem.hasEnchants() || !imbase.getEnchants().equals(imitem.getEnchants()))) {
			return false;
		}
		if(Main.debug)Main.cast(base.getData() + " ||| " + item.getData());
		return true;
	}


	public static boolean hasItemLore(List<ItemStack> items) {
		for(ItemStack item : items) {
			if(item != null && item.hasItemMeta() && item.getItemMeta().hasLore())return false;
		}
		return true;
	}
	public static float getCookRate(ArmorStand stand) {
		if(stand == null)return 1;
		List<String> lore = stand.getChestplate().getItemMeta().getLore();
		for(String l : lore)if(l.toLowerCase().matches("@cookrate:[0-9]+(.[0-9]+)?"))
			return Float.parseFloat(l.split(":")[1]);
		return 1;
	}

	public static float getBurnRate(ArmorStand stand) {
		if(stand == null)return 1;
		List<String> lore = stand.getChestplate().getItemMeta().getLore();
		for(String l : lore)if(l.toLowerCase().matches("@burnrate:[0-9]+(.[0-9]+)?"))
			return Float.parseFloat(l.split(":")[1]);
		return 1;
	}

	public static int getBurnTime(ItemStack item, int i) {
		if(item == null || !item.getItemMeta().hasLore())return i;
		for(String l : item.getItemMeta().getLore())if(l.toLowerCase().matches("@burntime:[0-9]+"))
			return Integer.parseInt(l.split(":")[1]);
		return i;
	}

	public static int getUsing(ItemStack item, String name, int def) {
		if(item == null || !item.getItemMeta().hasLore())return def;
		List<String> lore = item.getItemMeta().getLore();
		for(String l : lore)if(l.matches("@using:"+name+":[0-9]+"))def = Integer.parseInt(l.split(":")[2]);
		return def;
	}

	public static List<String> getInclude(ArmorStand stand, String name) {
		List<String> list = new ArrayList<String>();
		list.add(name);
		if(stand == null)return list;
		ItemStack item = stand.getChestplate();
		if(item == null || !item.hasItemMeta() || !item.getItemMeta().hasLore())return list;
		List<String> lore = item.getItemMeta().getLore();
		for(String l : lore)if(l.matches("@include:.*"))list.add(l.replaceFirst("@include:", ""));
		return list;
	}

	public static List<Location> Chunks(Location loc) {
		List<Location> list = new ArrayList<Location>();
		int chunkX = loc.getBlockX() >> 4;
		int chunkZ = loc.getBlockZ() >> 4;
		World world = loc.getWorld();
		for (int dX = -1; dX <= 1; dX++) {
			for (int dZ = -1; dZ <= 1; dZ++) {
				if(world.isChunkLoaded(chunkX + dX, chunkZ + dZ)) 
					list.add(new Location(loc.getWorld(), chunkX + dX, 0, chunkZ + dZ));
			}
		}
		return list;
	}
}
