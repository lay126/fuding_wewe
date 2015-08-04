package wewe.fuding.domain;

import java.util.Date;

public class Content {
	 
	private int foodId;			// 음식 프레임 고유 번호  
	private int contentId;	// 단계 순서 번호 
	private String content;		// 단계 별 설명 
	private String contentTime;	// 단계 별 소요 시간 
	private String photo;		// 사진 URL

	
	public int getFoodId() {
		return foodId;
	}
	public void setFoodId(int foodId) {
		this.foodId = foodId;
	}
	public int getContentId() {
		return contentId;
	}
	public void setContentId(int contentId) {
		this.contentId = contentId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getContentTime() {
		return contentTime;
	}
	public void setContentTime(String contentTime) {
		this.contentTime = contentTime;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}



}
