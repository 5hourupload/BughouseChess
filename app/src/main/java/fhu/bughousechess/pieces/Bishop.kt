package fhu.bughousechess.pieces

import android.widget.ImageView
import fhu.bughousechess.Move

import fhu.bughousechess.R
import java.util.HashSet

class Bishop(color: String?) : Piece() {
    override val resID: Int
        get() = if (color == "white") R.mipmap.bishop else R.mipmap.bbishop

    override fun getMoves(
        board: Array<Array<ImageView?>?>?,
        positions: Array<Array<Piece?>?>?,
        x: Int,
        y: Int
    ): Set<Move?>? {
        val moves: MutableSet<Move?> = HashSet()
        var inbetween = false
        var z = x
        if (y > x) {
            z = y
        }
        for (i in 1 until 8 - z) {
            for (j in 1 until i) {
                if (!positions!![x + j]!![y + j]!!.empty) {
                    inbetween = true
                }
            }
            if (positions!![x + i]!![y + i]!!.empty) {
                if (!inbetween) {
                    moves.add(Move(board, positions, x, y, x + i, y + i, "move"))
                }
            }
            if (positions[x + i]!![y + i]!!.isOpposite(this)) {
                if (!inbetween) {
                    moves.add(Move(board, positions, x, y, x + i, y + i, "take"))
                }
            }
        }
        inbetween = false
        z = if (x < y) {
            x
        } else {
            y
        }
        for (i in 1 until z + 1) {
            for (j in 1 until i) {
                if (!positions!![x - j]!![y - j]!!.empty) {
                    inbetween = true
                }
            }
            if (positions!![x - i]!![y - i]!!.empty) {
                if (!inbetween) {
                    moves.add(Move(board, positions, x, y, x - i, y - i, "move"))
                }
            }
            if (positions[x - i]!![y - i]!!.isOpposite(this)) {
                if (!inbetween) {
                    moves.add(Move(board, positions, x, y, x - i, y - i, "take"))
                }
            }
        }
        inbetween = false
        z = if (7 - x < y) {
            7 - x
        } else {
            y
        }
        for (i in 1 until z + 1) {
            for (j in 1 until i) {
                if (!positions!![x + j]!![y - j]!!.empty) {
                    inbetween = true
                }
            }
            if (positions!![x + i]!![y - i]!!.empty) {
                if (!inbetween) {
                    moves.add(Move(board, positions, x, y, x + i, y - i, "move"))
                }
            }
            if (positions[x + i]!![y - i]!!.isOpposite(this)) {
                if (!inbetween) {
                    moves.add(Move(board, positions, x, y, x + i, y - i, "take"))
                }
            }
        }
        inbetween = false
        z = if (7 - y < x) {
            7 - y
        } else {
            x
        }
        for (i in 1 until z + 1) {
            for (j in 1 until i) {
                if (!positions!![x - j]!![y + j]!!.empty) {
                    inbetween = true
                }
            }
            if (positions!![x - i]!![y + i]!!.empty) {
                if (!inbetween) {
                    moves.add(Move(board, positions, x, y, x - i, y + i, "move"))
                }
            }
            if (positions[x - i]!![y + i]!!.isOpposite(this)) {
                if (!inbetween) {
                    moves.add(Move(board, positions, x, y, x - i, y + i, "take"))
                }
            }
        }
        return moves
    }

    init {
        this.color = color
        type = "bishop"
        wasPawn = false
        backgroundColor = ""
        empty = false
    }
}