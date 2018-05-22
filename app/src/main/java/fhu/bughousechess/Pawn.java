package fhu.bughousechess;

import android.widget.ImageView;

import java.util.HashSet;
import java.util.Set;

public class Pawn extends Piece
{
    public Pawn(String color)
    {
        this.color = color;
        type = "pawn";
        wasPawn = true;
        backgroundColor = "";
        empty = false;
    }

    public int getResID()
    {
        if (color .equals("white"))
        return R.mipmap.pawn;
        return R.mipmap.bpawn;
    }

    @Override
    public Set<Move> getMoves(ImageView[][] board, Piece[][] positions, int x, int y)
    {
        Set<Move> moves = new HashSet<>();
        if (this.color.equals("white"))
        {
            if (y < 7)
            {
                if (positions[x][y + 1].empty)
                {
                    moves.add(new Move(board, positions, x, y, x, y + 1,"move"));
                    if (y == 1)
                    {
                        if (positions[x][y + 2].empty)
                        {
                            moves.add (new Move(board, positions, x, y, x, y + 2,"move"));
                        }
                    }
                }
            }
            if (x < 7 && y < 7)
            {
                if (positions[x + 1][y + 1].isOpposite(this))
                {
                    moves.add(new Move(board, positions, x, y, x + 1, y + 1,"take"));
                }
//            if (y == 4 && positions[x + 1][y].substring(0, 2).equals("BP"))
//            {
//                if (board(board, positions) == 1)
//                {
//                    if (enP[x + 1][1].substring(0, 1).equals("1") && enP[x + 1][1].substring(1, enP[x + 1][1].length()).equals(Integer.toString(board1Turn)))
//                    {
//                        whiteEnP(board, positions, x, x + 1);
//                    }
//                }
//                if (board(board, positions) == 2)
//                {
//                    if (enP[x + 1][3].substring(0, 1).equals("1") && enP[x + 1][3].substring(1, enP[x + 1][3].length()).equals(Integer.toString(board2Turn)))
//                    {
//                        whiteEnP(board, positions, x, x + 1);
//                    }
//                }
//            }
            }
            if (x > 0 && y < 7)
            {
                if (positions[x - 1][y + 1].isOpposite(this))
                {
                    moves.add(new Move(board, positions, x, y, x - 1, y + 1,"take"));
                }
//            if (y == 4 && positions[x - 1][y].substring(0, 2).equals("BP"))
//            {
//                if (board(board, positions) == 1)
//                {
//                    if (enP[x - 1][1].substring(0, 1).equals("1") && enP[x - 1][1].substring(1, enP[x - 1][1].length()).equals(Integer.toString(board1Turn)))
//                    {
//                        whiteEnP(board, positions, x, x - 1);
//                    }
//                }
//                if (board(board, positions) == 2)
//                {
//                    if (enP[x - 1][3].substring(0, 1).equals("1") && enP[x - 1][3].substring(1, enP[x - 1][3].length()).equals(Integer.toString(board2Turn)))
//                    {
//                        whiteEnP(board, positions, x, x - 1);
//                    }
//                }
//            }
            }
        }
        else
        {
            if (y > 0)
            {
                if (positions[x][y - 1].empty)
                {
                    moves.add(new Move(board, positions, x, y, x, y - 1,"move"));
                    if (y == 6)
                    {
                        if (positions[x][y - 2].empty)
                        {
                            moves.add (new Move(board, positions, x, y, x, y - 2,"move"));
                        }
                    }
                }
            }
            if (x < 7 && y > 0)
            {
                if (positions[x + 1][y - 1].isOpposite(this))
                {
                    moves.add(new Move(board, positions, x, y, x + 1, y - 1,"take"));
                }
//            if (y == 4 && positions[x + 1][y].substring(0, 2).equals("BP"))
//            {
//                if (board(board, positions) == 1)
//                {
//                    if (enP[x + 1][1].substring(0, 1).equals("1") && enP[x + 1][1].substring(1, enP[x + 1][1].length()).equals(Integer.toString(board1Turn)))
//                    {
//                        whiteEnP(board, positions, x, x + 1);
//                    }
//                }
//                if (board(board, positions) == 2)
//                {
//                    if (enP[x + 1][3].substring(0, 1).equals("1") && enP[x + 1][3].substring(1, enP[x + 1][3].length()).equals(Integer.toString(board2Turn)))
//                    {
//                        whiteEnP(board, positions, x, x + 1);
//                    }
//                }
//            }
            }
            if (x > 0 && y >0)
            {
                if (positions[x - 1][y - 1].isOpposite(this))
                {
                    moves.add(new Move(board, positions, x, y, x - 1, y - 1,"take"));
                }
//            if (y == 4 && positions[x - 1][y].substring(0, 2).equals("BP"))
//            {
//                if (board(board, positions) == 1)
//                {
//                    if (enP[x - 1][1].substring(0, 1).equals("1") && enP[x - 1][1].substring(1, enP[x - 1][1].length()).equals(Integer.toString(board1Turn)))
//                    {
//                        whiteEnP(board, positions, x, x - 1);
//                    }
//                }
//                if (board(board, positions) == 2)
//                {
//                    if (enP[x - 1][3].substring(0, 1).equals("1") && enP[x - 1][3].substring(1, enP[x - 1][3].length()).equals(Integer.toString(board2Turn)))
//                    {
//                        whiteEnP(board, positions, x, x - 1);
//                    }
//                }
//            }
            }
        }
        return moves;
    }
}
