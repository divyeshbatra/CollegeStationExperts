package PoAPackage;

import java.util.Scanner;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public class recommendation {

	public void recommendationLogic(Session session, String loginID) {

		@SuppressWarnings("resource")
		Scanner option = new Scanner(System.in);
		System.out.println("Please enter Employee id you want to provide recommendation for: ");
		String choice1 = option.nextLine();
		ResultSet results = session.execute(
				"Select EmployeeID, EmployeeFirstName, EmployeeLastName, Email, SkillName, Designation from EmpDetails where EmployeeID='"
						+ choice1 + "' allow filtering;");

		for (Row row : results) {
			System.out.format("%s \t%s %s \t%s \t%s \t%s\n", row.getString("EmployeeID"),
					row.getString("EmployeeFirstName"), row.getString("EmployeeLastName"), row.getString("Email"),
					row.getString("SkillName"), row.getString("Designation"));
		}
		
		System.out.println("\nDo you want to continue recommendation for this Employee? Press Y for Yes, N for No: ");
		String choice2 = option.nextLine();
		
		if (choice2.equals("Y") || choice2.equals("y")) {
			System.out.println("Provide Rating(out of 5,5 being the highest) for Employee " + choice1 + " : ");
			String rating = option.next();
			System.out.println("Write your Recommendation for Employee " + choice1 + " : ");
			option.nextLine();
			String recommendation = option.nextLine();
			System.out.println("Your Recommendation has been saved for Employee " + choice1 + " in the database.");
			
			ResultSet maxLine = session.execute("Select count(*) as c from Recommendations;");
			Row row1 = maxLine.one();
			long newLine = (row1.getLong(0) + 1);
			String RecomFname = "";
			String RecomLname = "";
			ResultSet results1 = session.execute(
					"Select EmployeeFirstName, EmployeeLastName from EmpDetails where EmployeeID='" + loginID + "';");
			for (Row row : results1) {
				RecomFname = row.getString("EmployeeFirstName");
				RecomLname = row.getString("EmployeeLastName");
			}

			session.execute(
					"Insert into Recommendations (ID, EmployeeID,Rating,Recommendation,RecommendeeID,RecommendeeName) Values('"
							+ newLine + "','" + choice1 + "','" + rating + "','" + recommendation + "','"
							+ loginID + "','" + RecomFname + " " + RecomLname + "');");


		} else if (choice2.equals("N") || choice2.equals("n")){
			System.out.println(
					"You chose not to recommend.\nTherefore, No Recommendation is saved.");

		}
		else {
			System.out.println(
					"You have entered wrong selection.\nHence, no change in Rating and Recommendation for any Employee");

		}
	
	}

}
