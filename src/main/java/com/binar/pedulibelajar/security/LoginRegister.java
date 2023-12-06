package com.binar.pedulibelajar.security;



import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.io.File;

public class LoginRegister {

    public static void main(String[] args) {
        Ask_User();
    }

    public static void Ask_User() {

        Scanner input = new Scanner(System.in);
        int choice;
        System.out.print("Please Enter your choice: ");
        System.out.println("1. Register");
        System.out.println("2. Login");

        choice = input.nextInt();

        if (choice == 1) {
            // Register
            Register();
        } else if (choice == 2) {
            // Login
            // Add your login logic here
            Login();
        } else {
            System.out.print("The username or password is incorrect, Please repeat!");
        }
    }

    public static void Register() {
        Scanner input = new Scanner(System.in);
        String usn, pass;
        try {
            FileWriter writer = new FileWriter("C:\\jp\\penyimpananlogin.txt");
            System.out.println("==========REGISTRATION==============");
            System.out.print("Enter Username : ");
            usn = input.nextLine();

            System.out.print("Enter Password : ");
            pass = input.nextLine();

            writer.write(usn + "-" + pass);

            writer.close();

            System.out.print("Successfully Registered");
        } catch (IOException ex) {
            System.out.print("An Error Occurred");
        }
    }

    public static void Login() {
        // Method implementation

        Scanner input = new Scanner(System.in);
        String _user, _pass;
        boolean _found = false;
        try
        {
            File myobj = new File("C:\\jp\\penyimpananlogin.txt");
            Scanner myreader = new Scanner(myobj);

            System.out.println("=======LOGIN=========");
            System.out.print("Enter Username : ");
            String user = input.nextLine();

            System.out.print("Enter Password : ");
            String pass = input.nextLine();

            while (myreader.hasNextLine()) {
                String data = myreader.nextLine();
                String[] acc = data.split("-");
                _user = acc[0];
                _pass = acc[1];

                if (_user.equals(user) && _pass.equals(pass)) {
                    _found = true;
                }
            }

            if (_found) {
                System.out.println("Successfully Login!");
            } else {
                System.out.println("Access Denied! Invalid Username or password");
            }
            myreader.close();

        } catch (IOException ex) {
            System.out.print("An error Occurred");
        }
    }
}