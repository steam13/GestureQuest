package dev.trickybear.ru.gestruequest.utils;

import android.gesture.Gesture;
import android.gesture.GestureStroke;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.widget.ImageView;

import java.util.ArrayList;

public class GestureImageUtils {

    public static Bitmap toBitmap(ImageView targetView, Gesture gesture) {
        int width = targetView.getWidth();
        int height = targetView.getHeight();
        int edge = 64;
        int numSample = 16;
        int color = -16711936;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        // canvas.translate((float) edge, (float) edge);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(color);
        paint.setStyle(Style.STROKE);
        paint.setStrokeJoin(Join.ROUND);
        paint.setStrokeCap(Cap.ROUND);
        paint.setStrokeWidth(32.0f);
        paint.setMaskFilter(new BlurMaskFilter(16.0f, Blur.SOLID));
        ArrayList<GestureStroke> strokes = gesture.getStrokes();
        int count = strokes.size();
        // for (int i = 0; i < count; i++) {
        //canvas.drawPath((strokes.get(i)).toPath((float) (width - (edge * 2)), (float) (height - (edge * 2)), numSample), paint);
        Path path = toPath(strokes);
        RectF bounds = new RectF();
        path.computeBounds(bounds, false);
        PointF center = new PointF((bounds.left + bounds.right) / 2,
                (bounds.top + bounds.bottom) / 2);
        float cx = width / 2;
        float cy = height / 2;
        canvas.translate(cx - center.x, cy - center.y);
        canvas.drawPath(path, paint);
        //}
        return bitmap;
    }

    private static Path toPath(ArrayList<GestureStroke> strokes) {
        Path path = new Path();
        int count = strokes.size();
        for (int i = 0; i < count; i++) {
            path.addPath((strokes.get(i)).getPath());
        }
        return path;
    }
}
