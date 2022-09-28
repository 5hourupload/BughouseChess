package fhu.bughousechess.pieces

import android.widget.ImageView

import fhu.bughousechess.R
import fhu.bughousechess.MainActivity
import fhu.bughousechess.Move
import java.util.HashSet

class King(color: String?) : Piece() {
    override val resID: Int
        get() = if (color == "white") R.mipmap.king else R.mipmap.bking

    override fun getMoves(
        board: Array<Array<ImageView?>?>?,
        positions: Array<Array<Piece?>?>?,
        x: Int,
        y: Int
    ): Set<Move?>? {
        val moves: MutableSet<Move?> = HashSet()
        if (x + 1 < 8) {
            if (positions!![x + 1]!![y]!!.empty) {
                moves.add(Move(board, positions, x, y, x + 1, y, "move"))
                if (positions[6]!![0]!!.empty && positions[4]!![0]!!.type == "king" && positions[4]!![0]!!.color == "white") {
                    //setting those empty positions to "white" so that they can be checked whether
                    //they are in check or not
                    if (MainActivity.board(board, positions) == 1) {
                        if (MainActivity.whiteCastleKing1 && positions[6]!![0]!!.empty && !MainActivity.castleCheckCheck(
                                "white",
                                board,
                                positions,
                                4,
                                0
                            ) && !MainActivity.castleCheckCheck(
                                "white",
                                board,
                                positions,
                                5,
                                0
                            ) && !MainActivity.castleCheckCheck("white", board, positions, 6, 0)
                        ) {
                            moves.add(Move(board, positions, x, y, 6, 0, "whiteKingCastle"))
                        }
                    } else {
                        if (MainActivity.whiteCastleKing2 && positions[6]!![0]!!.empty && !MainActivity.castleCheckCheck(
                                "white",
                                board,
                                positions,
                                4,
                                0
                            ) && !MainActivity.castleCheckCheck(
                                "white",
                                board,
                                positions,
                                5,
                                0
                            ) && !MainActivity.castleCheckCheck("white", board, positions, 6, 0)
                        ) {
                            moves.add(Move(board, positions, x, y, 6, 0, "whiteKingCastle"))
                        }
                    }
                }
                if (positions[6]!![7]!!.empty && positions[4]!![7]!!.type == "king" && positions[4]!![7]!!.color == "black") {
                    //setting those empty positions to "white" so that they can be checked whether
                    //they are in check or not
                    if (MainActivity.board(board, positions) == 1) {
                        if (MainActivity.whiteCastleKing1 && positions[6]!![7]!!.empty && !MainActivity.castleCheckCheck(
                                "black",
                                board,
                                positions,
                                4,
                                7
                            ) && !MainActivity.castleCheckCheck(
                                "black",
                                board,
                                positions,
                                5,
                                7
                            ) && !MainActivity.castleCheckCheck("black", board, positions, 6, 7)
                        ) {
                            moves.add(Move(board, positions, x, y, 6, 7, "blackKingCastle"))
                        }
                    } else {
                        if (MainActivity.whiteCastleKing2 && positions[6]!![7]!!.empty && !MainActivity.castleCheckCheck(
                                "black",
                                board,
                                positions,
                                4,
                                7
                            ) && !MainActivity.castleCheckCheck(
                                "black",
                                board,
                                positions,
                                5,
                                7
                            ) && !MainActivity.castleCheckCheck("black", board, positions, 6, 7)
                        ) {
                            moves.add(Move(board, positions, x, y, 6, 7, "blackKingCastle"))
                        }
                    }
                }
            }
            if (positions[x + 1]!![y]!!.isOpposite(this)) {
                moves.add(Move(board, positions, x, y, x + 1, y, "take"))
            }
            if (y + 1 < 8) {
                if (positions[x + 1]!![y + 1]!!.empty) {
                    moves.add(Move(board, positions, x, y, x + 1, y + 1, "move"))
                }
                if (positions[x + 1]!![y + 1]!!.isOpposite(this)) {
                    moves.add(Move(board, positions, x, y, x + 1, y + 1, "take"))
                }
            }
            if (y - 1 > -1) {
                if (positions[x + 1]!![y - 1]!!.empty) {
                    moves.add(Move(board, positions, x, y, x + 1, y - 1, "move"))
                }
                if (positions[x + 1]!![y - 1]!!.isOpposite(this)) {
                    moves.add(Move(board, positions, x, y, x + 1, y - 1, "take"))
                }
            }
        }
        if (x - 1 > -1) {
            if (positions!![x - 1]!![y]!!.empty) {
                moves.add(Move(board, positions, x, y, x - 1, y, "move"))
                if (positions[2]!![0]!!.empty && positions[1]!![0]!!.empty && positions[4]!![0]!!.type == "king") {
                    if (MainActivity.board(board, positions) == 1) {
                        if (MainActivity.whiteCastleQueen1 && positions[2]!![0]!!.empty && !MainActivity.castleCheckCheck(
                                "white",
                                board,
                                positions,
                                1,
                                0
                            ) && !MainActivity.castleCheckCheck(
                                "white",
                                board,
                                positions,
                                2,
                                0
                            ) && !MainActivity.castleCheckCheck(
                                "white",
                                board,
                                positions,
                                3,
                                0
                            ) && !MainActivity.castleCheckCheck("white", board, positions, 4, 0)
                        ) {
                            moves.add(Move(board, positions, x, y, 2, 0, "whiteQueenCastle"))
                        }
                    } else {
                        if (MainActivity.whiteCastleQueen2 && positions[2]!![0]!!.empty && !MainActivity.castleCheckCheck(
                                "white",
                                board,
                                positions,
                                1,
                                0
                            ) && !MainActivity.castleCheckCheck(
                                "white",
                                board,
                                positions,
                                2,
                                0
                            ) && !MainActivity.castleCheckCheck(
                                "white",
                                board,
                                positions,
                                3,
                                0
                            ) && !MainActivity.castleCheckCheck("white", board, positions, 4, 0)
                        ) {
                            moves.add(Move(board, positions, x, y, 2, 0, "whiteQueenCastle"))
                        }
                    }
                }
                if (positions[2]!![7]!!.empty && positions[1]!![7]!!.empty && positions[4]!![7]!!.type == "king") {
                    if (MainActivity.board(board, positions) == 1) {
                        if (MainActivity.whiteCastleQueen1 && positions[2]!![7]!!.empty && !MainActivity.castleCheckCheck(
                                "black",
                                board,
                                positions,
                                1,
                                7
                            ) && !MainActivity.castleCheckCheck(
                                "black",
                                board,
                                positions,
                                2,
                                7
                            ) && !MainActivity.castleCheckCheck(
                                "black",
                                board,
                                positions,
                                3,
                                7
                            ) && !MainActivity.castleCheckCheck("black", board, positions, 4, 7)
                        ) {
                            moves.add(Move(board, positions, x, y, 2, 7, "blackQueenCastle"))
                        }
                    } else {
                        if (MainActivity.whiteCastleQueen2 && positions[2]!![7]!!.empty && !MainActivity.castleCheckCheck(
                                "black",
                                board,
                                positions,
                                1,
                                7
                            ) && !MainActivity.castleCheckCheck(
                                "black",
                                board,
                                positions,
                                2,
                                7
                            ) && !MainActivity.castleCheckCheck(
                                "black",
                                board,
                                positions,
                                3,
                                7
                            ) && !MainActivity.castleCheckCheck("black", board, positions, 4, 7)
                        ) {
                            moves.add(Move(board, positions, x, y, 2, 7, "blackQueenCastle"))
                        }
                    }
                }
            }
            if (positions[x - 1]!![y]!!.isOpposite(this)) {
                moves.add(Move(board, positions, x, y, x - 1, y, "take"))
            }
            if (y + 1 < 8) {
                if (positions[x - 1]!![y + 1]!!.empty) {
                    moves.add(Move(board, positions, x, y, x - 1, y + 1, "move"))
                }
                if (positions[x - 1]!![y + 1]!!.isOpposite(this)) {
                    moves.add(Move(board, positions, x, y, x - 1, y + 1, "take"))
                }
            }
            if (y - 1 > -1) {
                if (positions[x - 1]!![y - 1]!!.empty) {
                    moves.add(Move(board, positions, x, y, x - 1, y - 1, "move"))
                }
                if (positions[x - 1]!![y - 1]!!.isOpposite(this)) {
                    moves.add(Move(board, positions, x, y, x - 1, y - 1, "take"))
                }
            }
        }
        if (y + 1 < 8) {
            if (positions!![x]!![y + 1]!!.empty) {
                moves.add(Move(board, positions, x, y, x, y + 1, "move"))
            }
            if (positions[x]!![y + 1]!!.isOpposite(this)) {
                moves.add(Move(board, positions, x, y, x, y + 1, "take"))
            }
        }
        if (y - 1 > -1) {
            if (positions!![x]!![y - 1]!!.empty) {
                moves.add(Move(board, positions, x, y, x, y - 1, "move"))
            }
            if (positions[x]!![y - 1]!!.isOpposite(this)) {
                moves.add(Move(board, positions, x, y, x, y - 1, "take"))
            }
        }
        return moves
    }

    init {
        this.color = color
        type = "king"
        wasPawn = false
        backgroundColor = ""
        empty = false
    }
}