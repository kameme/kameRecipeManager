/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package kame.kameRecipeManager.config.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kame.kameRecipeManager.recipe.RecipeType;
import kame.kameRecipeManager.recipe.VRecipe;

import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class BufferedRecipe {
	private final String name;
	private RecipeType type;
	private List<String> shape = new ArrayList<String>();
	private Map<Integer, ItemStack> rawmap = new HashMap<Integer, ItemStack>();
	private ItemStack result;
	private List<ItemStack> items = new ArrayList<ItemStack>();
	private ItemStack input;
	private int time = 200;
	private List<String> option = new ArrayList<String>();
	private Map<Character, ItemStack> map = new HashMap<Character, ItemStack>();
	private Map<Character, MaterialData> map1 = new HashMap<Character, MaterialData>();
	private String[] furnaceeffect = {""};
	private List<VRecipe> subItem = new ArrayList<VRecipe>();
	private List<ItemStack> fusioninput = new ArrayList<ItemStack>();
	private String[] fusioneffect = {""};
	private String[] fusionsound = {""};
	private boolean exclude = false;
	/**
	 * バッファー用のレシピ、ここにソート中は値をほり込んでいって、レシピが完成したら各レシピに入れていく、最初はレシピ名とレシピタイプを入力
	 * 定型  ：Shape,Result,ItemMap,(option)
	 * 不定形：Result,ItemList,(option)
	 * かまど：Result,Input,(Time),(option)
	 * @param name
	 * @param type
	 */
	@Type.All
	public BufferedRecipe(String name, RecipeType type) {
		this.name = name;
		this.type = type;
	}

	@Type.All
	public RecipeType getType() {
		return type;
	}

	@Type.All
	public void droped() {
		type = RecipeType.Default;
	}

	@Type.All
	public void exclude() {
		exclude = true;
	}
	
	@Type.All
	public boolean isExclude() {
		return exclude;
	}
	@Type.All
	public void setResult(ItemStack item) {
		result = item;
	}

	@Type.Shaped
	public void addShape(String shapes) {
		shape.add(shapes);
	}

	@Type.Shapeless
	public List<String> shape() {
		return shape;
	}

	@Type.Shaped
	public int[] shapeSize() {
		int[] size = new int[2];
		for(String shape : shape)if(shape.length() > size[0])size[0] = shape.length();
		size[1] = shape.size();
		return size;
	}

	@Type.Shaped
	public void addMapRaw(int key, ItemStack item) {
		if(item == null)return;
		rawmap.put(key, item);
	}

	@Type.Shaped
	public Map<Integer, ItemStack> getRawMap() {
		return rawmap;
	}

	@Type.Shaped
	public void addMap(Character key, ItemStack item) {
		if(item == null) {
			return;
		}
		map.put(key, item);
		map1.put(key, item.getData());
	}

	@Type.Shaped
	public Map<Character, ItemStack> getCustomMap() {
		return map;
	}

	@Type.Shaped
	public Map<Character, MaterialData> getBukkitMap() {
		return map1;
	}

	@Type.Shapeless
	public void addShapeless(ItemStack item) {
		if(items.size() < 10) {
			items.add(item);
		}
	}

	@Type.Shapeless
	public List<ItemStack> getShapeless() {
		return items;
	}

	@Type.Furnace
	public void setInput(ItemStack item) {
		input = item;
	}

	@Type.Furnace
	public void setFurnaceEffect(String effects) {
		String[] eff = effects.split(":");
		String[] effect = {"", "0", "0", "0", "0"};
		if(eff.length > 0)effect[0] = eff[0];
		if(eff.length > 1 && eff[1].matches("[0-9]+"))effect[1] = eff[1];
		if(eff.length > 2 && eff[1].matches("[0-9]+"))effect[2] = eff[2];
		if(eff.length > 3 && eff[1].matches("[0-9]+(.[0-9]+)?"))effect[3] = eff[3];
		if(eff.length > 4 && eff[1].matches("[0-9]+(.[0-9]+)?"))effect[4] = eff[4];
		furnaceeffect = effect;
	}

	@Type.Furnace
	public String[] getFurnaceEffect() {
		return furnaceeffect;
	}

	@Type.Furnace
	public ItemStack getInput() {
		return input;
	}

	@Type.Furnace
	public void setTime(int a) {
		time = a;
	}

	@Type.Furnace
	public int getTime() {
		return time;
	}

	@Type.All
	public void addOption(String str) {
		option.add(str);
	}

	@Type.All
	public void addVRecipe(VRecipe v) {
		subItem.add(v);
	}

	@Type.All
	public List<VRecipe> getVRecipe() {
		return subItem;
	}

	@Type.All
	public boolean isStrictly() {
		return option.contains("strictly:off");
	}

	@Type.Shaped
	public boolean isShapeFull() {
		return shape.size() >= 3;
	}

	@Type.All
	public boolean isReady() {
		switch (getType()) {
		case Default:
			return false;
		case Shapeless:
			return (result != null) && (items.size() > 0);
		case Furnace:
			return (result != null) && (input != null);
		case Shaped:
			return (result != null) && (rawmap.size() > 0) && (shape.size() > 0);
		case Fusion:
			return (result != null) && (fusioninput.size() > 0);
		default:
			break;
		}
		return false;
	}

	@Type.All
	public String getRecipeName() {
		return name;
	}

	@Type.All
	public ItemStack getResult() {
		return result;
	}

	@Type.All
	public List<String> getOption() {
		return option;
	}

	@Type.Fusion
	public void addFusionInput(ItemStack item) {
		fusioninput.add(item);
	}

	@Type.Fusion
	public List<ItemStack> getFusionInput() {
		return fusioninput;
	}

	@Type.Fusion
	public int getFusionTime() {
		return time;
	}

	@Type.Fusion
	public void setFusionTime(int time) {
		this.time = time;
	}

	@Type.Fusion
	public boolean isFusionFull() {
		return fusioninput.size() > 3;
	}

	@Type.Fusion
	public void setFusionEffect(String effects) {
		String[] eff = effects.split(":");
		String[] effect = {"", "0", "0", "0", "0"};
		if(eff.length > 0)effect[0] = eff[0];
		if(eff.length > 1 && eff[1].matches("[0-9]+"))effect[1] = eff[1];
		if(eff.length > 2 && eff[1].matches("[0-9]+"))effect[2] = eff[2];
		if(eff.length > 3 && eff[1].matches("[0-9]+(.[0-9]+)?"))effect[3] = eff[3];
		if(eff.length > 4 && eff[1].matches("[0-9]+(.[0-9]+)?"))effect[4] = eff[4];
		fusioneffect = effect;
	}

	@Type.Fusion
	public String[] getFusionEffect() {
		return fusioneffect;
	}

	@Type.Fusion
	public void setFusionSound(String sounds) {
		String[] eff = sounds.split(":");
		String[] effect = {"", "0", "0"};
		if(eff.length > 0)effect[0] = eff[0];
		if(eff.length > 1 && eff[1].matches("[0-9]+(.[0-9]+)?"))effect[1] = eff[1];
		if(eff.length > 2 && eff[1].matches("[0-9]+(.[0-9]+)?"))effect[2] = eff[2];
		fusionsound = effect;
	}

	@Type.Fusion
	public String[] getFusionSound() {
		return fusionsound;
	}
}
