package fhu.bughousechess.pieces;

import android.widget.ImageView;

import java.util.Set;

import fhu.bughousechess.Move;
import fhu.bughousechess.pieces.Piece;

public class Empty extends Piece
{
    public Empty()
    {
        color = "";
        type = "";
        wasPawn = false;
        empty = true;
    }

    @Override
    public int getResID()
    {
        return android.R.color.transparent;
    }

    @Override
    public Set<Move> getMoves(Piece[][] positions, int x, int y, int boardNumber)
    {
        return null;
    }
}
