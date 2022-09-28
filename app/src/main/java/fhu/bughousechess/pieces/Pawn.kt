package fhu.bughousechess.pieces

import android.widget.ImageView

import fhu.bughousechess.R
import fhu.bughousechess.MainActivity
import fhu.bughousechess.Move
import java.util.HashSet

class Pawn(color: String?) : Piece() {
    override val resID: Int
        get() = if (color == "white") R.mipmap.pawn else R.mipmap.bpawn

    override fun getMoves(
        board: Array<Array<ImageView?>?>?,
        positions: Array<Array<Piece?>?>?,
        x: Int,
        y: Int
    ): Set<Move?>? {
        val moves: MutableSet<Move?> = HashSet()
        if (color == "white") {
            if (y < 7) {
                if (positions!![x]!![y + 1]!!.empty) {
                    moves.add(Move(board, positions, x, y, x, y + 1, "move"))
                    if (y == 1) {
                        if (positions[x]!![y + 2]!!.empty) {
                            moves.add(Move(board, positions, x, y, x, y + 2, "move"))
                        }
                    }
                }
            }
            if (x < 7 && y < 7) {
                if (positions!![x + 1]!![y + 1]!!.isOpposite(this)) {
                    moves.add(Move(board, positions, x, y, x + 1, y + 1, "take"))
                }
                if (y == 4 && positions[x + 1]!![y]!!.color == "black" && positions[x + 1]!![y]!!.type == "pawn") {
                    if (MainActivity.board(board, positions) == 1) {
                        if (MainActivity.enP[x + 1][1].substring(
                                0,
                                1
                            ) == "1" && MainActivity.enP[x + 1][1].substring(
                                1,
                                MainActivity.enP[x + 1][1].length
                            ) == Integer.toString(MainActivity.board1Turn)
                        ) {
                            moves.add(Move(board, positions, x, 4, x + 1, 4, "whiteEnP"))
                        }
                    }
                    if (MainActivity.board(board, positions) == 2) {
                        if (MainActivity.enP[x + 1][3].substring(
                                0,
                                1
                            ) == "1" && MainActivity.enP[x + 1][3].substring(
                                1,
                                MainActivity.enP[x + 1][3].length
                            ) == Integer.toString(MainActivity.board2Turn)
                        ) {
                            moves.add(Move(board, positions, x, 4, x + 1, 4, "whiteEnP"))
                        }
                    }
                }
            }
            if (x > 0 && y < 7) {
                if (positions!![x - 1]!![y + 1]!!.isOpposite(this)) {
                    moves.add(Move(board, positions, x, y, x - 1, y + 1, "take"))
                }
                if (y == 4 && positions[x - 1]!![y]!!.color == "black" && positions[x - 1]!![y]!!.type == "pawn") {
                    if (MainActivity.board(board, positions) == 1) {
                        if (MainActivity.enP[x - 1][1].substring(
                                0,
                                1
                            ) == "1" && MainActivity.enP[x - 1][1].substring(
                                1,
                                MainActivity.enP[x - 1][1].length
                            ) == Integer.toString(MainActivity.board1Turn)
                        ) {
                            moves.add(Move(board, positions, x, 4, x - 1, 4, "whiteEnP"))
                        }
                    }
                    if (MainActivity.board(board, positions) == 2) {
                        if (MainActivity.enP[x - 1][3].substring(
                                0,
                                1
                            ) == "1" && MainActivity.enP[x - 1][3].substring(
                                1,
                                MainActivity.enP[x - 1][3].length
                            ) == Integer.toString(MainActivity.board2Turn)
                        ) {
                            moves.add(Move(board, positions, x, 4, x - 1, 4, "whiteEnP"))
                        }
                    }
                }
            }
        } else {
            if (y > 0) {
                if (positions!![x]!![y - 1]!!.empty) {
                    moves.add(Move(board, positions, x, y, x, y - 1, "move"))
                    if (y == 6) {
                        if (positions[x]!![y - 2]!!.empty) {
                            moves.add(Move(board, positions, x, y, x, y - 2, "move"))
                        }
                    }
                }
            }
            if (x < 7 && y > 0) {
                if (positions!![x + 1]!![y - 1]!!.isOpposite(this)) {
                    moves.add(Move(board, positions, x, y, x + 1, y - 1, "take"))
                }
                if (y == 3 && positions[x + 1]!![y]!!.color == "white" && positions[x + 1]!![y]!!.type == "pawn") {
                    if (MainActivity.board(board, positions) == 1) {
                        if (MainActivity.enP[x + 1][0].substring(
                                0,
                                1
                            ) == "1" && MainActivity.enP[x + 1][0].substring(
                                1,
                                MainActivity.enP[x + 1][0].length
                            ) == Integer.toString(MainActivity.board1Turn)
                        ) {
                            moves.add(Move(board, positions, x, 3, x + 1, 3, "blackEnP"))
                        }
                    }
                    if (MainActivity.board(board, positions) == 2) {
                        if (MainActivity.enP[x + 1][2].substring(
                                0,
                                1
                            ) == "1" && MainActivity.enP[x + 1][2].substring(
                                1,
                                MainActivity.enP[x + 1][2].length
                            ) == Integer.toString(MainActivity.board2Turn)
                        ) {
                            moves.add(Move(board, positions, x, 3, x + 1, 3, "blackEnP"))
                        }
                    }
                }
            }
            if (x > 0 && y > 0) {
                if (positions!![x - 1]!![y - 1]!!.isOpposite(this)) {
                    moves.add(Move(board, positions, x, y, x - 1, y - 1, "take"))
                }
                if (y == 3 && positions[x - 1]!![y]!!.color == "white" && positions[x - 1]!![y]!!.type == "pawn") {
                    if (MainActivity.board(board, positions) == 1) {
                        if (MainActivity.enP[x - 1][0].substring(
                                0,
                                1
                            ) == "1" && MainActivity.enP[x - 1][0].substring(
                                1,
                                MainActivity.enP[x - 1][0].length
                            ) == Integer.toString(MainActivity.board1Turn)
                        ) {
                            moves.add(Move(board, positions, x, 3, x - 1, 3, "blackEnP"))
                        }
                    }
                    if (MainActivity.board(board, positions) == 2) {
                        if (MainActivity.enP[x - 1][2].substring(
                                0,
                                1
                            ) == "1" && MainActivity.enP[x - 1][2].substring(
                                1,
                                MainActivity.enP[x - 1][2].length
                            ) == Integer.toString(MainActivity.board2Turn)
                        ) {
                            moves.add(Move(board, positions, x, 3, x - 1, 3, "blackEnP"))
                        }
                    }
                }
            }
        }
        return moves
    }

    override fun getRosterMoves(
        board: Array<Array<ImageView?>?>?,
        positions: Array<Array<Piece>>,
        roster: Array<ImageView?>?,
        rosterp: Array<Piece?>?,
        i: Int
    ): Set<Move>? {
        val moves: MutableSet<Move> = HashSet()
        for (x in 0..7) {
            if (MainActivity.firstrank) {
                if (rosterp!![i]!!.color == "white") {
                    for (y in 0..6) {
                        if (positions[x][y].empty) {
                            moves.add(Move(board, positions, roster, rosterp, i, x, y, "roster"))
                        }
                    }
                }
                if (rosterp[i]!!.color == "black") {
                    for (y in 1..7) {
                        if (positions[x][y].empty) {
                            moves.add(Move(board, positions, roster, rosterp, i, x, y, "roster"))
                        }
                    }
                }
            } else {
                for (y in 1..6) {
                    if (positions[x][y].empty) {
                        moves.add(Move(board, positions, roster, rosterp, i, x, y, "roster"))
                    }
                }
            }
        }
        return moves
    }

    init {
        this.color = color
        type = "pawn"
        wasPawn = true
        backgroundColor = ""
        empty = false
    }
}