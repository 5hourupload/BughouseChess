package fhu.bughousechess.pieces;

import android.widget.ImageView;

import java.util.HashSet;
import java.util.Set;

import fhu.bughousechess.Move;

public abstract class Piece
{
    public String color;
    public String type;
    public boolean wasPawn;
    public String backgroundColor;
    public boolean empty = true;
    public boolean onRoster = false;

    public abstract int getResID();
    public abstract Set<Move> getMoves(ImageView[][] board, Piece[][] positions, int x, int y);
    public boolean isOpposite(Piece piece)
    {
        if (color.equals("white"))
        return piece.color.equals("black");
        if (color.equals("black"))
            return piece.color.equals("white");
        return false;

    }
    public Set<Move> getRosterMoves(ImageView[][] board, Piece[][] positions, ImageView[] roster, Piece[] rosterp, int i)
    {
        Set<Move> moves = new HashSet<>();
        for (int x = 0; x < 8; x++)
        {
            for (int y = 0; y < 8; y++)
            {
                if (positions[x][y].empty)
                {
                    moves.add(new Move(board, positions, roster, rosterp, i, x, y, "roster"));
                }
            }
        }
        return  moves;
    }
}
