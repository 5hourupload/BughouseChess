package fhu.bughousechess.pieces;

import android.widget.ImageView;

import java.util.HashSet;
import java.util.Set;

import fhu.bughousechess.Move;
import fhu.bughousechess.R;

public class Rook extends Piece
{
    public Rook(String color)
    {
        this.color = color;
        type = "rook";
        wasPawn = false;
        backgroundColor = "";
        empty = false;
    }
    @Override
    public int getResID()
    {
        if (color .equals("white"))
            return R.mipmap.rook;
        return R.mipmap.brook;    }

    @Override
    public Set<Move> getMoves(Piece[][] positions, int x, int y, int boardNumber)
    {
        Set<Move> moves = new HashSet<>();
        boolean inbetween = false;
        for (int i = y + 1; i < 8; i++)
        {
            for (int j = y + 1; j < i; j++)
            {
                if (!positions[x][j].empty)
                {
                    inbetween = true;
                }
            }
            if (positions[x][i].empty)
            {
                if (!inbetween)
                {
                    moves.add(new Move(positions, x ,y, x ,i,"move"));
                }
            }
            if (positions[x][i].isOpposite(this))
            {
                if (!inbetween)
                {
                    moves.add(new Move(positions, x ,y, x ,i,"take"));
                }
            }
        }
        inbetween = false;
        for (int i = y - 1; i > -1; i--)
        {
            for (int j = y - 1; j > i; j--)
            {
                if (!positions[x][j].empty)
                {
                    inbetween = true;
                }
            }
            if (positions[x][i].empty)
            {
                if (!inbetween)
                {
                    moves.add(new Move(positions, x ,y, x ,i,"move"));
                }
            }
            if (positions[x][i].isOpposite(this))
            {
                if (!inbetween)
                {
                    moves.add(new Move(positions, x ,y, x ,i,"take"));
                }
            }
        }
        inbetween = false;
        for (int i = x + 1; i < 8; i++)
        {
            for (int j = x + 1; j < i; j++)
            {
                if (!positions[j][y].empty)
                {
                    inbetween = true;
                }
            }
            if (positions[i][y].empty)
            {
                if (!inbetween)
                {
                    moves.add(new Move(positions, x ,y, i ,y,"move"));
                }
            }
            if (positions[i][y].isOpposite(this))
            {
                if (!inbetween)
                {
                    moves.add(new Move(positions, x ,y, i ,y,"take"));
                }
            }
        }
        inbetween = false;
        for (int i = x - 1; i > -1; i--)
        {
            for (int j = x - 1; j > i; j--)
            {
                if (!positions[j][y].empty)
                {
                    inbetween = true;
                }
            }
            if (positions[i][y].empty)
            {
                if (!inbetween)
                {
                    moves.add(new Move(positions, x ,y, i ,y,"move"));
                }
            }
            if (positions[i][y].isOpposite(this))
            {
                if (!inbetween)
                {
                    moves.add(new Move(positions, x ,y, i ,y,"take"));
                }
            }
        }
        return moves;
    }
}
