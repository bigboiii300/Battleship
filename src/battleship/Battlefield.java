package battleship;

import java.util.Random;
import java.util.Scanner;

public class Battlefield {
    char[][] ocean;
    int countCarrier;
    int countBattleship;
    int countCruiser;
    int countDestroyer;
    int countSubmarine;
    int m;
    int n;
    boolean isOceanFull = true;
    static Scanner scanner = new Scanner(System.in);
    Carrier carrier = new Carrier();
    Battleship battleship = new Battleship();
    Cruiser cruiser = new Cruiser();
    Destroyer destroyer = new Destroyer();
    Submarine submarine = new Submarine();

    /**
     * Конструктор.
     *
     * @param m               - размер поля.
     * @param n               - размер поля.
     * @param countCarrier    - количество 5-палубных кораблей.
     * @param countBattleship - количество 4-палубных кораблей.
     * @param countCruiser    - количество 3-палубных кораблей.
     * @param countDestroyer  - количество 2-палубных кораблей.
     * @param countSubmarine  - количество 1-палубных кораблей.
     */
    Battlefield(int m, int n, int countCarrier, int countBattleship,
                int countCruiser, int countDestroyer, int countSubmarine) {
        ocean = new char[m][n];
        this.countCarrier = countCarrier;
        this.countBattleship = countBattleship;
        this.countCruiser = countCruiser;
        this.countDestroyer = countDestroyer;
        this.countSubmarine = countSubmarine;
        this.m = m;
        this.n = n;
    }

    Random random = new Random();

