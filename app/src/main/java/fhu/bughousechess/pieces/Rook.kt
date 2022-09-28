package fhu.bughousechess.pieces

import android.widget.ImageView
import fhu.bughousechess.Move
import fhu.bughousechess.R
import java.util.HashSet

class Rook(color: String?) : Piece() {
    override val resID: Int
        get() = if (color == "white") R.mipmap.rook else R.mipmap.brook

    override fun getMoves(
        board: Array<Array<ImageView?>?>?,
        positions: Array<Array<Piece?>?>?,
        x: Int,
        y: Int
    ): Set<Move?>? {
        val moves: MutableSet<Move?> = HashSet()
        var inbetween = false
        for (i in y + 1..7) {
            for (j in y + 1 until i) {
                if (!positions!![x]!![j]!!.empty) {
                    inbetween = true
                }
            }
            if (positions!![x]!![i]!!.empty) {
                if (!inbetween) {
                    moves.add(Move(board, positions, x, y, x, i, "move"))
                }
            }
            if (positions[x]!![i]!!.isOpposite(this)) {
                if (!inbetween) {
                    moves.add(Move(board, positions, x, y, x, i, "take"))
                }
            }
        }
        inbetween = false
        for (i in y - 1 downTo -1 + 1) {
            for (j in y - 1 downTo i + 1) {
                if (!positions!![x]!![j]!!.empty) {
                    inbetween = true
                }
            }
            if (positions!![x]!![i]!!.empty) {
                if (!inbetween) {
                    moves.add(Move(board, positions, x, y, x, i, "move"))
                }
            }
            if (positions[x]!![i]!!.isOpposite(this)) {
                if (!inbetween) {
                    moves.add(Move(board, positions, x, y, x, i, "take"))
                }
            }
        }
        inbetween = false
        for (i in x + 1..7) {
            for (j in x + 1 until i) {
                if (!positions!![j]!![y]!!.empty) {
                    inbetween = true
                }
            }
            if (positions!![i]!![y]!!.empty) {
                if (!inbetween) {
                    moves.add(Move(board, positions, x, y, i, y, "move"))
                }
            }
            if (positions[i]!![y]!!.isOpposite(this)) {
                if (!inbetween) {
                    moves.add(Move(board, positions, x, y, i, y, "take"))
                }
            }
        }
        inbetween = false
        for (i in x - 1 downTo -1 + 1) {
            for (j in x - 1 downTo i + 1) {
                if (!positions!![j]!![y]!!.empty) {
                    inbetween = true
                }
            }
            if (positions!![i]!![y]!!.empty) {
                if (!inbetween) {
                    moves.add(Move(board, positions, x, y, i, y, "move"))
                }
            }
            if (positions[i]!![y]!!.isOpposite(this)) {
                if (!inbetween) {
                    moves.add(Move(board, positions, x, y, i, y, "take"))
                }
            }
        }
        return moves
    }

    init {
        this.color = color
        type = "rook"
        wasPawn = false
        backgroundColor = ""
        empty = false
    }
}