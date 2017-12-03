package ca.ece.ubc.cpen221.mp5;

import java.util.ArrayList;
import java.util.HashMap;

public class YelpReview extends GenericReview{

	public YelpReview(String id, String businessId, HashMap<String, Integer> votes, String text, int stars,
			String userId, String date) {
		super(id, businessId, votes, text, stars, userId, date);
	}
	public String toJson() {
		ArrayList<String> keys = new ArrayList<String>();
		String output = "{\"type\": " + "\"" + "review" + "\"" + ", \"business_id\": " + "\"" + businessId + "\"" +
						", \"votes\": {";
		if(!votes.isEmpty()){
			keys.addAll(votes.keySet());
			output += "\"" + keys.get(0) + "\": " + votes.get(keys.get(0));
			for(int i = 1; i < keys.size(); i++) {
				output += ", \"" + keys.get(i) + "\": " + votes.get(keys.get(i));
			}
		}
		output += "}";
		output += ", \"review_id\": " + "\"" + id + "\"" + 
				  ", \"text\": " + "\"" + text + "\"" + ", \"stars\": " + stars + ", \"user_id\": "
				  + userId + ", \"date\": " + "\"" + date + "\"" + "}";
		output = output.replaceAll("\n", "\\\\n");
		output = output.replaceAll("\\[\\[", "\\[");
		output = output.replaceAll("\\]\\]", "\\]");
		
		return output;
	}
}
