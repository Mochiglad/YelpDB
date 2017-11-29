import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.simple.parser.ParseException;
import org.junit.Test;

import ca.ece.ubc.cpen221.mp5.YelpDb;

public class YelpDbTest {
	
	@Test
	public void test1() throws FileNotFoundException, IOException, ParseException{
		YelpDb db = new YelpDb("data/users.json","data/reviews.json","data/restaurants.json");
	    db.kMeansClusters_json(118);
	}
}
