import java.time.LocalDateTime;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int times = Integer.parseInt(s.nextLine());

        LocalDateTime base = LocalDateTime.MIN;
        for (int i = 0; i < times; i++) {
            LocalDateTime tempDate = LocalDateTime.parse(s.nextLine());
            if (tempDate.isAfter(base)) {
                base = tempDate;
            }
        }

        System.out.println(base);
    }
}