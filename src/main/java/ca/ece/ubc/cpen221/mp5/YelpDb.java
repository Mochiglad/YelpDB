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
		// TODO Auto-generated method stub
		return null;
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
			
			for(int i = 0; i < pointSet.size(); i++){
				Double[] newCenter = newCenter(points,pointSet.get(i));
				
				if(newCenter[0].doubleValue() != centers.get(i)[0].doubleValue() && newCenter[1].doubleValue() != centers.get(i)[1].doubleValue()){
					centers.remove(i);
					centers.add(i,newCenter);
					changed = true;
				}
			}
			
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
		ToDoubleBiFunction<String,MP5Db> predictFromPrice = (x,y) -> Prediction.predict(x,y,user);
		return predictFromPrice;
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException{
	    YelpDb db = new YelpDb("data/users.json","data/reviews.json","data/restaurants.json");
	    ToDoubleBiFunction p = db.getPredictorFunction("Vp14grGEIvYzmrsOdix4UQ");
	    System.out.println(p.applyAsDouble("2ciUQ05DREauhBC3xiA4qw", db));
	}
}
