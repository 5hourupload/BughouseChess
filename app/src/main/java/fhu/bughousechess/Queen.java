package fhu.bughousechess;

import android.widget.ImageView;

import java.util.HashSet;
import java.util.Set;

public class Queen extends Piece
{
    public Queen(String color)
    {
        this.color = color;
        type = "queen";
        wasPawn = false;
        backgroundColor = "";
        empty = false;
    }

    @Override
    public int getResID()
    {
        if (color.equals("white"))
            return R.mipmap.queen;
        return R.mipmap.bqueen;
    }

    @Override
    public Set<Move> getMoves(ImageView[][] board, Piece[][] positions, int x, int y)
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
                    moves.add(new Move(board, positions, x ,y, x ,i,"move"));
                }
            }
            if (positions[x][i].color.equals("black"))
            {
                if (!inbetween)
                {
                    moves.add(new Move(board, positions, x ,y, x ,i,"take"));
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
                    moves.add(new Move(board, positions, x ,y, x ,i,"move"));
                }
            }
            if (positions[x][i].isOpposite(this))
            {
                if (!inbetween)
                {
                    moves.add(new Move(board, positions, x ,y, x ,i,"take"));
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
                    moves.add(new Move(board, positions, x ,y, i ,y,"move"));
                }
            }
            if (positions[i][y].isOpposite(this))
            {
                if (!inbetween)
                {
                    moves.add(new Move(board, positions, x ,y, i ,y,"take"));
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
                    moves.add(new Move(board, positions, x ,y, i ,y,"move"));
                }
            }
            if (positions[i][y].isOpposite(this))
            {
                if (!inbetween)
                {
                    moves.add(new Move(board, positions, x ,y, i ,y,"take"));
                }
            }
        }
        inbetween = false;
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
