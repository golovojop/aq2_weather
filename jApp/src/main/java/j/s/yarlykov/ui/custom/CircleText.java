package j.s.yarlykov.ui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.view.View;

import j.s.yarlykov.R;

public class CircleText extends View {

    private String circleText;
    private Path circlePath;
    private Paint paint;

    public CircleText(Context context) {
        super(context);
        init();
    }

    public CircleText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.paint = new Paint();
        this.paint.setStrokeWidth(1);
        this.paint.setTextSize(22);
        this.paint.setAntiAlias(true);
        this.paint.setColor(Color.DKGRAY);
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
}
