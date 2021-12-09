import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.exception.outcomes.WrongAnswer;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.TestedProgram;

public class Tests extends StageTest<String> {
    // multiple line output
    @DynamicTest(order = 1)
    CheckResult multipleLineOutputTest() {

        TestedProgram main = new TestedProgram();
        main.start("./test/lausanne.json");
        String output = main.execute("/output \"m1\"").trim();

        String[] stations = new String[]{"Renes—Gare", "Epenex", "Crochy", "Cerisaie",
            "Bassenges", "EPFL", "UNL—Sorge", "Mouline", "UNL—Chemberonne", "Bourdonnette", "Melley",
            "Provence", "Montelly", "Vigie", "Lausanne—Flon"};

        checkDepots(output);
        checkOutputLength(output, stations.length + 2);
        assertStations(output, stations);

        stations = new String[]{"Croisettes", "Vennes", "Fourmi", "Sallaz", "CHUV", "Ours",
            "Riponne M.Bejart", "Bessieres", "Lausanne—Flon", "Lausanne Gare CFF", "Grancy", "Delices", "Jourdils",
            "Ouchy—Olympique"};

        output = main.execute("/output \"m2\"");
        checkDepots(output);
        checkOutputLength(output, stations.length + 2);
        assertStations(output, stations);

        return CheckResult.correct();
    }

    // addition test
    @DynamicTest(order = 2)
    CheckResult additionTest() {

        TestedProgram main = new TestedProgram();
        main.start("./test/lausanne.json");

        // added a station to the end of the line
        main.execute("/append \"m1\" \"Test station 1\"");
        String output = main.execute("/output \"m1\"");

        String[] stations = new String[]{"Renes—Gare", "Epenex", "Crochy", "Cerisaie",
            "Bassenges", "EPFL", "UNL—Sorge", "Mouline", "UNL—Chemberonne", "Bourdonnette", "Melley",
            "Provence", "Montelly", "Vigie", "Lausanne—Flon", "Test station 1"};

        checkDepots(output);
        checkOutputLength(output, stations.length + 2);
        assertStations(output, stations);

        // added another one
        main.execute("/append \"m1\" \"Test station 2\"");
        output = main.execute("/output \"m1\"");

        stations = new String[]{"Renes—Gare", "Epenex", "Crochy", "Cerisaie",
            "Bassenges", "EPFL", "UNL—Sorge", "Mouline", "UNL—Chemberonne", "Bourdonnette", "Melley",
            "Provence", "Montelly", "Vigie", "Lausanne—Flon", "Test station 1", "Test station 2"};

        checkDepots(output);
        checkOutputLength(output, stations.length + 2);
        assertStations(output, stations);

        // added one station to the beginning of the line
        main.execute("/add-head \"m1\" \"Head\"");
        output = main.execute("/output \"m1\"");

        stations = new String[]{"Head", "Renes—Gare", "Epenex", "Crochy", "Cerisaie",
            "Bassenges", "EPFL", "UNL—Sorge", "Mouline", "UNL—Chemberonne", "Bourdonnette", "Melley",
            "Provence", "Montelly", "Vigie", "Lausanne—Flon", "Test station 1", "Test station 2"};

        checkDepots(output);
        checkOutputLength(output, stations.length + 2);
        assertStations(output, stations
        );

        return CheckResult.correct();
    }

    // not existing file check
    @DynamicTest(order = 3)
    CheckResult nonexistingFileTest() {
        TestedProgram main = new TestedProgram();
        String output = main.start("tHiS_fIlE_DoEs_nOt_ExIsT.txt");
        if (output.trim().length() == 0) {
            return CheckResult.wrong("The program did not print anything when the file was not exist. ");
        }
        if (output.toLowerCase().startsWith("depot") || output.toLowerCase().endsWith("depot")) {
            return CheckResult.wrong("It looks like the program did not print an error message when the file was not exist.");
        }
        return CheckResult.correct();
    }

