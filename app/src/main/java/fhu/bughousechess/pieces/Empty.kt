package fhu.bughousechess.pieces

import android.R
import fhu.bughousechess.Move

class Empty : Piece() {
    init {
        color = ""
        type = ""
        wasPawn = false
        empty = true
    }

    override val resID: Int
        get() = R.color.transparent

    override fun getMoves(
        positions: Array<Array<Piece?>?>?,
        x: Int,
        y: Int,
        boardNumber: Int
    ): Set<Move?>? {
        return null
    }
}