package ca.ece.ubc.cpen221.mp5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.ToDoubleBiFunction;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class YelpDb implements MP5Db {
	private ArrayList<YelpUser> users;
	private ArrayList<YelpReview> reviews;
	private ArrayList<YelpRestaurant> restaurants;

	public YelpDb(String userJsonPath, String reviewJsonPath, String restaurantJsonPath)
			throws FileNotFoundException, IOException, ParseException {
		readRestaurantJson(restaurantJsonPath);
		readReviewJson(reviewJsonPath);
		readUserJson(userJsonPath);
	}

	private void readRestaurantJson(String filePath) throws FileNotFoundException, IOException, ParseException {
		/*
		 * id, status, url, longitude, latitude, neighborhoods, categories, schools,
		 * businessId, name, state, BType, stars, city, fullAddress, reviewCount,
		 * photoUrl, price
		 */
		YelpRestaurant yRestaurant;
		boolean status;
		String url;
		double longitude;
		double latitude;
		HashSet<String> neighborhoods;
		HashSet<String> categories;
		HashSet<String> schools;
		String businessId;
		String name;
		String state;
		String BType;
		int stars;
		String city;
		String fullAddress;
		int reviewCount;
		String photoUrl;
		int price;

		JSONParser parser = new JSONParser();
		JSONArray jsonArray = (JSONArray) parser.parse(new FileReader(filePath));

		for (Object o : jsonArray) {
			JSONObject restaurant = (JSONObject) o;
			status = (boolean) restaurant.get("open");
			url = (String) restaurant.get("url");
			businessId = (String) restaurant.get("business_id");
			name = (String) restaurant.get("name");
			state = (String) restaurant.get("state");
			city = (String) restaurant.get("city");
			BType = (String) restaurant.get("type");
			fullAddress = (String) restaurant.get("full_address");
			photoUrl = (String) restaurant.get("photo_url");
			longitude = (Double) restaurant.get("longitude");
			latitude = (Double) restaurant.get("latitude");
			stars = (Integer) restaurant.get("stars");
			reviewCount = (Integer) restaurant.get("review_count");
			price = (Integer) restaurant.get("price");
			neighborhoods = (HashSet<String>) restaurant.get("categories");
			categories = (HashSet<String>) restaurant.get("categories");
			schools = (HashSet<String>) restaurant.get("schools");

			yRestaurant = new YelpRestaurant(businessId, status, url, longitude, latitude, neighborhoods, categories,
					schools, businessId, name, state, BType, stars, city, fullAddress, reviewCount, photoUrl, price);
			restaurants.add(yRestaurant);
		}

	}

	private void readReviewJson(String filePath) throws FileNotFoundException, IOException, ParseException {
		YelpReview yReview;
		String businessId;
		HashMap<String, Integer> votes;
		String text;
		int stars;
		String userId;
		String date;

		JSONParser parser = new JSONParser();
		JSONArray jsonArray = (JSONArray) parser.parse(new FileReader(filePath));

		for (Object o : jsonArray) {
			JSONObject review = (JSONObject) o;
			businessId = (String) review.get("business_id");
			votes = (HashMap<String, Integer>) review.get("votes");
			text = (String) review.get("text");
			stars = (Integer) review.get("stars");
			userId = (String) review.get("user_id");
			date = (String) review.get("date");

			yReview = new YelpReview(businessId, businessId, votes, text, stars, userId, date);
			reviews.add(yReview);
		}
	}

	private void readUserJson(String filePath) throws FileNotFoundException, IOException, ParseException {
		YelpUser yUser;
		String url;
		HashMap<String, Integer> votes;
		int reviewCount;
		String name;
		int averageStars;
		String userId;
		
		JSONParser parser = new JSONParser();
		JSONArray jsonArray = (JSONArray) parser.parse(new FileReader(filePath));

		for (Object o : jsonArray) {
			JSONObject user = (JSONObject) o;
			url = (String) user.get("url");
			votes = (HashMap<String, Integer>) user.get("votes");
			reviewCount = (Integer) user.get("review_count");
			averageStars = (Integer) user.get("average_stars");
			name = (String) user.get("name");
			userId = (String) user.get("user_id");
			
			yUser = new YelpUser(userId, url, votes, reviewCount, name, averageStars);
			users.add(yUser);
		}
	}

	@Override
	public Set getMatches(String queryString) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String kMeansClusters_json(int k) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ToDoubleBiFunction getPredictorFunction(String user) {
		// TODO Auto-generated method stub
		return null;
	}

}
