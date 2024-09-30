import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Activity8_Villareal {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        System.out.println("Enter Name: ");
        String name = scan.nextLine();

        Pattern pattern = Pattern.compile("^[a-zA-Z ñÑ.]+$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(name);

        if (matcher.find()) {
            System.out.println("Valid Name: " + name);
        } else {
            String cleanedName = name.replaceAll("[0-9]", "");
            System.out.println("Invalid Name (Numbers removed): " + cleanedName);
        }

        scan.close();
    }
}
