package fhu.bughousechess.pieces;

import android.widget.ImageView;

import java.util.HashSet;
import java.util.Set;

import fhu.bughousechess.Move;
import fhu.bughousechess.R;

public class Bishop extends Piece
{
    public Bishop(String color)
    {
        this.color = color;
        type = "bishop";
        wasPawn = false;
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
    public Set<Move> getMoves(Piece[][] positions, int x, int y, int boardNumber)
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
                    moves.add(new Move(positions, x, y, x + i, y + i,"move"));
                }
            }
            if (positions[x + i][y + i].isOpposite(this))
            {
                if (!inbetween)
                {
                    moves.add(new Move(positions, x, y, x + i, y + i,"take"));
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
                    moves.add(new Move(positions, x, y, x - i, y - i,"move"));
                }
            }
            if (positions[x - i][y - i].isOpposite(this))
            {
                if (!inbetween)
                {
                    moves.add(new Move(positions, x, y, x - i, y - i,"take"));
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
                    moves.add(new Move(positions, x, y, x + i, y - i,"move"));
                }
            }
            if (positions[x + i][y - i].isOpposite(this))
            {
                if (!inbetween)
                {
                    moves.add(new Move(positions, x, y, x + i, y - i,"take"));
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
                    moves.add(new Move(positions, x, y, x - i, y + i,"move"));
                }
            }
            if (positions[x - i][y + i].isOpposite(this))
            {
                if (!inbetween)
                {
                    moves.add(new Move(positions, x, y, x - i, y + i,"take"));
                }
            }
        }
        return moves;
    }
}
