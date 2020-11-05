package com.ingenious.items;

/**
 * Company : Ingenious
 * Detailed : Software Development Company in Pakistan
 * Developer : Ingenious
 * Contact : Ingenious.mcc@gmail.com
 * Website : https://www.ingenious.pk/
 */public class ItemWallpaper {

	private String id, CId, CName, image, imageThumb, totalViews, totalRate, averageRate, pay, totalDownloads, resolution="", size="", userRating = "0", total_set="0", total_share="0";

	public ItemWallpaper(String id, String cId, String cName, String image, String imageThumb, String totalViews, String totalRate, String averageRate, String pay, String totalDownloads) {
		this.id = id;
		this.CId = cId;
		this.CName = cName;
		this.image = image;
		this.imageThumb = imageThumb;
		this.totalViews = totalViews;
		this.totalRate = totalRate;
		this.averageRate = averageRate;
		this.pay = pay;
		this.totalDownloads = totalDownloads;
	}

	public String getId() {
		return id;
	}

	public String getCId() {
		return CId;
	}

	public String getCName() {
		return CName;
	}

	public String getImage() {
		return image;
	}

	public String getImageThumb() {
		return imageThumb;
	}

	public String getTotalViews() {
		return totalViews;
	}

	public String getTotalRate() {
		return totalRate;
	}

	public String getAverageRate() {
		return averageRate;
	}

	public void setTotalViews(String totalViews) {
		this.totalViews = totalViews;
	}

	public void setTotalRate(String totalRate) {
		this.totalRate = totalRate;
	}

	public void setAverageRate(String averageRate) {
		this.averageRate = averageRate;
	}

	public String getPay() {
		return pay;
	}

	public String getTotalDownloads() {
		return totalDownloads;
	}

	public void setTotalDownloads(String totalDownloads) {
		this.totalDownloads = totalDownloads;
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


	public String getTotal_set() {
		return total_set;
	}

	public void setTotal_set(String total_set) {
		this.total_set = total_set;
	}

	public String getTotal_share() {
		return total_share;
	}

	public void setTotal_share(String total_share) {
		this.total_share = total_share;
	}
}
