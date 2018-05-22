package fhu.bughousechess;

import android.widget.ImageView;

import java.util.HashSet;
import java.util.Set;

import static fhu.bughousechess.MainActivity.board;
import static fhu.bughousechess.MainActivity.checkCheck;
import static fhu.bughousechess.MainActivity.whiteCastleKing1;
import static fhu.bughousechess.MainActivity.whiteCastleKing2;
import static fhu.bughousechess.MainActivity.whiteCastleQueen1;
import static fhu.bughousechess.MainActivity.whiteCastleQueen2;

public class King extends Piece
{
    public King(String color)
    {
        this.color = color;
        type = "king";
        wasPawn = false;
        backgroundColor = "";
        empty = false;
    }

    @Override
    public int getResID()
    {
        if (color.equals("white"))
            return R.mipmap.king;
        return R.mipmap.bking;
    }

    @Override
    public Set<Move> getMoves(ImageView[][] board, Piece[][] positions, int x, int y)
    {
        Set<Move> moves = new HashSet<>();
        if (x + 1 < 8)
        {
            if (positions[x + 1][y].empty)
            {
                moves.add(new Move(board, positions, x, y, x + 1, y,"move"));
                if (positions[6][0].empty && positions[4][0].type.equals("king") && positions[4][0].color.equals("white"))
                {
                    //setting those empty positions to "white" so that they can be checked whether
                    //they are in check or not
                    positions[5][0].color = "white";
                    positions[6][0].color = "white";
                    if (board(board, positions) == 1)
                    {
                        if (whiteCastleKing1 && positions[6][0].empty && !checkCheck(board, positions, 4, 0) && !checkCheck(board, positions, 5, 0) && !checkCheck(board, positions, 6, 0))
                        {
                            moves.add(new Move(board, positions, x, y, 6, 0, "whiteKingCastle"));
                        }
                    }
                    else
                    {
                        if (whiteCastleKing2 && positions[6][0].empty && !checkCheck(board, positions, 4, 0) && !checkCheck(board, positions, 5, 0) && !checkCheck(board, positions, 6, 0))
                        {
                            moves.add(new Move(board, positions, x, y, 6, 0, "whiteKingCastle"));

                        }
                    }
                    positions[5][0].color = "";
                    positions[6][0].color = "";
                }
                if (positions[6][7].empty && positions[4][7].type.equals("king") && positions[4][7].color.equals("black"))
                {
                    //setting those empty positions to "white" so that they can be checked whether
                    //they are in check or not
                    positions[5][7].color = "black";
                    positions[6][7].color = "black";
                    if (board(board, positions) == 1)
                    {
                        if (whiteCastleKing1 && positions[6][7].empty && !checkCheck(board, positions, 4, 7) && !checkCheck(board, positions, 5, 7) && !checkCheck(board, positions, 6, 7))
                        {
                            moves.add(new Move(board, positions, x, y, 6, 7, "blackKingCastle"));
                        }
                    }
                    else
                    {
                        if (whiteCastleKing2 && positions[6][7].empty && !checkCheck(board, positions, 4, 7) && !checkCheck(board, positions, 5, 7) && !checkCheck(board, positions, 6, 7))
                        {
                            moves.add(new Move(board, positions, x, y, 6, 7, "blackKingCastle"));

                        }
                    }
                    positions[5][7].color = "";
                    positions[6][7].color = "";
                }
            }
            if (positions[x + 1][y].isOpposite(this))
            {
                moves.add(new Move(board, positions, x, y, x + 1, y, "take"));
            }
            if (y + 1 < 8)
            {
                if (positions[x + 1][y + 1].empty)
                {
                    moves.add(new Move(board, positions, x, y, x + 1, y + 1, "move"));
                }
                if (positions[x + 1][y + 1].isOpposite(this))
                {
                    moves.add(new Move(board, positions, x, y, x + 1, y + 1,"take"));
                }
            }
            if (y - 1 > -1)
            {
                if (positions[x + 1][y - 1].empty)
                {
                    moves.add(new Move(board, positions, x, y, x + 1, y - 1,"move"));
                }
                if (positions[x + 1][y - 1].isOpposite(this))
                {
                    moves.add(new Move(board, positions, x, y, x + 1, y - 1,"take"));
                }
            }
        }
        if (x - 1 > -1)
        {
            if (positions[x - 1][y].empty)
            {
                moves.add(new Move(board, positions, x, y, x - 1, y,"move"));
                if (positions[2][0].empty && positions[1][0].empty && positions[4][0].type.equals("king"))
                {
                    positions[1][0].color = "white";
                    positions[2][0].color = "white";
                    positions[3][0].color = "white";
                    if (board(board, positions) == 1)
                    {

                        if (whiteCastleQueen1 && positions[2][0].empty && !checkCheck(board, positions, 1, 0) && !checkCheck(board, positions, 2, 0) && !checkCheck(board, positions, 3, 0) && !checkCheck(board, positions, 4, 0))
                        {
                            positions[1][0].color = "";
                            positions[2][0].color = "";
                            positions[3][0].color = "";
                            moves.add(new Move(board, positions, x, y, 2, 0,"whiteQueenCastle"));
                        }
                    }
                    else
                    {
                        if (whiteCastleQueen2 && positions[2][0].empty && !checkCheck(board, positions, 1, 0) && !checkCheck(board, positions, 2, 0) && !checkCheck(board, positions, 3, 0) && !checkCheck(board, positions, 4, 0))
                        {
                            positions[1][0] .color = "";
                            positions[2][0].color = "";
                            positions[3][0].color = "";
                            moves.add(new Move(board, positions, x, y, 2, 0,"whiteQueenCastle"));
                        }
                    }
                    positions[1][0].color = "";
                    positions[2][0].color = "";
                    positions[3][0].color = "";
                }
                if (positions[2][7].empty && positions[1][7].empty && positions[4][7].type.equals("king"))
                {
                    positions[1][7].color = "black";
                    positions[2][7].color = "black";
                    positions[3][7].color = "black";
                    if (board(board, positions) == 1)
                    {

                        if (whiteCastleQueen1 && positions[2][7].empty && !checkCheck(board, positions, 1, 7) && !checkCheck(board, positions, 2, 7) && !checkCheck(board, positions, 3, 7) && !checkCheck(board, positions, 4, 7))
                        {
                            positions[1][7].color = "";
                            positions[2][7].color = "";
                            positions[3][7].color = "";
                            moves.add(new Move(board, positions, x, y, 2, 7,"blackQueenCastle"));
                        }
                    }
                    else
                    {
                        if (whiteCastleQueen2 && positions[2][7].empty && !checkCheck(board, positions, 1, 7) && !checkCheck(board, positions, 2, 7) && !checkCheck(board, positions, 3, 7) && !checkCheck(board, positions, 4, 7))
                        {
                            positions[1][7].color = "";
                            positions[2][7].color = "";
                            positions[3][7].color = "";
                            moves.add(new Move(board, positions, x, y, 2, 7,"blackQueenCastle"));
                        }
                    }
                    positions[1][7].color = "";
                    positions[2][7].color = "";
                    positions[3][7].color = "";
                }
            }
            if (positions[x - 1][y].isOpposite(this))
            {
                moves.add(new Move(board, positions, x, y, x - 1, y, "take"));
            }
            if (y + 1 < 8)
            {
                if (positions[x - 1][y + 1].empty)
                {
                    moves.add(new Move(board, positions, x, y, x - 1, y + 1,"move"));
                }
                if (positions[x - 1][y + 1].isOpposite(this))
                {
                    moves.add(new Move(board, positions, x, y, x - 1, y + 1,"take"));
                }
            }
            if (y - 1 > -1)
            {
                if (positions[x - 1][y - 1].empty)
                {
                    moves.add(new Move(board, positions, x, y, x - 1, y - 1,"move"));
                }
                if (positions[x - 1][y - 1].isOpposite(this))
                {
                    moves.add(new Move(board, positions, x, y, x - 1, y - 1, "take"));
                }
            }
        }
        if (y + 1 < 8)
        {
            if (positions[x][y + 1].empty)
            {
                moves.add(new Move(board, positions, x, y, x, y + 1,"move"));
            }
            if (positions[x][y + 1].isOpposite(this))
            {
                moves.add(new Move(board, positions, x, y, x, y + 1,"take"));
            }
        }
        if (y - 1 > -1)
        {
            if (positions[x][y - 1].empty)
            {
                moves.add(new Move(board, positions, x, y, x, y - 1, "move"));
            }
            if (positions[x][y - 1].isOpposite(this))
            {
                moves.add(new Move(board, positions, x, y, x, y - 1,"take"));
            }
        }
        return moves;
    }
}
