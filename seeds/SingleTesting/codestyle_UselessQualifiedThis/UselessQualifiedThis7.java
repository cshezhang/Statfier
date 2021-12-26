
public class Board {
    private int[] Board;


    public Board(int[] b) {
        for (int i = 0; i < b.length; i++)
            Board[i] = b[i];
    }


    public int size() {
        return Board.length;
    }
}
        