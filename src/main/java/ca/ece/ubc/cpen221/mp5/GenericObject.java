package ca.ece.ubc.cpen221.mp5;

public class GenericObject {
	//Representation Invariant: Object is not null
	//AF: (this) -> Generic Object
	protected final String id; //id of object
	protected final String type; //type of object
	
	//Creates an object with specified id and type.
	public GenericObject(String id, String type) {
		this.id = id;
		this.type = type;
	}
	
	//Returns the Id of the object
	public String getId() {
		return this.id;
	}
	
	//Returns the type of the object
	public String getType() {
		return this.type;
	}
}
