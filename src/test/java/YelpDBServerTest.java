
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;
import java.util.function.ToDoubleBiFunction;

import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ca.ece.ubc.cpen221.mp5.YelpDb;
import ca.ece.ubc.cpen221.mp5.YelpRestaurant;
import ca.ece.ubc.cpen221.mp5.YelpDBServer;
import ca.ece.ubc.cpen221.mp5.InvalidPortException;

public class YelpDBServerTest {

	// https://stackoverflow.com/questions/1119385/junit-test-for-system-out-println
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

	@Before
	public void setUp() throws InvalidPortException, ParseException {
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
	}

	@After
	public void cleanUpStreams() {
		System.setOut(null);
		System.setErr(null);
	}

	@Test
	public void test1() throws FileNotFoundException, IOException, ParseException, InvalidPortException {
		int specialCharLength = 2;
		String err1 = "ERR: NO_SUCH_RESTAURANT";
		String err2 = "ERR: NO_SUCH_USER";
		String err3 = "ERR: ILLEGAL_REQUEST";
		try {
			YelpDBServer server = new YelpDBServer(4950);
			server.test(
					"ADDREVIEW {\"type\": \"review\", \"business_id\": \"0\", \"votes\": {\"cool\": 0, \"useful\": 0, \"funny\": 0}, \"text\": \"I was rather disappointed being new to Btown, and discovering there are no Mountain Mike's or Papa John's nearby, and that the first few pizza places I called don't deliver.\n\nI finally found La Val's number. Ordered an easy pep and ground sausage for my first dinner in my new apartment- very good, and very cheap and quick. I ordered and the delivery guy showed up in less than 30 mins (it was a Thursday night after 8pm). Medium w/ 2 tops only $13-sweet!\n\nGo La Val's! You got yourselves a new custy!!\", \"stars\": 2, \"user_id\": \"0\", \"date\": \"2008-05-02\"}");
			assertEquals(errContent.toString().length(), err1.length() + specialCharLength);
			server.test(
					"ADDRESTAURANT {\"open\": true, \"url\": \"http://www.yelp.com/biz/d-yar-berkeley\", \"longitude\": -122.2585448, \"neighborhoods\": [\"Telegraph Ave\", \"UC Campus Area\"], \"name\": \"D'Yar\", \"categories\": [\"Mediterranean\", \"Restaurants\"], \"state\": \"CA\", \"type\": \"business\", \"city\": \"Berkeley\", \"full_address\": \"2511 Durant Ave\\nTelegraph Ave\\nBerkeley, CA 94704\", \"photo_url\": \"http://s3-media2.ak.yelpcdn.com/bphoto/6gkkJgYtO6cDRDltYrRkwg/ms.jpg\", \"schools\": [\"University of California at Berkeley\"], \"latitude\": 37.8678828, \"price\": 1}");
			server.test(
					"ADDREVIEW {\"type\": \"review\", \"business_id\": \"0\", \"votes\": {\"cool\": 0, \"useful\": 0, \"funny\": 0}, \"text\": \"I was rather disappointed being new to Btown, and discovering there are no Mountain Mike's or Papa John's nearby, and that the first few pizza places I called don't deliver.\n\nI finally found La Val's number. Ordered an easy pep and ground sausage for my first dinner in my new apartment- very good, and very cheap and quick. I ordered and the delivery guy showed up in less than 30 mins (it was a Thursday night after 8pm). Medium w/ 2 tops only $13-sweet!\n\nGo La Val's! You got yourselves a new custy!!\", \"stars\": 2, \"user_id\": \"0\", \"date\": \"2008-05-02\"}");
			assertEquals(errContent.toString().length(), err1.length() + err2.length() + 2 * specialCharLength);
			server.test("ADDUSER {\"name\": \"Sathish G.\"}");
			server.test(
					"ADDREVIEW {\"type\": \"review\", \"business_id\": \"0\", \"votes\": {\"cool\": 0, \"useful\": 0, \"funny\": 0}, \"text\": \"I was rather disappointed being new to Btown, and discovering there are no Mountain Mike's or Papa John's nearby, and that the first few pizza places I called don't deliver.\n\nI finally found La Val's number. Ordered an easy pep and ground sausage for my first dinner in my new apartment- very good, and very cheap and quick. I ordered and the delivery guy showed up in less than 30 mins (it was a Thursday night after 8pm). Medium w/ 2 tops only $13-sweet!\n\nGo La Val's! You got yourselves a new custy!!\", \"stars\": 2, \"user_id\": \"0\", \"date\": \"2008-05-02\"}");
			server.test(
					"{\"type\": \"review\", \"business_id\": \"0\", \"votes\": {\"cool\": 0, \"useful\": 0, \"funny\": 0}, \"text\": \"I was rather disappointed being new to Btown, and discovering there are no Mountain Mike's or Papa John's nearby, and that the first few pizza places I called don't deliver.\n\nI finally found La Val's number. Ordered an easy pep and ground sausage for my first dinner in my new apartment- very good, and very cheap and quick. I ordered and the delivery guy showed up in less than 30 mins (it was a Thursday night after 8pm). Medium w/ 2 tops only $13-sweet!\n\nGo La Val's! You got yourselves a new custy!!\", \"stars\": 2, \"user_id\": \"0\", \"date\": \"2008-05-02\"}");
			assertEquals(errContent.toString().length(),
					err1.length() + err2.length() + err3.length() + 3 * specialCharLength);
			server.test("GETRESTAURANT 0");
			server.test("rating < 3");
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void test2() throws FileNotFoundException, IOException, ParseException, InvalidPortException {
		int specialCharLength = 2;
		String err1 = "ERR: NO_SUCH_RESTAURANT";
		String err2 = "ERR: INVALID_REVIEW_STRING";
		String err3 = "ERR: ILLEGAL_REQUEST";
		try {
			YelpDBServer server = new YelpDBServer(4950);
			assertEquals(errContent.toString().length(), 0);
			server.test(
					"ADDREVIEW {\"type\": \"review\",iness_id\": \"0\", \"votes\": {\"cool\": 0, \"useful\": 0, \"funny\": 0}, \"text\": \"I was rather disappointed being new to Btown, and discovering there are no Mountain Mike's or Papa John's nearby, and that the first few pizza places I called don't deliver.\n\nI finally found La Val's number. Ordered an easy pep and ground sausage for my first dinner in my new apartment- very good, and very cheap and quick. I ordered and the delivery guy showed up in less than 30 mins (it was a Thursday night after 8pm). Medium w/ 2 tops only $13-sweet!\n\nGo La Val's! You got yourselves a new custy!!\", \"stars\": 2, \"user_id\": \"0\", \"date\": \"2008-05-02\"}");
			assertEquals(errContent.toString().length(), err2.length() + specialCharLength);
			server.test("");
			assertEquals(errContent.toString().length(), err2.length() + err3.length() + 2 * specialCharLength);
			fail("Should have put an exception.");
		} catch (Exception e) {
		}
	}

	@Test
	public void test3() throws FileNotFoundException, IOException, ParseException, InvalidPortException {
		try {
			YelpDBServer server = new YelpDBServer(-1);
			fail("Should have put an exception.");
		} catch (Exception e) {
		}
	}

}
