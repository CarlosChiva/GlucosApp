package com.example.tfg.models

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import java.time.LocalDateTime

@SuppressLint("ViewConstructor")
class Painter(context: Context, attrs: AttributeSet, lista: List<Pair<LocalDateTime, Int>>) :
    View(context) {
    val linePaint: Paint
    val textPaint: Paint
    val paddingTop: Int
    val paddingBottom: Int
    val paddingLeft: Int
    val paddingRight: Int
    val contentWidth: Int
    val contentHeight: Int
    val xStep: Float
    val yStep: Float
    lateinit var list: List<Pair<LocalDateTime, Int>>

    init {
        linePaint = Paint()
        linePaint.setColor(Color.RED)
        linePaint.setStrokeWidth(5f)
        this.list = lista
        textPaint = Paint()
        textPaint.setColor(Color.BLACK)
        textPaint.setTextSize(30f)
        textPaint.setAntiAlias(true)
        textPaint.setTextAlign(Paint.Align.CENTER)

        paddingTop = getPaddingTop()
        paddingBottom = getPaddingBottom()
        paddingLeft = getPaddingLeft()
        paddingRight = getPaddingRight()

        contentWidth = getWidth() - paddingLeft - paddingRight
        contentHeight = getHeight() - paddingTop - paddingBottom

        xStep = (contentWidth / 12).toFloat()
        yStep = (contentHeight / 16).toFloat()
    }

    @Override
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas);

        drawGrid(canvas)
        // drawLine(canvas);
        drawPoints(canvas, list)
    }

    private fun drawGrid(canvas: Canvas) {
        // Dibuja las líneas de la cuadrícula vertical
        for (i in 0..12 step 2) {
            val x = paddingLeft + i * xStep;
            canvas.drawLine(
                x, paddingTop.toFloat(), x,
                (paddingTop + contentHeight).toFloat(), linePaint
            );
        }

        // Dibuja las líneas de la cuadrícula horizontal
        for (j in 0..16 step 2) {
            val y = paddingTop + j * yStep;
            canvas.drawLine(
                paddingLeft.toFloat(), y,
                (paddingLeft + contentWidth).toFloat(), y, linePaint
            );
        }

        // Etiqueta las horas en el eje x
        for (w in 0..12 step 2) {
            val x = paddingLeft + w * xStep
            val hour = w + 8
            val text = String.format("%02d:00", hour);
            canvas.drawText(text, x, (paddingTop + contentHeight + 50).toFloat(), textPaint);
        }

        // Etiqueta los niveles de glucosa en el eje y
        for (q in 0..16 step 2) {
            val y = paddingTop + q * yStep;
            val glucose = 400 - q * 20;
            val text = Integer.toString(glucose);
            canvas.drawText(text, (paddingLeft - 50).toFloat(), y + 10, textPaint);
        }
    }

    //    private fun drawLine(canvas: Canvas) {
//        // Dibuja la línea de la gráfica
//
//        if (glucoseDataPoints.size() > 0) {
//            val path = Path()
//            for (i in 0 until glucoseDataPoints.size()) {
//                val dataPoint: GlucoseDataPoint = glucoseDataPoints.get(i)
//                val x = paddingLeft + i * xStep
//                val y: Float =
//                    paddingTop + contentHeight - (dataPoint.getGlucoseLevel() - 40) * yStep / 20
//                if (i == 0) {
//                    path.moveTo(x, y)
//                } else {
//                    path.lineTo(x, y)
//                }
//            }
//            canvas.drawPath(path, linePaint)
//        }
//    }
    private fun drawPoints(canvas: Canvas, glucoseData: List<Pair<LocalDateTime, Int>>) {
        // Dibujar los puntos de glucosa en la gráfica
        val pointRadius = 5f
        val pointPaint = Paint()
        pointPaint.color = Color.GREEN
        pointPaint.style = Paint.Style.FILL
        if (glucoseData.size > 0) {
            for (i in glucoseData.indices) {
                val (first, second) = glucoseData[i]
                val x = paddingLeft + getXPosition(first)
                val y = paddingTop + getYPosition(second)
                canvas.drawCircle(x, y, pointRadius, pointPaint)
            }
        }
    }

    private fun getXPosition(dateTime: LocalDateTime): Float {
        // Obtener la posición X para una fecha/hora dada
        val hourOfDay = dateTime.hour
        val minuteOfHour = dateTime.minute
        return hourOfDay + minuteOfHour / 60f * xStep
    }

    private fun getYPosition(glucoseLevel: Int): Float {
        // Obtener la posición Y para un nivel de glucosa dado
        return contentHeight - (glucoseLevel - 40) * yStep / 20
    }
}


