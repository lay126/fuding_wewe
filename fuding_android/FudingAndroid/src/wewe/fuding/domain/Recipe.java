package wewe.fuding.domain;

import java.util.Date;

public class Recipe {
	private int recipeNum;			// 
	private int recipeQty;			//

	private String recipeName;		// 요리이름 
	private Date recipeCreatedDate;	// 업로드시간 
	private Date recipeUpdatedDate;	// 수정시간 
	private String recipeTimes;		// 소요시간 
	private String recipeTag;		// 재료태그 

	private String content;			// 단계설명 
	private String imageURL;		// 사진 URL


	public Recipe() {
		super();
	}

	public String getContent() {
		return content;
	}

	public String getImageURL() {
		return imageURL;
	}
	
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	
	public void setContent(String content) {
		this.content = content;
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
