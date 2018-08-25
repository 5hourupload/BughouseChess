package fhu.bughousechess;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static fhu.bughousechess.MainActivity.blackInCheck;
import static fhu.bughousechess.MainActivity.checking;
import static fhu.bughousechess.MainActivity.whiteInCheck;

public class AIMinimax
{
    private Map<String, Integer> pieceValues = new HashMap<>();
    private double highestValue = -999;
    private Move bestMove = null;
    private int count = 0;

    public AIMinimax(String color, ImageView[][] board, Piece[][] positions, ImageView[] roster1,
                     Piece[] roster1p, ImageView[] roster2, Piece[] roster2p)
    {
        pieceValues.put("pawn", 1);
        pieceValues.put("rook", 5);
        pieceValues.put("knight", 3);
        pieceValues.put("bishop", 3);
        pieceValues.put("queen", 9);
        pieceValues.put("king", 420);

        Set<Move> regMoves = new HashSet<>();
        Set<Move> rosterMoves = new HashSet<>();
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                if (positions[i][j].color.equals(color))
                {
                    regMoves.addAll(positions[i][j].getMoves(board,positions,i,j));
                }
            }
        }
//        for (int i = 0; i < 30; i++)
//        {
//            if (!roster1p[i].empty)
//            {
//                rosterMoves.addAll(roster1p[i].getRosterMoves(board, positions,roster1, roster1p,i));
//            }
//        }
        for (Move m : regMoves)
        {
            String moveType = m.type;
            Piece[][] tempPositions = getArrayClone(positions);
            int x = m.x;
            int y = m.y;
            int x1 = m.x1;
            int y1 = m.y1;
            MainActivity.switchPositions(moveType, tempPositions, x, y, x1, y1);
            if (checking)
            {
                if (color.equals("white"))
                {
                    if (whiteInCheck(board,tempPositions)) continue;
                }
                if (color.equals("black"))
                {
                    if (blackInCheck(board,tempPositions)) continue;
                }
            }

            double value = findMax(color,board,tempPositions,0);
            if (value > highestValue)
            {
                highestValue = value;
                bestMove = m;
            }
        }
//        for (Move m : rosterMoves)
//        {
//            Piece[][] tempPositions = getArrayClone(positions);
//            int x = m.x;
//            int x1 = m.x1;
//            int y1 = m.y1;
//            Piece[] tempRoster = roster1p.clone();
//            tempPositions[x1][y1] = tempRoster[x];
//            tempRoster[x] = new Empty();
//            if (checking)
//            {
//                if (color.equals("white"))
//                {
//                    if (whiteInCheck(board,tempPositions)) continue;
//                }
//                if (color.equals("black"))
//                {
//                    if (blackInCheck(board,tempPositions)) continue;
//                }
//            }
//
//            int value = findMinOrMax(color,board,tempPositions,roster1p,roster2p,0);
//            if (value > highestValue)
//            {
//                highestValue = value;
//                bestMove = m;
//            }
//        }
//        MainActivity.switchPositions(bestMove.type, positions, bestMove.x, bestMove.y, bestMove.x1, bestMove.y1);
//
//        int score = findMinOrMax(oppositeColor, board, positions,roster1,roster2,depth+1);
//
//        findMinOrMax(color, board, position, roster1,roster2,0);

    }
    private double findMax(String color, ImageView[][] board, Piece[][] positions, int depth)
    {
        if (depth > 1) {
            return getBoardValue(positions,color,getOppositeColor(color));
        }
        double highest = -999;
        Set<Move> allMoves = new HashSet<>();
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                if (positions[i][j].color.equals(color))
                {
                    allMoves.addAll(positions[i][j].getMoves(board,positions,i,j));
                }
            }
        }
        for (Move m : allMoves)
        {
            String moveType = m.type;
            Piece[][] tempPositions = getArrayClone(positions);
            int x = m.x;
            int y = m.y;
            int x1 = m.x1;
            int y1 = m.y1;

            MainActivity.switchPositions(moveType, tempPositions, x, y, x1, y1);
            //only gonna check for checking in this first iteration, might be important
            //do so in the recursive function?
            if (checking)
            {
                if (color.equals("white"))
                {
                    if (whiteInCheck(board,tempPositions)) continue;
                }
                if (color.equals("black"))
                {
                    if (blackInCheck(board,tempPositions)) continue;
                }
            }
            double value = findMin(color, board, positions,depth+1);
            if (value > highest)
            {
                highest = value;
            }
        }
        return highest;
    }

    private double findMin(String color, ImageView[][] board, Piece[][] positions, int depth)
    {
        if (depth > 1)
        {
            return getBoardValue(positions, color, getOppositeColor(color));
        }
        double lowest = 999;
        Set<Move> allMoves = new HashSet<>();
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                if (positions[i][j].color.equals(color))
                {
                    allMoves.addAll(positions[i][j].getMoves(board,positions,i,j));
                }
            }
        }
        for (Move m : allMoves)
        {
            String moveType = m.type;
            Piece[][] tempPositions = getArrayClone(positions);
            int x = m.x;
            int y = m.y;
            int x1 = m.x1;
            int y1 = m.y1;

            MainActivity.switchPositions(moveType, tempPositions, x, y, x1, y1);
            //only gonna check for checking in this first iteration, might be important
            //do so in the recursive function?
            if (checking)
            {
                if (color.equals("white"))
                {
                    if (whiteInCheck(board,tempPositions)) continue;
                }
                if (color.equals("black"))
                {
                    if (blackInCheck(board,tempPositions)) continue;
                }
            }
            double value = findMax(color, board, positions,depth+1);
            if (value < lowest)
            {
                lowest = value;
            }
        }
        return lowest;
    }

    private double getBoardValue(Piece[][] position, String posColor, String negColor)
    {
        double total = 0;
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                if (!position[i][j].empty)
                {
                    if (position[i][j].color.equals(posColor))
                    {
                        total+=pieceValues.get(position[i][j].type);
                        total+=(Math.abs(3.5-Math.hypot(i-3.5, j-3.5)))/100.0;
                    }
                    else if (position[i][j].color.equals(negColor))
                    {
                        total-=pieceValues.get(position[i][j].type);
                        total-=(Math.abs(3.5-Math.hypot(i-3.5, j-3.5)))/100.0;
                    }
                    else System.out.println("SHOULDNT BE HERE");
                }
            }
        }
        return total;
    }
    private String getOppositeColor(String color)
    {
        String oppositeColor;
        if (color.equals("white")) oppositeColor = "black";
        else oppositeColor = "white";
        return oppositeColor;
    }
    public Move getBestMove()
    {
        return bestMove;
    }

    private Piece[][] getArrayClone(Piece[][] positions)
    {
        Piece[] temp1 = positions[0].clone();
        Piece[] temp2 = positions[1].clone();
        Piece[] temp3 = positions[2].clone();
        Piece[] temp4 = positions[3].clone();
        Piece[] temp5 = positions[4].clone();
        Piece[] temp6 = positions[5].clone();
        Piece[] temp7 = positions[6].clone();
        Piece[] temp8 = positions[7].clone();
        Piece[][] tempPositions = {temp1, temp2, temp3, temp4, temp5, temp6, temp7, temp8};
        return tempPositions;

    }
}