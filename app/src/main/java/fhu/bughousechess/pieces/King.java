package fhu.bughousechess.pieces;

import android.widget.ImageView;

import java.util.HashSet;
import java.util.Set;

import fhu.bughousechess.Move;
import fhu.bughousechess.R;
import fhu.bughousechess.pieces.Piece;

import static fhu.bughousechess.GameStateManager.castleCheckCheck;
import static fhu.bughousechess.GameStateManager.whiteCastleKing1;
import static fhu.bughousechess.GameStateManager.whiteCastleKing2;
import static fhu.bughousechess.GameStateManager.whiteCastleQueen1;
import static fhu.bughousechess.GameStateManager.whiteCastleQueen2;

public class King extends Piece
{
    public King(String color)
    {
        this.color = color;
        type = "king";
        wasPawn = false;
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
    public Set<Move> getMoves(Piece[][] positions, int x, int y, int boardNumber)
    {
        Set<Move> moves = new HashSet<>();
        if (x + 1 < 8)
        {
            if (positions[x + 1][y].empty)
            {
                moves.add(new Move(positions, x, y, x + 1, y,"move"));
                if (positions[6][0].empty && positions[4][0].type.equals("king") && positions[4][0].color.equals("white"))
                {
                    //setting those empty positions to "white" so that they can be checked whether
                    //they are in check or not
                    if (boardNumber == 0)
                    {
                        if (whiteCastleKing1 && positions[6][0].empty && !castleCheckCheck("white", positions, 4, 0, boardNumber) && !castleCheckCheck("white",positions, 5, 0, boardNumber) && !castleCheckCheck("white",positions, 6, 0, boardNumber))
                        {
                            moves.add(new Move(positions, x, y, 6, 0, "whiteKingCastle"));
                        }
                    }
                    else
                    {
                        if (whiteCastleKing2 && positions[6][0].empty && !castleCheckCheck("white", positions, 4, 0, boardNumber) && !castleCheckCheck("white",positions, 5, 0, boardNumber) && !castleCheckCheck("white",positions, 6, 0, boardNumber))
                        {
                            moves.add(new Move(positions, x, y, 6, 0, "whiteKingCastle"));

                        }
                    }
                }
                if (positions[6][7].empty && positions[4][7].type.equals("king") && positions[4][7].color.equals("black"))
                {
                    //setting those empty positions to "white" so that they can be checked whether
                    //they are in check or not
                    if (boardNumber == 0)
                    {
                        if (whiteCastleKing1 && positions[6][7].empty && !castleCheckCheck("black",positions, 4, 7, boardNumber) && !castleCheckCheck("black",positions, 5, 7, boardNumber) && !castleCheckCheck("black",positions, 6, 7, boardNumber))
                        {
                            moves.add(new Move(positions, x, y, 6, 7, "blackKingCastle"));
                        }
                    }
                    else
                    {
                        if (whiteCastleKing2 && positions[6][7].empty && !castleCheckCheck("black",positions, 4, 7, boardNumber) && !castleCheckCheck("black",positions, 5, 7, boardNumber) && !castleCheckCheck("black",positions, 6, 7, boardNumber))
                        {
                            moves.add(new Move(positions, x, y, 6, 7, "blackKingCastle"));
                        }
                    }
                }
            }
            if (positions[x + 1][y].isOpposite(this))
            {
                moves.add(new Move(positions, x, y, x + 1, y, "take"));
            }
            if (y + 1 < 8)
            {
                if (positions[x + 1][y + 1].empty)
                {
                    moves.add(new Move(positions, x, y, x + 1, y + 1, "move"));
                }
                if (positions[x + 1][y + 1].isOpposite(this))
                {
                    moves.add(new Move(positions, x, y, x + 1, y + 1,"take"));
                }
            }
            if (y - 1 > -1)
            {
                if (positions[x + 1][y - 1].empty)
                {
                    moves.add(new Move(positions, x, y, x + 1, y - 1,"move"));
                }
                if (positions[x + 1][y - 1].isOpposite(this))
                {
                    moves.add(new Move(positions, x, y, x + 1, y - 1,"take"));
                }
            }
        }
        if (x - 1 > -1)
        {
            if (positions[x - 1][y].empty)
            {
                moves.add(new Move(positions, x, y, x - 1, y,"move"));
                if (positions[2][0].empty && positions[1][0].empty && positions[4][0].type.equals("king"))
                {
                    if (boardNumber == 0)
                    {

                        if (whiteCastleQueen1 && positions[2][0].empty && !castleCheckCheck("white",positions, 1, 0, boardNumber) && !castleCheckCheck("white",positions, 2, 0, boardNumber) && !castleCheckCheck("white",positions, 3, 0, boardNumber) && !castleCheckCheck("white",positions, 4, 0, boardNumber))
                        {
                            moves.add(new Move(positions, x, y, 2, 0,"whiteQueenCastle"));
                        }
                    }
                    else
                    {
                        if (whiteCastleQueen2 && positions[2][0].empty && !castleCheckCheck("white",positions, 1, 0, boardNumber) && !castleCheckCheck("white",positions, 2, 0, boardNumber) && !castleCheckCheck("white",positions, 3, 0, boardNumber) && !castleCheckCheck("white",positions, 4, 0, boardNumber))
                        {
                            moves.add(new Move(positions, x, y, 2, 0,"whiteQueenCastle"));
                        }
                    }
                }
                if (positions[2][7].empty && positions[1][7].empty && positions[4][7].type.equals("king"))
                {
                    if (boardNumber == 0)
                    {
                        if (whiteCastleQueen1 && positions[2][7].empty && !castleCheckCheck("black",positions, 1, 7, boardNumber) && !castleCheckCheck("black",positions, 2, 7, boardNumber) && !castleCheckCheck("black",positions, 3, 7, boardNumber) && !castleCheckCheck("black",positions, 4, 7, boardNumber))
                        {
                            moves.add(new Move(positions, x, y, 2, 7,"blackQueenCastle"));
                        }
                    }
                    else
                    {
                        if (whiteCastleQueen2 && positions[2][7].empty && !castleCheckCheck("black",positions, 1, 7, boardNumber) && !castleCheckCheck("black",positions, 2, 7, boardNumber) && !castleCheckCheck("black",positions, 3, 7, boardNumber) && !castleCheckCheck("black",positions, 4, 7, boardNumber))
                        {
                            moves.add(new Move(positions, x, y, 2, 7,"blackQueenCastle"));
                        }
                    }
                }
            }
            if (positions[x - 1][y].isOpposite(this))
            {
                moves.add(new Move(positions, x, y, x - 1, y, "take"));
            }
            if (y + 1 < 8)
            {
                if (positions[x - 1][y + 1].empty)
                {
                    moves.add(new Move(positions, x, y, x - 1, y + 1,"move"));
                }
                if (positions[x - 1][y + 1].isOpposite(this))
                {
                    moves.add(new Move(positions, x, y, x - 1, y + 1,"take"));
                }
            }
            if (y - 1 > -1)
            {
                if (positions[x - 1][y - 1].empty)
                {
                    moves.add(new Move(positions, x, y, x - 1, y - 1,"move"));
                }
                if (positions[x - 1][y - 1].isOpposite(this))
                {
                    moves.add(new Move(positions, x, y, x - 1, y - 1, "take"));
                }
            }
        }
        if (y + 1 < 8)
        {
            if (positions[x][y + 1].empty)
            {
                moves.add(new Move(positions, x, y, x, y + 1,"move"));
            }
            if (positions[x][y + 1].isOpposite(this))
            {
                moves.add(new Move(positions, x, y, x, y + 1,"take"));
            }
        }
        if (y - 1 > -1)
        {
            if (positions[x][y - 1].empty)
            {
                moves.add(new Move(positions, x, y, x, y - 1, "move"));
            }
            if (positions[x][y - 1].isOpposite(this))
            {
                moves.add(new Move(positions, x, y, x, y - 1,"take"));
            }
        }
        return moves;
    }
}
