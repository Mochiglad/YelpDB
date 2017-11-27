package ca.ece.ubc.cpen221.mp5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.ToDoubleBiFunction;
import java.io.BufferedReader;
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
	private HashMap<String,YelpUser> users = new HashMap<String,YelpUser>();
	private HashMap<String,YelpReview> reviews = new HashMap<String,YelpReview>();;
	private HashMap<String,YelpRestaurant> restaurants = new HashMap<String,YelpRestaurant>();;

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
		HashSet<String> neighborhoods = new HashSet<String>();
		HashSet<String> categories = new HashSet<String>();
		HashSet<String> schools = new HashSet<String>();
		String businessId;
		String name;
		String state;
		String BType;
		double stars;
		String city;
		String fullAddress;
		int reviewCount;
		String photoUrl;
		int price;

		JSONParser parser = new JSONParser();
		JSONArray jsonArray;
		BufferedReader buffRead = new BufferedReader(new FileReader(filePath));
		String line;
		
		while(!((line = buffRead.readLine()) == null)){
		//for (Object o : jsonArray) {
			JSONObject restaurant = (JSONObject) parser.parse(line);
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
			stars = (double) restaurant.get("stars");
			reviewCount = ((Long) restaurant.get("review_count")).intValue();
			price = ((Long) restaurant.get("price")).intValue();
			
			jsonArray = (JSONArray) restaurant.get("neighborhoods");
			for(int i = 0; i < jsonArray.size(); i++){
				neighborhoods.add(jsonArray.toString());
			}
			
			jsonArray = (JSONArray) restaurant.get("categories");
			for(int i = 0; i < jsonArray.size(); i++){
				categories.add(jsonArray.toString());
			}
			
			jsonArray = (JSONArray) restaurant.get("schools");
			for(int i = 0; i < jsonArray.size(); i++){
				schools.add(jsonArray.toString());
			}

			yRestaurant = new YelpRestaurant(businessId, status, url, longitude, latitude, neighborhoods, categories,
					schools, businessId, name, state, BType, stars, city, fullAddress, reviewCount, photoUrl, price);
			restaurants.put(businessId,yRestaurant);
		}

	}

	private void readReviewJson(String filePath) throws FileNotFoundException, IOException, ParseException {
		YelpReview yReview;
		String businessId;
		HashMap<String, Integer> votes;
		String reviewId;
		String text;
		int stars;
		String userId;
		String date;

		JSONParser parser = new JSONParser();
		JSONArray jsonArray;
		BufferedReader buffRead = new BufferedReader(new FileReader(filePath));
		String line;
		
		while(!((line = buffRead.readLine()) == null)){
		//for (Object o : jsonArray) {
			JSONObject review = (JSONObject) parser.parse(line);
			businessId = (String) review.get("business_id");
			votes = (HashMap<String, Integer>) review.get("votes");
			reviewId = (String) review.get("review_id");
			text = (String) review.get("text");
			stars = ((Long) review.get("stars")).intValue();
			userId = (String) review.get("user_id");
			date = (String) review.get("date");

			yReview = new YelpReview(reviewId, businessId, votes, text, stars, userId, date);
			reviews.put(reviewId,yReview);
		}
	}

	private void readUserJson(String filePath) throws FileNotFoundException, IOException, ParseException {
		YelpUser yUser;
		String url;
		HashMap<String, Integer> votes;
		int reviewCount;
		String name;
		double averageStars;
		String userId;
		
		JSONParser parser = new JSONParser();
		JSONArray jsonArray;
		BufferedReader buffRead = new BufferedReader(new FileReader(filePath));
		String line;
		
		while(!((line = buffRead.readLine()) == null)){
		//for (Object o : jsonArray) {
			JSONObject user = (JSONObject) parser.parse(line);
			url = (String) user.get("url");
			votes = (HashMap<String, Integer>) user.get("votes");
			reviewCount = ((Long) user.get("review_count")).intValue();
			averageStars = (double) user.get("average_stars");
			name = (String) user.get("name");
			userId = (String) user.get("user_id");
			
			yUser = new YelpUser(userId, url, votes, reviewCount, name, averageStars);
			users.put(userId,yUser);
		}
	}
	
	public void addRestaurant (YelpRestaurant restaurant){
		restaurants.put(restaurant.getId(), restaurant);
	}
	
	public void addUser (YelpUser user){
		users.put(user.getId(), user);
	}
	
	public void addReview (YelpReview review){
		reviews.put(review.getId(), review);
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
