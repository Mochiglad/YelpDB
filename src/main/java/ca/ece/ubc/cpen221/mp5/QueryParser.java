package ca.ece.ubc.cpen221.mp5;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.function.ToDoubleBiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.simple.parser.ParseException;

public class QueryParser {
	
	String query;
	Map<String,YelpRestaurant> restaurants;
	
	//constructor
	public QueryParser(String query,YelpDb data){
		if(!query.contains("&&") && !query.contains("\\|\\|")){
			if(query.matches("^.*?(\\)|\\d) .*?$")){
				throw new IllegalArgumentException("query not valid");
			}
			query = query + " &&";
		}
		
		query = clean(query);
		checkNums(query);
		
		String valid = query.replaceAll("((^|[ \\(])in\\(.*?\\))|((^|[ \\(])name\\(.*?\\))|((^|[ \\(])"
				+ "category\\(.*?\\))|((^|[ \\(])rating [=><]=? \\d)|((^|[ \\(])(price [=><]=? \\d))"," ");
		
		if(valid.matches("(^| )[^ ]+\\(.*?")||query.matches(".*?[&\\|]{2,} *[&\\|]+.*?")){
			throw new IllegalArgumentException("query not valid");
		}
		
		query = query.replaceAll("(.+)","($1)");
		query = query.replaceAll("(price [<>=]=? \\d)", "($1)");
		query = query.replaceAll("(rating [<>=]=? \\d)", "($1)");
		query = query.replaceAll("(in\\(.*?\\))", "($1)");
		query = query.replaceAll("(category\\(.+?\\))", "($1)");
		query = query.replaceAll("(name\\(.+?\\))", "($1)");
		this.query = query;
		restaurants = data.getRestaurants();
	}
	
	public HashSet<YelpRestaurant> findRestaurant(){
		ArrayList<String> atoms = parse();
		ArrayList<String[]> ands = new ArrayList<String[]>();
		ArrayList<YelpRestaurant> restaurantList = new ArrayList<YelpRestaurant>(restaurants.values());
		
		for(String atom : atoms){
			String[] ors = atom.split("&&");
			
			for(String s : ors){
				String[] tokens = s.split("\\|\\|");
				ands.add(tokens);
			}
		}
		
		for(String[] S : ands){
			if(S.length == 1){
				if(S[0].trim().matches("\\d\\d+")){
					//already done
				} else {
					restaurantList = filter(S[0],restaurantList);
				}
			} else {
				restaurantList = filter(S,restaurantList);
			}
		}
		
		for(int i = 0; i < restaurantList.size();i++){
			System.out.println(restaurantList.get(i).toJson());
		}
		return new HashSet<YelpRestaurant>(restaurantList);
	}
	
	/**
	 * helper that parses query into atoms
	 * @return an arraylist of atoms
	 */
	public ArrayList<String> parse(){
		Stack<Integer> openIndex = new Stack<Integer>();
		ArrayList<String> atoms = new ArrayList<String>();
		String token;
		int close = 0;
		int open = 0;
		int a = 10;
		
		for(int i = 0; i < query.length(); i++){
			char here = query.charAt(i);
			
			if(here == '('){
				openIndex.add(i);
			} else if(here == ')'){
				if(openIndex.isEmpty()){
					throw new IllegalArgumentException("query is invalid");
				} else {
					close = i;
					open = openIndex.pop();
					token = query.substring(open+1, close);
					if((token.startsWith("in")||token.startsWith("category")||token.startsWith("rating")
							||token.startsWith("price")||token.startsWith("name")
							||token.startsWith("(")||token.matches("^\\d+.*"))&&(token.contains("||")||token.contains("&&"))){
						System.out.println(a + ": " + token);
						atoms.add(a-10,token);
						query = query.replace("("+token+")", Integer.toString(a));
						a++;
						i = i - token.length();
					}
				}
			}
		}
		System.out.println(query);
		return atoms;
	}
	
