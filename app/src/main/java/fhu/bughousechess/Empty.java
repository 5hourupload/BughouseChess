package fhu.bughousechess;

import android.widget.ImageView;

import java.util.Set;

public class Empty extends Piece
{
    public Empty()
    {
        color = "";
        type = "";
        wasPawn = false;
        backgroundColor = "";
        empty = true;
    }

    @Override
    public int getResID()
    {
        return android.R.color.transparent;
    }

    @Override
    public Set<Move> getMoves(ImageView[][] board, Piece[][] positions, int x, int y)
    {
        return null;
    }
}
