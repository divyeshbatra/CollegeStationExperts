package PoAPackage;

import java.util.Scanner;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public class editProfile {

	public void edit(Session session, String loginID) {
		@SuppressWarnings("resource")
		Scanner editProfile = new Scanner(System.in);
		int secondNumber = 0;
		String Email = "";
		String EmployeeFirstName = "";
		String EmployeeLastName = "";
		ResultSet results1 = session
				.execute("Select EmployeeFirstName, EmployeeLastName, Email from EmpDetails where EmployeeID='"
						+ loginID + "';");
		for (Row row1 : results1) {
			EmployeeFirstName = row1.getString("EmployeeFirstName");
			EmployeeLastName = row1.getString("EmployeeLastName");
			Email = row1.getString("Email");
		}
		System.out.print(
				"What would you like to edit in your profile- \n1 for Contact\n2 for Address\n3 for Update Skill Level\n4 for Skill\n5 for Interest: ");

		while (secondNumber != 1 && secondNumber != 2 && secondNumber != 3 && secondNumber != 4 && secondNumber != 5) {

			if (editProfile.hasNextInt()) {
				secondNumber = editProfile.nextInt();
			} else {
				try {
					if (editProfile.hasNext()) {
						secondNumber = Integer.parseInt(editProfile.next());
					}
				} catch (NumberFormatException e) {
					secondNumber = 9;
				}
			}

			switch (secondNumber) {
			case 1:
				System.out.print("\nEnter New Contact Number: ");
				editProfile.nextLine();
				String contact = editProfile.nextLine();
				System.out.print("\n");
				session.execute(
						"Update EmpDetails set employeeContact='" + contact + "' where employeeId='" + loginID + "';");
				break;

			case 2:
				System.out.print("\nEnter New Street Address: ");
				editProfile.nextLine();
				String street = editProfile.nextLine();
				System.out.print("\n");
				session.execute(
						"Update EmpDetails set employeeAddress='" + street + "' where employeeId='" + loginID + "';");
				System.out.print("\nEnter City: ");
				String city = editProfile.nextLine();
				session.execute(
						"Update EmpDetails set employeeCity='" + city + "' where employeeId='" + loginID + "';");
				break;

			case 3:
				System.out.print("\nEnter the Skill Name for which you want to update the skill level: ");
				editProfile.nextLine();
				String skillEntered = editProfile.nextLine();
				ResultSet count = session.execute(
						"Select skillName,ID,employeeFirstName,employeeLastName,email from SkillDetails where skillName='"
								+ skillEntered + "' and EmployeeID='" + loginID + "' allow filtering;");
				Row r = count.one();
				if (r != null) {
					String id = r.getString("ID");
					System.out.print("\nUpdate the Skill level(Novice, Intermediate or Expert): ");
					String skillLevel = editProfile.nextLine();
					session.execute("Update skillDetails set skillLevel='" + skillLevel + "' where ID='" + id + "';");

				} else {
					System.out.print("\nThe skill you have entered doesn't exist. Kindly add the skill:");
					String newSkill = editProfile.nextLine();
					System.out.print("\nEnter the skill level for the entered skill (Novice/ Intermediate/ Expert):");
					String newSkillLevel = editProfile.nextLine();
					ResultSet maxCount = session.execute("Select count(*) as a from skillDetails;");
					Row row = maxCount.one();
					long newCounter = (row.getLong(0) + 1);
					session.execute(
							"Insert into skillDetails (ID, employeeID,email,skillName,skillLevel,employeeFirstName,employeeLastName) Values('"
									+ newCounter + "','" + loginID + "','" + Email + "','" + newSkill + "','"
									+ newSkillLevel + "','" + EmployeeFirstName + "','" + EmployeeLastName + "');");
				}
				break;
			case 4:
				System.out.print("\nEnter New Skill: ");
				editProfile.nextLine();
				String newSkillEntered = editProfile.nextLine();
				System.out.print("\n");
				System.out.print("Enter the skill level for the entered skill (Novice/ Intermediate/ Expert):");
				String newSkillLevelEntered = editProfile.nextLine();
				ResultSet maxCount = session.execute("Select count(*) as a from skillDetails;");
				Row row = maxCount.one();
				long newCounter = (row.getLong(0) + 1);
				session.execute(
						"Insert into skillDetails (ID, employeeID,email,skillName,skillLevel,employeeFirstName,employeeLastName) Values('"
								+ newCounter + "','" + loginID + "','" + Email + "','" + newSkillEntered + "','"
								+ newSkillLevelEntered + "','" + EmployeeFirstName + "','" + EmployeeLastName + "');");
				break;

			case 5:
				System.out.println("\nEnter New Interest: ");
				editProfile.nextLine();
				String newInterestEntered = editProfile.nextLine();
				ResultSet maxCount1 = session.execute("Select count(*) as a from InterestDetails;");
				Row row1 = maxCount1.one();
				long newCounter1 = (row1.getLong(0) + 1);
				session.execute(
						"Insert into InterestDetails (ID, employeeID,email,interest,employeeFirstName,employeeLastName) Values('"
								+ newCounter1 + "','" + loginID + "','" + Email + "','" + newInterestEntered + "','"
								+ EmployeeFirstName + "','" + EmployeeLastName + "');");
				break;

			default:
				System.out.println("\nInvalid selection, Please make the correct selection");
				System.out.println(
						"Edit Profile- \n1 for Contact \n2 for Address \n3 for Skill Level\n4 for Skill\n5 for Interest: ");

			}
		}
	}
}



