package PoAPackage;

import java.util.Scanner;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public class viewRecommendations {
	
	public void view(Session session) {
		
		@SuppressWarnings("resource")
		Scanner empid = new Scanner(System.in);
		
		reEnter: while (true) {
			System.out.print("Enter the Employee ID you want to see recommendations for: ");
			String enteredID = empid.next();
			
			System.out.println("\nEmployee Name \tEmail \t\t\t\t\tDesignation \t\t\tContact");
			ResultSet viewresults = session.execute(
					"Select EmployeeFirstName, EmployeeLastName, Email, EmployeeContact, Designation from EmpDetails where EmployeeID='"
							+ enteredID + "' allow filtering;");

			for (Row row : viewresults) {
				System.out.format("%s %s \t%s \t\t%s \t\t\t%s \n", row.getString("EmployeeFirstName"),
						row.getString("EmployeeLastName"), row.getString("Email"),
						row.getString("Designation"), row.getString("EmployeeContact"));
			}
			
			System.out.println("\n***Average Rating***");
			ResultSet viewresults1 = session.execute("Select count(*) as a from Recommendations where EmployeeID = '"+ enteredID +"' allow filtering;");
			Row row1 = viewresults1.one();
			double newCounter1 = (row1.getLong(0));
			ResultSet viewresults2 = session.execute("Select Rating from Recommendations where EmployeeID = '"+ enteredID +"' allow filtering;");
			double totalrating=0;
			for (Row row : viewresults2) {
				totalrating = totalrating+Double.parseDouble(row.getString("Rating"));
			}
			
			double averageRating = totalrating/newCounter1;
			String avg = String.format("%.2f", averageRating);
			System.out.println(avg);
			
			System.out.println("\n**********Comments**********");
			ResultSet viewresults3 = session.execute(
					"Select RecommendeeID, RecommendeeName, Rating, Recommendation from Recommendations where EmployeeID='"
							+ enteredID + "' allow filtering;");
			System.out.println("Recommendee ID\t\tRecommendee Name\t\tRating\t\tRecommendations");
			for (Row row : viewresults3) {
				System.out.format("%s\t\t\t%s \t\t%s\t\t%s\n", row.getString("RecommendeeID"),
						row.getString("RecommendeeName"), row.getString("Rating"), row.getString("Recommendation"));
			}
			
			System.out.println("\nWould you like to see recommendations for any other employee?\nPress Y for Yes, N for No");
			String selection = empid.next();
			if (selection.equals("Y") || selection.equals("y")) {
				continue reEnter;
			} else {
				System.out.println("You either chose not to continue or entered wrong input. Please select V to view recommendations again");
				break reEnter;
			} 
			

		}
	}
}

