package hospitalData;
import java.util.*;
import java.util.Map.Entry;


/* Hospital Database (01/13/17)
 * 
 * Prompt: Rank hospitals within a database based on response times to health emergencies within recovery rooms
 * as well as consumer ratings.  As well, since database would cover one particular area (for now), can give
 * average so that can give a general rating and response time expectation for hospitals within a district.
 * 
 * Improvement plans:
 * (1) Utilize JDBC to connect to a real database rather than makeshift HashMap
 * (2) Create different districts (multiple db tables) so that averages are more relevant
 * (3) Implement ranking system to find best hospital for a certain district
 * (4) Potentially app-ify?
 * 
 * *** SEE HOSPSQL.JAVA FOR IMPROVEMENTS ***
 * 
 * No current bugs to report.
 * 
 */
public class HospitalMain {
	
	public static double choose(int choice) {
		System.out.println("Evaluating rating . . .");
		double sum = 0.0;
		ArrayList<Object> hold;
		Set<Entry<Object, ArrayList<Object>>> set = db.entrySet();
		Iterator<Entry<Object, ArrayList<Object>>> it = set.iterator();
		while (it.hasNext()) {
			hold = db.get(it.next().getKey()); // iterator returns ArrayList of Objects
			if (choice == 1)
				sum += (double)hold.get(0);
			else if (choice == 2)
				sum += (double)hold.get(1);	
		}
		double avg = sum/(double)db.size();
		return avg;
	}
	
	public static double agg() {
		double keep = 0.0;
		double fullAvg = 0.0;
		double max = 0.0;
		ArrayList<Object> hold;
		Set<Entry<Object, ArrayList<Object>>> set = db.entrySet();
		Iterator<Entry<Object, ArrayList<Object>>> it = set.iterator();
		while (it.hasNext()) {
			hold = db.get(it.next().getKey());
			keep = (double)hold.get(1) + (double)hold.get(2);
			fullAvg = keep/2;
			if (fullAvg > max) 
				max = fullAvg;
		}
		
		return max;
	}

	static Map<Object, ArrayList<Object>> db = new HashMap<Object, ArrayList<Object>>(); // acts as makeshift hospital database lol

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		Scanner inp = new Scanner(System.in);
		int in = 0;
		String addin;
		
		// initialize database
		ArrayList<Object> StJudes = new ArrayList<Object>();
		ArrayList<Object> StMary = new ArrayList<Object>();
		ArrayList<Object> Mendota = new ArrayList<Object>();
		ArrayList<Object> University = new ArrayList<Object>();
		ArrayList<Object> Columbus = new ArrayList<Object>();
		StJudes.add(4.3);
		StJudes.add(8.7);
		StMary.add(3.7);
		StMary.add(10.6);
		Mendota.add(4.1);
		Mendota.add(9.1);
		University.add(4.8);
		University.add(15.3);
		Columbus.add(3.9);
		Columbus.add(13.0);
		db.put("StJudes", StJudes);
		db.put("StMary", StMary);
		db.put("Mendota", Mendota);
		db.put("University", University);
		db.put("Columbus", Columbus);
		
		
		while(true) {
			
			System.out.println("Choose a number: ");
			System.out.println("(1) View database "
					+ "(2) Add database entry "
					+ "(3) Find average for district "
					+ "(4) Evaluation "
					+ "(5) Exit");
			in = sc.nextInt();
			if(in > 4 || in < 1) {
				System.out.println("Error: Please choose from the menu.");
				continue;
			}
			switch (in) {
			
			case 1: 
				Set<Entry<Object, ArrayList<Object>>> set = db.entrySet();
				Iterator<Entry<Object, ArrayList<Object>>> i = set.iterator();
				while (i.hasNext()) {
					Map.Entry ent = (Map.Entry)i.next();
					System.out.println(ent.getKey() + ": " + ent.getValue());
				}
				break;
			case 2:
				System.out.println("Please enter as follows: name, rating, responseTime");
				addin = inp.nextLine();
				String[] addinn = addin.split(","); // split method returns array of values
													// (0) Name , (1) Rating , (2) rT
				//System.out.println(Arrays.toString(addinn));
				// TODO: check if rating is above 5.0
				double chg = Double.parseDouble(addinn[1]);
				if (chg > 5.0 || chg < 0.0) {
					System.out.println("Error: invalid rating");
					System.out.println("Entry addition unsuccessful");
					break;
				}
				ArrayList<Object> rates = new ArrayList<Object>(); // holds rating, rT
				rates.add(addinn[1]);
				rates.add(addinn[2]);
				db.put(addinn[0], rates);
				System.out.println("Entry added successfully!");
				break;
			case 3:
				System.out.println("Which average: rating (choose 1) or response time (choose 2)?");
				int input = inp.nextInt();
				if (input > 2 || input < 1) {
					System.out.println("Error: invalid choice");
					break;
				}
				double rate = choose(input);
				System.out.println("The average for ratings is: " + rate);
				break;
			case 4:
				double bestScore = agg();
			case 5:
				System.out.println("Exiting database . . .");
				System.exit(1);
			}
		}

	}
	
}
