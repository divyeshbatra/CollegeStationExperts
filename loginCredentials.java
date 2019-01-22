package PoAPackage;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import java.util.Scanner;
public class loginCredentials {

	public String verifyCredentials(Session session) {

		@SuppressWarnings("resource")
		Scanner option = new Scanner(System.in);
		int check = 1;
		String resultLoginInput = "";
		String resultLoginPassword = "";
		String resultID = "";
		while (check == 1) {
			check = 0;
			System.out.println("Signin using your Email-ID: ");
			String loginInput = option.next();
			System.out.println("Enter your password: ");
			String loginPassword = option.next();
			ResultSet resultLogin = session
					.execute("SELECT EmployeeID, UserName, Password from credentials where UserName='" + loginInput + "'and Password='" + loginPassword + "' allow filtering;");

			for (Row row : resultLogin) {
				resultID = row.getString("EmployeeID");
				resultLoginInput = row.getString("UserName");
				resultLoginPassword = row.getString("Password");
			}

			if (resultLoginInput.equals(loginInput) && resultLoginPassword.equals(loginPassword)) {
				System.out.println("\nYou have been logged in successfully.");
			} else {
				System.out.println("\nINVALID CREDENTIALS, Please try again.\n");
				check = 1;
			}
		}
		return resultID;
	}
}
