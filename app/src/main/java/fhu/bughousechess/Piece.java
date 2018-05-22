package fhu.bughousechess;

import android.widget.ImageView;

import java.util.Set;

public abstract class Piece
{
    public String color;
    public String type;
    public boolean wasPawn;
    public String backgroundColor;
    public boolean empty = true;

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


}
