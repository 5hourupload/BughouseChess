package fhu.bughousechess;

import android.widget.ImageView;

public class Move
{
    ImageView[][] board;
    Piece[][] positions;
    ImageView[] roster;
    Piece[] rosterp;
    int i;
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
    public Move(ImageView[][] board, Piece[][] positions, ImageView[] roster, Piece[] rosterp, int i, int x1, int y1)
    {
        this.board = board;
        this.positions = positions;
        this.roster = roster;
        this.rosterp = rosterp;
        this.i = i;
        this.x1 = x1;
        this.y1 = y1;
    }
}
