package ca.ece.ubc.cpen221.mp5;

import java.util.HashMap;

public class GenericUser extends GenericObject{
	protected final String url;
	protected final HashMap<String, Integer> votes;
	protected final int reviewCount;
	protected final String name;
	protected final double averageStars;
	public GenericUser(String id, String url, HashMap<String, Integer> votes, int reviewCount, String name,
					   double averageStars2) {
		super(id, "user");
		this.url = url;
		this.votes = votes;
		this.reviewCount = reviewCount;
		this.name = name;
		this.averageStars = averageStars2;
	}
	public String getUrl() {
		return this.url;
	}
	public String getName() {
		return this.name;
	}
	public int getReviewCount() {
		return this.reviewCount;
	}
	public double getAverageStars() {
		return this.averageStars;
	}
	public HashMap<String, Integer> getVotes(){
		return new HashMap<String, Integer>(this.votes);
	}
}
