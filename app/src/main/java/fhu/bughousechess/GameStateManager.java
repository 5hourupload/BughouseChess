package fhu.bughousechess;

import android.widget.ImageView;

import java.util.Set;

import fhu.bughousechess.pieces.Bishop;
import fhu.bughousechess.pieces.Empty;
import fhu.bughousechess.pieces.King;
import fhu.bughousechess.pieces.Knight;
import fhu.bughousechess.pieces.Pawn;
import fhu.bughousechess.pieces.Piece;
import fhu.bughousechess.pieces.Queen;
import fhu.bughousechess.pieces.Rook;

public class GameStateManager {

    public Piece positions[][][] = new Piece[2][8][8];
    public Piece roster1p[] = new Piece[30];
    public Piece roster2p[] = new Piece[30];
    public Piece roster3p[] = new Piece[30];
    public Piece roster4p[] = new Piece[30];


    int whiteTurn1 = 1;
    int whiteTurn2 = 1;

    public static boolean whiteCastleQueen1 = true;
    public static boolean whiteCastleKing1 = true;
    public static boolean blackCastleQueen1 = true;
    public static boolean blackCastleKing1 = true;
    public static boolean whiteCastleQueen2 = true;
    public static boolean whiteCastleKing2 = true;
    public static boolean blackCastleQueen2 = true;
    public static boolean blackCastleKing2 = true;

    static boolean checking = true;
    static boolean placing = true;
    static boolean reverting = true;
    public static boolean firstrank = false;

    static int gameState = 0;
    int turnSave1 = 0;
    int turnSave2 = 0;

    public static String[][] enP = new String[8][4];
    public static int board1Turn = 0;
    public static int board2Turn = 0;

    boolean checkmate1 = false;
    boolean checkmate2 = false;

    static boolean position1 = true;
    static boolean position2 = true;
    static boolean position3 = true;
    static boolean position4 = true;

    public GameStateManager()
    {
        resetEnPassantTracker();
        setStartingPieces(positions);
    }

