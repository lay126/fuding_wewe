package wewe.fuding.domain;

import java.util.Date;

public class Recipe {
	private int recipeNum;
	private String recipeName;
	private Date recipeCreatedDate;
	private Date recipeUpdatedDate;
	private String recipeTimes;
	private String recipeTag;
	private int recipeQty;

	public Recipe() {
		super();
	}

	public int getRecipeNum() {
		return recipeNum;
	}

	public void setRecipeNum(int recipeNum) {
		this.recipeNum = recipeNum;
	}

	public String getRecipeName() {
		return recipeName;
	}

	public void setRecipeName(String recipeName) {
		this.recipeName = recipeName;
	}

	public Date getRecipeCreatedDate() {
		return recipeCreatedDate;
	}

	public void setRecipeCreatedDate(Date recipeCreatedDate) {
		this.recipeCreatedDate = recipeCreatedDate;
	}

	public Date getRecipeUpdatedDate() {
		return recipeUpdatedDate;
	}

	public void setRecipeUpdatedDate(Date recipeUpdatedDate) {
		this.recipeUpdatedDate = recipeUpdatedDate;
	}

	public String getRecipeTimes() {
		return recipeTimes;
	}

	public void setRecipeTimes(String recipeTimes) {
		this.recipeTimes = recipeTimes;
	}

	public String getRecipeTag() {
		return recipeTag;
	}

	public void setRecipeTag(String recipeTag) {
		this.recipeTag = recipeTag;
	}

	public int getRecipeQty() {
		return recipeQty;
	}

	public void setRecipeQty(int recipeQty) {
		this.recipeQty = recipeQty;
	}

}
