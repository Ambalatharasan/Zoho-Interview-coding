import java.util.Scanner;

public class countAlpha {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the string: ");
        String input = sc.nextLine().toUpperCase(); 
        int result = 0;
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            int value = ch - 'A' + 1; 
            result = result * 26 + value;
        }
        System.out.println("Output: " + result);
    }
}
