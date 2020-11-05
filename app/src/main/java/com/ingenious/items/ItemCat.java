package com.ingenious.items;

/**
 * Company : Ingenious
 * Detailed : Software Development Company in Pakistan
 * Developer : Ingenious
 * Contact : Ingenious.mcc@gmail.com
 * Website : https://www.ingenious.pk/
 */public class ItemCat {

	private String id, name, image, image_thumb, tot_Wall;

	public ItemCat(String id, String name, String image,String image_thumb, String tot_Wall) {
		this.id = id;
		this.name = name;
		this.image = image;
		this.image_thumb = image_thumb;
		this.tot_Wall = tot_Wall;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getImage() {
		return image;
	}

	public String getImageThumb() {
		return image_thumb;
	}

	public String getTotalWallpaper() {
		return tot_Wall;
	}
}
