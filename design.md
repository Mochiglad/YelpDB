We will create a generic object interface that is sub-classed by three objects:
	- Generic Restaurant
	- Generic Review
	- Generic User
	
These three objects are subclassed by the following respectively:
	- Yelp Restaurant
	- Yelp Review
	- Yelp User
YelpDB uses these three objects in its representation. HashMap to map ID to object

All Generic Objects contain ID and type, which are both Strings.

Operations:
  - Generic Object: getId - returns ID
				    getType - returns type
				   
  - Yelp Restaurant: return representation types.
  - Yelp Reviews: return representation types.
  - Yelp Users: return representation types.
  YelpDB implements MP5Db
  - YelpDB: - return representation types.
  			- ADDRESTAURANT
  			- ADDUSER
  			- ADDREVIEW
  			- k-means Cluster for restaurants + helper methods 
  			- JSON parser
  
  			
  