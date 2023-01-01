package com.bennyplo.graphics2d;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Shader;
import android.view.View;

/**
 * Created by benlo on 09/05/2018.
 */

public class MyView extends View {
    private final Paint fillPaint;
    private final Paint strokePaint;
    private final Path path;
    private final Point [] initPoints = new Point[] {
            new Point(50, 300),
            new Point(150, 400),
            new Point(180, 340),
            new Point(240, 420),
            new Point(300, 200),
    };
    public MyView(Context context) {
        super(context, null);
        fillPaint = new Paint();
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setShader( new LinearGradient(
                initPoints[0].x, initPoints[0].y,
                initPoints[initPoints.length-1].x, initPoints[initPoints.length-1].y,
                Color.BLUE, Color.RED,
                Shader.TileMode.MIRROR
        ));
        strokePaint = new Paint();
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(5);
        strokePaint.setARGB(0xFF, 0, 0, 0);
        path = new Path();
        UpdatePath(translate(initPoints));
    }
    private void UpdatePath(Point [] newpoints) {
        path.reset();
        path.moveTo(newpoints[0].x, newpoints[0].y);
        for (int i = 1; i < newpoints.length; i++) {
            path.lineTo(newpoints[i].x, newpoints[i].y);
        }
        path.close();
    }
    public static Point[] affineTransformation(Point [] vertices, double [][] matrix) {
        Point [] result = new Point[vertices.length];
        for (int i = 0; i < vertices.length; i++) {
            result[i] = new Point(
                    (int)(matrix[0][0] * vertices[i].x + matrix[0][1] * vertices[i].y + matrix[0][2]),
                    (int)(matrix[1][0] * vertices[i].x + matrix[1][1] * vertices[i].y + matrix[1][2])
            );
        }
        return result;
    }

    private Point[] translate(Point [] points) { //}, int px, int py) {
        double ang1 = Math.PI / 4;
        double[][][] matrices = {
                {{1, 2, 0},
                 {0, 1, 0}},
                {{.5, 0, 0},
                 {0, 3, 0}},
                {{Math.cos(ang1), -Math.sin(ang1), 0},
                 {Math.sin(ang1),  Math.cos(ang1), 0}},
                {{1, 0, 550},
                 {0, 1, 0}},
        };
        for (double [][] matrix : matrices) {
            points = affineTransformation(points, matrix);
        }
        return points;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, fillPaint);
        canvas.drawPath(path, strokePaint);
    }
}
