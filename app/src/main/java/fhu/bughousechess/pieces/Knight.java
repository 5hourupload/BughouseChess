package fhu.bughousechess.pieces;

import android.widget.ImageView;

import java.util.HashSet;
import java.util.Set;

import fhu.bughousechess.Move;
import fhu.bughousechess.R;
import fhu.bughousechess.pieces.Piece;

public class Knight extends Piece
{
    public Knight(String color)
    {
        this.color = color;
        type = "knight";
        wasPawn = false;
        backgroundColor = "";
        empty = false;
    }

    @Override
    public int getResID()
    {
        if (color.equals("white"))
            return R.mipmap.knight;
        return R.mipmap.bknight;
    }

    @Override
    public Set<Move> getMoves(ImageView[][] board, Piece[][] positions, int x, int y)
    {
        Set<Move> moves = new HashSet<>();
        if (x + 1 < 8 && y + 2 < 8)
        {
            if (!positions[x + 1][y + 2].color.equals(color))
            {
                if (positions[x + 1][y + 2].isOpposite(this))
                {
                    moves.add(new Move(board, positions, x , y, x+1, y+2, "take"));
                }
                else
                {
                    moves.add(new Move(board,positions,x,y,x+1,y+2,"move"));
                }
            }
        }
        if (x + 2 < 8 && y + 1 < 8)
        {
            if (!positions[x + 2][y + 1].color.equals(color))
            {
                if (positions[x + 2][y + 1].isOpposite(this))
                {
                    moves.add(new Move(board, positions, x , y, x+2, y+1, "take"));
                }
                else
                {
                    moves.add(new Move(board, positions, x , y, x+2, y+1, "move"));
                }
            }
        }
        if (x - 1 > -1 && y + 2 < 8)
        {
            if (!positions[x - 1][y + 2].color.equals(color))
            {
                if (positions[x - 1][y + 2].isOpposite(this))
                {
                    moves.add(new Move(board, positions, x , y, x-1, y+2, "take"));
                }
                else
                {
                    moves.add(new Move(board, positions, x , y, x-1, y+2, "move"));
                }
            }
        }
        if (x - 2 > -1 && y + 1 < 8)
        {
            if (!positions[x - 2][y + 1].color.equals(color))
            {
                if (positions[x - 2][y + 1].isOpposite(this))
                {
                    moves.add(new Move(board, positions, x , y, x-2, y+1, "take"));
                }
                else
                {
                    moves.add(new Move(board, positions, x , y, x-2, y+1, "move"));
                }
            }
        }
        if (x + 1 < 8 && y - 2 > -1)
        {
            if (!positions[x + 1][y - 2].color.equals(color))
            {
                if (positions[x + 1][y - 2].isOpposite(this))
                {
                    moves.add(new Move(board, positions, x , y, x+1, y-2, "take"));
                }
                else
                {
                    moves.add(new Move(board, positions, x , y, x+1, y-2, "move"));
                }
            }
        }
        if (x + 2 < 8 && y - 1 > -1)
        {
            if (!positions[x + 2][y - 1].color.equals(color))
            {
                if (positions[x + 2][y - 1].isOpposite(this))
                {
                    moves.add(new Move(board, positions, x , y, x+2, y-1, "take"));
                }
                else
                {
                    moves.add(new Move(board, positions, x , y, x+2, y-1, "move"));
                }
            }
        }
        if (x - 1 > -1 && y - 2 > -1)
        {
            if (!positions[x - 1][y - 2].color.equals(color))
            {
                if (positions[x - 1][y - 2].isOpposite(this))
                {
                    moves.add(new Move(board, positions, x , y, x-1, y-2, "take"));
                }
                else
                {
                    moves.add(new Move(board, positions, x , y, x-1, y-2, "move"));
                }
            }
        }
        if (x - 2 > -1 && y - 1 > -1)
        {
            if (!positions[x - 2][y - 1].color.equals(color))
            {
                if (positions[x - 2][y - 1].isOpposite(this))
                {
                    moves.add(new Move(board, positions, x , y, x-2, y-1, "take"));
                }
                else
                {
                    moves.add(new Move(board, positions, x , y, x-2, y-1, "move"));
                }
            }
        }
        return moves;
    }
}
