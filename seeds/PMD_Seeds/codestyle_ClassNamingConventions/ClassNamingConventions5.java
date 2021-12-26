
public class Position {
    final int x;
    final int y;


    private Position(int px, int py) {
        x = px;
        y = py;
    }

    static Position of(int px, int py) {
        new Position(px, py);
    }

}
        