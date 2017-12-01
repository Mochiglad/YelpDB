import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.ToDoubleBiFunction;

import org.json.simple.parser.ParseException;
import org.junit.Test;

import ca.ece.ubc.cpen221.mp5.YelpDb;
import ca.ece.ubc.cpen221.mp5.YelpRestaurant;

public class YelpDbTest {
	
	@Test
	public void test1() throws FileNotFoundException, IOException, ParseException{
		YelpDb db = new YelpDb("data/users.json","data/reviews.json","data/restaurants.json");
	    db.kMeansClusters_json(50);
	}
	
	@Test
	public void test2() throws FileNotFoundException, IOException, ParseException{
		YelpDb db = new YelpDb("data/users.json","data/reviews.json","data/restaurants.json");
	    ToDoubleBiFunction p = db.getPredictorFunction("Vp14grGEIvYzmrsOdix4UQ");
	    double x = p.applyAsDouble("2ciUQ05DREauhBC3xiA4qw", db);
	    assert(3.128 == x);
	}
	
	@Test
	public void test3() throws FileNotFoundException, IOException, ParseException{
		YelpDb db = new YelpDb("data/users.json","data/reviews.json","data/restaurants.json");
		Set<YelpRestaurant> restSet = db.getMatches("category(Mexican) && in(Telegraph Ave)");
		assertEquals(7, restSet.size());
	}
	
	@Test
	public void test4() throws FileNotFoundException, IOException, ParseException{
		YelpDb db = new YelpDb("data/users.json","data/reviews.json","data/restaurants.json");
		Set<YelpRestaurant> restSet = db.getMatches("(category(Chinese) && in(Telegraph Ave)) || name(Pancho's)");
		assertEquals(8, restSet.size());
	}
	
	@Test
	public void test5() throws FileNotFoundException, IOException, ParseException{
		YelpDb db = new YelpDb("data/users.json","data/reviews.json","data/restaurants.json");
		Set<YelpRestaurant> restSet = db.getMatches("price <= 2 && rating <= 2");
		assertEquals(3, restSet.size());
	}
}
