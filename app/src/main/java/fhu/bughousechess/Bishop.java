package fhu.bughousechess;

import android.widget.ImageView;

import java.util.HashSet;
import java.util.Set;

public class Bishop extends Piece
{
    public Bishop(String color)
    {
        this.color = color;
        type = "bishop";
        wasPawn = false;
        backgroundColor = "";
        empty = false;
    }

    @Override
    public int getResID()
    {
        if (color.equals("white"))
            return R.mipmap.bishop;
        return R.mipmap.bbishop;
    }

    @Override
    public Set<Move> getMoves(ImageView[][] board, Piece[][] positions, int x, int y)
    {
        Set<Move> moves = new HashSet<>();
        boolean inbetween = false;
        int z = x;
        if (y > x)
        {
            z = y;
        }
        for (int i = 1; i < 8 - z; i++)
        {
            for (int j = 1; j < i; j++)
            {
                if (!positions[x + j][y + j].empty)
                {
                    inbetween = true;
                }
            }
            if (positions[x + i][y + i].empty)
            {
                if (!inbetween)
                {
                    moves.add(new Move(board, positions, x, y, x + i, y + i,"move"));
                }
            }
            if (positions[x + i][y + i].isOpposite(this))
            {
                if (!inbetween)
                {
                    moves.add(new Move(board, positions, x, y, x + i, y + i,"take"));
                }
            }
        }
        inbetween = false;
        if (x < y)
        {
            z = x;
        }
        else
        {
            z = y;
        }
        for (int i = 1; i < z + 1; i++)
        {
            for (int j = 1; j < i; j++)
            {
                if (!positions[x - j][y - j].empty)
                {
                    inbetween = true;
                }
            }
            if (positions[x - i][y - i].empty)
            {
                if (!inbetween)
                {
                    moves.add(new Move(board, positions, x, y, x - i, y - i,"move"));
                }
            }
            if (positions[x - i][y - i].isOpposite(this))
            {
                if (!inbetween)
                {
                    moves.add(new Move(board, positions, x, y, x - i, y - i,"take"));
                }
            }
        }
        inbetween = false;
        if (7 - x < y)
        {
            z = 7 - x;
        }
        else
        {
            z = y;
        }
        for (int i = 1; i < z + 1; i++)
        {
            for (int j = 1; j < i; j++)
            {
                if (!positions[x + j][y - j].empty)
                {
                    inbetween = true;
                }
            }
            if (positions[x + i][y - i].empty)
            {
                if (!inbetween)
                {
                    moves.add(new Move(board, positions, x, y, x + i, y - i,"move"));
                }
            }
            if (positions[x + i][y - i].isOpposite(this))
            {
                if (!inbetween)
                {
                    moves.add(new Move(board, positions, x, y, x + i, y - i,"take"));
                }
            }
        }
        inbetween = false;
        if (7 - y < x)
        {
            z = 7 - y;
        }
        else
        {
            z = x;
        }
        for (int i = 1; i < z + 1; i++)
        {
            for (int j = 1; j < i; j++)
            {
                if (!positions[x - j][y + j].empty)
                {
                    inbetween = true;
                }
            }
            if (positions[x - i][y + i].empty)
            {
                if (!inbetween)
                {
                    moves.add(new Move(board, positions, x, y, x - i, y + i,"move"));
                }
            }
            if (positions[x - i][y + i].isOpposite(this))
            {
                if (!inbetween)
                {
                    moves.add(new Move(board, positions, x, y, x - i, y + i,"take"));
                }
            }
        }
        return moves;
    }
}
