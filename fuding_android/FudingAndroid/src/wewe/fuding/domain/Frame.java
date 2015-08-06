package wewe.fuding.domain;

import org.json.JSONException;
import org.json.JSONObject;

public class Frame {
	private String userId; // 글쓴이 아이디
	private String foodName; // 요리 이름
	private String ingre; // 재료 태그
	private String amount; // 요리 양
	private String totalTime; // 소요 시간
	private String tag; //  기타 태그
	private int likeCnt; // 좋아요 수
	private String writeDate;

	public Frame() {
	}

	public Frame(String userId, String foodName, String ingre, String amount,
			String totalTime, String tag, int likeCnt, String writeDate) {
		this.userId = userId;
		this.foodName = foodName;
		this.ingre = ingre;
		this.amount = amount;
		this.totalTime = totalTime;
		this.tag = tag;
		this.likeCnt = likeCnt;
		this.writeDate = writeDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFoodName() {
		return foodName;
	}

	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}

	public String getIngre() {
		return ingre;
	}

	public void setIngre(String ingre) {
		this.ingre = ingre;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public int getLikeCnt() {
		return likeCnt;
	}

	public void setLikeCnt(int likeCnt) {
		this.likeCnt = likeCnt;
	}

	public String getWriteDate() {
		return writeDate;
	}

	public void setWriteDate(String writeDate) {
		this.writeDate = writeDate;
	}

	public static Frame fromJson(JSONObject object) throws JSONException {
		return new Frame(object.getString("userId"),
				object.getString("foodName"), object.getString("ingre"),
				object.getString("amount"), object.getString("totalTime"),
				object.getString("tag"), object.getInt("likeCnt"),
				object.getString("writeDate"));
	}

}
