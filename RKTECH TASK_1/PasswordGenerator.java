//Task 1
import java.util.Scanner;
import java.security.SecureRandom;

public class PasswordGenerator {
	
    public static void main(String[] args) {
    	System.out.print("Enter the no. of characters: "); 
    	Scanner sc = new Scanner(System.in);
        int passLength = sc.nextInt(); //Taking the input from the user
        String password = generatePass(passLength); 
        System.out.println("Generated password is : " + password);
        sc.close();
    }

    public static String generatePass(int length) {
    	String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; 
    	String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String specialCharacters = "~`!@#$%^&*()-_=+[{]}|;:',.<>?/";
        
        SecureRandom random = new SecureRandom();
        StringBuilder newpassword = new StringBuilder(length);
        // Getting password with a mix of characters
        for (int i = 0; i < length; i++) {
            int type = random.nextInt(4); 

            switch (type) { // 0: lowerCase, 1: upperCase, 2: numbers, 3: specialCharacters
                case 0:
                    newpassword.append(lowerCase.charAt(random.nextInt(lowerCase.length())));
                    break;
                case 1:
                    newpassword.append(upperCase.charAt(random.nextInt(upperCase.length())));
                    break;
                case 2:
                    newpassword.append(numbers.charAt(random.nextInt(numbers.length())));
                    break;
                case 3:
                    newpassword.append(specialCharacters.charAt(random.nextInt(specialCharacters.length())));
                    break;
            }
        }
        return newpassword.toString(); //returning the value in the string format
    }
}
