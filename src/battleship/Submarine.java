package battleship;

public class Submarine extends Ship {
    int length = 1;
    int space = 8;

    /**
     * Переопределенный метод для получения длины корабля.
     *
     * @return длина корабля.
     */
    @Override
    public int getLength() {
        return length;
    }

    /**
     * Переопределенный метод для получения количество клеток вокруг корабля.
     *
     * @return количество клеток вокруг корабля.
     */
    @Override
    public int getRequiredSpace() {
        return space;
    }
}
