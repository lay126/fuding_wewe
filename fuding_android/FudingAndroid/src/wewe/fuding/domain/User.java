package wewe.fuding.domain;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
	private String userEmail;
	private String userPwd;
	private String userId;
	private String userName;
	private String userPhoto;
	private int followings;
	private int followers;
	private int writes;

	public User() {
		super();
	}

	public User(String userEmail, String userPwd, String userId,
			String userName, String userPhoto, int followings, int followers,
			int writes) {
		super();
		this.userEmail = userEmail;
		this.userPwd = userPwd;
		this.userId = userId;
		this.userName = userName;
		this.userPhoto = userPhoto;
		this.followings = followings;
		this.followers = followers;
		this.writes = writes;
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

	public int getFollowings() {
		return followings;
	}

	public void setFollowings(int followings) {
		this.followings = followings;
	}

	public int getFollowers() {
		return followers;
	}

	public void setFollowers(int followers) {
		this.followers = followers;
	}

	public int getWrites() {
		return writes;
	}

	public void setWrites(int writes) {
		this.writes = writes;
	}

	public static User fromJson(JSONObject object) throws JSONException {
		return new User(object.getString("userEmail"),
				object.getString("userPwd"), object.getString("userId"),
				object.getString("userName"), object.getString("userPhoto"),
				object.getInt("followings"), object.getInt("followers"),
				object.getInt("writes"));
	}
}
