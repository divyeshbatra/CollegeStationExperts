package PoAPackage;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;

public class CassandraConnection {

	private static String EmployeeID;
	private static String EmployeeFirstName;
	private static String EmployeeLastName;
	private static String Email;
	private static String EmployeeContact;
	private static String EmployeeAddress;
	private static String EmployeeCity;
	private static String SkillLevel;
	private static String SkillName;
	private static String Designation;
	private static String Interest;
	private static String Rating;
	private static String Recommendation;
	private static String RecommendeeID;
	private static String RecommendeeName;
	private static String UserName;
	private static String Password;
	private static String ID;

	public static void main(String[] args) {
		
		System.out.println("cassandra Java Connection Tester");
		// Creating connection with Cassandra
		Cluster cluster = Cluster.builder().addContactPoint("localhost").build();
		Session session = cluster.connect();
		System.out.println("Connection built successfully");

		// Creating a Keyspace
		session.execute("DROP keyspace if exists CstatCorp;");
		session.execute(
				"CREATE KEYSPACE CstatCorp WITH replication = {'class':'SimpleStrategy','replication_factor':3};");
		session.execute("USE CstatCorp");

		// Creating Column Families
		session.execute("DROP COLUMNFAMILY if exists EmpDetails;");
		String query = "CREATE COLUMNFAMILY EmpDetails(EmployeeID text, EmployeeFirstName text,EmployeeLastName text, Email text, EmployeeContact text, EmployeeAddress text, EmployeeCity text, SkillLevel text, SkillName text, Designation text, Interest text, primary key(EmployeeID));";
		session.execute(query);

		session.execute("DROP COLUMNFAMILY if exists credentials;");
		session.execute(
				"CREATE COLUMNFAMILY credentials(EmployeeID text, UserName text, Password text, primary key(EmployeeID));");

		session.execute("DROP COLUMNFAMILY if exists SkillDetails;");
		session.execute(
				"CREATE COLUMNFAMILY SkillDetails(ID text, EmployeeID text, Email text,SkillName text, SkillLevel text,EmployeeFirstName text, EmployeeLastName text, primary key(ID));");

		session.execute("DROP COLUMNFAMILY if exists InterestDetails;");
		session.execute(
				"CREATE COLUMNFAMILY InterestDetails(ID text, EmployeeID text, Email text,Interest text, EmployeeFirstName text, EmployeeLastName text, primary key(ID));");

		session.execute("DROP COLUMNFAMILY if exists Recommendations;");
		session.execute(
				"CREATE COLUMNFAMILY Recommendations(ID text, EmployeeID text,Rating text, Recommendation text, RecommendeeID text, RecommendeeName text, primary key(ID));");

		// Loading EmpDetails file data from csv to Cassandra DB
		try {
			FileReader fr = new FileReader("../Team4-project/Data/Employee.csv");
			BufferedReader bf = new BufferedReader(fr);
			String line;
			while ((line = bf.readLine()) != null) {
				String[] a = line.split(",");
				EmployeeID = a[0];
				EmployeeFirstName = a[1];
				EmployeeLastName = a[2];
				Email = a[3];
				EmployeeContact = a[4];
				EmployeeAddress = a[5];
				EmployeeCity = a[6];
				SkillLevel = a[7];
				SkillName = a[8];
				Designation = a[9];
				Interest = a[10];

				// writing the insert query
				PreparedStatement statement = session.prepare(
						"INSERT INTO EmpDetails(EmployeeID,EmployeeFirstName,EmployeeLastName,Email,EmployeeContact,EmployeeAddress,EmployeeCity,SkillLevel,SkillName,Designation,Interest) VALUES (?,?,?,?,?,?,?,?,?,?,?);");
				BoundStatement boundStatement = new BoundStatement(statement);
				session.execute(boundStatement.bind(EmployeeID, EmployeeFirstName, EmployeeLastName, Email,
						EmployeeContact, EmployeeAddress, EmployeeCity, SkillLevel, SkillName, Designation, Interest));

			} 
			bf.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}

		// Loading credentials file data from csv to Cassandra DB
		try {
			FileReader fr = new FileReader("../Team4-project/Data/credentials.csv");
			BufferedReader bf = new BufferedReader(fr);
			String line;

			while ((line = bf.readLine()) != null) {
				String[] a = line.split(",");
				EmployeeID = a[0];
				UserName = a[1];
				Password = a[2];
				PreparedStatement statement = session
						.prepare("INSERT INTO credentials(EmployeeID,UserName,Password) VALUES (?,?,?);");
				BoundStatement boundStatement = new BoundStatement(statement);
				session.execute(boundStatement.bind(EmployeeID, UserName, Password));
			}
			bf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Loading Skills file data from csv to Cassandra DB
		try {
			FileReader fr1 = new FileReader("../Team4-project/Data/SkillDetails.csv");
			BufferedReader bf1 = new BufferedReader(fr1);
			String line;
			while ((line = bf1.readLine()) != null) {
				String[] a = line.split(",");
				ID = a[0];
				EmployeeID = a[1];
				Email = a[2];
				SkillName = a[3];
				SkillLevel = a[4];
				EmployeeFirstName = a[5];
				EmployeeLastName = a[6];
				PreparedStatement statement = session.prepare(
						"INSERT INTO SkillDetails(ID,EmployeeID,Email,SkillName,SkillLevel, EmployeeFirstName, EmployeeLastName) VALUES (?,?,?,?,?,?,?);");
				BoundStatement boundStatement = new BoundStatement(statement);
				session.execute(boundStatement.bind(ID, EmployeeID, Email, SkillName, SkillLevel, EmployeeFirstName,
						EmployeeLastName));

			}
			bf1.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Loading Interest file data from csv to Cassandra DB
		try {
			FileReader fr2 = new FileReader("../Team4-project/Data/InterestDetails.csv");
			BufferedReader bf2 = new BufferedReader(fr2);
			String line;
			while ((line = bf2.readLine()) != null) {
				String[] a = line.split(",");
				ID = a[0];
				EmployeeID = a[1];
				Email = a[2];
				Interest = a[3];
				EmployeeFirstName = a[4];
				EmployeeLastName = a[5];
				PreparedStatement statement = session.prepare(
						"INSERT INTO InterestDetails(ID,EmployeeID,Email,Interest,EmployeeFirstName, EmployeeLastName) VALUES (?,?,?,?,?,?);");
				BoundStatement boundStatement = new BoundStatement(statement);
				session.execute(
						boundStatement.bind(ID, EmployeeID, Email, Interest, EmployeeFirstName, EmployeeLastName));

			}
			bf2.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	// Loading Recommendation file data from csv to Cassandra DB
	try {
		FileReader fr3 = new FileReader("../Team4-project/Data/Recommendations.csv");
		BufferedReader bf3 = new BufferedReader(fr3);
		String line;
		while ((line = bf3.readLine()) != null) {
			String[] a = line.split(",");
			ID = a[0];
			EmployeeID = a[1];
			Rating = a[2];
			Recommendation = a[3];
			RecommendeeID = a[4];
			RecommendeeName = a[5];
			PreparedStatement statement = session.prepare(
					"INSERT INTO Recommendations(ID,EmployeeID,Rating,Recommendation,RecommendeeID, RecommendeeName) VALUES (?,?,?,?,?,?);");
			BoundStatement boundStatement = new BoundStatement(statement);
			session.execute(
					boundStatement.bind(ID, EmployeeID, Rating, Recommendation, RecommendeeID, RecommendeeName));

		}
		bf3.close();
	} catch (IOException e) {
		e.printStackTrace();
	}
		

		System.out.println("Records entered into Cassandra Database successfully\n");

		loginCredentials lc = new loginCredentials();
		String loginID = lc.verifyCredentials(session);

		restart: while (true) {
			System.out.println(
					"\nDo you want to Recommend, Search, Edit Profile, View Recommendations or Sign out?\nPress S for Search\nR for Recommendation\nE for Editing your Profile\nV for Viewing Recommendations\nExit to Sign out ");
			Scanner option = new Scanner(System.in);
			String choice = option.next();

			if (choice.equals("R") || choice.equals("r")) {

				recommendation rc = new recommendation();
				rc.recommendationLogic(session, loginID);
				continue restart;

			} else if (choice.equals("S") || choice.equals("s")) {

				searchCriteria sc = new searchCriteria();
				sc.search(session, loginID);
				continue restart;
			} else if (choice.equals("E") || choice.equals("e")) {

				editProfile ep = new editProfile();
				ep.edit(session, loginID);
				continue restart;

			} else if (choice.equals("V") || choice.equals("v")) {

				viewRecommendations vr = new viewRecommendations();
				vr.view(session);
				continue restart;

			} else if (choice.equals("EXIT") || choice.equals("exit") || choice.equals("Exit")) {

				option.close();
				break restart;

			} else {
				System.out.println("Invalid input, Please select the correct option");
				continue restart;
			}

		}

		// Closing Connection
		session.close();
		cluster.close();
	}

}




