package j.s.yarlykov.ui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.view.View;

public class CircleText extends View {

    private String text;
    private Paint paint;
    private Path path;

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
        this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.paint.setStrokeWidth(1);
        this.paint.setTextSize(32);
        this.path = new Path();
        this.text = "GEEKBRAINS STUDENT";
    }


    /**
     * https://startandroid.ru/ru/uroki/vse-uroki-spiskom/316-urok-143-risovanie-path.html
     * http://developer.alexanderklimov.ru/android/catshop/android.graphics.path.php
     * https://developer.android.com/reference/android/graphics/Canvas.html#drawTextOnPath(java.lang.String,%20android.graphics.Path,%20float,%20float,%20android.graphics.Paint)
     * @param canvas
     */

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawARGB(0, 255, 255, 255);
        float mRatioRadius = 0.5f;

        // Размеры View
        int width = getWidth();
        int height = getHeight();

        if ((width == 0) || (height == 0)) {
            return;
        }

        // Координаты центра
        float x = (float) width / 2.0f;
        float y = (float) height / 2.0f;

        float radius;
        if (width > height) {
            radius = height * mRatioRadius;
        } else {
            radius = width * mRatioRadius;
        }

        path.reset();

        // Определить форму и позицию контура
        path.addCircle(x, y, radius * 1.1f, Path.Direction.CCW);

        // Смещение центра
        path.offset(0, 0);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLUE);
        canvas.drawTextOnPath(text, path, 3.14f * radius, 0, paint);

        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, paint);
    }
}
