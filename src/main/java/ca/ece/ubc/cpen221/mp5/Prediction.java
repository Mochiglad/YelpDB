package ca.ece.ubc.cpen221.mp5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.ToDoubleBiFunction;
import java.util.stream.Collectors;

public class Prediction{
	
	public static double predict(String businessID,MP5Db p,String userID,MP5Db database){
		HashMap<String,YelpReview> reviews = (HashMap<String, YelpReview>) ((YelpDb)database).getReviews();
		HashMap<String,YelpRestaurant> restaurants = (HashMap<String, YelpRestaurant>) ((YelpDb)database).getRestaurants();
		HashMap<String,YelpRestaurant> pRestaurants = (HashMap<String, YelpRestaurant>) ((YelpDb)p).getRestaurants();
		
		List<YelpReview> relevantReviews = reviews.values().stream()
				.filter(review-> review.getUserId().equals(userID)).collect(Collectors.toList());
		List<YelpRestaurant> relevantRestaurant = restaurants.values().stream()
				.filter(rest -> relevantReviews.stream().map(review -> review.getBusinessId())
						.collect(Collectors.toList()).contains(rest.getId())).collect(Collectors.toList());
		List<Double[]> points = new ArrayList<Double[]>();
		double xMean = 0;
		double yMean = 0;
		
		for(YelpReview r : relevantReviews){
			YelpRestaurant rest = restaurants.get(r.getBusinessId());
			double x = rest.getPrice();
			double y = r.getStars();
			points.add(new Double[]{x,y});
		}
		
		for(Double[] point : points){
			xMean += point[0];
			yMean += point[1];
		}
		xMean = xMean/points.size();
		yMean = yMean/points.size();
		
		double Sxx = 0;
		double Syy = 0;
		double Sxy = 0;
		for(Double[] point : points){
			double x = point[0];
			double y = point[1];
			Sxx += Math.pow(x-xMean, 2);
			Syy += Math.pow(y-yMean, 2);
			Sxy += (x-xMean)*(y-yMean);
		}
		
		if(Sxx == 0 || Sxy == 0){
			throw new IllegalArgumentException("not enough distinct data points");
		}
		
		double b = Sxy/Sxx;
		double a = yMean - b*xMean;
		
		double ans = b*pRestaurants.get(businessID).getPrice()+a;
		if(ans < 1){
			ans = 1;
		} else if(ans > 5){
			ans = 5;
		}
		
		return ans;
	}
}
