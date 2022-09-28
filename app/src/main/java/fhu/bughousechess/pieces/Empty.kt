package fhu.bughousechess.pieces

import android.R
import android.widget.ImageView
import fhu.bughousechess.Move
import fhu.bughousechess.pieces.Piece

class Empty : Piece() {
    override val resID: Int
        get() = R.color.transparent

    override fun getMoves(
        board: Array<Array<ImageView?>?>?,
        positions: Array<Array<Piece?>?>?,
        x: Int,
        y: Int
    ): Set<Move?>? {
        return null
    }

    init {
        color = ""
        type = ""
        wasPawn = false
        backgroundColor = ""
        empty = true
    }
}