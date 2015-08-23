package wewe.fuding.domain;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
	private int userNum;
	private String userEmail;
	private String userPwd;
	private String userId;
	private String userName;
	private String userPhoto;

	public User() {
		super();
	}

	public User(int userNum, String userEmail, String userPwd, String userId,
			String userName, String userPhoto) {
		this.userNum = userNum;
		this.userEmail = userEmail;
		this.userPwd = userPwd;
		this.userId = userId;
		this.userName = userName;
		this.userPhoto = userPhoto;
	}

	public int getUserNum() {
		return userNum;
	}

	public void setUserNum(int userNum) {
		this.userNum = userNum;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPhoto() {
		return userPhoto;
	}

	public void setUserPhoto(String userPhoto) {
		this.userPhoto = userPhoto;
	}

	public static User fromJson(JSONObject object) throws JSONException {
		return new User(object.getInt("userNum"),
				object.getString("userEmail"), object.getString("userPwd"),
				object.getString("userId"), object.getString("userName"),
				object.getString("userPhoto"));
	}
}