    /**
     * Добавление кораблей в заданное поле.
     *
     * @throws IllegalArgumentException количество кораблей не влезает или нежелательно для размещения.
     */
    void addShips() {
        try {

            // Проверяем сколько максимально может занимать данное количество кораблей.
            int reqSpace = countCarrier * carrier.getRequiredSpace()
                    + countBattleship * battleship.getRequiredSpace() +
                    countCruiser * cruiser.getRequiredSpace() + countDestroyer * destroyer.getRequiredSpace()
                    + countSubmarine * submarine.getRequiredSpace();
            int reqSlots = countCarrier * carrier.getLength() + countBattleship * battleship.getLength() +
                    countCruiser * cruiser.getLength() + countDestroyer * destroyer.getLength()
                    + countSubmarine * submarine.getLength() + reqSpace;
            if (reqSpace == 0)
                throw new IllegalArgumentException("At least 1 ship must be entered! " +
                        "Please re-enter the number of cells and ships!\n");
            if (m * n < reqSlots)
                throw new IllegalArgumentException("There are not enough space for all ships! " +
                        "Please re-enter the number of cells and ships!\n");
            int[] shipsArrayLength = {countCarrier, countBattleship, countCruiser,
                    countDestroyer, countSubmarine};
            int typeShip = 0;
            for (int j : shipsArrayLength) {
                // Определение типа корабля.
                typeShip++;
                generateShipPlace(m, n, ocean, random, j, typeShip);
            }
        } catch (IllegalArgumentException ex) {
            isOceanFull = false;
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Рандомное распределение кораблей по полю и проверка на возможность размещения.
     *
     * @param m        - размер поля.
     * @param n        - размер поля.
     * @param ocean    - двухмерный массив, содержащий координаты кораблей.
     * @param random   - рандом.
     * @param numb     - количество типов кораблей.
     * @param typeShip - тип корабля.
     */
    public void generateShipPlace(int m, int n,
                                  char[][] ocean, Random random, int numb,
                                  int typeShip) {
        int lengthShip = 6 - typeShip;
        while (numb > 0) {
            int x, y, axisX, axisY = 0;
            x = random.nextInt(m);
            y = random.nextInt(n);
            boolean canSetX = true;
            boolean canSetY = true;
            axisX = random.nextInt(2);
            if (axisX == 0)
                axisY = 1;

            if (axisX == 1)
                canSetX = checkSetX(m, n, ocean, lengthShip, x, y);
            if (axisY == 1)
                canSetY = checkSetY(m, n, ocean, lengthShip, x, y);


            if (!canSetX)
                continue;
            if (!canSetY)
                continue;
            addShipToOcean(m, n, ocean, lengthShip, x, y, axisX, axisY);
            numb--;
        }
    }

    /**
     * Добавление корабля на поле.
     *
     * @param m          - размер поля.
     * @param n          - размер поля.
     * @param ocean      - двухмерный массив, содержащий координаты кораблей.
     * @param lengthShip - длина корабля.
     * @param x          - координата x.
     * @param y          - координата y.
     * @param axisX      - заполнение по оси x.
     * @param axisY      - заполнение по оси y.
     */
    private void addShipToOcean(int m, int n, char[][] ocean,
                                int lengthShip, int x, int y, int axisX, int axisY) {
        if (axisX == 1) {
            // Если возможно заполнить массив слева направо и не выйти за пределы.
            if (n >= y + lengthShip) {
                for (int i = 0; i < lengthShip; ++i) {
                    ocean[x][y + i] = '1';
                }
                // Заполняем справа налево.
            } else if (y - lengthShip >= 0) {
                for (int i = 0; i < lengthShip; ++i) {
                    ocean[x][y - i] = '1';
                }
            }
        }

        if (axisY == 1) {
            // Если возможно заполнить сверху вниз.
            if (m >= x + lengthShip) {
                for (int i = 0; i < lengthShip; ++i) {
                    ocean[x + i][y] = '1';
                }
                // Заполнение снизу вверх.
            } else if (x - lengthShip >= 0) {
                for (int i = 0; i < lengthShip; ++i) {
                    ocean[x - i][y] = '1';
                }

            }
        }
    }

    /**
     * Проверка на возможность размещение корабля, если происходит заполнение по y.
     *
     * @param m          - размер поля.
     * @param n          - размер поля.
     * @param ocean      - двухмерный массив, содержащий координаты кораблей.
     * @param lengthShip - длина корабля.
     * @param x          - координата x.
     * @param y          - координата y.
     * @return положительное или отрицательное значение для проверки.
     */
    private boolean checkSetY(int m, int n, char[][] ocean,
                              int lengthShip, int x, int y) {
        if (checkDown(m, n, ocean, lengthShip, x, y)) return false;
        return !checkUp(m, n, ocean, lengthShip, x, y);
    }

    /**
     * Проверка когда заполнение происходит по вертикали снизу вверх.
     *
     * @param m          - размер поля.
     * @param n          - размер поля.
     * @param ocean      - двухмерный массив, содержащий координаты кораблей.
     * @param lengthShip - длина корабля.
     * @param x          - координата x.
     * @param y          - координата y.
     * @return положительное или отрицательное значение для проверки.
     */
    private boolean checkUp(int m, int n, char[][] ocean, int lengthShip, int x, int y) {
        if (x - lengthShip >= 0) {
            // Проверка вершины корабля и соседних клеток.
            if (x + 1 < m && y - 1 >= 0) {
                if (ocean[x + 1][y - 1] == '1')
                    return true;
            }
            if (x + 1 < m) {
                if (ocean[x + 1][y] == '1')
                    return true;
            }
            if (x + 1 < m && y + 1 < n) {
                if (ocean[x + 1][y + 1] == '1')
                    return true;
            }
            // Проверка боковых клеток.
            for (int i = 0; i <= lengthShip; ++i) {
                if (x - i >= 0 && y - 1 >= 0) {
                    if (ocean[x - i][y - 1] == '1')
                        return true;
                }
                if (x - i >= 0 && y + 1 < n) {
                    if (ocean[x - i][y + 1] == '1')
                        return true;
                }
                if (x - i >= 0) {
                    if (ocean[x - i][y] == '1')
                        return true;
                }
            }
            // Проверка хвоста корабля.
            if (x - lengthShip >= 0 && y - 1 >= 0) {
                if (ocean[x - lengthShip][y - 1] == '1')
                    return true;
            }
            if (x - lengthShip - 1 >= 0 && y + 1 < n) {
                if (ocean[x - lengthShip][y + 1] == '1')
                    return true;
            }
            if (x - lengthShip >= 0) {
                return ocean[x - lengthShip][y] == '1';
            }
        }
        return false;
    }

    /**
     * Проверка когда заполнение происходит по вертикали сверху вниз.
     *
     * @param m          - размер поля.
     * @param n          - размер поля.
     * @param ocean      - двухмерный массив, содержащий координаты кораблей.
     * @param lengthShip - длина корабля.
     * @param x          - координата x.
     * @param y          - координата y.
     * @return положительное или отрицательное значение для проверки.
     */
    private boolean checkDown(int m, int n, char[][] ocean, int lengthShip, int x, int y) {
        if (m >= x + lengthShip) {
            // Проверка вершины корабля.
            if (x - 1 >= 0 && y - 1 >= 0) {
                if ((ocean[x - 1][y - 1] == '1'))
                    return true;
            }
            if (x - 1 >= 0) {
                if (ocean[x - 1][y] == '1')
                    return true;
            }
            if (x - 1 >= 0 && y + 1 < n) {
                if (ocean[x - 1][y + 1] == '1')
                    return true;
            }
            // Проверка боковых клеток.
            for (int i = 0; i <= lengthShip; ++i) {
                if (x + i < m && y - 1 >= 0) {
                    if (ocean[x + i][y - 1] == '1')
                        return true;
                }
                if (x + i < m && y + 1 < n) {
                    if (ocean[x + i][y + 1] == '1')
                        return true;
                }
                if (x + i < m) {
                    if (ocean[x + i][y] == '1')
                        return true;
                }
            }
            // Проверка хвоста корабля.
            if (x + lengthShip < m && y - 1 >= 0) {
                if (ocean[x + lengthShip][y - 1] == '1')
                    return true;
            }
            if (x + lengthShip < m && y + 1 < n) {
                if (ocean[x + lengthShip][y + 1] == '1')
                    return true;
            }
            if (x + lengthShip < m) {
                return ocean[x + lengthShip][y] == '1';
            }
        }
        return false;
    }

    /**
     * Проверка когда заполнение происходит по x
     *
     * @param m          - размер поля.
     * @param n          - размер поля.
     * @param ocean      - двухмерный массив, содержащий координаты кораблей.
     * @param lengthShip - длина корабля.
     * @param x          - координата x.
     * @param y          - координата y.
     * @return положительное или отрицательное значение для проверки.
     */
    private boolean checkSetX(int m, int n, char[][] ocean,
                              int lengthShip, int x, int y) {
        if (checkRight(m, n, ocean, lengthShip, x, y)) return false;
        return !checkLeft(m, n, ocean, lengthShip, x, y);
    }

    /**
     * Проверка когда заполнение происходит по горизонтали справа налево.
     *
     * @param m          - размер поля.
     * @param n          - размер поля.
     * @param ocean      - двухмерный массив, содержащий координаты кораблей.
     * @param lengthShip - длина корабля.
     * @param x          - координата x.
     * @param y          - координата y.
     * @return положительное или отрицательное значение для проверки.
     */
    private boolean checkLeft(int m, int n, char[][] ocean, int lengthShip, int x, int y) {
        if (y - lengthShip >= 0) {
            // Проверка вершины корабля.
            if (x - 1 >= 0 && y + 1 < n) {
                if (ocean[x - 1][y + 1] == '1')
                    return true;
            }
            if (y + 1 < n) {
                if (ocean[x][y + 1] == '1')
                    return true;
            }
            if (y + 1 < n && x + 1 < m) {
                if (ocean[x + 1][y + 1] == '1')
                    return true;
            }
            // Проверка боковых клеток.
            for (int i = 0; i <= lengthShip; ++i) {
                if (y - i >= 0 && x - 1 >= 0) {
                    if (ocean[x - 1][y - i] == '1')
                        return true;
                }
                if (x + 1 < m && y - i >= 0) {
                    if (ocean[x + 1][y - i] == '1') {
                        return true;
                    }
                }
                if (y - i >= 0) {
                    if (ocean[x][y - i] == '1')
                        return true;
                }
            }
            // Проверка хвоста.
            if (x - 1 >= 0 && y - lengthShip >= 0) {
                if (ocean[x - 1][y - lengthShip] == '1')
                    return true;
            }
            if (y - lengthShip >= 0) {
                if (ocean[x][y - lengthShip] == '1')
                    return true;
            }
            if (x + 1 < m && y - lengthShip >= 0) {
                return ocean[x + 1][y - lengthShip] == '1';
            }
        }
        return false;
    }

    /**
     * Проверка когда заполнение происходит по горизонтали слева направо.
     *
     * @param m          - размер поля.
     * @param n          - размер поля.
     * @param ocean      - двухмерный массив, содержащий координаты кораблей.
     * @param lengthShip - длина корабля.
     * @param x          - координата x.
     * @param y          - координата y.
     * @return положительное или отрицательное значение для проверки.
     */
    private boolean checkRight(int m, int n, char[][] ocean, int lengthShip, int x, int y) {
        if (n >= y + lengthShip) {
            // Проверка вершины и соседних клеток.
            if (x - 1 >= 0 && y - 1 >= 0) {
                if ((ocean[x - 1][y - 1] == '1'))
                    return true;
            }
            if (y - 1 >= 0) {
                if ((ocean[x][y - 1] == '1'))
                    return true;
            }
            if (x + 1 < m && y - 1 >= 0) {
                if (ocean[x + 1][y - 1] == '1')
                    return true;
            }
            // Проверка боковых клеток.
            for (int i = 0; i <= lengthShip; ++i) {
                if (x - 1 >= 0 && y + i < n) {
                    if (ocean[x - 1][y + i] == '1') {
                        return true;
                    }
                }
                if (x + 1 < m && y + i < n) {
                    if (ocean[x + 1][y + i] == '1') {
                        return true;
                    }
                }
                if (y + i < n) {
                    if (ocean[x][y + i] == '1') {
                        return true;
                    }
                }
            }
            // Проверка хвоста.
            if (x - 1 >= 0 && y + lengthShip < n) {
                if (ocean[x - 1][y + lengthShip] == '1')
                    return true;
            }
            if (y + lengthShip < n && x + 1 < m) {
                if (ocean[x + 1][y + lengthShip] == '1')
                    return true;
            }
            if (y + lengthShip < n) {
                return ocean[x][y + lengthShip] == '1';
            }
        }
        return false;
    }

    /**
     * Вывод поля в консоль.
     */
    public void outputField() {
        int counterX = 0;
        int counterY = 0;
        System.out.print("   ");
        for (int i = 0; i < n; ++i) {
            System.out.print(counterX + " ");
            counterX++;
        }
        System.out.println();
        for (int i = 0; i < m; ++i) {
            System.out.print(counterY + "  ");
            counterY++;
            for (int j = 0; j < n; ++j) {
                if (ocean[i][j] == '1')
                    System.out.print(". ");
                else if (ocean[i][j] == '\u0000')
                    System.out.print(". ");
                else
                    System.out.print(ocean[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * Метод, реализующий выстрел игрока.
     *
     * @return Положительное или отрицательное значение для проверки выстрела.
     * @throws IndexOutOfBoundsException выход за границы массива.
     * @throws NumberFormatException     некорректный ввод.
     */
    public boolean Shot() {
        try {
            System.out.println("Input x and y:");
            int x = Integer.parseInt(scanner.next());
            int y = Integer.parseInt(scanner.next());
            if (ocean[x][y] != '1') {
                ocean[x][y] = '*';
                System.out.println("Miss");
            } else if (ocean[x][y] == '1') {
                ocean[x][y] = '#';
                System.out.println("Hit");
                if (isShipDestroyed(x, y))
                    System.out.println("This ship is sunk");
            }
        } catch (IndexOutOfBoundsException ex) {
            System.out.println("you need to enter values that don't go beyond the size of the field ");
            return false;
        } catch (NumberFormatException ex) {
            System.out.println("You need to enter a number, not a string! The shot was not fired");
            return false;
        }
        return true;
    }

    /**
     * Проверка полного уничтожения.
     *
     * @param x - координата x.
     * @param y - координата y.
     * @return уничтожен корабль или нет.
     */
    private boolean isShipDestroyed(int x, int y) {
        // Проверка нижней части корабля.
        for (int i = 0; i < 5; ++i) {
            if (x + i < m) {
                if (ocean[x + i][y] == '*' || ocean[x + i][y] == '\u0000')
                    break;
                if (ocean[x + i][y] == '1')
                    return false;
            }
        }
        // Проверка верхней части корабля.
        for (int i = 0; i < 5; ++i) {
            if (x - i >= 0) {
                if (ocean[x - i][y] == '*' || ocean[x - i][y] == '\u0000')
                    break;
                if (ocean[x - i][y] == '1')
                    return false;
            }
        }
        // Проверка правой части.
        for (int i = 0; i < 5; ++i) {
            if (y + i < n) {
                if (ocean[x][y + i] == '*' || ocean[x][y + i] == '\u0000')
                    break;
                if (ocean[x][y + i] == '1')
                    return false;
            }
        }
        // Проверка левой части.
        for (int i = 0; i < 5; ++i) {
            if (y - i >= 0) {
                if (ocean[x][y - i] == '*' || ocean[x][y - i] == '\u0000')
                    break;
                if (ocean[x][y - i] == '1')
                    return false;
            }
        }
        return true;
    }

    /**
     * Проверка на наличие неуничтоженных кораблей.
     *
     * @return положительное или отрицательное значение.
     */
    public boolean isGameActive() {
        for (char[] gates : ocean) {
            for (char gate : gates) {
                if ('1' == gate) {
                    return true;
                }
            }
        }
        return false;
    }
}