    public void resetEnPassantTracker()
    {
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                enP[i][j] = "0000";
            }
        }
    }

    private void setStartingPieces(Piece[][][] positions)
    {
        for (int b = 0; b < 2; b++)
        {
            for (int i = 0; i < 8; i++)
            {
                for (int j = 0; j < 8; j++)
                {
                    positions[b][i][j] = new Empty();
                }
            }
            for (int i = 0; i < 8; i++)
            {
                positions[b][i][1] = new Pawn("white");
            }
            for (int i = 0; i < 8; i++)
            {
                positions[b][i][6] = new Pawn("black");
            }
            positions[b][0][0] = new Rook("white");
            positions[b][7][0] = new Rook("white");
            positions[b][0][7] = new Rook("black");
            positions[b][7][7] = new Rook("black");
            positions[b][1][0] = new Knight("white");
            positions[b][6][0] = new Knight("white");
            positions[b][1][7] = new Knight("black");
            positions[b][6][7] = new Knight("black");
            positions[b][2][0] = new Bishop("white");
            positions[b][5][0] = new Bishop("white");
            positions[b][2][7] = new Bishop("black");
            positions[b][5][7] = new Bishop("black");
            positions[b][3][0] = new Queen("white");
            positions[b][3][7] = new Queen("black");
            positions[b][4][0] = new King("white");
            positions[b][4][7] = new King("black");
        }




        for (int i = 0; i < 30; i++)
        {
            roster1p[i] = new Empty();
            roster2p[i] = new Empty();
            roster3p[i] = new Empty();
            roster4p[i] = new Empty();
        }

    }
    public void resetBooleans()
    {
        whiteTurn1 = 1;
        whiteTurn2 = 1;

        whiteCastleQueen1 = true;
        whiteCastleKing1 = true;
        blackCastleQueen1 = true;
        blackCastleKing1 = true;
        whiteCastleQueen2 = true;
        whiteCastleKing2 = true;
        blackCastleQueen2 = true;
        blackCastleKing2 = true;
        checkmate1 = false;
        checkmate2 = false;

        for (int i = 0; i < 6; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                enP[i][j] = "0000";
            }
        }
    }

    public void clean() {
        for (int b = 0; b < 2; b++)
        {
            for (int i = 0; i < 8; i++)
            {
                for (int j = 0; j < 8; j++)
                {
                    positions[b][i][j].backgroundColor = "0";
                }
            }
        }


        for (int i = 0; i < 30; i++)
        {
            roster1p[i].backgroundColor = "0";
            roster2p[i].backgroundColor = "0";
            roster3p[i].backgroundColor = "0";
            roster4p[i].backgroundColor = "0";
        }

    }

    public void performMove(String moveType, int x, int y, final int x1, final int y1, int boardNumber)
    {
        //?? not sur what this is for, possible a fringe case catcher?
        if (positions[boardNumber][x][y].empty)
        {
            whiteTurn1 = 3;
            whiteTurn2 = 3;
            gameState = 2;
            return;
        }
        turnChange(positions[boardNumber][x][y].color, boardNumber);

        if (positions[boardNumber][x][y].color.equals("white") && positions[boardNumber][x][y].type.equals("pawn") && y == 1 && y1 == 3)
        {
            if (boardNumber == 0) enP[x][0] = "1" + Integer.toString(board1Turn);
            if (boardNumber == 1) enP[x][2] = "1" + Integer.toString(board2Turn);
        }
        if (positions[boardNumber][x][y].color.equals("black") && positions[boardNumber][x][y].type.equals("pawn") && y == 6 && y1 == 4)
        {
            if (boardNumber == 0) enP[x][1] = "1" + Integer.toString(board1Turn);
            if (boardNumber == 1) enP[x][3] = "1" + Integer.toString(board2Turn);
        }
        switchPositions(moveType, positions[boardNumber], x, y, x1, y1);
    }

    public void performRosterMove(int i, int x, int y, int boardNumber)
    {
        Piece[] roster = null;
        if (boardNumber == 0 && whiteTurn1 == 1) roster = roster1p;
        if (boardNumber == 0 && whiteTurn1 == 2) roster = roster2p;
        if (boardNumber == 1 && whiteTurn1 == 1) roster = roster4p;
        if (boardNumber == 1 && whiteTurn1 == 2) roster = roster3p;

        if (roster[i].empty)
        {
            whiteTurn1 = 3;
            whiteTurn2 = 3;
            gameState = 2;
            return;
        }
        turnChange(roster[i].color, boardNumber);
        positions[boardNumber][x][y] = roster[i];
        roster[i] = new Empty();
    }

    private void turnChange(String prevColor, int boardNumber)
    {
        if (boardNumber == 0)
        {
            if (prevColor.equals("white"))
            {
                if (gameState == 2)
                {
                    turnSave1 = 2;
                }
                else
                {
                    whiteTurn1 = 2;
                }
            }
            else
            {
                if (gameState == 2)
                {
                    turnSave1 = 1;
                }
                else
                {
                    whiteTurn1 = 1;
                }

            }
            board1Turn++;
        }
        else
        {
            if (prevColor.equals("white"))
            {
                if (gameState == 2)
                {
                    turnSave2 = 2;
                }
                else
                {
                    whiteTurn2 = 2;
                }
            }
            else
            {
                if (gameState == 2)
                {
                    turnSave2 = 1;
                }
                else
                {
                    whiteTurn2 = 1;
                }
            }
            board2Turn++;
        }

    }

    public static void switchPositions(String moveType, Piece[][] positions, int x, int y, int x1, int y1)
    {
        if (moveType.equals("whiteKingCastle"))
        {
            positions[x1][y1] = positions[x][y];
            positions[x][y] = new Empty();
            positions[7][0] = new Empty();
            positions[5][0] = new Rook("white");
            return;
        }
        if (moveType.equals("whiteQueenCastle"))
        {
            positions[x1][y1] = positions[x][y];
            positions[x][y] = new Empty();
            positions[0][0] = new Empty();
            positions[3][0] = new Rook("white");
            return;
        }
        if (moveType.equals("blackKingCastle"))
        {
            positions[x1][y1] = positions[x][y];
            positions[x][y] = new Empty();
            positions[7][7] = new Empty();
            positions[5][7] = new Rook("black");
            return;
        }
        if (moveType.equals("blackQueenCastle"))
        {
            positions[x1][y1] = positions[x][y];
            positions[x][y] = new Empty();
            positions[0][7] = new Empty();
            positions[3][7] = new Rook("black");
            return;
        }
        if (moveType.equals("whiteEnP"))
        {
            positions[x1][4] = new Empty();
            positions[x1][5] = positions[x][y];
            positions[x][y] = new Empty();
            return;
        }
        if (moveType.equals("blackEnP"))
        {
            positions[x1][3] = new Empty();
            positions[x1][2] = positions[x][y];
            positions[x][y] = new Empty();
            return;
        }
        positions[x1][y1] = positions[x][y];
        positions[x][y] = new Empty();
    }


    public boolean checkIfMoveResultsInCheck(final String moveType, final int x, final int y, final int x1, final int y1, int boardNumber)
    {
        if (checking)
        {
            Piece[] temp1 = positions[boardNumber][0].clone();
            Piece[] temp2 = positions[boardNumber][1].clone();
            Piece[] temp3 = positions[boardNumber][2].clone();
            Piece[] temp4 = positions[boardNumber][3].clone();
            Piece[] temp5 = positions[boardNumber][4].clone();
            Piece[] temp6 = positions[boardNumber][5].clone();
            Piece[] temp7 = positions[boardNumber][6].clone();
            Piece[] temp8 = positions[boardNumber][7].clone();
            Piece[][] tempPositions = {temp1, temp2, temp3, temp4, temp5, temp6, temp7, temp8};
            switchPositions(moveType, tempPositions, x, y, x1, y1);
            if (boardNumber == 0)
            {
                if (whiteTurn1 == 1) if (whiteInCheck(tempPositions, boardNumber)) return true;
                if (whiteTurn1 == 2) if (blackInCheck(tempPositions, boardNumber)) return true;
            }
            else
            {
                if (whiteTurn2 == 1) if (whiteInCheck(tempPositions, boardNumber)) return true;
                if (whiteTurn2 == 2) if (blackInCheck(tempPositions, boardNumber)) return true;
            }
        }

        return false;
    }

    public boolean rosterMoveIsLegal(Piece rosterPiece, int x, int y, int boardNumber)
    {
        if (!placing)
        {
            Piece old = positions[boardNumber][x][y];
            positions[boardNumber][x][y] = rosterPiece;
            if (rosterPiece.color.equals("white"))
            {
                if (blackInCheck(positions[boardNumber], boardNumber))
                {
                    positions[boardNumber][x][y] = old;
                    return false;
                }
            }
            else
            {
                if (whiteInCheck(positions[boardNumber], boardNumber))
                {
                    positions[boardNumber][x][y] = old;
                    return false;
                }
            }
            positions[boardNumber][x][y] = old;
        }
        if (checking)
        {
            Piece old = positions[boardNumber][x][y];
            positions[boardNumber][x][y] = rosterPiece;
            if (rosterPiece.color.equals("white"))
            {
                if (whiteInCheck(positions[boardNumber], boardNumber))
                {
                    positions[boardNumber][x][y] = old;
                    return false;
                }
            }
            else
            {
                if (blackInCheck(positions[boardNumber], boardNumber))
                {
                    positions[boardNumber][x][y] = old;
                    return false;
                }
            }
            positions[boardNumber][x][y] = old;
        }

        return true;

    }


    /**
     * Along with blackInCheck, this requires the positions and boardnumber variable because
     * the positions might be temporary, separate from the class variable
     */
    public static boolean whiteInCheck(Piece[][] positions, int boardNumber)
    {
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                if (positions[i][j].color.equals("white") &&
                        positions[i][j].type.equals("king") &&
                        checkCheck(positions, i, j, boardNumber))
                {
                    return true;
                }
            }
        }
        return false;

    }
    public static boolean blackInCheck(Piece[][] positions,int boardNumber)
    {
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                if (positions[i][j].color.equals("black") &&
                        positions[i][j].type.equals("king") &&
                        checkCheck(positions, i, j, boardNumber))
                {
                    return true;
                }

            }
        }
        return false;
    }

    public static  boolean checkCheck(Piece[][] positions, int x, int y, int boardNumber)
    {
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                if (positions[i][j].isOpposite(positions[x][y]))
                {
                    Set<Move> moves = positions[i][j].getMoves(positions, i, j, boardNumber);
                    for (Move m : moves)
                    {
                        if (m.type.equals("take") && m.x1 == x && m.y1 == y) return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * This method is just for checking if kings can castle
     * If we use the regular check checking method, then there will be a stack overflow
     * king's get moves call check check, which ends up calling king's get moves, etc
     * I think it is extremely unlikely that an opposing king would prevent castling
     */
    public static boolean castleCheckCheck(String color, Piece[][] positions, int x, int y, int boardNumber)
    {
        String oppositeColor = "black";
        if (color.equals("black")) oppositeColor = "white";
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                if (positions[i][j].color.equals(oppositeColor))
                {
                    if (!positions[i][j].type.equals("king"))
                    {
                        Set<Move> moves = positions[i][j].getMoves(positions, i, j, boardNumber);
                        for (Move m : moves)
                        {
                            if (m.type.equals("take") && m.x1 == x && m.y1 == y) return true;
                        }
                    }

                }
            }
        }
        return false;
    }


    public void checkIfKingsStillStanding(int boardNumber)
    {
        boolean player1 = false;
        boolean player2 = false;
        boolean player3 = false;
        boolean player4 = false;

        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                if (boardNumber == 0)
                {
                    if (positions[boardNumber][i][j].color.equals("white") && positions[boardNumber][i][j].type.equals("king"))
                    {
                        player1 = true;
                    }
                    if (positions[boardNumber][i][j].color.equals("black") && positions[boardNumber][i][j].type.equals("king"))
                    {
                        player2 = true;
                    }
                }
                if (boardNumber == 1)
                {
                    if (positions[boardNumber][i][j].color.equals("white") && positions[boardNumber][i][j].type.equals("king"))
                    {
                        player3 = true;
                    }
                    if (positions[boardNumber][i][j].color.equals("black") && positions[boardNumber][i][j].type.equals("king"))
                    {
                        player4 = true;
                    }
                }
            }
        }

        if (boardNumber == 0)
        {
            if (!player2)
            {
                gameEndProcedures(0, 1);
            }
            if (!player1)
            {
                gameEndProcedures(1, 1);
            }
        }
        if (boardNumber == 1)
        {
            if (!player4)
            {
                gameEndProcedures(1, 1);
            }
            if (!player3)
            {
                gameEndProcedures(0, 1);
            }
        }


    }

    public void updateLegalCastlingVariables(int boardNumber)
    {
        if (boardNumber == 0)
        {
            if (!positions[boardNumber][0][0].color.equals("white") || !positions[boardNumber][0][0].type.equals("rook"))
            {
                whiteCastleQueen1 = false;
            }
            if (!positions[boardNumber][7][0].color.equals("white") || !positions[boardNumber][7][0].type.equals("rook"))
            {
                whiteCastleKing1 = false;
            }
            if (!positions[boardNumber][4][0].color.equals("white") || !positions[boardNumber][4][0].type.equals("king"))
            {
                whiteCastleKing1 = false;
                whiteCastleQueen1 = false;
            }
            if (!positions[boardNumber][0][7].color.equals("black") || !positions[boardNumber][0][7].type.equals("rook"))
            {
                blackCastleQueen1 = false;
            }
            if (!positions[boardNumber][7][7].color.equals("black") || !positions[boardNumber][7][7].type.equals("rook"))
            {
                blackCastleKing1 = false;
            }
            if (!positions[boardNumber][4][7].color.equals("black") || !positions[boardNumber][4][7].type.equals("king"))
            {
                blackCastleKing1 = false;
                blackCastleQueen1 = false;
            }
        }
        else
        {
            if (!positions[boardNumber][0][0].color.equals("white") || !positions[boardNumber][0][0].type.equals("rook"))
            {
                whiteCastleQueen2 = false;
            }
            if (!positions[boardNumber][7][0].color.equals("white") || !positions[boardNumber][7][0].type.equals("rook"))
            {
                whiteCastleKing2 = false;
            }
            if (!positions[boardNumber][4][0].color.equals("white") || !positions[boardNumber][4][0].type.equals("king"))
            {
                whiteCastleKing2 = false;
                whiteCastleQueen2 = false;
            }
            if (!positions[boardNumber][0][7].color.equals("black") || !positions[boardNumber][0][7].type.equals("rook"))
            {
                blackCastleQueen2 = false;
            }
            if (!positions[boardNumber][7][7].color.equals("black") || !positions[boardNumber][7][7].type.equals("rook"))
            {
                blackCastleKing2 = false;
            }
            if (!positions[boardNumber][4][7].color.equals("black") || !positions[boardNumber][4][7].type.equals("king"))
            {
                blackCastleKing2 = false;
                blackCastleQueen2 = false;
            }
        }
    }

    public void checkForCheckmate(String color, int boardNumber)
    {
        boolean checkmate = true;

        for (int x = 0; x < 8; x++)
        {
            for (int y = 0; y < 8; y++)
            {
                if (positions[boardNumber][x][y].color.equals(color))
                {
                    Set<Move> moves = positions[boardNumber][x][y].getMoves(positions[boardNumber], x, y, boardNumber);
                    for (Move m : moves)
                    {
                        if (!checkIfMoveResultsInCheck(m.type,x,y,m.x1,m.y1,boardNumber))
                        {
                            checkmate = false;
                            break;
                        }
                    }
                }
            }
        }

        boolean piecesOnRoster = false;
        Piece[] roster = null;
        if (boardNumber == 0 && color.equals("white")) roster = roster1p;
        if (boardNumber == 0 && color.equals("black")) roster = roster2p;
        if (boardNumber == 1 && color.equals("white")) roster = roster4p;
        if (boardNumber == 1 && color.equals("black")) roster = roster3p;

        for (int i = 0; i < 30; i++)
        {
            if (!roster[i].empty)
            {
                piecesOnRoster = true;
                Set<Move> moves = roster[i].getRosterMoves(positions[boardNumber], roster, i);
                for (Move m : moves)
                {
                    if (!rosterMoveIsLegal(roster[i],m.x1,m.y1,boardNumber))
                    {
                        checkmate = false;
                        break;
                    }
                }
            }
        }
        if (!piecesOnRoster)
        {
            roster[0] = new Queen(color);
            Set<Move> moves = roster[0].getRosterMoves(positions[boardNumber], roster, 0);
            for (Move m : moves)
            {
                if (!rosterMoveIsLegal(roster[0],m.x1,m.y1,boardNumber))
                {
                    checkmate = false;
                    break;
                }
            }
            roster[0] = new Empty();
        }

        if (checkmate)
        {
            if (boardNumber == 0 && color.equals("white")) gameEndProcedures(1, 0);
            if (boardNumber == 0 && color.equals("black")) gameEndProcedures(0, 0);
            if (boardNumber == 1 && color.equals("white")) gameEndProcedures(0, 0);
            if (boardNumber == 1 && color.equals("black")) gameEndProcedures(1, 0);
        }
    }

    private void gameEndProcedures(int x, int y)
    {

    }

    public Piece[] getCurrentRosterArray(int boardNumber)
    {
        if (boardNumber == 0)
        {
            if (whiteTurn1 == 1) return roster1p;
            if (whiteTurn1 == 2) return roster2p;
        }
        if (boardNumber == 1)
        {
            if (whiteTurn1 == 1) return roster4p;
            if (whiteTurn1 == 2) return roster3p;
        }
        return null;
    }



}
