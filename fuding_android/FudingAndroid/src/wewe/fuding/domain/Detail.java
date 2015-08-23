package wewe.fuding.domain;

public class Detail {
	private String image; // 음식사진 
	private String content; // 설명 
	private String time; // 소요시간 

	public Detail() {
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
}
