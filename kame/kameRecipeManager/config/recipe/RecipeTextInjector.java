/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package kame.kameRecipeManager.config.recipe;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import kame.kameRecipeManager.Main;
import kame.kameRecipeManager.Utils.Sorts;
import kame.kameRecipeManager.Utils.Utils;
import kame.kameRecipeManager.recipe.KFurnaceRecipe;
import kame.kameRecipeManager.recipe.KFusionRecipe;
import kame.kameRecipeManager.recipe.KShapedRecipe;
import kame.kameRecipeManager.recipe.KShapelessRecipe;
import kame.kameRecipeManager.recipe.KameRecipes;
import kame.kameRecipeManager.recipe.RecipeType;
import kame.kameRecipeManager.recipe.VRecipe;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

public class RecipeTextInjector implements CommandExecutor, TabCompleter {
	private static String recipeName = "";
	private static int readline;
	private static int nameline;
	private static int failrecipe;
	private static int addrecipe;
	private static List<String> option = new ArrayList<String>();
	private static BufferedReader br;
	private static File dir = Main.getPlugin().getDataFolder();
	boolean load = true;
	private static BufferedRecipe buf;
	private boolean loadFiles() {
		readline = RecipeTextInjector.failrecipe = RecipeTextInjector.addrecipe = 0;
		try {
			File file = new File(dir, "\\recipes");
			if(!file.exists()) {
				file.mkdirs();
			}
			int i = 0;
			for(File f : file.listFiles()) {
				if(f.getName().endsWith(".txt")) {
					i++;
					System.out
							.println("<kameRecipe> Loading to " + f.getName());
					if(!loadFile(f)) {
						System.out.println("<kameRecipe> ファイルの読み込みに失敗しました");
						System.out.println("<kameRecipe> Can't load " + f.getName());
						System.out.println("<kameRecipe> Please check the File");
					}
				}
			}
			if(i == 0) {
				if(this.load) {
					FileWriter fw = new FileWriter(new File(file, "\\sample.txt"));
					PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
					for(String stream : new Utils().getDefault())pw.println(stream);
					pw.close();
					loadFiles();
				} else {
					System.err.println("<kameRecipe> cant write default file!!");
					System.err.println("<kameRecipe> file is locked?");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("<kameRecipe> Added the" + addrecipe + " recipe[s]");
		System.out.println("<kameRecipe> failed recipe is " + failrecipe);
		return true;
	}

	private boolean loadFile(File file) {
		if(!file.canRead()) {
			return false;
		}
		try {
			br = new BufferedReader(new FileReader(file));
			readline = 0;
			buf = null;
			loadRecipes();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	private void loadRecipes() throws IOException {
		String line = "#";
		do {
			if(line.matches("(CRAFT|COMBINE|SMELT|FUSION)\\s*.*")) {
				Main.cast("ふにゅう！！！！");
				recipePut();
				nameline = readline;
				recipeName = line.replaceFirst("(CRAFT|COMBINE|SMELT|FUSION)\\s*", "");
				RecipeType type = RecipeType.Default;
				if(line.matches("CRAFT.*")) {
					if(recipeName.equals(""))recipeName = "クラフト";
					type = RecipeType.Shaped;
				}else
				if(line.matches("COMBINE.*")) {
					if(recipeName.equals(""))recipeName = "クラフト";
					type = RecipeType.Shapeless;
				}else
				if(line.matches("SMELT.*")) {
					if(recipeName.equals(""))recipeName = "かまど";
					type = RecipeType.Furnace;
				}else
				if(line.matches("FUSION.*")) {
					type = RecipeType.Fusion;
				}
				buf = new BufferedRecipe(recipeName, type);
			}else
			if(buf != null) {
				switch (buf.getType()) {
				case Shaped:
					if(line.replaceAll(" ", "").matches("[1-9](\\+[1-9](\\+[1-9])?)?") || line.matches("[1-9] = .*"))
						shapedRecipe(line);
					break;
				case Shapeless:
					if(line.matches("I[1-9 ]= .*"))
						shapelessRecipe(line);
					break;
				case Furnace:
					if(line.matches("I = .*|\\$times:[0-9]+|\\$effect:.*"))
						furnaceRecipe(line);
					break;
				case Fusion:
					if(line.matches("I = .*|\\$times:[0-9]+|\\$effect:.*|\\$sound"))
						fusionRecipe(line);
					break;
				default:
					break;
				}
				if(line.matches("\\+([0-9 %]+)?= .*|=== .*|\\$option .*"))allRecipe(line);
				if(line.matches("@exclude"))buf.exclude();
			}
			Main.cast(line);
		}while((line = readLine()) != null);
		if(buf != null)recipePut();
	}

	private String readLine() {
		readline ++;
		try {
			return br.readLine();
		} catch (IOException e) {}
		return null;
	}

	private void getOptions(String[] option) {
		Main.cast("[鯖たん] オプション追加にゃん!(ฅ•ω•ฅ)");
		for(String line : option)buf.addOption(line);
	}

	private ItemStack parseItemStack(String line) {
		try {
			ItemStack item = parseItem(line);
			Main.cast("[鯖たん] アイテム追加にゃん!(ฅ•ω•ฅ)" + item);
			return item;
		} catch (ItemNullException e) {
			buf.droped();
			errorStack(e.getError());
		}
		return null;
	}

	private ItemStack parseItem(String line) throws ItemNullException {
		String[] metas = line.split(" @");
		String[] item = metas[0].split(":");
		Material material = Utils.parseMaterial(metas[0]);
		if(material == null)throw new ItemNullException("<kameRecipe> Line " + readline + " Material." + metas[0] + " is unknown Item");
		ItemStack result = new ItemStack(material);
		short dat = -1;
		int amount = 1;
		if(item.length > 1)dat = (short) parse(item[1], -1);
		if(item.length > 2)amount = parse(item[2], 1);
		result.setAmount(amount);
		if(dat == -1)result.setData(new MaterialData(material, (byte) dat));
		else result.setDurability(dat);

		ItemMeta im = result.getItemMeta();
		List<String> lore = new ArrayList<String>();
		for(String meta : metas) {
			item = meta.split(":");
			switch(item[0]) {
			case "name":
				im.setDisplayName(item[1]);
				break;
			case "lore":
				lore.add(replace(meta, "lore:"));
				im.setLore(lore);
				break;
			case "ench":
				String[] enchs = meta.split(":");
				if(enchs.length == 3) {
					Enchantment e;
					if(enchs[1].matches("[0-9]+"))e = Enchantment.getById(parse(enchs[1], -1));else e = Enchantment.getByName(enchs[1]);
					if(e == null)throw new ItemNullException("<kameRecipe> Line " + readline + " Enchantment." + enchs[1] + " is unknown Enchant");
					im.addEnchant(e, parse(enchs[2], 1), true);

				}else throw new ItemNullException("<kameRecipe> Line " + readline + " Enchantment. too few arguments");
				break;
			case "tag":
				try {
					im = Bukkit.getUnsafe().modifyItemStack(result, meta.replaceFirst("tag:", "")).getItemMeta();
				} catch (Exception e) {
					errorStack("<kameRecipe> Line " + readline + " " + meta + " is failed");
				}
				break;
			default:
			}
		}
		result.setItemMeta(im);
		return result;
	}

	private void shapedRecipe(String line) {
		if(line.replaceAll(" ", "").matches("[1-9](\\+[1-9](\\+[1-9])?)?") || !buf.isShapeFull())buf.addShape(line.replaceAll("[^1-9]", ""));
		if(line.matches("[1-9] = .*"))buf.addMapRaw(parse(line.split(" = ")[0], 1), parseItemStack(replace(line, "[1-9] = ")));
	}

	private void shapelessRecipe(String line) {
		if(line.matches("I[1-9 ]= .*"))for(int i = 0; i < loop(String.valueOf(line.charAt(1))); i++)buf.addShapeless(parseItemStack(replace(line, "I[1-9 ]= ")));
	}

	private void fusionRecipe(String line) {
		if(line.matches("I = .*"))buf.addFusionInput(parseItemStack(replace(line, "I = ")));
		if(line.matches("\\$times:[0-9]+"))buf.setFusionTime(parse(replace(line, "\\$times:"), 0));
		if(line.matches("\\$effect:.*"))buf.setFusionEffect(line.replaceFirst("\\$effect:", ""));
		if(line.matches("\\$sound:.*"))buf.setFusionSound(line.replaceFirst("\\$sound:", ""));
	}

	private void furnaceRecipe(String line) {
		if(line.matches("I = .*"))buf.setInput(parseItemStack(replace(line, "I = ")));
		if(line.matches("\\$times:[0-9]+"))buf.setTime(parse(replace(line, "\\$times:"), 0));
		if(line.matches("\\$effect:.*"))buf.setFurnaceEffect(line.replaceFirst("\\$effect:", ""));
	}

	private void allRecipe(String line) {
		if(line.matches("\\+([0-9 %]+)?= .*"))buf.addVRecipe(new VRecipe(parseItemStack(replace(line, "\\+([0-9 %]+)?= ")), parsePer(line.split("=")[0].replaceAll("[^0-9]", ""))));
		if(line.matches("=== .*"))buf.setResult(parseItemStack(replace(line, "=== ")));
		if(line.matches("\\$option @.*"))getOptions(line.replaceFirst("\\$option @", "").split(" @"));
	}

	private int loop(String str) {
		if(str.matches(" "))return 1;
		return Integer.parseInt(str);
	}

	private void recipePut() {
		if(buf == null)return;
		Main.cast("IN");
		if(!buf.isReady()) {
			errorStack("<kameRecipe> Line " + nameline + " " + buf.getRecipeName() + " is not added");
			failrecipe ++;
			return;
		}
		Main.cast("[鯖たん] レシピを追加するよ!(ฅ•ω•ฅ)");
		ItemStack item;
		switch(buf.getType()) {
		case Shaped:
			ShapedRecipe recipe = new ShapedRecipe(buf.getResult());
			StringBuilder builder = new StringBuilder();
			int[] size = buf.shapeSize();
			char ch = 'a';
			int[] cara = parseRecipe(buf.shape());
			recipe.shape(new String[] { "abc", "def", "ghi" });
			for(char i = 0; i < 9; i++) {
				if(i % 3 < size[0] && i / 3 < size[1]) {
					builder.append(ch);
					item = buf.getRawMap().get(cara[i]);
					if((item != null) && (cara[i] != 0)) {
						buf.addMap(ch, item);
						recipe.setIngredient(ch, item.getData());
					}
					ch++;
				}
				if(i ==2 || i == 5)builder.append(":");
			}
			String[] c = builder.toString().split(":");
			recipe.shape(c);
			if(buf.isExclude()) {
				KameRecipes.addExclude(recipe);
				break;
			}
			KameRecipes.addBukkitRecipe("$CRAFT " + buf.getRecipeName(), new KShapedRecipe(recipe, buf));
			for(String s : c)Main.cast(s);
			Main.cast(buf.getCustomMap());
			Main.cast("OUT =" + buf.getResult());
			Main.cast("Option = " + buf.getOption());
			Main.cast("Struct = " + buf.isStrictly());
			break;
		case Shapeless:
			if(buf.getShapeless().size() == 1) {
				ShapedRecipe r = new ShapedRecipe(buf.getResult());
				ItemStack in = buf.getShapeless().get(0);
				r.shape(new String[] { "a" });
				r.setIngredient('a', in.getData());
				if(buf.isExclude()) {
					KameRecipes.addExclude(r);
					break;
				}
				buf.addMap('a', in);
				KameRecipes.addBukkitRecipe("$CRAFT " + buf.getRecipeName(), new KShapedRecipe(r, buf));
			}else {
				Collections.sort(buf.getShapeless(), new Sorts());
				ShapelessRecipe recipe1 = new ShapelessRecipe(buf.getResult());
				for(ItemStack i : buf.getShapeless())recipe1.addIngredient(i.getData());
				if(buf.isExclude()) {
					KameRecipes.addExclude(recipe1);
					break;
				}
				KameRecipes.addBukkitRecipe("$COMBINE " + buf.getRecipeName(), new KShapelessRecipe(recipe1, buf));
				Main.cast(buf.getShapeless());
				Main.cast("OUT =" + buf.getResult());
				Main.cast("Option = " + buf.getOption());
				Main.cast("Struct = " + buf.isStrictly());
			}
			break;
		case Furnace:
			FurnaceRecipe recipe2 = new FurnaceRecipe(buf.getResult(), buf.getInput().getType());
			if(buf.isExclude()) {
				KameRecipes.addExclude(recipe2);
				break;
			}
			KameRecipes.addBukkitRecipe("$SMELT " + buf.getRecipeName(), new KFurnaceRecipe(recipe2, buf));
			Main.cast("IN =" + buf.getInput());
			Main.cast("OUT =" + buf.getResult());
			Main.cast("Option = " + buf.getOption());
			Main.cast("Struct = " + buf.isStrictly());
			break;
		case Fusion:
			if(buf.isExclude())break;
			KameRecipes.addRecipe("$FUSION " + buf.getRecipeName(), new KFusionRecipe(buf));
			Main.cast("IN =" + buf.getInput());
			Main.cast("OUT =" + buf.getResult());
			Main.cast("Option = " + buf.getOption());
			Main.cast("Struct = " + buf.isStrictly());
			break;
		default:
			break;
		}
		addrecipe = addrecipe + 1;
	}

	private void errorStack(String error) {
		System.err.println(error);
	}

	private int[] parseRecipe(List<String> list) {
		int[] k = new int[10];
		for(int i = 0; i < 9; i++) {
			if(list.size() == i / 3)break;
			String line = list.get(i / 3);
			if(i % 3 < line.length())k[i] = parse(line.charAt(i % 3) + "", 0);
		}
		return k;
	}

	private String replace(String name, String arg) {
		return name.replaceFirst(arg, "");
	}

	private int parsePer(String name) {
		if(name.equals(""))return 100;
		int i = parse(name, 100);
		if(i < 0)i = 0;
		return i;
	}
	private int parse(String name, int b) {
		try {
			return Integer.parseInt(name);
		} catch (Exception e) {
		}
		return b;
	}
	public void loadRecipe() {
		long time = System.currentTimeMillis();
		Bukkit.resetRecipes();
		KameRecipes.clear();
		Iterator<Recipe> i = Bukkit.recipeIterator();
		ArrayList<Recipe> rec = new ArrayList<Recipe>();
		while (i.hasNext())rec.add((Recipe) i.next());
		loadFiles();

		option.clear();
		for(Recipe r : rec) {
			if((r instanceof ShapedRecipe)) {
				KameRecipes.addRecipe("$CRAFT クラフト", new KShapedRecipe((ShapedRecipe) r, option));
			}else if((r instanceof ShapelessRecipe)) {
				KameRecipes.addRecipe("$COMBINE クラフト", new KShapelessRecipe((ShapelessRecipe) r, option));
			}else if((r instanceof FurnaceRecipe)) {
				KameRecipes.addRecipe("$SMELT かまど", new KFurnaceRecipe((FurnaceRecipe) r, option));
			}
		}
		System.err.println("<kameRecipe> load recipe " + (float) (System.currentTimeMillis() - time) / 1000.0F + "[s]");
	}

	public static boolean isKeep(Player player) {
		return keeps.contains(player.getUniqueId());
	}

	private static List<UUID> keeps = new ArrayList<UUID>();

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(sender.hasPermission("kamerecipe.user") && args.length == 1 && args[0].equals("keep") && (sender instanceof Player)) {
			if(keeps.contains(((Player) sender).getUniqueId())) {
				keeps.remove(((Player) sender).getUniqueId());
				sender.sendMessage(Main.getConf().getString("message.DispKeepOff"));
			} else {
				keeps.add(((Player) sender).getUniqueId());
				sender.sendMessage(Main.getConf().getString("message.DispKeepOn"));
			}
		}
		if(!sender.hasPermission("kamerecipe.admin"))return false;
		if(sender.isOp() && args.length == 1 && args[0].equals("list")) {
			for(String name : KameRecipes.getListMap())
				sender.sendMessage("[kame.] " + name + " in " + KameRecipes.getRecipe(name).size() + " recipe[s]");
		}
		if(args.length == 1 && args[0].equals("reload")) {
			loadRecipe();
			Main.loadConfig();
			sender.sendMessage("[kame.] configReloaded!");
		}
		if(args.length > 0 && args[0].equals("debug")) {
			Main.debug = !Main.debug;
			sender.sendMessage("[kame.] DebugMode= " + Main.debug);
		}
		if(args.length == 2 && sender instanceof Player) {
			if(args[0].equals("sounds")) {
				try {
					Sound s = Sound.valueOf(args[1]);
					if(s != null)((Player)sender).playSound(((Player)sender).getLocation(), s, 1, 1);
					sender.sendMessage("[kame.] Sounds. ID= §e" + s.ordinal() + "§r Name= §e" + s.name());
				}catch(IllegalArgumentException e) {
					sender.sendMessage("[kame.] 選択されたものが見つかりませんでした");
				}

			}else
			if(args[0].equals("effects")) {
				try {
					Effect s = Effect.valueOf(args[1]);
					if(s != null)((Player)sender).playEffect(((Player)sender).getLocation(), s, 1);
					sender.sendMessage("[kame.] Effects. ID= §e" + s.ordinal() + "§r Name= §e" + s.name());
				}catch(IllegalArgumentException e) {
					sender.sendMessage("[kame.] 選択されたものが見つかりませんでした");
				}
			}
		}
		return true;
	}

	public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		List<String> list = new ArrayList<String>();
		if(args.length == 2 && (sender.isOp() || sender.hasPermission("kamerecipe.admin"))) {
			if(args[0].equals("sounds")) {
				for(Sound s : Sound.values())if(s.toString().toLowerCase().startsWith(args[1].toLowerCase()))list.add(s.toString());

			}else
			if(args[0].equals("effects")) {
				for(Effect s : Effect.values())if(s.toString().toLowerCase().startsWith(args[1].toLowerCase()))list.add(s.toString());
			}else
			if(args[0].equals("items")) {
				for(Material s : Material.values())if(s.toString().toLowerCase().startsWith(args[1].toLowerCase()))list.add(s.toString());
			}
		}
		if((sender.isOp() || sender.hasPermission("kamerecipe.admin")) && args.length == 1) {
			String[] arg = {"reload", "list", "debug", "items", "sounds", "effects"};
			for(String str : arg)if(str.startsWith(args[0]))list.add(str);
		}
		if(args.length == 1) {
			String[] arg = {"keep"};
			for(String str : arg)if(str.startsWith(args[0]))list.add(str);
		}

		return list;
	}
}