package fhu.bughousechess.pieces

import android.widget.ImageView
import fhu.bughousechess.Move
import fhu.bughousechess.pieces.Piece
import java.util.HashSet

abstract class Piece {
    @JvmField
    var color: String? = null
    @JvmField
    var type: String? = null
    @JvmField
    var wasPawn = false
    @JvmField
    var backgroundColor: String? = null
    @JvmField
    var empty = true
    var onRoster = false
    abstract val resID: Int
    abstract fun getMoves(
        board: Array<Array<ImageView?>?>?,
        positions: Array<Array<Piece?>?>?,
        x: Int,
        y: Int
    ): Set<Move?>?

    fun isOpposite(piece: Piece): Boolean {
        if (color == "white") return piece.color == "black"
        return if (color == "black") piece.color == "white" else false
    }

    open fun getRosterMoves(
        board: Array<Array<ImageView?>?>?,
        positions: Array<Array<Piece>>,
        roster: Array<ImageView?>?,
        rosterp: Array<Piece?>?,
        i: Int
    ): Set<Move>? {
        val moves: MutableSet<Move> = HashSet()
        for (x in 0..7) {
            for (y in 0..7) {
                if (positions[x][y].empty) {
                    moves.add(Move(board, positions, roster, rosterp, i, x, y, "roster"))
                }
            }
        }
        return moves
    }
}