package ca.ece.ubc.cpen221.mp5;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class YelpUser extends GenericUser{

	public YelpUser(String id, String url, HashMap<String, Integer> votes, int reviewCount, String name,
			double averageStars) {
		super(id, url, votes, reviewCount, name, averageStars);
	}
	public String toJson() {
		String output = "{\"url\": " + "\"" + url + "\"" + ", \"votes\": {";
		ArrayList<String> keys = new ArrayList<String>();
		if(!votes.isEmpty()){
			keys.addAll(votes.keySet());
			output += "\"" + keys.get(0) + "\": " + votes.get(keys.get(0));
			for(int i = 1; i < keys.size(); i++) {
				output += ", \"" + keys.get(i) + "\": " + votes.get(keys.get(i));
			}
		}
		output += "}";
		output += ", \"review_count\": " + reviewCount + ", \"type\": " + "\"" + "user" + "\"" + 
				  ", \"user_id\": " + "\"" + id + "\"" + ", \"name\": " + "\"" + name + "\"" + ", \"average_stars\": "
				   + averageStars + "}";
		output = output.replaceAll("\n", "\\\\n");
		output = output.replaceAll("\\[\\[", "\\[");
		output = output.replaceAll("\\]\\]", "\\]");
		
		return output;
	}
}
