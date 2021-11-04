package battleship;

import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    private static boolean restart = true;
    static int countCarrier = 0;
    static int countBattleship = 0;
    static int countCruiser = 0;
    static int countDestroyer = 0;
    static int countSubmarine = 0;

    public static void main(String[] args) {
        System.out.println("Hello!");
        System.out.println("Please enter your name:");
        String playersName = scanner.next();
        try {
            while (restart) {
                Battlefield field = startGame();
                gameplay(field, playersName);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Старт игры и создание объекта поля
     *
     * @return объект игрового поля
     * @throws NumberFormatException некорректный ввод (ввод строки)
     */
    private static Battlefield startGame() {
        try {
            System.out.println("Please input the size of the ocean");
            System.out.println("For example: 10 10");
            int m = Integer.parseInt(scanner.next());
            int n = Integer.parseInt(scanner.next());
            System.out.println("Set the number of ships for the following types");
            System.out.print("Carrier(5 cells): ");
            countCarrier = Integer.parseInt(scanner.next());
            System.out.print("Battleship(4 cells): ");
            countBattleship = Integer.parseInt(scanner.next());
            System.out.print("Cruiser(3 cells): ");
            countCruiser = Integer.parseInt(scanner.next());
            System.out.print("Destroyer(2 cell): ");
            countDestroyer = Integer.parseInt(scanner.next());
            System.out.print("Submarine(1 cell): ");
            countSubmarine = Integer.parseInt(scanner.next());
            Battlefield field = new Battlefield(m, n, countCarrier, countBattleship,
                    countCruiser, countDestroyer, countSubmarine);
            field.addShips();
            return field;
        } catch (NumberFormatException ex) {
            System.out.println("You need to enter a number, not a string! The game will start again");
            restart = true;
            return null;
        }
    }

    /**
     * Метод, который отвечает за игровой процесс, а именно за выстрелы игрока.
     *
     * @param field       - игровое поле.
     * @param playersName - имя игрока.
     * @throws NullPointerException в случае, если передается null.
     */
    private static void gameplay(Battlefield field, String playersName) {
        try {
            // Если поле пустое.
            if (!field.isOceanFull) {
                restart = true;
                return;
            }
            int shots = 0;
            restart = false;
            while (field.isGameActive()) {
                if (field.Shot())
                    shots++;
                field.outputField();
            }
            if (!field.isGameActive()) {
                finalPhase(playersName, shots);
            }
        } catch (NullPointerException nEx) {
            restart = true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            restart = true;
        }
    }

    /**
     * Конец игры и вывод количества выстрелов, а также возможность перезапустить игру.
     *
     * @param playersName - имя игрока.
     * @param shots       - количество выстрелов, которые сделал игрок.
     */
    private static void finalPhase(String playersName, int shots) {
        System.out.println(playersName + ", You win!");
        System.out.println("Total shots: " + shots);
        System.out.println("Total ships destroyed: " +
                (countCarrier + countBattleship + countDestroyer + countCruiser + countSubmarine));
        System.out.println();
        System.out.println("If you want to play again enter restart");
        System.out.println("If you want to exit enter exit");
        boolean reenter;
        do {
            String endGame = scanner.next();
            switch (endGame) {
                case "exit" -> {
                    System.out.println("Good game, well played. Goodbye!");
                    reenter = false;
                }
                case "restart" -> {
                    restart = true;
                    reenter = false;
                }
                default -> {
                    System.out.println("I don't know what you entered. Re-enter");
                    reenter = true;
                }
            }
        } while (reenter);
    }
}