import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String stringWithNumbers = scanner.nextLine();

        Pattern pattern = Pattern.compile("\\d{10,}");
        Matcher matcher = pattern.matcher(stringWithNumbers);
        
        while (matcher.find()) {
            System.out.println(String.format("%s:%d", matcher.group(), matcher.end() - matcher.start()));
        }
    }
}
