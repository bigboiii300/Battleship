package battleship;

class Carrier extends Ship {
    int length = 5;
    int space = 16;

    /**
     * Переопределенный метод для получения длины корабля.
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
