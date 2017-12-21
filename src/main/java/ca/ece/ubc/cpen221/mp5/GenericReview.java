package ca.ece.ubc.cpen221.mp5;

import java.util.HashMap;

public class GenericReview extends GenericObject{
	//RI: votes for any category cannot be negative
	//AF: (this representation) -> Generic Review
	protected final String businessId;
	protected final HashMap<String, Integer> votes;
	protected final String text;
	protected final int stars;
	protected final String userId;
	protected final String date;
	
	//Constructs a generic website review from a representation
	public GenericReview(String id, String businessId, HashMap<String, Integer> votes, String text, int stars2,
						String userId, String date) {
		super(id, "review");
		this.businessId = businessId;
		this.votes = votes;
		this.text = text;
		this.stars = stars2;
		this.userId = userId;
		this.date = date;
	}
	
	//Return immutable copies of traits
	public String getBusinessId() {
		return this.businessId;
	}
	public String getText() {
		return this.text;
	}
	public String getUserId() {
		return this.userId;
	}
	public String getDate() {
		return this.date;
	}
	public int getStars() {
		return this.stars;
	}
	public HashMap<String, Integer> getVotes() {
		return new HashMap<String, Integer>(this.votes);
	}
}
