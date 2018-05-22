package fhu.bughousechess;

import android.widget.ImageView;

public class Move
{
    ImageView[][] board;
    Piece[][] positions;
    int x;
    int y;
    int x1;
    int y1;
    String type;

    public Move(ImageView[][] board, Piece[][] positions, int x, int y, int x1, int y1, String type)
    {
        this.board = board;
        this.positions = positions;
        this.x = x;
        this.y = y;
        this.x1 = x1;
        this.y1 = y1;
        this.type = type;
    }
}
