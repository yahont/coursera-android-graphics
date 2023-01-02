package com.bennyplo.graphics2d;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Shader;
import android.util.Log;
import android.view.View;

public class MyView extends View {
    private final Paint fillPaint = new Paint();
    private Path path;
    int [] plotdata = {11,29,10,20,12,5,31,24,21,13};

    public MyView(Context context) {
        super(context, null);
        fillPaint.setStyle(Paint.Style.STROKE);
        fillPaint.setStrokeWidth(5);
        fillPaint.setARGB(0xff, 0xff, 0, 0);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //Log.i("HAHAHA", "W: " + w + ", H: " + h);
        path = createLineGraph(plotdata, w, h);
    }
    private Path createLineGraph(int [] input, int w, int h) {
        Path path = new Path();
        Point [] points = new Point[input.length];
        int minValue = Integer.MAX_VALUE;
        int maxValue = Integer.MIN_VALUE;
        for (int i = 0; i < input.length; i++) {
            points[i] = new Point(i, input[i]);
            maxValue = Math.max(maxValue, input[i]);
            minValue = Math.min(minValue, input[i]);
        }
        translate(points, 0, -minValue);
        scale(points, w/(points.length-1.0), (double)h/(maxValue-minValue));
        path.moveTo(points[0].x, points[0].y);
        //Log.i("HAHAHA", "points["+0+"]: " + points[0].x + ", " + points[0].y);
        for (int i = 1; i < points.length; i++) {
            //Log.i("HAHAHA", "points["+i+"]: " + points[i].x + ", " + points[i].y);
            path.lineTo(points[i].x, points[i].y);
        }
        return path;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, fillPaint);
    }
    // transformations
    static public void translate(Point [] points, double x, double y) {
        double[][] matrix = {
                {1, 0, x},
                {0, 1, y},
        };
        transform(points, matrix);
    }
    static public void scale(Point [] points, double x, double y) {
        double[][] matrix = {
                {x, 0, 0},
                {0, y, 0},
        };
        transform(points, matrix);
    }
    static public void transform(Point [] points, double [][] matrix) {
        for (Point p : points) {
            int x = p.x, y = p.y;
            p.x = (int)(matrix[0][0] * x + matrix[0][1] * y + matrix[0][2]);
            p.y = (int)(matrix[1][0] * x + matrix[1][1] * y + matrix[1][2]);
        }
    }
}
