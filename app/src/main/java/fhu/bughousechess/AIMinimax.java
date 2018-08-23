package fhu.bughousechess;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AIMinimax
{
    private Map<String, Integer> pieceValues = new HashMap<>();
    private int highestValue = -9999;
    private Move bestMove = null;

    public AIMinimax(String color, ImageView[][] board, Piece[][] positions, Piece[] roster1, Piece[] roster2)
    {
        pieceValues.put("pawn", 1);
        pieceValues.put("rook", 5);
        pieceValues.put("knight", 3);
        pieceValues.put("bishop", 3);
        pieceValues.put("queen", 9);
        pieceValues.put("king", 0);

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
            Piece[] temp1 = positions[0].clone();
            Piece[] temp2 = positions[1].clone();
            Piece[] temp3 = positions[2].clone();
            Piece[] temp4 = positions[3].clone();
            Piece[] temp5 = positions[4].clone();
            Piece[] temp6 = positions[5].clone();
            Piece[] temp7 = positions[6].clone();
            Piece[] temp8 = positions[7].clone();
            Piece[][] tempPositions = {temp1, temp2, temp3, temp4, temp5, temp6, temp7, temp8};
            int x = m.x;
            int y = m.y;
            int x1 = m.x1;
            int y1 = m.y1;
            MainActivity.switchPositions(moveType, tempPositions, x, y, x1, y1);
//            int value = getBoardValue(tempPositions, color, getOppositeColor(color));
            int value = findMinOrMax(color,board,tempPositions,roster1,roster2,0);
            if (value > highestValue)
            {
                System.out.println("value: " + value);
                highestValue = value;
                bestMove = m;
            }
        }
//        MainActivity.switchPositions(bestMove.type, positions, bestMove.x, bestMove.y, bestMove.x1, bestMove.y1);
//
//        int score = findMinOrMax(oppositeColor, board, positions,roster1,roster2,depth+1);
//
//        findMinOrMax(color, board, position, roster1,roster2,0);

    }
    private int findMinOrMax(String color, ImageView[][] board, Piece[][] positions,
                              Piece[] roster1, Piece[] roster2, int depth)
    {

        if (depth > 3) return getBoardValue(positions,color,getOppositeColor(color));

        int highest = -9999;
        Move bestMove = null;
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
            Piece[] temp1 = positions[0].clone();
            Piece[] temp2 = positions[1].clone();
            Piece[] temp3 = positions[2].clone();
            Piece[] temp4 = positions[3].clone();
            Piece[] temp5 = positions[4].clone();
            Piece[] temp6 = positions[5].clone();
            Piece[] temp7 = positions[6].clone();
            Piece[] temp8 = positions[7].clone();
            Piece[][] tempPositions = {temp1, temp2, temp3, temp4, temp5, temp6, temp7, temp8};
            int x = m.x;
            int y = m.y;
            int x1 = m.x1;
            int y1 = m.y1;

            MainActivity.switchPositions(moveType, tempPositions, x, y, x1, y1);
            int value = getBoardValue(tempPositions, color, getOppositeColor(color));
            if (value > highest)
            {
                highest = value;
                bestMove = m;
            }
        }
        MainActivity.switchPositions(bestMove.type, positions, bestMove.x, bestMove.y, bestMove.x1, bestMove.y1);

        int score = findMinOrMax(getOppositeColor(color), board, positions,roster1,roster2,depth+1);
        return score;
    }

    private int getBoardValue(Piece[][] position, String posColor, String negColor)
    {
        int total = 0;
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                if (!position[i][j].empty)
                {
                    if (position[i][j].color.equals(posColor))
                        total+=pieceValues.get(position[i][j].type);
                    else if (position[i][j].color.equals(negColor))
                        total-=pieceValues.get(position[i][j].type);
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
}