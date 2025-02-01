//input-[{1,2},{2,3},{2,4},{4,5}]
//output-[1,2,3,4,5]

import java.util.*;

public class ShuffleArray {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the number of pairs: ");
        int n = sc.nextInt();
        Set<Integer> uniqueValues = new HashSet<>();
        System.out.println("Enter the pair list:");
        for (int i = 0; i < n; i++) {
            int a = sc.nextInt();
            int b = sc.nextInt();
            uniqueValues.add(a);
            uniqueValues.add(b);
        }
        List<Integer> output = new ArrayList<>(uniqueValues);
        Collections.sort(output);
        System.out.println("Output: " + output);
    }
}
