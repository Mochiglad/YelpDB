package ca.ece.ubc.cpen221.mp5;

public class GenericObject {
	protected final String id;
	protected final String type;
	
	public GenericObject(String id, String type) {
		this.id = id;
		this.type = type;
	}
	
	public String getId() {
		return this.id;
	}
	
	public String getType() {
		return this.type;
	}
}
