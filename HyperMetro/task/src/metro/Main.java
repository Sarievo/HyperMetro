package metro;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File(args[0]);
        if (file.exists()) {
            Scanner s = new Scanner(file);
            if (!s.hasNextLine()) return;

            String d1 = "depot", d2 = s.nextLine().trim(), d3;
            while (s.hasNextLine()) {
                d3 = s.nextLine().trim();
                System.out.printf("%s - %s - %s\n", d1, d2, d3);
                d1 = d2;
                d2 = d3;
            }
            System.out.printf("%s - %s - %s\n", d1, d2, "depot");
        } else {
            error();
        }
    }

    static void error() {
        System.out.println("Error! Such a file doesn't exist!");
    }
}
