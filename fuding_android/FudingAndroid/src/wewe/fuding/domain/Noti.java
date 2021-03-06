package wewe.fuding.domain;

public class Noti {
	private String image; // 친구 이미지 
	private String type; // 글 타입 
	private String date; // 날짜, 시간 
	private String friendId; // 친구 아이디  -> 친구를 누르면 친구의 프로필로 넘어가야하지 않을까? 
	private String wf_index; // 글번호 
	private String noti_read; //글 읽기 확인 
	
	
	public Noti() {
	}
	public Noti(String image, String type, String date, String friendId) {
		this.image = image;
		this.type = type;
		this.date = date;
		this.friendId = friendId;
		
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getFriendId() {
		return friendId;
	}

	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}

	public String getWf_index() {
		return wf_index;
	}

	public void setWf_index(String wf_index) {
		this.wf_index = wf_index;
	}

	public String getNoti_read() {
		return noti_read;
	}

	public void setNoti_read(String noti_read) {
		this.noti_read = noti_read;
	}
	
}
