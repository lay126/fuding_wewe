package wewe.fuding.domain;

public class Userdata {
	private int userId;
	private int userPoints;
	private int userWrites;
	private int userLikes;

	public Userdata() {
		super();
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getUserPoints() {
		return userPoints;
	}

	public void setUserPoints(int userPoints) {
		this.userPoints = userPoints;
	}

	public int getUserWrites() {
		return userWrites;
	}

	public void setUserWrites(int userWrites) {
		this.userWrites = userWrites;
	}

	public int getUserLikes() {
		return userLikes;
	}

	public void setUserLikes(int userLikes) {
		this.userLikes = userLikes;
	}

}
