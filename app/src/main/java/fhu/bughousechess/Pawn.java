package fhu.bughousechess;

import android.widget.ImageView;

import java.util.HashSet;
import java.util.Set;

import static fhu.bughousechess.MainActivity.board;
import static fhu.bughousechess.MainActivity.board1Turn;
import static fhu.bughousechess.MainActivity.board2Turn;
import static fhu.bughousechess.MainActivity.enP;

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
        if (color.equals("white"))
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
                    moves.add(new Move(board, positions, x, y, x, y + 1, "move"));
                    if (y == 1)
                    {
                        if (positions[x][y + 2].empty)
                        {
                            moves.add(new Move(board, positions, x, y, x, y + 2, "move"));
                        }
                    }
                }
            }
            if (x < 7 && y < 7)
            {
                if (positions[x + 1][y + 1].isOpposite(this))
                {
                    moves.add(new Move(board, positions, x, y, x + 1, y + 1, "take"));
                }
                if (y == 4 && positions[x + 1][y].color.equals("black") && positions[x + 1][y].type.equals("pawn"))
                {
                    if (board(board, positions) == 1)
                    {
                        if (enP[x + 1][1].substring(0, 1).equals("1") && enP[x + 1][1].substring(1, enP[x + 1][1].length()).equals(Integer.toString(board1Turn)))
                        {
                            moves.add(new Move(board, positions, x, 4, x + 1, 4, "whiteEnP"));
                        }
                    }
                    if (board(board, positions) == 2)
                    {
                        if (enP[x + 1][3].substring(0, 1).equals("1") && enP[x + 1][3].substring(1, enP[x + 1][3].length()).equals(Integer.toString(board2Turn)))
                        {
                            moves.add(new Move(board, positions, x, 4, x + 1, 4, "whiteEnP"));
                        }
                    }
                }
            }
            if (x > 0 && y < 7)
            {
                if (positions[x - 1][y + 1].isOpposite(this))
                {
                    moves.add(new Move(board, positions, x, y, x - 1, y + 1, "take"));
                }
                if (y == 4 && positions[x - 1][y].color.equals("black") && positions[x - 1][y].type.equals("pawn"))
                {
                    if (board(board, positions) == 1)
                    {
                        if (enP[x - 1][1].substring(0, 1).equals("1") && enP[x - 1][1].substring(1, enP[x - 1][1].length()).equals(Integer.toString(board1Turn)))
                        {
                            moves.add(new Move(board, positions, x, 4, x - 1, 4, "whiteEnP"));
                        }
                    }
                    if (board(board, positions) == 2)
                    {
                        if (enP[x - 1][3].substring(0, 1).equals("1") && enP[x - 1][3].substring(1, enP[x - 1][3].length()).equals(Integer.toString(board2Turn)))
                        {
                            moves.add(new Move(board, positions, x, 4, x - 1, 4, "whiteEnP"));
                        }
                    }
                }
            }
        }
        else
        {
            if (y > 0)
            {
                if (positions[x][y - 1].empty)
                {
                    moves.add(new Move(board, positions, x, y, x, y - 1, "move"));
                    if (y == 6)
                    {
                        if (positions[x][y - 2].empty)
                        {
                            moves.add(new Move(board, positions, x, y, x, y - 2, "move"));
                        }
                    }
                }
            }
            if (x < 7 && y > 0)
            {
                if (positions[x + 1][y - 1].isOpposite(this))
                {
                    moves.add(new Move(board, positions, x, y, x + 1, y - 1, "take"));
                }
                if (y == 3 && positions[x + 1][y].color.equals("white") && positions[x + 1][y].type.equals("pawn"))
                {
                    if (board(board, positions) == 1)
                    {
                        if (enP[x + 1][0].substring(0, 1).equals("1") && enP[x + 1][0].substring(1, enP[x + 1][0].length()).equals(Integer.toString(board1Turn)))
                        {
                            moves.add(new Move(board, positions, x, 3, x + 1, 3, "blackEnP"));
                        }
                    }
                    if (board(board, positions) == 2)
                    {
                        if (enP[x + 1][2].substring(0, 1).equals("1") && enP[x + 1][2].substring(1, enP[x + 1][2].length()).equals(Integer.toString(board2Turn)))
                        {
                            moves.add(new Move(board, positions, x, 3, x + 1, 3, "blackEnP"));
                        }
                    }
                }
            }
            if (x > 0 && y > 0)
            {
                if (positions[x - 1][y - 1].isOpposite(this))
                {
                    moves.add(new Move(board, positions, x, y, x - 1, y - 1, "take"));
                }
                if (y == 3 && positions[x - 1][y].color.equals("white")&& positions[x - 1][y].type.equals("pawn"))
                {
                    if (board(board, positions) == 1)
                    {
                        if (enP[x - 1][0].substring(0, 1).equals("1") && enP[x - 1][0].substring(1, enP[x - 1][0].length()).equals(Integer.toString(board1Turn)))
                        {
                            moves.add(new Move(board, positions, x, 3, x - 1,3, "blackEnP"));
                        }
                    }
                    if (board(board, positions) == 2)
                    {
                        if (enP[x - 1][2].substring(0, 1).equals("1") && enP[x - 1][2].substring(1, enP[x - 1][2].length()).equals(Integer.toString(board2Turn)))
                        {
                            moves.add(new Move(board, positions, x, 3, x - 1, 3, "blackEnP"));
                        }
                    }
                }
            }
        }
        return moves;
    }
}
