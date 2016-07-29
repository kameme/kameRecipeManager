package kame.kameRecipeManager.recipe;

import java.util.List;

import kame.kameRecipeManager.config.recipe.BufferedRecipe;

import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public class KFusionRecipe implements KRecipe {


	public class FusionEffect {
		private Effect effect;
		private int id;
		private int data;
		private float speed;

		private FusionEffect(Effect e, String... str) {
			effect = e;
			id = (int) parse(str[1]);
			data = (int) parse(str[2]);
			speed = parse(str[3]);
		}

		public Effect Effect() {
			return effect;
		}
		public int Id() {
			return id;
		}
		public int Data() {
			return data;
		}
		public float Speed() {
			return speed;
		}
	}

	public class FusionSound {
		private Sound sound;
		private float volume;
		private float pitch;

		private FusionSound(Sound s, String... str) {
			sound = s;
			volume = parse(str[1]);
			pitch = parse(str[2]);
		}

		public Sound Sound() {
			return sound;
		}
		public float Volume() {
			return volume;
		}
		public float Pitch() {
			return pitch;
		}
	}

	private float parse(String s) {
		return Float.parseFloat(s);
	}
	private List<ItemStack> input;
	private ItemStack result;
	private List<String> option;
	private int time;
	private boolean struct;
	private FusionEffect effect;
	private FusionSound sound;
	private List<VRecipe> sub;

	public KFusionRecipe(BufferedRecipe buffer) {
		this.result = buffer.getResult();
		this.input = buffer.getFusionInput();
		this.time = buffer.getFusionTime();
		this.option = buffer.getOption();
		this.struct = !buffer.isStrictly();
		this.sub = buffer.getVRecipe();

		String[] E = buffer.getFusionEffect();
		String[] S = buffer.getFusionSound();
		if(!E[0].equals("")) {
			Effect e = getEffect(E[0]);
			if(e != null && E.length == 5)effect = new FusionEffect(e, E);
		}
		if(!S[0].equals("")) {
			Sound s = getSound(S[0]);
			if(s != null && S.length == 5)sound = new FusionSound(s, S);
		}
	}

	private Effect getEffect(String s) {
		try {
			return Effect.valueOf(s.toUpperCase());
		}catch(IllegalArgumentException t) {
			for(Effect e : Effect.values())if(s.equals(e.ordinal() + ""))return e;
		}
		return null;
	}

	private Sound getSound(String s) {
		try {
			return Sound.valueOf(s.toUpperCase());
		}catch(IllegalArgumentException t) {
			for(Sound e : Sound.values())if(s.equals(e.ordinal() + ""))return e;
		}
		return null;
	}

	public List<ItemStack> getInput() {
		return this.input;
	}

	public int getCookTime() {
		return this.time;
	}

	public Recipe getBukkitRecipe() {
		return null;
	}

	public ItemStack getResult() {
		return this.result;
	}

	public List<String> getOption() {
		return this.option;
	}

	public RecipeType getType() {
		return RecipeType.Fusion;
	}

	public FusionEffect getEffect() {
		return effect;
	}

	public FusionSound getSound() {
		return sound;
	}

	public boolean isStrictly() {
		return this.struct;
	}

	public List<VRecipe> getProducts() {
		return sub;
	}

}
