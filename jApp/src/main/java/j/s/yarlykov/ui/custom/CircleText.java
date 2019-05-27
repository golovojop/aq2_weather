package j.s.yarlykov.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import j.s.yarlykov.R;

public class CircleText extends View {

    private String circleText;
    private Path circlePath;
    private Paint paint;
    private int color = Color.DKGRAY;
    private int textZise = 24;

    public CircleText(Context context) {
        super(context);
        initView();
    }

    public CircleText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
        initView();
    }

    public CircleText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        initView();
    }

    private void initAttr(Context context , AttributeSet attrs) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CircleText);
        setColor(attributes.getColor(R.styleable.CircleText_cv_Color, Color.DKGRAY));
        setTextSize(attributes.getInt(R.styleable.CircleText_cv_TextSize, 22));
        attributes.recycle();
    }

    private void initView() {
        this.paint = new Paint();
        this.paint.setStrokeWidth(1);
        this.paint.setAntiAlias(true);
        this.paint.setColor(color);
        this.paint.setTextSize(textZise);
        this.circlePath = new Path();
        this.circleText = getResources().getString(R.string.text_avatar).toUpperCase();
    }

    /**
     * https://startandroid.ru/ru/uroki/vse-uroki-spiskom/316-urok-143-risovanie-path.html
     * http://developer.alexanderklimov.ru/android/catshop/android.graphics.path.php
     * https://developer.android.com/reference/android/graphics/Canvas.html#drawTextOnPath(java.lang.String,%20android.graphics.Path,%20float,%20float,%20android.graphics.Paint)
     */

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int centerX = getWidth()/2;
        int centerY = getHeight()/2;
        int radius = Math.min(centerX, centerY);

        float textLength = paint.measureText(circleText);

        // Path.Direction.CCW - отсчет угла против часовой стрелки
        circlePath.addCircle(centerX, centerY, radius, Path.Direction.CCW);

        // Позиционирование текста внизу круга по центру
        canvas.drawTextOnPath(circleText,
                circlePath,
                (float) (1.5f*Math.PI*radius) - textLength/2,
                -5,
                paint);

        // Отрисовка окружности
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(circlePath, paint);
    }

    private void setColor(int color) {
        this.color = color;
    }

    private void setTextSize(int textSize) {
        this.textZise = textZise;
    }
}
