package models;

import java.io.Serializable;

import java.util.List;


/**
 * The persistent class for the UserChannel database table.
 * 
 */

public class UserChannel implements Serializable {
	private static final long serialVersionUID = 1L;


	private long id;

	private String channelURL;

	//bi-directional many-to-one association to ChannelCategory

	private List<ChannelCategory> channelCategories;

	//bi-directional many-to-one association to NewsArchive

	private List<NewsArchive> newsArchives;

	//bi-directional many-to-one association to Account
	
	private Account account;

	public UserChannel() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getChannelURL() {
		return this.channelURL;
	}

	public void setChannelURL(String channelURL) {
		this.channelURL = channelURL;
	}

	public List<ChannelCategory> getChannelCategories() {
		return this.channelCategories;
	}

	public void setChannelCategories(List<ChannelCategory> channelCategories) {
		this.channelCategories = channelCategories;
	}

	public ChannelCategory addChannelCategory(ChannelCategory channelCategory) {
		getChannelCategories().add(channelCategory);
		channelCategory.setUserChannel(this);

		return channelCategory;
	}

	public ChannelCategory removeChannelCategory(ChannelCategory channelCategory) {
		getChannelCategories().remove(channelCategory);
		channelCategory.setUserChannel(null);

		return channelCategory;
	}

	public List<NewsArchive> getNewsArchives() {
		return this.newsArchives;
	}

	public void setNewsArchives(List<NewsArchive> newsArchives) {
		this.newsArchives = newsArchives;
	}

	public NewsArchive addNewsArchive(NewsArchive newsArchive) {
		getNewsArchives().add(newsArchive);
		newsArchive.setUserChannel(this);

		return newsArchive;
	}

	public NewsArchive removeNewsArchive(NewsArchive newsArchive) {
		getNewsArchives().remove(newsArchive);
		newsArchive.setUserChannel(null);

		return newsArchive;
	}

	public Account getAccount() {
		return this.account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

}