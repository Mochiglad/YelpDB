package ca.ece.ubc.cpen221.mp5;

import java.util.HashSet;

import org.json.simple.JSONObject;

public class YelpRestaurant extends GenericRestaurant{
	//RI: Restaurant must not be null, stars must be >= 0 and <= 5, 0 <= price <= 5, 
	//AF: (this representation) -> Yelp Restaurant
	public YelpRestaurant(String id, boolean status, String url, double longitude, double latitude,
			HashSet<String> neighborhoods, HashSet<String> categories, HashSet<String> schools, String businessId,
			String name, String state, String BType, double stars, String city, String fullAddress, int reviewCount,
			String photoUrl, int price) {
		super(id, status, url, longitude, latitude, neighborhoods, categories, schools, businessId, name, state, BType, stars,
				city, fullAddress, reviewCount, photoUrl, price);
	}
	
	//Return the object as a json String.
	public String toJson() {
		String output = "{\"open\": " + status + ", \"url\": " + "\"" + url + "\""+ ", \"longitude\": " + longitude + 
				", \"neighborhoods\": " + neighborhoods + ", \"business_id\": " + "\"" + id + "\"" + ", \"name\": " +  "\"" + name + "\""
				+ ", \"categories\": " + categories + ", \"state\": " + "\"" + state + "\"" + ", \"type\": " + "\"" + BType + "\""
				+ ", \"stars\": " + "\"" + stars + "\"" + ", \"city\": " + "\"" + city + "\"" + ", \"full_address\": " + "\"" + fullAddress + "\"" + ", \"longitude\": " + longitude
				+ ", \"review_count\": " + reviewCount + ", \"photo_url\": " + "\"" + photoUrl + "\"" + ", \"schools\": " + schools
				+ ", \"latitude\": " + latitude + ", \"price\": " + price + "}";
		output = output.replaceAll("\n", "\\\\n");
		output = output.replaceAll("\\[\\[", "\\[");
		output = output.replaceAll("\\]\\]", "\\]");
		
		return output;
	}

}