    // output with transfers
    @DynamicTest(order = 4)
    CheckResult outputLineWithTransfer() {
        TestedProgram main = new TestedProgram();
        main.start("./test/prague.json");

        String[][] stations = new String[][]{
            {"Nemocnice Motol", null},
            {"Petriny", null},
            {"Nadrazi Veleslavin", null},
            {"Borislavka", null},
            {"Dejvicka", null},
            {"Hradcanska", null},
            {"Malostranska", null},
            {"Staromestska", null},
            {"Mustek", "Linka B"},
            {"Muzeum", "Linka C"},
            {"Namesti Miru", null},
            {"Jiriho z Podebrad", null},
            {"Flora", null},
            {"Zelivskeho", null},
            {"Strasnicka", null},
            {"Skalka", null},
            {"Depo Hostivar", null}
        };

        String output = main.execute("/output \"Linka A\"");
        checkDepots(output);

        checkOutputLength(output, stations.length + 2);
        assertWithTransfer(output, stations);

        return CheckResult.correct();
    }

    // connections test
    @DynamicTest(order = 5)
    CheckResult connectLinesTest() {

        TestedProgram main = new TestedProgram();
        main.start("./test/prague.json");

        String[][] stations = new String[][]{{"Nemocnice Motol", null}, {"Petriny", "Linka C"},
            {"Nadrazi Veleslavin", null}, {"Borislavka", null}, {"Dejvicka", null}, {"Hradcanska", null},
            {"Malostranska", null}, {"Staromestska", null}, {"Mustek", "Linka B"}, {"Muzeum", "Linka C"},
            {"Namesti Miru", null}, {"Jiriho z Podebrad", null}, {"Flora", null}, {"Zelivskeho", null},
            {"Strasnicka", null}, {"Skalka", null}, {"Depo Hostivar", null}
        };
        main.execute("/connect \"Linka C\" \"I.P.Pavlova\" \"Linka A\" \"Petriny\"");

        String output = main.execute("/output \"Linka A\"");

        checkDepots(output);
        checkOutputLength(output, stations.length + 2);
        assertWithTransfer(output, stations);

        return CheckResult.correct();
    }

    @DynamicTest(order = 6)
    CheckResult simpleRouteTest() {
        TestedProgram main = new TestedProgram();
        main.start("./test/prague.json");

        String[] correctRoute = {"Petriny", "Nadrazi Veleslavin", "Borislavka", "Dejvicka", "Hradcanska", "Malostranska",
            "Staromestska", "Mustek", "Muzeum", "Namesti Miru", "Jiriho z Podebrad", "Flora"};

        String[] sOutput = main.execute("/route \"Linka A\" \"Petriny\" \"Linka A\" \"Flora\"").toLowerCase().split("\n");

        if (sOutput.length != correctRoute.length) {
            return CheckResult.wrong("Your program printed an incorrect number of stations in the route.\n" +
                "Expected: " + correctRoute.length + " stations." +
                "\nYour output: " + sOutput.length + " stations.");
        }

        for (int i = 0; i < correctRoute.length; i++) {
            if (i == 0 && !correctRoute[i].equalsIgnoreCase(sOutput[i])) {
                return CheckResult.wrong("The first station in the output should be '" + correctRoute[i] + "'.");
            } else if (i != 0) {
                if (!correctRoute[i].equalsIgnoreCase(sOutput[i])) {
                    return CheckResult.wrong("After '" + correctRoute[i - 1] + "' should be '" + correctRoute[i] + "'.");
                }
            }
        }

        return CheckResult.correct();
    }

    // advanced route test
    @DynamicTest(order = 7)
    CheckResult advancedRouteTest() {
        TestedProgram main = new TestedProgram();
        main.start("./test/prague.json");

        String[] route = {"Vysehrad", "I.P.Pavlova", "Muzeum",
            "Linka A", "Muzeum", "Mustek",
            "Linka B", "Mustek", "Namesti Republiky"};
        String[] sOutput = main.execute("/route \"Linka C\" \"Vysehrad\" \"Linka B\" \"Namesti Republiky\"").split("\n");

        if (sOutput.length != 9) {
            return CheckResult.wrong("Your program printed an incorrect number of stations in the route.");
        }

        int index = 0;
        for (String station : sOutput) {
            if (!station.toLowerCase().trim().contains(route[index].toLowerCase())) {
                return CheckResult.wrong("The route is incorrect. Wrong stations were displayed");
            }
            index++;
        }

        return CheckResult.correct();
    }

