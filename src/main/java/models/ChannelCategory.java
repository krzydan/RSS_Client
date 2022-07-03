package models;

import java.io.Serializable;


/**
 * The persistent class for the ChannelCategory database table.
 * 
 */

public class ChannelCategory implements Serializable {
	private static final long serialVersionUID = 1L;


	private long id;

	//bi-directional many-to-one association to Category

	private Category category;

	//bi-directional many-to-one association to UserChannel

	private UserChannel userChannel;

	public ChannelCategory() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Category getCategory() {
		return this.category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public UserChannel getUserChannel() {
		return this.userChannel;
	}

	public void setUserChannel(UserChannel userChannel) {
		this.userChannel = userChannel;
	}

}