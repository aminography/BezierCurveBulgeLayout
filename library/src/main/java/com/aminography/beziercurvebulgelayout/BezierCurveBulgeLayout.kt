package com.aminography.beziercurvebulgelayout

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.FrameLayout

class BezierCurveBulgeLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val curveWidth: Int
    private val flatWidth: Int
    private val flatHeight: Int
    private val bulgeColor: Int
    private val bulgeType: BulgeType

    private var path = Path()
    private var paint = Paint()

    private val startCurveStartPoint = Point()
    private val startCurveEndPoint = Point()
    private val startCurveFirstControlPoint = Point()
    private val startCurveSecondControlPoint = Point()

    private val endCurveStartPoint = Point()
    private val endCurveEndPoint = Point()
    private val endCurveFirstControlPoint = Point()
    private val endCurveSecondControlPoint = Point()

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.BezierCurveBulgeLayout, defStyleAttr, 0)
        curveWidth = a.getDimensionPixelSize(R.styleable.BezierCurveBulgeLayout_curveWidth, 0)
        flatWidth = a.getDimensionPixelSize(R.styleable.BezierCurveBulgeLayout_flatWidth, 0)
        flatHeight = a.getDimensionPixelSize(R.styleable.BezierCurveBulgeLayout_flatHeight, 0)
        bulgeColor = a.getColor(R.styleable.BezierCurveBulgeLayout_bulgeColor, Color.WHITE)
        bulgeType = BulgeType.values()[a.getInt(R.styleable.BezierCurveBulgeLayout_bulgeType, BulgeType.BULGE.ordinal)]
        a.recycle()

        init()
    }

    private fun init() {
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color = bulgeColor
        setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val viewWidth = width
        val viewHeight = height
        var baseY = 0
        var flatY = 0
        when (bulgeType) {
            BulgeType.BULGE -> baseY = flatHeight
            BulgeType.NOTCH -> flatY = flatHeight
        }

        startCurveStartPoint.set(viewWidth / 2 - flatWidth / 2 - curveWidth * 7 / 6, baseY)
        startCurveEndPoint.set(viewWidth / 2 - flatWidth / 2, flatY)

        endCurveStartPoint.set(viewWidth / 2 + flatWidth / 2, flatY)
        endCurveEndPoint.set(viewWidth / 2 + flatWidth / 2 + curveWidth * 7 / 6, baseY)

        startCurveFirstControlPoint.set(startCurveStartPoint.x + curveWidth * 5 / 8, startCurveStartPoint.y)
        startCurveSecondControlPoint.set(startCurveEndPoint.x - curveWidth / 2, startCurveEndPoint.y)

        endCurveFirstControlPoint.set(endCurveStartPoint.x + curveWidth / 2, endCurveStartPoint.y)
        endCurveSecondControlPoint.set(endCurveEndPoint.x - curveWidth * 5 / 8, endCurveEndPoint.y)

        path.reset()
        path.moveTo(0f, baseY.toFloat())
        path.lineTo(startCurveStartPoint.x.toFloat(), startCurveStartPoint.y.toFloat())

        path.cubicTo(
            startCurveFirstControlPoint.x.toFloat(), startCurveFirstControlPoint.y.toFloat(),
            startCurveSecondControlPoint.x.toFloat(), startCurveSecondControlPoint.y.toFloat(),
            startCurveEndPoint.x.toFloat(), startCurveEndPoint.y.toFloat()
        )

        path.lineTo(endCurveStartPoint.x.toFloat(), endCurveStartPoint.y.toFloat())

        path.cubicTo(
            endCurveFirstControlPoint.x.toFloat(), endCurveFirstControlPoint.y.toFloat(),
            endCurveSecondControlPoint.x.toFloat(), endCurveSecondControlPoint.y.toFloat(),
            endCurveEndPoint.x.toFloat(), endCurveEndPoint.y.toFloat()
        )

        path.lineTo(viewWidth.toFloat(), baseY.toFloat())
        path.lineTo(viewWidth.toFloat(), viewHeight.toFloat())
        path.lineTo(0f, viewHeight.toFloat())
        path.close()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(path, paint)
    }

    private enum class BulgeType {
        BULGE,
        NOTCH
    }

}
