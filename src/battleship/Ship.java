package battleship;

public abstract class Ship {

    /**
     * Абстрактный метод для получения длины корабля.
     *
     * @return длина корабля.
     */
    public abstract int getLength();

    /**
     * Абстрактный методы для получения максимального количества клеток вокруг корабля.
     *
     * @return количество клеток вокруг корабля.
     */
    public abstract int getRequiredSpace();
}
