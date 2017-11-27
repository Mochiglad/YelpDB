package ca.ece.ubc.cpen221.mp5;

import java.util.HashMap;

public class YelpUser extends GenericUser{

	public YelpUser(String id, String url, HashMap<String, Integer> votes, int reviewCount, String name,
			int averageStars) {
		super(id, url, votes, reviewCount, name, averageStars);
	}

}