    // simple test with time
    @DynamicTest(order = 8)
    CheckResult simpleTime() {
        TestedProgram main = new TestedProgram();
        main.start("./test/prague_w_time.json");

        String[] route = {"Borislavka", "Dejvicka", "Hradcanska", "Malostranska", "Staromestska", "Mustek", "Muzeum",
            "Namesti Miru", "Jiriho z Podebrad", "Flora", "44"};
        String[] sOutput = main.execute("/fastest-route \"Linka A\" \"Borislavka\" \"Linka A\" \"Flora\"").split("\n");

        if (sOutput.length != route.length) {
            return CheckResult.wrong("Your program printed an incorrect number of stations in the route. Expected 10 stations and the total time!");
        }

        int index = 0;
        for (String station : sOutput) {
            if (!station.toLowerCase().trim().contains(route[index].toLowerCase())) {
                return CheckResult.wrong("The route is incorrect. Wrong stations were displayed");
            }
            index++;
        }

        return CheckResult.correct();
    }

    // advanced test with time
    @DynamicTest(order = 9)
    CheckResult advancedTime() {
        TestedProgram main = new TestedProgram();
        main.start("./test/prague_w_time.json");

        String[] route = {"Vysehrad", "I.P.Pavlova", "Muzeum", "Hlavni nadrazi", "Florenc",
            "Linka B", "Florenc", "Namesti Republiky", "29"
        };

        String[] sOutput = main.execute("/fastest-route \"Linka C\" \"Vysehrad\" \"Linka B\" \"Namesti Republiky\"")
            .split("\n");

        if (sOutput.length != 9) {
            return CheckResult.wrong("Your program printed an incorrect number of stations in the route. Expected 8 stations and the total time!");
        }

        int index = 0;
        for (String station : sOutput) {
            if (!station.toLowerCase().trim().contains(route[index].toLowerCase())) {
                return CheckResult.wrong("The route is incorrect. Wrong stations were displayed");
            }
            index++;
        }

        return CheckResult.correct();
    }

    // checks for "depot" at the start and at the end
    void checkDepots(String output) {
        output = output.trim().toLowerCase();
        if (!output.startsWith("depot")) {
            throw new WrongAnswer("Your output should start with 'depot'.");
        } else if (!output.endsWith("depot")) {
            throw new WrongAnswer("Your output should end with 'depot'.");
        }
    }

    // checks number of stations in output
    void checkOutputLength(String output, int correctLength) {
        int length = output.trim().split("\n").length;
        if (length != correctLength) {
            throw new WrongAnswer("You output contains wrong number of lines.\n" +
                "Expected: " + correctLength + " lines\n" +
                "Your output: " + length + " lines");
        }
    }

    // checks stations
    void assertStations(String output, String[] stations) {

        String[] sOutput = output.trim().split("\n");

        for (int i = 0; i < stations.length; i++) {
            if (!sOutput[i + 1].equals(stations[i])) {
                throw new WrongAnswer("Can't find station '" + stations[i] + "' in the line number " + (i + 2));
            }
        }
    }

    void assertWithTransfer(String output, String[][] stations) {

        String[] sOutput = output.split("\n");

        for (int i = 0; i < stations.length; i++) {
            String currentLine = sOutput[i + 1].toLowerCase().trim();
            String currentStation = stations[i][0];
            String currentTransfer = stations[i][1];
            if (currentTransfer == null) {
                if (!currentLine.equals(currentStation.toLowerCase())) {
                    throw new WrongAnswer("There is an error in your program's reply. Some stations were not found.");
                }
            } else if (!currentLine.contains(currentStation.toLowerCase()) ||
                !currentLine.contains(currentTransfer.toLowerCase())) {
                throw new WrongAnswer("Expected transfer to '" + currentTransfer + "' from '" + currentStation + "' station.");
            }
        }
    }
}