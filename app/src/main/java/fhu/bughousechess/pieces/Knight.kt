package fhu.bughousechess.pieces

import fhu.bughousechess.Move
import fhu.bughousechess.R
import java.util.HashSet

class Knight(color: String?) : Piece() {
    init {
        this.color = color
        type = "knight"
        wasPawn = false
        empty = false
    }

    override val resID: Int
        get() = if (color == "white") R.mipmap.knight else R.mipmap.bknight

    override fun getMoves(
        positions: Array<Array<Piece?>?>?,
        x: Int,
        y: Int,
        boardNumber: Int
    ): Set<Move?>? {
        val moves: MutableSet<Move?> = HashSet()
        if (x + 1 < 8 && y + 2 < 8) {
            if (positions!![x + 1]!![y + 2]!!.color != color) {
                if (positions[x + 1]!![y + 2]!!.isOpposite(this)) {
                    moves.add(Move(positions, x, y, x + 1, y + 2, "take"))
                } else {
                    moves.add(Move(positions, x, y, x + 1, y + 2, "move"))
                }
            }
        }
        if (x + 2 < 8 && y + 1 < 8) {
            if (positions!![x + 2]!![y + 1]!!.color != color) {
                if (positions[x + 2]!![y + 1]!!.isOpposite(this)) {
                    moves.add(Move(positions, x, y, x + 2, y + 1, "take"))
                } else {
                    moves.add(Move(positions, x, y, x + 2, y + 1, "move"))
                }
            }
        }
        if (x - 1 > -1 && y + 2 < 8) {
            if (positions!![x - 1]!![y + 2]!!.color != color) {
                if (positions[x - 1]!![y + 2]!!.isOpposite(this)) {
                    moves.add(Move(positions, x, y, x - 1, y + 2, "take"))
                } else {
                    moves.add(Move(positions, x, y, x - 1, y + 2, "move"))
                }
            }
        }
        if (x - 2 > -1 && y + 1 < 8) {
            if (positions!![x - 2]!![y + 1]!!.color != color) {
                if (positions[x - 2]!![y + 1]!!.isOpposite(this)) {
                    moves.add(Move(positions, x, y, x - 2, y + 1, "take"))
                } else {
                    moves.add(Move(positions, x, y, x - 2, y + 1, "move"))
                }
            }
        }
        if (x + 1 < 8 && y - 2 > -1) {
            if (positions!![x + 1]!![y - 2]!!.color != color) {
                if (positions[x + 1]!![y - 2]!!.isOpposite(this)) {
                    moves.add(Move(positions, x, y, x + 1, y - 2, "take"))
                } else {
                    moves.add(Move(positions, x, y, x + 1, y - 2, "move"))
                }
            }
        }
        if (x + 2 < 8 && y - 1 > -1) {
            if (positions!![x + 2]!![y - 1]!!.color != color) {
                if (positions[x + 2]!![y - 1]!!.isOpposite(this)) {
                    moves.add(Move(positions, x, y, x + 2, y - 1, "take"))
                } else {
                    moves.add(Move(positions, x, y, x + 2, y - 1, "move"))
                }
            }
        }
        if (x - 1 > -1 && y - 2 > -1) {
            if (positions!![x - 1]!![y - 2]!!.color != color) {
                if (positions[x - 1]!![y - 2]!!.isOpposite(this)) {
                    moves.add(Move(positions, x, y, x - 1, y - 2, "take"))
                } else {
                    moves.add(Move(positions, x, y, x - 1, y - 2, "move"))
                }
            }
        }
        if (x - 2 > -1 && y - 1 > -1) {
            if (positions!![x - 2]!![y - 1]!!.color != color) {
                if (positions[x - 2]!![y - 1]!!.isOpposite(this)) {
                    moves.add(Move(positions, x, y, x - 2, y - 1, "take"))
                } else {
                    moves.add(Move(positions, x, y, x - 2, y - 1, "move"))
                }
            }
        }
        return moves
    }
}