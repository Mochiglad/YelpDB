package ca.ece.ubc.cpen221.mp5;

import java.util.HashSet;

public class YelpRestaurant extends GenericRestaurant{

	public YelpRestaurant(String id, boolean status, String url, double longitude, double latitude,
			HashSet<String> neighborhoods, HashSet<String> categories, HashSet<String> schools, String businessId,
			String name, String state, String BType, double stars, String city, String fullAddress, int reviewCount,
			String photoUrl, int price) {
		super(id, status, url, longitude, latitude, neighborhoods, categories, schools, businessId, name, state, BType, stars,
				city, fullAddress, reviewCount, photoUrl, price);
	}
	
}
