package fhu.bughousechess;

import fhu.bughousechess.pieces.Piece;

public class Move
{
    Piece[][] positions;
    Piece[] rosterp;
    int i;
    int x;
    int y;
    int x1;
    int y1;
    String type;

    public Move(Piece[][] positions, int x, int y, int x1, int y1, String type)
    {
        this.positions = positions;
        this.x = x;
        this.y = y;
        this.x1 = x1;
        this.y1 = y1;
        this.type = type;
    }
    public Move(Piece[][] positions, Piece[] rosterp, int i, int x1, int y1, String type)
    {
        this.positions = positions;
        this.rosterp = rosterp;
        this.i = i;
        this.x1 = x1;
        this.y1 = y1;
        this.type = type;
    }
}
