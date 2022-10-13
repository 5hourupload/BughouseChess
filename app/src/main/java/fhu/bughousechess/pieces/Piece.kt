package fhu.bughousechess.pieces

import fhu.bughousechess.Move
import java.util.HashSet

abstract class Piece {
    @JvmField
    var color: String? = null
    @JvmField
    var type: String? = null
    @JvmField
    var wasPawn = false
    @JvmField
    var empty = true
    abstract fun getMoves(
        positions: Array<Array<Piece>>,
        x: Int,
        y: Int,
        boardNumber: Int
    ): Set<Move?>?

    fun isOpposite(piece: Piece): Boolean {
        if (color == "white") return piece.color == "black"
        return if (color == "black") piece.color == "white" else false
    }

    open fun getRosterMoves(positions: Array<Array<Piece>>, rosterp: Array<Piece>, i: Int): Set<Move> {
        val moves: MutableSet<Move> = HashSet()
        for (x in 0..7) {
            for (y in 0..7) {
                if (positions[x][y].empty) {
                    moves.add(Move(positions, rosterp, i, x, y, "roster"))
                }
            }
        }
        return moves
    }
}