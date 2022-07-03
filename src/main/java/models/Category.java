package models;

import java.io.Serializable;

import java.util.List;


/**
 * The persistent class for the Category database table.
 * 
 */

public class Category implements Serializable {
	private static final long serialVersionUID = 1L;


	private long id;

	private String category;

	//bi-directional many-to-one association to Account

	private Account account;

	//bi-directional many-to-one association to ChannelCategory

	private List<ChannelCategory> channelCategories;

	public Category() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Account getAccount() {
		return this.account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public List<ChannelCategory> getChannelCategories() {
		return this.channelCategories;
	}

	public void setChannelCategories(List<ChannelCategory> channelCategories) {
		this.channelCategories = channelCategories;
	}

	public ChannelCategory addChannelCategory(ChannelCategory channelCategory) {
		getChannelCategories().add(channelCategory);
		channelCategory.setCategory(this);

		return channelCategory;
	}

	public ChannelCategory removeChannelCategory(ChannelCategory channelCategory) {
		getChannelCategories().remove(channelCategory);
		channelCategory.setCategory(null);

		return channelCategory;
	}

}