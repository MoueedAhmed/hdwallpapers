package com.ingenious.items;

/**
 * Company : Ingenious
 * Detailed : Software Development Company in Pakistan
 * Developer : Ingenious
 * Contact : Ingenious.mcc@gmail.com
 * Website : https://www.ingenious.pk/
 */public class ItemGIF {

	private String id, image, views, totalRate, aveargeRate, pay, totalDownload, resolution="", size="", userRating = "0";;

	public ItemGIF(String id, String image, String views, String totalRate, String aveargeRate, String pay, String totalDownload) {
		this.id = id;
		this.image = image;
		this.views = views;
		this.totalRate = totalRate;
		this.aveargeRate = aveargeRate;
		this.pay = pay;
		this.totalDownload = totalDownload;
	}

	public String getId() {
		return id;
	}

	public String getImage() {
		return image;
	}

	public String getTotalViews() {
		return views;
	}

	public void setTotalViews(String views) {
		this.views = views;
	}

	public String getTotalRate() {
		return totalRate;
	}

	public String getAveargeRate() {
		return aveargeRate;
	}

	public void setAveargeRate(String aveargeRate) {
		this.aveargeRate = aveargeRate;
	}

	public String getTotalDownload() {
		return totalDownload;
	}

	public void setTotalDownload(String totalDownload) {
		this.totalDownload = totalDownload;
	}

	public String getPay() {
		return pay;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getUserRating() {
		return userRating;
	}

	public void setUserRating(String userRating) {
		this.userRating = userRating;
	}
}
