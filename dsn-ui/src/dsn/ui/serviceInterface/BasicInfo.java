package dsn.ui.serviceInterface;

public class BasicInfo {
	private String userid;
	private String name;
	private String photoUrl;
	private String LargePhotoUrl;
	private String link;
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhotoUrl() {
		return photoUrl;
	}
	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}
	public String getLargePhotoUrl() {
		return LargePhotoUrl;
	}
	public void setLargePhotoUrl(String largePhotoUrl) {
		LargePhotoUrl = largePhotoUrl;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}

}
