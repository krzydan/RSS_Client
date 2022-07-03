package models;

import java.io.Serializable;

import java.sql.Time;


/**
 * The persistent class for the NewsArchive database table.
 * 
 */

public class NewsArchive implements Serializable {
	private static final long serialVersionUID = 1L;

	
	private long id;

	private String image;

	private String link;

	private Time pubDate;

	private String title;

	//bi-directional many-to-one association to UserChannel

	private UserChannel userChannel;

	public NewsArchive() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getImage() {
		return this.image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getLink() {
		return this.link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Time getPubDate() {
		return this.pubDate;
	}

	public void setPubDate(Time pubDate) {
		this.pubDate = pubDate;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public UserChannel getUserChannel() {
		return this.userChannel;
	}

	public void setUserChannel(UserChannel userChannel) {
		this.userChannel = userChannel;
	}

}