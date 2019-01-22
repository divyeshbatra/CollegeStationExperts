package PoAPackage;

import java.util.Scanner;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public class searchCriteria {

	public void search(Session session, String loginID) {

		@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);
		int firstNumber = 0;
		System.out.print(
				"Enter the search criteria- \n1 for Skills, \n2 for Interests, \n3 for Designation \n4 for Employee ID ");

		while (firstNumber != 1 && firstNumber != 2 && firstNumber != 3 && firstNumber != 4) {

			if (input.hasNextInt()) {
				firstNumber = input.nextInt();
			} else {
				try {
					if (input.hasNext()) {
						firstNumber = Integer.parseInt(input.next());
					}
				} catch (NumberFormatException e) {
					firstNumber = 9;
				}
			}

			switch (firstNumber) {
			case 1:
				System.out.print("\nEnter the skill you are looking expertise in: ");
				input.nextLine();
				String searchCriteria = input.nextLine();
				System.out.print("\n");
				System.out.println("ID\tEmployee Name \tEmail  \tSkill Name");
				ResultSet results = session.execute(
						"Select EmployeeID,EmployeeFirstName, EmployeeLastName, Email, SkillName from SkillDetails where SkillName='"
								+ searchCriteria + "' and SkillLevel='Expert' allow filtering;");
				for (Row row : results) {
					System.out.format("%s\t%s %s \t%s \t%s\n", row.getString("EmployeeID"), row.getString("EmployeeFirstName"),
							row.getString("EmployeeLastName"), row.getString("Email"),
							row.getString("SkillName"));
				}
				System.out.println("\nEmployees who are EXPERTS in " + searchCriteria + " are displayed above");
				break;

			case 2:
				System.out.print("\nEnter the Interest for which you want to connect with people: ");
				input.nextLine();
				String searchCriteria2 = input.nextLine();
				System.out.print("\n");
				System.out.println("Employee Name \tEmail \t\t\tInterest");
				ResultSet results2 = session.execute(
						"Select EmployeeFirstName, EmployeeLastName, Email, Interest from InterestDetails where Interest='"
								+ searchCriteria2 + "' allow filtering;");

				for (Row row : results2) {
					System.out.format("%s %s \t%s \t%s\n", row.getString("EmployeeFirstName"),
							row.getString("EmployeeLastName"), row.getString("Email"),
							row.getString("Interest"));
				}
				System.out.println("\nEmployees who share the Interest in " + searchCriteria2 + " are displayed above");
				break;
			case 3:
				System.out.println("\nEnter the Designation for which you would like to see Employees: ");
				input.nextLine();
				String searchCriteria3 = input.nextLine();
				System.out.println("Employee Name \tContact \tDesignation");
				ResultSet results3 = session.execute(
						"Select EmployeeFirstName, EmployeeLastName, EmployeeContact, Designation from EmpDetails where Designation='"
								+ searchCriteria3 + "' allow filtering;");

				for (Row row : results3) {
					System.out.format("%s %s \t%s \t%s\n", row.getString("EmployeeFirstName"),
							row.getString("EmployeeLastName"), row.getString("EmployeeContact"),
							row.getString("Designation"));
				}
				System.out.println("\nEmployees whose Designation is " + searchCriteria3 + " are displayed above");
				break;	
			case 4:
				System.out.println("\nEnter the Employee ID you are searching for: ");
				input.nextLine();
				String searchCriteria4 = input.nextLine();
				System.out.println("Employee Name \tEmail \t\t\t\t\tDesignation \t\t\tContact");
				ResultSet results4 = session.execute(
						"Select EmployeeFirstName, EmployeeLastName, Email, EmployeeContact, Designation from EmpDetails where EmployeeID='"
								+ searchCriteria4 + "' allow filtering;");

				for (Row row : results4) {
					System.out.format("%s %s \t%s \t\t%s \t\t%s \n", row.getString("EmployeeFirstName"),
							row.getString("EmployeeLastName"), row.getString("Email"),
							row.getString("Designation"), row.getString("EmployeeContact"));
				}
				
				System.out.println("\nSkills for this employee are as follows: ");
				System.out.println("SKILL \t\tLEVEL");
				ResultSet results5 = session.execute(
						"Select SkillName, SkillLevel from SkillDetails where EmployeeID='"
								+ searchCriteria4 + "' allow filtering;");

				for (Row row : results5) {
					System.out.format("%s \t\t%s \n", row.getString("SkillName"), row.getString("SkillLevel"));
				}
				
				System.out.println("\nInterests of this employee are as follows: ");
				ResultSet results6 = session.execute(
						"Select Interest from InterestDetails where EmployeeID='"
								+ searchCriteria4 + "' allow filtering;");

				for (Row row : results6) {
					System.out.format("%s \n", row.getString("Interest"));
				}
				System.out.println("\n***Average Rating***");
				ResultSet results7 = session.execute("Select count(*) as c from Recommendations where EmployeeID = '"+ searchCriteria4 +"' allow filtering;");
				Row row1 = results7.one();
				double newCounter2 = (row1.getLong(0));
				ResultSet results8 = session.execute("Select Rating from Recommendations where EmployeeID = '"+ searchCriteria4 +"' allow filtering;");
				double totalrating=0;
				for (Row row : results8) {
					totalrating = totalrating+Double.parseDouble(row.getString("Rating"));
				}
				double averageRating = totalrating/newCounter2;
				String avg = String.format("%.2f", averageRating);
				System.out.println(avg);
				break;
			default:
				System.out.println("\nInvalid selection, Please make the correct selection");
				System.out.println("Enter the search criteria- 1 for Skills, 2 for Interests, 3 for Designation and 4 for Employee ID : ");
			}
		}
	}
}
