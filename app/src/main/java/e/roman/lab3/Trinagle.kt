package e.roman.lab3

import android.R.attr.*
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import kotlin.math.*


class Triangle(private var coords1: FloatArray, private var coords2: FloatArray, private var coords3: FloatArray, private val canvas: Canvas, private val paint: Paint, private val color: IntArray, private val shine: Float, val type: Int) {

    private var size = 1f
    var zMax = if (coords1[2] >= coords2[2] && coords1[2] >= coords3[2])
        coords1[2]
    else if (coords2[2] >= coords1[2] && coords2[2] >= coords3[2])
        coords2[2]
    else
        coords3[2]
    var zMin= if (coords1[2] <= coords2[2] && coords1[2] <= coords3[2])
        coords1[2]
    else if (coords2[2] <= coords1[2] && coords2[2] <= coords3[2])
        coords2[2]
    else
        coords3[2]
    var z = abs(zMax) + abs(zMin)

    private var a = sqrt((coords1[0] - coords2[0]).pow(2) + (coords1[1] - coords2[1]).pow(2) + (coords1[2] - coords2[2]).pow(2))
    private var b = sqrt((coords1[0] - coords3[0]).pow(2) + (coords1[1] - coords3[1]).pow(2) + (coords1[2] - coords3[2]).pow(2))
    private var c = sqrt((coords3[0] - coords2[0]).pow(2) + (coords3[1] - coords2[1]).pow(2) + (coords3[2] - coords2[2]).pow(2))
    private var p = (a + b + c) / 2
    private val s = sqrt(p * (p - a) * (p - b) * (p - c))

    fun draw(angleX: Float, angleY: Float, size: Float){
        this.size += size
        val coords11 = floatArrayOf(
                coords1[0],
                coords1[1] * cos(angleY) - coords1[2] * sin(angleY),
                coords1[1] * sin(angleY) + coords1[2] * cos(angleY)
        )
        val coords21 = floatArrayOf(
                coords2[0],
                coords2[1] * cos(angleY) - coords2[2] * sin(angleY),
                coords2[1] * sin(angleY) + coords2[2] * cos(angleY)
        )
        val coords31 = floatArrayOf(
                coords3[0],
                coords3[1] * cos(angleY) - coords3[2] * sin(angleY),
                coords3[1] * sin(angleY) + coords3[2] * cos(angleY)
        )
        coords1 = floatArrayOf(
                coords11[0] * cos(angleX) + coords11[2] * sin(angleX),
                coords11[1],
                -coords11[0] * sin(angleX) + coords11[2] * cos(angleX)
        )
        coords2 = floatArrayOf(
                coords21[0] * cos(angleX) + coords21[2] * sin(angleX),
                coords21[1],
                -coords21[0] * sin(angleX) + coords21[2] * cos(angleX)
        )
        coords3 = floatArrayOf(
                coords31[0] * cos(angleX) + coords31[2] * sin(angleX),
                coords31[1],
                -coords31[0] * sin(angleX) + coords31[2] * cos(angleX)
        )
        if (((coords2[0] - coords1[0]) * (coords3[1] - coords1[1]) > (coords3[0] - coords1[0]) * (coords2[1] - coords1[1]) && type == 1) ||
            ((coords2[0] - coords1[0]) * (coords3[1] - coords1[1]) <= (coords3[0] - coords1[0]) * (coords2[1] - coords1[1]) && type == 2)) {
            /*canvas.drawLine(
                coords1[0] * this.size * 100 + 500f,
                coords1[1] * this.size * 100 + 500f,
                coords2[0] * this.size * 100 + 500f,
                coords2[1] * this.size * 100 + 500f,
                paint
            )
            canvas.drawLine(
                    coords1[0] * this.size * 100 + 500f,
                    coords1[1] * this.size * 100 + 500f,
                    coords3[0] * this.size * 100 + 500f,
                    coords3[1] * this.size * 100 + 500f,
                    paint
            )
            canvas.drawLine(
                    coords2[0] * this.size * 100 + 500f,
                    coords2[1] * this.size * 100 + 500f,
                    coords3[0] * this.size * 100 + 500f,
                    coords3[1] * this.size * 100 + 500f,
                    paint
            )*/

            a = sqrt((coords1[0] - coords2[0]).pow(2) + (coords1[1] - coords2[1]).pow(2))
            b = sqrt((coords1[0] - coords3[0]).pow(2) + (coords1[1] - coords3[1]).pow(2))
            c = sqrt((coords3[0] - coords2[0]).pow(2) + (coords3[1] - coords2[1]).pow(2))
            p = (a + b + c) / 2
            val s1 = sqrt(p * (p - a) * (p - b) * (p - c))
            //val s1 = s


            var path = Path()
            paint.color = Color.parseColor("#${Integer.toHexString((color[0] + ((s1 / s) + z) / 1.2f * shine).toInt())}${Integer.toHexString(((color[1] * (s1 / s) + z) * shine).toInt())}${Integer.toHexString((color[2] + ((s1 / s) + z) / 1.2f * shine).toInt())}${Integer.toHexString((((color[3] * (s1 / s) + z) * shine).toInt()))}${Integer.toHexString((color[4] + ((s1 / s) + z) / 1.2f * shine).toInt())}${Integer.toHexString(((color[5] * (s1 / s) + z) * shine).toInt())}")
            path.moveTo(coords1[0] * this.size * 100 + 500f, coords1[1] * this.size * 100 + 500f)
            path.lineTo(coords2[0] * this.size * 100 + 500f, coords2[1] * this.size * 100 + 500f)
            path.lineTo(coords3[0] * this.size * 100 + 500f, coords3[1] * this.size * 100 + 500f)
            path.lineTo(coords1[0] * this.size * 100 + 500f, coords1[1] * this.size * 100 + 500f)
            path.close()
            canvas.drawPath(path, paint)
        }
        zMax = if (coords1[2] >= coords2[2] && coords1[2] >= coords3[2])
            coords1[2]
        else if (coords2[2] >= coords1[2] && coords2[2] >= coords3[2])
            coords2[2]
        else
            coords3[2]
        zMin= if (coords1[2] <= coords2[2] && coords1[2] <= coords3[2])
            coords1[2]
        else if (coords2[2] <= coords1[2] && coords2[2] <= coords3[2])
            coords2[2]
        else
            coords3[2]
        z = (-zMax - zMin) / 7
        //z=0f
    }
}