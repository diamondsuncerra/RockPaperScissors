package rockpaperscissors;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

class HelperMethods {

    public static ArrayList<String> getBigger(String currentOption, int optionNumbers, ArrayList<String> options) {

        // Returns the options that beat the current option
        ArrayList<String> bigger = new ArrayList<>();

        optionNumbers--;
        optionNumbers/=2;

        int m = options.indexOf(currentOption) + 1;

        int aux = 0;

        while (aux < optionNumbers) {

            if (m >= options.size()) {
                m = 0;
            }

            bigger.add(options.get(m));
            m++;
            aux++;
        }

        return bigger;
    }

    public static String getString (int n, ArrayList<String> options) {
        return options.get(n);
    }

    public static int getWinner (String player, String computer, ArrayList<String> bigger) {

        // 1 - player wins
        //-1 - computer wins
        // 0 - draw

        if (player.equals(computer))
            return 0;

        if (bigger.contains(computer))
            return -1;

        return 1;
    }

    public static boolean isValid (String string, ArrayList<String> options) {
        if (options.contains(string))
            return true;
        if(string.equals("!rating"))
            return true;
        if (string.equals("!exit"))
            return true;
        return false;
    }

    public static int printWinningStatement (String player, String computer, int n) {

        if (n == 0) {
            System.out.println("There is a draw (" + player +")");
            return 50;
        } else if (n == 1) {
            System.out.println("Well done. The computer chose " + computer + " and failed");
            return  100;
        } else {
            System.out.println("Sorry, but the computer chose " + computer);
            return 0;
        }
    }


}

public class Main {


    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your name:");
        String playerName = scanner.next();
        System.out.println("Hello, " + playerName);

        // Reading the options - implemented only for odd numbers

        ArrayList<String> options = new ArrayList<>();
        scanner.nextLine();
        String optionString = scanner.nextLine();

        if (optionString.isEmpty()) {
            options.add("rock");
            options.add("paper");
            options.add("scissors");
        } else {
            StringTokenizer tokenizer = new StringTokenizer(optionString);
            while (tokenizer.hasMoreTokens()) {
                options.add(tokenizer.nextToken(","));
            }
        }

        System.out.println("Okay, let's start");

        int playerScore = 0;

        HashMap<String,Integer> scores = new HashMap<>();

        // entering the scores from the ratings
        BufferedReader bufferedReader = new BufferedReader(new FileReader("rating.txt"));
        String line;
        while( (line = bufferedReader.readLine() ) != null) {
            StringTokenizer tokenizer = new StringTokenizer(line);
            String name = tokenizer.nextToken();
            Integer score = Integer.valueOf(tokenizer.nextToken());
            scores.put(name, score);
        }

        if (scores.get(playerName) != null)
            playerScore += scores.get(playerName);

        while (true) {

            String playerMove = scanner.next();

            if (!HelperMethods.isValid(playerMove, options)) {
                System.out.println("Invalid input");
                continue;
            }

            if (playerMove.equals("!exit")) {
                System.out.println("Bye!");
                break;
            }

            if (playerMove.equals("!rating")) {
                System.out.println("Your rating: " + playerScore);
                continue;
            }

            // Generate random move
            Random random = new Random();
            int move = random.nextInt(options.size());
            String computerMove = HelperMethods.getString(move, options);

            ArrayList<String> bigger = HelperMethods.getBigger(playerMove, options.size(), options);
            playerScore += HelperMethods.printWinningStatement(playerMove, computerMove, HelperMethods.getWinner(playerMove, computerMove, bigger));
        }

        FileWriter writer = new FileWriter("rating.txt");
        writer.write(playerName + " " + playerScore + "\n");

    }
}
