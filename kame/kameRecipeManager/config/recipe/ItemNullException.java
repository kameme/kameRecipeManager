/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package kame.kameRecipeManager.config.recipe;

public class ItemNullException extends Exception {
	String error;

	public ItemNullException(String string) {
		this.error = string;
	}

	public String getError() {
		return this.error;
	}
}