	/**
	 * AND Filter: filters out restaurants from the list by a command in s for && commands
	 * 
	 * @param s the command to be performed
	 * @param restList the list of restaurants that is being filtered
	 * @return a filtered list of restaurants
	 */
	private ArrayList<YelpRestaurant> filter (String s,ArrayList<YelpRestaurant> restList) {
		s = s.trim();
		if(s.contains("in(")){
			String nbh = s.substring(4,s.length()-2);
			restList = (ArrayList<YelpRestaurant>) restList.stream().filter(r -> r.getNeighborhoods().contains(nbh)).collect(Collectors.toList());
		} else if(s.contains("category(")){
			String cat = s.substring(10,s.length()-2);
			restList = (ArrayList<YelpRestaurant>) restList.stream().filter(r -> r.getCategories().contains(cat)).collect(Collectors.toList());
		} else if(s.contains("name(")){
			String name = s.substring(6,s.length()-2);
			restList = (ArrayList<YelpRestaurant>) restList.stream().filter(r -> r.getName().equals(name)).collect(Collectors.toList());
		} else if(s.contains("rating ")){
			int rating = Integer.parseInt(s.substring(s.length()-2,s.length()-1));
			if(s.contains(" < ")){
				restList = (ArrayList<YelpRestaurant>) restList.stream().filter(r -> r.getStars() < rating).collect(Collectors.toList());
			} else if(s.contains(" > ")){
				restList = (ArrayList<YelpRestaurant>) restList.stream().filter(r -> r.getStars() > rating).collect(Collectors.toList());
			} else if(s.contains(">=")){
				restList = (ArrayList<YelpRestaurant>) restList.stream().filter(r -> r.getStars() >= rating).collect(Collectors.toList());
			} else if(s.contains("<=")){
				restList = (ArrayList<YelpRestaurant>) restList.stream().filter(r -> r.getStars() <= rating).collect(Collectors.toList());
			} else if(s.contains("=")){
				restList = (ArrayList<YelpRestaurant>) restList.stream().filter(r -> r.getStars() == rating).collect(Collectors.toList());
			}
		} else if(s.contains("price ")){
			int price = Integer.parseInt(s.substring(s.length()-2,s.length()-1));
			if(s.contains(" < ")){
				restList = (ArrayList<YelpRestaurant>) restList.stream().filter(r -> r.getPrice() < price).collect(Collectors.toList());
			} else if(s.contains(" > ")){
				restList = (ArrayList<YelpRestaurant>) restList.stream().filter(r -> r.getPrice() > price).collect(Collectors.toList());
			} else if(s.contains(">=")){
				restList = (ArrayList<YelpRestaurant>) restList.stream().filter(r -> r.getPrice() >= price).collect(Collectors.toList());
			} else if(s.contains("<=")){
				restList = (ArrayList<YelpRestaurant>) restList.stream().filter(r -> r.getPrice() <= price).collect(Collectors.toList());
			} else if(s.contains("=")){
				restList = (ArrayList<YelpRestaurant>) restList.stream().filter(r -> r.getPrice() == price).collect(Collectors.toList());
			}
		}
		return restList;
	}
	
	/**
	 * OR Filter: filters out restaurants from the list by a command in s for || commands 
	 * @param S the commands to be performed
	 * @param restList the list of restaurants that is being filtered
	 * @return a filtered list of restaurants
	 */
	private ArrayList<YelpRestaurant> filter(String[] S, ArrayList<YelpRestaurant> restList){
		List<List<YelpRestaurant>> holder = new ArrayList<List<YelpRestaurant>>();
		for(String s : S){
			ArrayList<YelpRestaurant> temp = new ArrayList<YelpRestaurant>(restaurants.values());
			
			if(s.trim().matches("\\d\\d+")){
				temp = restList;
			} else {
				temp = filter(s,temp);
			}
			holder.add(temp);
		}
		Set<YelpRestaurant> combine = new HashSet<YelpRestaurant>();
		for(List<YelpRestaurant> list : holder){
			combine.addAll(list);
		}
		return new ArrayList<YelpRestaurant>(combine);
	}
	
	/**
	 * cleans the query (removes double spaces, redundant parentheses)
	 * @param query the query that is being "cleaned"
	 * @return the "clean" query
	 */
	private synchronized String clean(String query){
		String americanNew = "American (New)";
		String americanTrad = "American (Traditional)";
		query = query.trim();
		while(query.matches("\\((.*)\\)")&& !query.contains("&&") && !query.contains("\\|\\|")){
			query = query.replaceAll("\\((.*)\\)", "$1");
		}
		query = query.replaceAll(" {2,}", " ");
		query = query.replaceAll("\\( +", "\\(");
		query = query.replaceAll("\\) +","\\)");
		query = query.replace(americanNew, "ThisisAPLaceHOlderANdNOoneINTHeirRIGHtmINDWOulDtypEthiN");
		query = query.replace(americanTrad, "ThisisAPLaceHOlderANdNOoneINTHeirRIGHtmINDWOulDtypEthiT");
		query = query.replaceAll("\\(\\(+(.*?\\(.*?\\))\\)\\)+", "($1)");
		query = query.replace("ThisisAPLaceHOlderANdNOoneINTHeirRIGHtmINDWOulDtypEthiN", americanNew);
		query = query.replace("ThisisAPLaceHOlderANdNOoneINTHeirRIGHtmINDWOulDtypEthiT", americanTrad);
		return query;
	}
	
	/**
	 * Ensures that the query only contains nums form 1 - 5
	 * @throws IllegalArgumentException if query contains num outside of 1 - 5
	 * @param query the query to be checked
	 */
	private void checkNums(String query){
		String numsOnly = query.replaceAll("[^\\d ]", "");
		String[] inputNums = numsOnly.split(" +");
		
		for(String n : inputNums){
			if(n.matches("[\\d]+")){
				if(Integer.parseInt(n) > 5 || Integer.parseInt(n) < 1){
					throw new IllegalArgumentException ("query not valid");
				}
			}
		}
	}	
}
