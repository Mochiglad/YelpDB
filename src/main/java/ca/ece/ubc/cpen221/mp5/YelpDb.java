package ca.ece.ubc.cpen221.mp5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.ToDoubleBiFunction;
import java.util.stream.Collectors;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class YelpDb implements MP5Db<YelpRestaurant> {
	private HashMap<String,YelpUser> users = new HashMap<String,YelpUser>();
	private HashMap<String,YelpReview> reviews = new HashMap<String,YelpReview>();;
	private HashMap<String,YelpRestaurant> restaurants = new HashMap<String,YelpRestaurant>();;

	public YelpDb(String userJsonPath, String reviewJsonPath, String restaurantJsonPath)
			throws FileNotFoundException, IOException, ParseException {
		readRestaurantJson(restaurantJsonPath);
		readReviewJson(reviewJsonPath);
		readUserJson(userJsonPath);
	}
	
	public static YelpRestaurant restaurantParser(JSONObject restaurant, boolean newRestaurant, String newId) {
		YelpRestaurant yRestaurant;
		boolean status;
		String url;
		double longitude;
		double latitude;
		HashSet<String> neighborhoods = new HashSet<String>();
		HashSet<String> categories = new HashSet<String>();
		HashSet<String> schools = new HashSet<String>();
		String businessId = newId;
		String name;
		String state;
		String BType;
		double stars = 0;
		String city;
		String fullAddress;
		int reviewCount = 0;
		String photoUrl;
		int price;
		
		JSONArray jsonArray;
		
		status = (boolean) restaurant.get("open");
		url = (String) restaurant.get("url");
		name = (String) restaurant.get("name");
		state = (String) restaurant.get("state");
		city = (String) restaurant.get("city");
		BType = (String) restaurant.get("type");
		fullAddress = (String) restaurant.get("full_address");
		photoUrl = (String) restaurant.get("photo_url");
		longitude = (Double) restaurant.get("longitude");
		latitude = (Double) restaurant.get("latitude");
		price = ((Long) restaurant.get("price")).intValue();
		HashSet<String> neighborhoodsCopy;
		HashSet<String> categoriesCopy;
		HashSet<String> schoolsCopy;
		
		if(!newRestaurant) {
			stars = (double) restaurant.get("stars");
			businessId = (String) restaurant.get("business_id");
			reviewCount = ((Long) restaurant.get("review_count")).intValue();
		} 
		neighborhoods.clear();
		categories.clear();
		schools.clear();
		
		jsonArray = (JSONArray) restaurant.get("neighborhoods");
		for(int i = 0; i < jsonArray.size(); i++){
			neighborhoods.add(jsonArray.get(i).toString());
		}
		
		jsonArray = (JSONArray) restaurant.get("categories");
		for(int i = 0; i < jsonArray.size(); i++){
			categories.add(jsonArray.get(i).toString());
		}
		
		jsonArray = (JSONArray) restaurant.get("schools");
		for(int i = 0; i < jsonArray.size(); i++){
			schools.add(jsonArray.get(i).toString());
		}
		
		neighborhoodsCopy = new HashSet<String>(neighborhoods);
		categoriesCopy = new HashSet<String>(categories);
		schoolsCopy = new HashSet<String>(schools);
		return new YelpRestaurant(businessId, status, url, longitude, latitude, neighborhoodsCopy, categoriesCopy,
				schoolsCopy, businessId, name, state, BType, stars, city, fullAddress, reviewCount, photoUrl, price);
	}
	private void readRestaurantJson(String filePath) throws FileNotFoundException, IOException, ParseException {
		/*
		 * id, status, url, longitude, latitude, neighborhoods, categories, schools,
		 * businessId, name, state, BType, stars, city, fullAddress, reviewCount,
		 * photoUrl, price
		 */
		YelpRestaurant restaurantAdd;
		BufferedReader buffRead = new BufferedReader(new FileReader(filePath));
		String line;
		
		JSONParser parser = new JSONParser();
		
		while(!((line = buffRead.readLine()) == null)){
		//for (Object o : jsonArray) {
			JSONObject restaurant = (JSONObject) parser.parse(line);
			restaurantAdd = YelpDb.restaurantParser(restaurant, false, (String)restaurant.get("business_id"));
			restaurants.put(restaurantAdd.getId(),restaurantAdd);
		}
	}

	public static YelpReview reviewParser(JSONObject review, boolean newReview, String newId) {
		YelpReview yReview;
		String businessId;
		HashMap<String, Integer> votes = new HashMap<String, Integer>();
		String reviewId = newId;
		String text;
		int stars;
		String userId;
		String date;
		
		if(newReview) {
			votes.put("cool",0);
			votes.put("useful", 0);
			votes.put("funny", 0);
		}
		if(!newReview) {
			votes = (HashMap<String, Integer>) review.get("votes");
			reviewId = (String) review.get("review_id");
		}
		businessId = (String) review.get("business_id");
		stars = ((Long) review.get("stars")).intValue();
		text = (String) review.get("text");
		
		userId = (String) review.get("user_id");
		date = (String) review.get("date");

		return new YelpReview(reviewId, businessId, new HashMap<String, Integer>(votes), text, stars, userId, date);
	}
	private void readReviewJson(String filePath) throws FileNotFoundException, IOException, ParseException {
		YelpReview reviewAdd;
		JSONParser parser = new JSONParser();
		BufferedReader buffRead = new BufferedReader(new FileReader(filePath));
		String line;
		while(!((line = buffRead.readLine()) == null)){
		//for (Object o : jsonArray) {
			JSONObject review = (JSONObject) parser.parse(line);
			reviewAdd = YelpDb.reviewParser(review, false, (String)review.get("review_id"));
			reviews.put(reviewAdd.getId(),reviewAdd);
		}
	}

	public static YelpUser userParser(JSONObject user, boolean newUser, String newId) {
		YelpUser yUser;
		String url = "http://www.yelp.com/user_details?userid=" + newId;
		HashMap<String, Integer> votes = new HashMap<String, Integer>();
		int reviewCount = 0;
		String name;
		double averageStars = 0;
		String userId = newId;
		
		name = (String) user.get("name");
		if(!newUser) {
			url = (String) user.get("url");
			votes = (HashMap<String, Integer>) user.get("votes");
			reviewCount = ((Long) user.get("review_count")).intValue();
			averageStars = (double) user.get("average_stars");
			userId = (String) user.get("user_id");
		}
		
		return new YelpUser(userId, url, new HashMap<String, Integer>(votes), reviewCount, name, averageStars);
	}
	private void readUserJson(String filePath) throws FileNotFoundException, IOException, ParseException {
		YelpUser yUser;
		
		JSONParser parser = new JSONParser();
		JSONArray jsonArray;
		BufferedReader buffRead = new BufferedReader(new FileReader(filePath));
		String line;
		
		while(!((line = buffRead.readLine()) == null)){
		//for (Object o : jsonArray) {
			JSONObject user = (JSONObject) parser.parse(line);
			yUser = YelpDb.userParser(user, false, (String)user.get("user_id"));
			users.put(yUser.getId(),yUser);
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
	
	public Map<String,YelpRestaurant> getRestaurants(){
		return restaurants;
	}
	
	public Map<String,YelpReview> getReviews(){
		return reviews;
	}
	
	public Map<String,YelpUser> getUsers(){
		return users;
	}
	

	@Override
	public Set getMatches(String queryString) {
		QueryParser qp = new QueryParser(queryString,this);
		return qp.findRestaurant();
	}

	@Override
	public String kMeansClusters_json(int k) throws IllegalArgumentException {
		int check = 0;
		double X_LOWER_BOUND = 0;
		double X_UPPER_BOUND = 0;
		double Y_LOWER_BOUND = 0;
		double Y_UPPER_BOUND = 0;
		ArrayList<YelpRestaurant> restaurantList = new ArrayList<YelpRestaurant>(restaurants.values());
		HashMap<Integer,Double[]> points = new HashMap<Integer,Double[]>();
		ArrayList<Double[]> centers = new ArrayList<Double[]>();
		ArrayList<Set<Integer>> pointSet = new ArrayList<Set<Integer>>();
		
		//initialize pointSet
		for(int i = 0; i < k; i++){
			pointSet.add(new HashSet<Integer>());
		}
		
		for(int i = 0; i < restaurantList.size(); i++){
			YelpRestaurant temp = restaurantList.get(i);
			points.put(i,new Double[]{temp.getLongitude(),temp.getLatitude()});
			if(i == 0){
				X_LOWER_BOUND = temp.getLongitude();
				X_UPPER_BOUND = temp.getLongitude();
				Y_LOWER_BOUND = temp.getLatitude();
				Y_UPPER_BOUND = temp.getLatitude();
			}
			if(X_LOWER_BOUND > temp.getLongitude()){
				X_LOWER_BOUND = temp.getLongitude();
			} else if(X_UPPER_BOUND < temp.getLongitude()){
				X_UPPER_BOUND = temp.getLongitude();
			}
			if(Y_LOWER_BOUND > temp.getLatitude()){
				Y_LOWER_BOUND = temp.getLatitude();
			} else if(Y_UPPER_BOUND < temp.getLatitude()){
				Y_UPPER_BOUND = temp.getLatitude();
			}
		}
		double X_RANGE = X_UPPER_BOUND - X_LOWER_BOUND;
		double Y_RANGE = Y_UPPER_BOUND - Y_LOWER_BOUND;
		
		Random r = new Random();
		int a = 0;
		for(int i = 0; i < k; i++){
			Double[] Center = new Double[]{restaurantList.get(a).getLongitude(),restaurantList.get(a).getLatitude()};
			if(!centers.contains(Center)){
				centers.add(Center);
			} else {
				i--;
			}
			a++;
		}
		
		boolean changed = true;
		int numPoints = restaurantList.size();
		
		while(changed){
			for(int i = 0; i < k; i++){
				pointSet.get(i).clear();
			}
			changed = false;
			for(int i = 0; i < numPoints; i++){
				Integer closest = 0;
				double shortestDist = distance(centers.get(0),points.get(i));
				for(int j = 0; j < centers.size(); j++){
					double distance = distance(centers.get(j),points.get(i));
					if(distance < shortestDist){
						shortestDist = distance;
						closest = j;
					}
				}
				pointSet.get(closest).add(i);
			}
			
			synchronized(this){
				for(int i = 0; i < pointSet.size(); i++){
					Double[] newCenter = newCenter(points,pointSet.get(i));
					
					if(newCenter[0].doubleValue() != centers.get(i)[0].doubleValue() && newCenter[1].doubleValue() != centers.get(i)[1].doubleValue()){
						centers.remove(i);
						centers.add(i,newCenter);
						changed = true;
					}
				}
			}
			
			synchronized(this){
				boolean hasEmpty = false;
				for(int i = 0; i < pointSet.size(); i++){
					if(pointSet.get(i).isEmpty()){
						hasEmpty = true;
						centers.add(i,new Double[]{r.nextDouble()*X_RANGE+X_LOWER_BOUND,r.nextDouble()*Y_RANGE+Y_LOWER_BOUND});
						centers.remove(i+1);
					}
				}
				if(hasEmpty)
					check++;
				if(check == 300000){
					throw new IllegalArgumentException("not enough unique data points to cluster into "+k+" clusters");
				}
			}
		} 
		
		ArrayList<Set<YelpRestaurant>> restaurantSet = new ArrayList<Set<YelpRestaurant>>();
		for(int i = 0; i < pointSet.size(); i++){
			Iterator iterator = pointSet.get(i).iterator();
			Set<YelpRestaurant> thisCluster = new HashSet<YelpRestaurant>();
			while(iterator.hasNext()){
				thisCluster.add(restaurantList.get((int) iterator.next()));
			}
			restaurantSet.add(thisCluster);
		}
		System.out.println(listSetToJson(restaurantSet));
		return listSetToJson(restaurantSet);
	}
	
	private double distance(Double[] center, Double[] point){
		return Math.pow(center[0].doubleValue() - point[0].doubleValue(),2) + Math.pow(center[1].doubleValue() - point[1].doubleValue(), 2);
	}
	
	public void updateDB(String reviewId, String userId) {
		YelpRestaurant updateRestaurant;
		YelpUser updateUser;
		boolean status;
		String url;
		double longitude;
		double latitude;
		HashSet<String> neighborhoods;
		HashSet<String> categories;
		HashSet<String> schools;
		String name;
		String state;
		String BType;
		double stars;
		String city;
		String fullAddress;
		int reviewCount;
		String photoUrl;
		int price;
		String businessId = reviews.get(reviewId).getBusinessId();
		
		String urlUser;
		HashMap<String, Integer> votes;
		int reviewCountUser;
		String nameUser;
		double averageStars;
		
		
		updateRestaurant = restaurants.get(businessId);
		status = updateRestaurant.getStatus();
		url = updateRestaurant.getUrl();
		longitude = updateRestaurant.getLongitude();
		latitude = updateRestaurant.getLatitude();
		neighborhoods = new HashSet<String>(updateRestaurant.getNeighborhoods());
		categories = new HashSet<String>(updateRestaurant.getCategories());
		schools = new HashSet<String>(updateRestaurant.getSchools());
		name = updateRestaurant.getName();
		state = updateRestaurant.getState();
		BType = updateRestaurant.getBType();
		stars = updateRestaurant.getStars();
		city = updateRestaurant.getCity();
		fullAddress = updateRestaurant.getFullAddress();
		reviewCount = updateRestaurant.getReviewCount();
		photoUrl = updateRestaurant.getPhotoUrl();
		price = updateRestaurant.getPrice();
		
		System.out.println("asdfasdf" + reviews.get(reviewId).getStars());
		stars = updateStars(reviewCount, stars, (double)reviews.get(reviewId).getStars());
		stars = Math.floor(stars * 100) / 100;
		reviewCount++;

		restaurants.put(businessId,new YelpRestaurant(businessId, status, url, longitude, latitude, neighborhoods, categories, schools, businessId, name,
				state, BType, stars, city, fullAddress, reviewCount, photoUrl, price));
		
		updateUser = users.get(reviews.get(reviewId).getUserId());
		urlUser = updateUser.getUrl();
		votes = new HashMap<String, Integer>(updateUser.getVotes());
		reviewCountUser = updateUser.getReviewCount();
		averageStars = updateUser.getAverageStars();
		
		averageStars = updateStars(reviewCountUser, averageStars, reviews.get(reviewId).getStars());
		reviewCountUser++;
		users.put(updateUser.getId(), new YelpUser( updateUser.getId(),  urlUser, votes,  reviewCountUser,  name,
				 averageStars));
	}
	
	private static double updateStars(int reviewCount, double stars, double reviewStars) {
		double newStars;
		newStars = reviewCount * stars;
		newStars += reviewStars;
		newStars /= (reviewCount + 1);
		System.out.println(newStars + " " + reviewCount + " " + reviewStars);
		return newStars;
	}
	private Double[] newCenter(HashMap<Integer,Double[]> Allpoints,Set<Integer> pointSet){
		double xAvg = 0;
		double yAvg = 0;
		Double[] pointHolder;

		Iterator<Integer> pointIterator = pointSet.iterator();
		while(pointIterator.hasNext()){
			Integer restaurant = pointIterator.next();
			pointHolder = Allpoints.get(restaurant);
			xAvg += pointHolder[0];
			yAvg += pointHolder[1];
		}
		
		xAvg = xAvg/pointSet.size();
		yAvg = yAvg/pointSet.size();
		return new Double[]{xAvg,yAvg};
	}
	
	private String listSetToJson(ArrayList<Set<YelpRestaurant>> restaurantSet){
		String jsonFormat = "[";
		for(int i = 0; i < restaurantSet.size(); i++){
			Iterator iterator = restaurantSet.get(i).iterator();
			
			while(iterator.hasNext()){
				YelpRestaurant rest = (YelpRestaurant) iterator.next();
				jsonFormat += "{\"x\": " + rest.getLongitude() + ", \"y\": " + rest.getLatitude() +
						", \"name\": \"" + rest.getName() + "\" , \"cluster\": " + i + ", \"weight\": 1.0}, ";
			}
		}
		jsonFormat = jsonFormat.substring(0, jsonFormat.length()-2);
		jsonFormat += "]";
		return jsonFormat;
	}

	@Override
	public ToDoubleBiFunction getPredictorFunction(String user) {
		ToDoubleBiFunction<String,MP5Db> predictFromPrice = (x,y) -> Prediction.predict(x,y,user,this);
		return predictFromPrice;
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException{
	    YelpDb db = new YelpDb("data/users.json","data/reviews.json","data/restaurants.json");
	    ToDoubleBiFunction p = db.getPredictorFunction("Vp14grGEIvYzmrsOdix4UQ");
	    System.out.println(p.applyAsDouble("2ciUQ05DREauhBC3xiA4qw", db));
	}
}
