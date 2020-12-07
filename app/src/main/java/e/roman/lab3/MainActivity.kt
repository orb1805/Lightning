package e.roman.lab3

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.AdaptiveIconDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    private lateinit var imView: ImageView
    private lateinit var bitmap: Bitmap
    private lateinit var canvas: Canvas
    private lateinit var text: TextView
    private lateinit var approxTV: TextView
    private lateinit var shineTV: TextView
    private lateinit var approxPlusBtn: Button
    private lateinit var approxMinusBtn: Button
    private lateinit var shinePlusBtn: Button
    private lateinit var shineMinusBtn: Button
    private lateinit var drawBtn: Button
    private var previousX: Float = 0f
    private var previousY: Float = 0f
    private lateinit var sectorBottom: MutableList<Triangle>
    private lateinit var sectorCeiling: MutableList<Triangle>
    private lateinit var side: MutableList<Triangle>
    private val color = intArrayOf(5, 5, 5, 5, 5, 5)   //Color.parseColor("#475B66")
    private val transX = -1.3f
    private val transZ = 1f
    private var approxRate = 10
    private var shineRate = 1f

    @SuppressLint("ClickableViewAccessibility")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        text = findViewById(R.id.text)
        imView = findViewById(R.id.im_view)
        bitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888)
        imView.setImageBitmap(bitmap)
        canvas = Canvas(bitmap)
        val paint = Paint()
        paint.style = Paint.Style.FILL
        approxTV = findViewById(R.id.txt_approx)
        shineTV = findViewById(R.id.txt_shine)
        approxPlusBtn = findViewById(R.id.btn_plus_approx)
        approxMinusBtn = findViewById(R.id.btn_minus_approx)
        shinePlusBtn = findViewById(R.id.btn_plus_shine)
        shineMinusBtn = findViewById(R.id.btn_minus_shine)
        drawBtn = findViewById(R.id.btn_draw)
        approxTV.text = approxRate.toString()
        shineTV.text = shineRate.toString()

        approxPlusBtn.setOnClickListener {
            if (approxRate <= 100){
                approxRate++
                approxTV.text = approxRate.toString()
            }
        }

        approxMinusBtn.setOnClickListener {
            if (approxRate > 2) {
                approxRate--
                approxTV.text = approxRate.toString()
            }
        }

        shinePlusBtn.setOnClickListener {
            if (shineRate < 1f){
                shineRate += 0.1f
                shineTV.text = (shineRate * 100).toInt().toString()
            }
        }

        shineMinusBtn.setOnClickListener {
            if (shineRate > 0f){
                shineRate -= 0.1f
                shineTV.text = (shineRate * 100).toInt().toString()
            }
        }

        drawBtn.setOnClickListener {
            canvas.drawColor(Color.parseColor("#FFFFFF"))
            sectorBottom = mutableListOf()
            sectorCeiling = mutableListOf()
            side = mutableListOf()
            var x2 = 0.8f
            var x = 0f
            var dx = (x2 - x) / approxRate
            while (x <= x2) {
                sectorBottom.add(Triangle(
                        floatArrayOf(4 * x / approxRate + transX, 2.5f, -4 * y(x) + (-2f + 4 * y(x)) * (approxRate - 1) / approxRate + transZ),
                        floatArrayOf(4 * (x + dx) / approxRate + transX, 2.5f, -4 * y(x + dx) + (-2f + 4 * y(x + dx)) * (approxRate - 1) / approxRate + transZ),
                        floatArrayOf(transX, 2.5f, -2f + transZ),
                        canvas, paint, color, shineRate, 2)
                )
                sectorCeiling.add(Triangle(
                        floatArrayOf(4 * x / approxRate + transX, -2.5f, -4 * y(x) + (-2f + 4 * y(x)) * (approxRate - 1) / approxRate + transZ),
                        floatArrayOf(4 * (x + dx) / approxRate + transX, -2.5f, -4 * y(x + dx) + (-2f + 4 * y(x + dx)) * (approxRate - 1) / approxRate + transZ),
                        floatArrayOf(transX, -2.5f, -2f + transZ),
                        canvas, paint, color, shineRate, 1)
                )
                for (i in 1 until  approxRate){
                    sectorCeiling.add(Triangle(
                            floatArrayOf(4 * x * i / approxRate + transX, -2.5f, -4 * y(x) + (-2f + 4 * y(x)) * (approxRate - i) / approxRate + transZ),
                            floatArrayOf(4 * x * (i + 1) / approxRate + transX, -2.5f, -4 * y(x) + (-2f + 4 * y(x)) * (approxRate - i - 1) / approxRate + transZ),
                            floatArrayOf(4 * (x + dx) * i / approxRate + transX, -2.5f, -4 * y(x + dx) + (-2f + 4 * y(x + dx)) * (approxRate - i) / approxRate + transZ),
                            canvas, paint, color, shineRate, 1)
                    )
                    sectorCeiling.add(Triangle(
                            floatArrayOf(4 * (x + dx) * i / approxRate + transX, -2.5f, -4 * y(x + dx) + (-2f + 4 * y(x + dx)) * (approxRate - i) / approxRate + transZ),
                            floatArrayOf(4 * x * (i + 1) / approxRate + transX, -2.5f, -4 * y(x) + (-2f + 4 * y(x)) * (approxRate - i - 1) / approxRate + transZ),
                            floatArrayOf(4 * (x + dx) * (i + 1) / approxRate + transX, -2.5f, -4 * y(x + dx) + (-2f + 4 * y(x + dx)) * (approxRate - i - 1) / approxRate + transZ),
                            canvas, paint, color, shineRate, 1)
                    )
                    sectorBottom.add(Triangle(
                            floatArrayOf(4 * x * i / approxRate + transX, 2.5f, -4 * y(x) + (-2f + 4 * y(x)) * (approxRate - i) / approxRate + transZ),
                            floatArrayOf(4 * x * (i + 1) / approxRate + transX, 2.5f, -4 * y(x) + (-2f + 4 * y(x)) * (approxRate - i - 1) / approxRate + transZ),
                            floatArrayOf(4 * (x + dx) * i / approxRate + transX, 2.5f, -4 * y(x + dx) + (-2f + 4 * y(x + dx)) * (approxRate - i) / approxRate + transZ),
                            canvas, paint, color, shineRate, 2)
                    )
                    sectorBottom.add(Triangle(
                            floatArrayOf(4 * (x + dx) * i / approxRate + transX, 2.5f, -4 * y(x + dx) + (-2f + 4 * y(x + dx)) * (approxRate - i) / approxRate + transZ),
                            floatArrayOf(4 * x * (i + 1) / approxRate + transX, 2.5f, -4 * y(x) + (-2f + 4 * y(x)) * (approxRate - i - 1) / approxRate + transZ),
                            floatArrayOf(4 * (x + dx) * (i + 1) / approxRate + transX, 2.5f, -4 * y(x + dx) + (-2f + 4 * y(x + dx)) * (approxRate - i - 1) / approxRate + transZ),
                            canvas, paint, color, shineRate, 2)
                    )
                }
                for (i in 0 until approxRate) {
                    side.add(Triangle(
                            floatArrayOf(4 * x + transX, ((i + 1) * 2.5 / approxRate).toFloat(), -4 * y(x) + transZ),
                            floatArrayOf( 4 * (x + dx) + transX, ((i + 1) * 2.5 / approxRate).toFloat(), -4 * y(x + dx) + transZ),
                            floatArrayOf( 4 * x + transX, (i * 2.5 / approxRate).toFloat(), -4 * y(x) + transZ),
                            canvas, paint, color, shineRate, 1)
                    )
                    side.add(Triangle(
                            floatArrayOf(4 * x + transX, (i * 2.5 / approxRate).toFloat(), -4 * y(x) + transZ),
                            floatArrayOf(4 * (x + dx) + transX, (i * 2.5 / approxRate).toFloat(), -4 * y(x + dx) + transZ),
                            floatArrayOf(4 * (x + dx) + transX, ((i + 1) * 2.5 / approxRate).toFloat(), -4 * y(x + dx) + transZ),
                            canvas, paint, color, shineRate, 2)
                    )
                    side.add(Triangle(
                            floatArrayOf(4 * x + transX, ((-i + 1) * 2.5 / approxRate).toFloat(), -4 * y(x) + transZ),
                            floatArrayOf(4 * (x + dx) + transX, ((-i + 1) * 2.5 / approxRate).toFloat(), -4 * y(x + dx) + transZ),
                            floatArrayOf(4 * x + transX, (-i * 2.5 / approxRate).toFloat(), -4 * y(x) + transZ),
                            canvas, paint, color, shineRate, 1)
                    )
                    side.add(Triangle(
                            floatArrayOf(4 * x + transX, (-i * 2.5 / approxRate).toFloat(), -4 * y(x) + transZ),
                            floatArrayOf(4 * (x + dx) + transX, (-i * 2.5 / approxRate).toFloat(), -4 * y(x + dx) + transZ),
                            floatArrayOf(4 * (x + dx) + transX, ((-i + 1) * 2.5 / approxRate).toFloat(), -4 * y(x + dx) + transZ),
                            canvas, paint, color, shineRate, 2)
                    )
                }
                side.add(Triangle(
                        floatArrayOf(4 * x + transX, ((-approxRate + 1) * 2.5 / approxRate).toFloat(), -4 * y(x) + transZ),
                        floatArrayOf(4 * (x + dx) + transX, ((-approxRate + 1) * 2.5 / approxRate).toFloat(), -4 * y(x + dx) + transZ),
                        floatArrayOf(4 * x + transX, (-approxRate * 2.5 / approxRate).toFloat(), -4 * y(x) + transZ),
                        canvas, paint, color, shineRate, 1)
                )
                side.add(Triangle(
                        floatArrayOf(4 * x + transX, (-approxRate * 2.5 / approxRate).toFloat(), -4 * y(x) + transZ),
                        floatArrayOf(4 * (x + dx) + transX, (-approxRate * 2.5 / approxRate).toFloat(), -4 * y(x + dx) + transZ),
                        floatArrayOf(4 * (x + dx) + transX, ((-approxRate + 1) * 2.5 / approxRate).toFloat(), -4 * y(x + dx) + transZ),
                        canvas, paint, color, shineRate, 2)
                )
                x += dx
            }
            var y = -2f
            val y2 = -4 * y(x)
            val dy = (y2 - y) / approxRate
            x2 = x
            x = 0f
            dx = (x2 - x) / approxRate
            while (x < x2) {
                for (i in 0 until approxRate) {
                    side.add(Triangle(
                            floatArrayOf(4 * x + transX, ((i + 1) * 2.5 / approxRate).toFloat(), y + transZ),
                            floatArrayOf(4 * x + transX, (i * 2.5 / approxRate).toFloat(), y + transZ),
                            floatArrayOf(4 * (x + dx) + transX, ((i + 1) * 2.5 / approxRate).toFloat(), (y + dy) + transZ),
                            canvas, paint, color, shineRate, 1)
                    )
                    side.add(Triangle(
                            floatArrayOf(4 * x + transX, (i * 2.5 / approxRate).toFloat(), y + transZ),
                            floatArrayOf(4 * (x + dx)+ transX, (i * 2.5 / approxRate).toFloat(), (y + dy) + transZ),
                            floatArrayOf(4 * (x + dx) + transX, ((i + 1) * 2.5 / approxRate).toFloat(), (y + dy) + transZ),
                            canvas, paint, color, shineRate, 1)
                    )
                    side.add(Triangle(
                            floatArrayOf(4 * x + transX, ((-i + 1) * 2.5 / approxRate).toFloat(), y + transZ),
                            floatArrayOf(4 * x + transX, (-i * 2.5 / approxRate).toFloat(), y + transZ),
                            floatArrayOf(4 * (x + dx) + transX, ((-i + 1) * 2.5 / approxRate).toFloat(), (y + dy) + transZ),
                            canvas, paint, color, shineRate, 1)
                    )
                    side.add(Triangle(
                            floatArrayOf(4 * x + transX, (-i * 2.5 / approxRate).toFloat(), y + transZ),
                            floatArrayOf(4 * (x + dx)+ transX, (-i * 2.5 / approxRate).toFloat(), (y + dy) + transZ),
                            floatArrayOf(4 * (x + dx) + transX, ((-i + 1) * 2.5 / approxRate).toFloat(), (y + dy) + transZ),
                            canvas, paint, color, shineRate, 1)
                    )
                }
                side.add(Triangle(
                        floatArrayOf(4 * x + transX, ((-approxRate + 1) * 2.5 / approxRate).toFloat(), y + transZ),
                        floatArrayOf(4 * x + transX, (-approxRate * 2.5 / approxRate).toFloat(), y + transZ),
                        floatArrayOf(4 * (x + dx) + transX, ((-approxRate + 1) * 2.5 / approxRate).toFloat(), (y + dy) + transZ),
                        canvas, paint, color, shineRate, 1)
                )
                side.add(Triangle(
                        floatArrayOf(4 * x + transX, (-approxRate * 2.5 / approxRate).toFloat(), y + transZ),
                        floatArrayOf(4 * (x + dx)+ transX, (-approxRate * 2.5 / approxRate).toFloat(), (y + dy) + transZ),
                        floatArrayOf(4 * (x + dx) + transX, ((-approxRate + 1) * 2.5 / approxRate).toFloat(), (y + dy) + transZ),
                        canvas, paint, color, shineRate, 1)
                )
                x += dx
                y += dy
            }
            x = 0f
            x2 = -2f
            dx = (x2 - x) / approxRate
            while (x > x2) {
                for (i in 0 until approxRate){
                    side.add(Triangle(
                            floatArrayOf(transX, ((i + 1) * 2.5 / approxRate).toFloat(), x + transZ),
                            floatArrayOf(transX, ((i + 1) * 2.5 / approxRate).toFloat(), x + dx + transZ),
                            floatArrayOf(transX, (i * 2.5 / approxRate).toFloat(), x + transZ),
                            canvas, paint, color, shineRate, 2)
                    )
                    side.add(Triangle(
                            floatArrayOf(transX, (i * 2.5 / approxRate).toFloat(), x + transZ),
                            floatArrayOf(transX, (i * 2.5 / approxRate).toFloat(), x + dx + transZ),
                            floatArrayOf(transX, ((i + 1) * 2.5 / approxRate).toFloat(), x + dx + transZ),
                            canvas, paint, color, shineRate, 1)
                    )
                    side.add(Triangle(
                            floatArrayOf(transX, ((-i + 1) * 2.5 / approxRate).toFloat(), x + transZ),
                            floatArrayOf(transX, ((-i + 1) * 2.5 / approxRate).toFloat(), x + dx + transZ),
                            floatArrayOf(transX, (-i * 2.5 / approxRate).toFloat(), x + transZ),
                            canvas, paint, color, shineRate, 2)
                    )
                    side.add(Triangle(
                            floatArrayOf(transX, (-i * 2.5 / approxRate).toFloat(), x + transZ),
                            floatArrayOf(transX, (-i * 2.5 / approxRate).toFloat(), x + dx + transZ),
                            floatArrayOf(transX, ((-i + 1) * 2.5 / approxRate).toFloat(), x + dx + transZ),
                            canvas, paint, color, shineRate, 1)
                    )
                }
                side.add(Triangle(
                        floatArrayOf(transX, ((-approxRate + 1) * 2.5 / approxRate).toFloat(), x + transZ),
                        floatArrayOf(transX, ((-approxRate + 1) * 2.5 / approxRate).toFloat(), x + dx + transZ),
                        floatArrayOf(transX, (-approxRate * 2.5 / approxRate).toFloat(), x + transZ),
                        canvas, paint, color, shineRate, 2)
                )
                side.add(Triangle(
                        floatArrayOf(transX, (-approxRate * 2.5 / approxRate).toFloat(), x + transZ),
                        floatArrayOf(transX, (-approxRate * 2.5 / approxRate).toFloat(), x + dx + transZ),
                        floatArrayOf(transX, ((-approxRate + 1) * 2.5 / approxRate).toFloat(), x + dx + transZ),
                        canvas, paint, color, shineRate, 1)
                )
                x += dx
            }
            for (i in sectorBottom) {
                i.draw(0f, 0f, 0f)
            }
            for (i in sectorCeiling) {
                i.draw(0f, 0f, 0f)
            }
            for (i in side) {
                i.draw(0f, 0f, 0f)
            }
        }

        imView.setOnTouchListener { view, event ->
            val x: Float = event.x
            val y: Float = event.y

            when (event.action) {
                MotionEvent.ACTION_MOVE -> {
                    var dx: Float = x - previousX
                    var dy: Float = y - previousY

                    canvas.drawColor(Color.WHITE)
                    for (i in sectorBottom) {
                        i.draw(-dx / 200, dy / 200, 0f)
                    }
                    for (i in sectorCeiling)
                        i.draw(-dx / 200, dy / 200, 0f)
                    for (i in side) {
                        i.draw(-dx / 200, dy / 200, 0f)
                    }
                    imView.setImageBitmap(bitmap)
                    text.text = dx.toString() + " : " + dy.toString()
                }
            }
            previousX = x
            previousY = y
            true
        }
    }

    private fun y(x: Float):Float{
        return x*x
    }
}