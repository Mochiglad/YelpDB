package ca.ece.ubc.cpen221.mp5;

import java.util.HashMap;

public class YelpReview extends GenericReview{

	public YelpReview(String id, String businessId, HashMap<String, Integer> votes, String text, int stars,
			String userId, String date) {
		super(id, businessId, votes, text, stars, userId, date);
	}

}
