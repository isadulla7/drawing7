package dot.isadulla.dotpaint.core.cache

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color

class BitmapCache(width: Int, height: Int) {
    private var bitmap: Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    private var canvas: Canvas = Canvas(bitmap)

    fun getCanvas(): Canvas = canvas
    fun getBitmap(): Bitmap = bitmap

    fun clear() {
        canvas.drawColor(Color.TRANSPARENT)
    }
}