/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package kame.kameRecipeManager.Utils;

import java.util.ArrayList;
import java.util.List;

import kame.kameRecipeManager.Main;
import kame.kameRecipeManager.recipe.KRecipe;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Buffer {
	private class Sounds {
		private Sound sound;
		private float pitch;
		private float volume;

		private Sounds(Sound sound, float volume, float pitch) {
			this.sound = sound;
			this.volume = volume;
			this.pitch = pitch;
		}

		private Sound S() {
			return this.sound;
		}

		private float V() {
			return this.volume;
		}

		private float P() {
			return this.pitch;
		}
	}

	private class Effects {
		private Effect effect;
		private int volume;

		private Effects(Effect effect, int volume) {
			this.effect = effect;
			this.volume = volume;
		}

		private Effect E() {
			return this.effect;
		}

		private int V() {
			return this.volume;
		}
	}

	private float p(String i) {
		return i.matches("[0-9]+(.[0-9]+)?") ? Float.parseFloat(i) : 0.0F;
	}

	private List<Effects> pEffect = new ArrayList<Effects>();//path
	private List<Effects> fEffect = new ArrayList<Effects>();//fail
	private List<Effects> sEffect = new ArrayList<Effects>();//success
	private List<Sounds> pSound = new ArrayList<Sounds>();
	private List<Sounds> fSound = new ArrayList<Sounds>();
	private List<Sounds> sSound = new ArrayList<Sounds>();

	private List<String> fem = new ArrayList<String>();
	private List<String> sem = new ArrayList<String>();
	private List<String> opt = new ArrayList<String>();
	private List<String> ops = new ArrayList<String>();
	private List<String> opf = new ArrayList<String>();
	private String perm = "";




	private int levelmax = Integer.MAX_VALUE;
	private int levelmin = 0;
	private int xpmax = Integer.MAX_VALUE;
	private int xpmin = 0;
	private int xp = 0;
	private int cost = 0;
	private String biome = "";
	private String world = "";
	private Player p;
	private ItemStack item;
	private KRecipe recipe;

	public Buffer(Player p, ItemStack item, KRecipe recipe) {
		this.p = p;
		this.item = item;
		this.recipe = recipe;
	}

	public KRecipe getRecipe() {
		return recipe;
	}

	public ItemStack getResult() {
		return item;
	}

	public void setPerm(String perm) {
		this.perm = perm;
	}

	public boolean hasPerm(Player player) {
		return player.hasPermission(this.perm);
	}

	public void plays(Location l, Boolean bool) {
		if(bool==null)for(Sounds s : (Sounds[])sSound.toArray(new Sounds[0]))p.playSound(l, s.S(), s.V(), s.P());
		else if (bool)for(Sounds s : (Sounds[])pSound.toArray(new Sounds[0]))p.playSound(l, s.S(), s.V(), s.P());
		else if(!bool)for(Sounds s : (Sounds[])fSound.toArray(new Sounds[0]))p.playSound(l, s.S(), s.V(), s.P());
	}

	public void playe(Location l, Boolean bool) {
		if(bool==null)for(Effects e : (Effects[])sEffect.toArray(new Effects[0]))p.playEffect(l, e.E(), e.V());
		else if (bool)for(Effects e : (Effects[])pEffect.toArray(new Effects[0]))p.playEffect(l, e.E(), e.V());
		else if(!bool)for(Effects e : (Effects[])fEffect.toArray(new Effects[0]))p.playEffect(l, e.E(), e.V());
	}

	public void playm(boolean bool) {
		if(bool){
			p.sendMessage((String[]) this.sem.toArray(new String[0]));
			sem.clear();
		}else {
			p.sendMessage((String[]) this.fem.toArray(new String[0]));
			fem.clear();
		}
	}

	public void playo(Location loc, Boolean bool, Player player) {
		if(bool == null) {
		}else if(bool) {
			for(String line : this.ops) {
				if(line.matches("pathexpand(:[0-9]+(:true|:false)?)?"))expand(loc, player, line.split(":"));
				if(line.matches("pathplcmd:.*"))command(player, replaceLists(line.replaceFirst(".*cmd:",""), loc, player));
				if(line.matches("pathclcmd:.*"))command(replaceLists(line.replaceFirst(".*cmd:",""), loc, player));
			}
		}else {
			for(String line : this.opf){
				if(line.matches("failexpand(:[0-9]+(:true|:false)?)?"))expand(loc, player, line.split(":"));
				if(line.matches("failplcmd:.*"))command(player, replaceLists(line.replaceFirst(".*cmd:",""), loc, player));
				if(line.matches("failclcmd:.*"))command(replaceLists(line.replaceFirst(".*cmd:",""), loc, player));
			}
		}
	}
	public static String replaceLists(String line, Location loc, Player player) {
		if(loc == null)loc = player.getLocation();
		line = line.replaceAll("<loc>", loc(loc)).replaceAll("<player>", player.getName());
		return line.startsWith("/") ? line.replaceAll("/", "") : line;
	}
	
	private static String loc(Location loc) {
		return loc.getX() + " " + loc.getY() + " " + loc.getZ();
	}
	private void expand(Location loc, Player player, String... args) {
		float f = 4;
		boolean b = false;
		if (args.length > 1)f = p(args[1]);
		if ((args.length > 2) && (args[2].equals("true")))b = true;
		loc.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), f, b, b);
		player.closeInventory();
	}

	private void command(CommandSender sender, String cmd) {
		new BukkitRunnable() {
			@Override
			public void run() {
				if(sender.isOp()) {
					Bukkit.dispatchCommand(sender, cmd);
				}else {
					sender.setOp(true);
					Bukkit.dispatchCommand(sender, cmd);
					sender.setOp(false);
				}
				Main.cast("コマンド実行にゃん！ " + cmd);
				
			}
			
		}.runTask(Main.getPlugin());
	}

	private void command(String cmd) {
		command(Bukkit.getConsoleSender(), cmd);
	}
	public void addOption(boolean b, String str) {
		if(b)ops.add(str);
		else opf.add(str);
	}

	public void addSound(Sound s, float v, float p) {
		this.pSound.add(new Sounds(s, v, p));
	}

	public void S(List<Sounds> sss, String... s) {
		Sound ss = null;
		float v = 1;
		float p = 1;
		try {
			if(s.length > 1)ss = Sound.valueOf(s[1].toUpperCase());
			if(s.length > 2)v = p(s[2]);
			if(s.length > 3)p = p(s[3]);
		} catch (Exception e) {}
		Main.cast(ss + " " + v + " " + p);
		if (ss != null)sss.add(new Sounds(ss, v, p));
	}

	public void E(List<Effects> fff, String... s) {
		Effect ff = null;
		int v = 1;
		try {
			if (s.length > 1)ff = Effect.valueOf(s[1].toUpperCase());
			if (s.length > 2)v = (int) p(s[2]);
		} catch (Exception e) {}
		Main.cast(ff + " " + v);
		if (ff != null)fff.add(new Effects(ff, v));
	}

	public void addSound(String... option) {
		S(sSound, option);
	}

	public void addEffect(String... option) {
		E(sEffect, option);
	}

	public void addSound(boolean b, String... option) {
		if(!b)S(fSound, option);
		else S(pSound, option);
	}

	public void addEffect(boolean b, String... option) {
		if(!b)E(fEffect, option);
		else E(pEffect, option);
	}

	public void addMessage(boolean b, String... option) {
		if(!b)fem.add(option[0]);
		else sem.add(option[0]);

	}

	public boolean result(Player player, Block block) {
		if(!isMatchLevel(player.getLevel()))return false;
		if(!isMatchXp(player.getExp()))return false;
		if(!perm.equals("") && !hasPerm(player))return false;
		if(!world.equals("") && !block.getWorld().getName().equals(world))return false;
		if(!biome.equals("") && !block.getBiome().name().equals(biome))return false;
		return true;
	}

	public boolean resultCraft(Player player, Block block) {
		if(!isMatchLevel(player.getLevel()))return false;
		if(!isMatchXp(player.getExp()))return false;
		if(!perm.equals("") && !hasPerm(player))return false;
		if(!world.equals("") && !block.getWorld().getName().equals(world))return false;
		if(!biome.equals("") && !block.getBiome().name().equals(biome))return false;

		player.setLevel(player.getLevel() - cost);
		player.setExp(player.getExp() - xp);
		return true;
	}

	private boolean isMatchLevel(int playerlevel) {
		return playerlevel <= levelmax && playerlevel >= levelmin;
	}

	public void setLevel(String... s) {
		if ((s.length > 1) && (s[1].replaceAll(" ", "").matches("[0-9]+(-[0-9]+)?"))) {
			String[] args = s[1].split("-");
			levelmin = ((int) p(args[0]));
			if (args.length > 1)levelmax = (int) p(args[1]);
		}
	}
	public void levelCost(boolean b) {
		p.setLevel(p.getLevel() - this.cost);
	}

	public boolean isMatchXp(float playerlevel) {
		return (playerlevel <= xpmax) && (playerlevel >= xpmin);
	}

	public void setXp(String... s) {
		if ((s.length > 1) && (s[1].replaceAll(" ", "").matches("[0-9]+(-[0-9]+)?"))) {
			String[] args = s[1].split("-");
			xpmin = (int) p(args[0]);
			if (args.length > 1)xpmax = (int) p(args[1]);
		}
	}

	public void setCost(String[] args) {
		if(args.length > 1 && args[1].matches("[0-9]+"))cost = ((int) p(args[1]));
	}

	public void setPerm(String[] args) {
		if(args.length > 1)perm = args[1];
	}

	public void setWorld(String[] args) {
		if(args.length > 1)world = args[1];
	}

	public void setBiome(String[] args) {
		if(args.length > 1)biome = args[1];
	}
}
