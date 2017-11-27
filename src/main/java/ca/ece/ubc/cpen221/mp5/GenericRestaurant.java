package ca.ece.ubc.cpen221.mp5;

import java.util.HashSet;

public class GenericRestaurant extends GenericObject{
	protected final boolean status;
	protected final String url;
	protected final double longitude;
	protected final double latitude;
	protected final HashSet<String> neighborhoods;
	protected final HashSet<String> categories;
	protected final HashSet<String> schools;
	protected final String businessId;
	protected final String name;
	protected final String state;
	protected final String BType;
	protected final double stars;
	protected final String city;
	protected final String fullAddress;
	protected final int reviewCount;
	protected final String photoUrl;
	protected final int price;
	
	public GenericRestaurant(String id, boolean status, String url, double longitude, double latitude,
							 HashSet<String> neighborhoods, HashSet<String> categories, HashSet<String> schools,
							 String businessId, String name, String state, String BType, double stars2, String city,
							 String fullAddress, int reviewCount, String photoUrl, int price) {
		super(id, "restaurant");
		this.status = status;
		this.url = url;
		this.longitude = longitude;
		this.latitude = latitude;
		this.neighborhoods = neighborhoods;
		this.categories = categories;
		this.schools = schools;
		this.businessId = businessId;
		this.name = name;
		this.state = state;
		this.BType = BType;
		this.stars = stars2;
		this.city = city;
		this.fullAddress = fullAddress;
		this.reviewCount = reviewCount;
		this.photoUrl = photoUrl;
		this.price = price;
	}
	public boolean getStatus() {
		return this.status;
	}
	public String getId() {
		return this.id;
	}
	public String getBusinessId() {
		return this.businessId;
	}
	public String getUrl() {
		return this.url;
	}
	public String getName() {
		return this.name;
	}
	public String getState() {
		return this.state;
	}
	public String getBType() {
		return this.BType;
	}
	public double getStars() {
		return this.stars;
	}
	public String getCity() {
		return this.city;
	}
	public String getFullAddress() {
		return this.fullAddress;
	}
	public String getPhotoUrl() {
		return this.photoUrl;
	}
	public int getReviewCount() {
		return this.reviewCount;
	}
	public int getPrice() {
		return this.price;
	}
	public double getLongitude() {
		return this.longitude;
	}
	public double getLatitude() {
		return this.latitude;
	}
	public HashSet<String> getNeighborhoods() {
		return new HashSet<String>(this.neighborhoods);
	}
	public HashSet<String> getCategories() {
		return new HashSet<String>(this.categories);
	}
	public HashSet<String> getSchools() {
		return new HashSet<String>(this.schools);
	}
}
