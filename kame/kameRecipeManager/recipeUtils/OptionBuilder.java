package kame.kameRecipeManager.recipeUtils;

import java.util.List;

import kame.kameRecipeManager.Utils.Buffer;
import kame.kameRecipeManager.recipe.KRecipe;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class OptionBuilder {

	public static Buffer optionBuild(List<String> options, Player player, ItemStack item, Location loc, KRecipe recipe) {
		Buffer buf = new Buffer(player, item, recipe);
		for(String option : options) {
			String[] args = option.split(":");
			switch (args[0]) {
			case "pathsound":
			case "failsound":
				buf.addSound(args[0].startsWith("path"), args);
				break;
			case "patheffect":
			case "faileffect":
				buf.addEffect(args[0].startsWith("path"), args);
				break;
			case "pathexpand":
			case "failexpand":
				buf.addOption(args[0].startsWith("path"), option);
				break;
			case "pathplcmd":
			case "failplcmd":
				buf.addOption(args[0].startsWith("path"), option);
				break;
			case "pathclcmd":
			case "failclcmd":
				buf.addOption(args[0].startsWith("path"), option);
				break;
			case "pathinfo":
			case "failinfo":
				buf.addMessage(args[0].startsWith("path"), Buffer.replaceLists(option.replaceFirst(args[0] + ":", ""), loc, player));
				break;
			case "playsound":
				buf.addSound(args);
				buf.plays(player.getLocation(), null);
				break;
			case "playeffect":
				buf.addEffect(args);
				buf.playe(loc, null);
				break;
			case "broadcast":
				Bukkit.broadcastMessage(Buffer.replaceLists(option.replaceFirst(args[0] + ":", ""), loc, player));
				break;
			case "info":
				player.sendMessage(Buffer.replaceLists(option.replaceFirst(args[0] + ":", ""), loc, player));
				break;
			case "permission":
				buf.setPerm(args);
				break;
			case "level":
				buf.setLevel(args);
				break;
			case "levelcost":
				buf.setCost(args);
				break;
			case "world":
				buf.setWorld(args);
				break;
			case "biome":
				buf.setBiome(args);
				break;
			default:
			}
		}
		return buf;
	}
}
