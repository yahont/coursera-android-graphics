package com.bennyplo.android_mooc_graphics_3d;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class OneTransformationView extends View {
    private enum Axis {X,Y,Z};
    private final Paint edgePaint;
    private final double [][] transformationMatrix = buildIdentityMatrix();
    private final double [][] finalTransformationMatrix = buildIdentityMatrix();
    private final Timer rotationTimer = new Timer();
    private final Coordinate [][] cube = {
            {
                    new Coordinate(1,1,1,1),
                    new Coordinate(1,-1,1,1),
                    new Coordinate(-1,-1,1,1),
                    new Coordinate(-1,1,1,1),
            },
            {
                    new Coordinate(1,1,-1,1),
                    new Coordinate(1,-1,-1,1),
                    new Coordinate(-1,-1,-1,1),
                    new Coordinate(-1,1,-1,1),
            }
    };
    public OneTransformationView(Context context) {
        super(context);
        edgePaint = new Paint();
        edgePaint.setStyle(Paint.Style.STROKE);
        edgePaint.setStrokeWidth(2);
        edgePaint.setColor(Color.RED);
        translate(finalTransformationMatrix, 8, 8, 0);
        scale(finalTransformationMatrix, 40, 40, 1);
        rotationTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                rotate(finalTransformationMatrix, 1, Axis.X);
                rotate(finalTransformationMatrix, 1, Axis.Y);
                rotate(finalTransformationMatrix, 1, Axis.Z);
                invalidate();
            }
        }, 100, 20);
        invalidate();
    }
    private void drawCube(Canvas canvas, Coordinate [][] drawCube) {
        for (Coordinate [] face : drawCube)
            for (int i = 0; i < face.length; i++)
                drawLine(canvas, face[i], face[(i+1) % face.length]);
        Coordinate [] face1 = drawCube[0];
        Coordinate [] face2 = drawCube[1];
        for (int i = 0; i < face1.length; i++)
            drawLine(canvas, face1[i], face2[i]);
    }
    private void drawLine(Canvas canvas, Coordinate a, Coordinate b) {
        canvas.drawLine((int)a.x, (int)a.y, (int)b.x, (int)b.y, edgePaint);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Coordinate [][] drawCube = new Coordinate[cube.length][cube[0].length];
        for (int i = 0; i < drawCube.length; i++)
            for (int j = 0; j < drawCube[i].length; j++) {
                Coordinate o = cube[i][j];
                transform(transformationMatrix, o);
                drawCube[i][j] = new Coordinate(o.x, o.y, o.z, o.w);
                transform(finalTransformationMatrix, drawCube[i][j]);
            }
        drawCube(canvas, drawCube);
    }
    // transformations
    private void transform(double [][] matrix, Coordinate c) {
        double x = c.x, y = c.y, z = c.z;
        c.x = matrix[0][0] * x + matrix[0][1] * y + matrix[0][2] * z + matrix[0][3];
        c.y = matrix[1][0] * x + matrix[1][1] * y + matrix[1][2] * z + matrix[1][3];
        c.z = matrix[2][0] * x + matrix[2][1] * y + matrix[2][2] * z + matrix[2][3];
    }
    private double [][] buildIdentityMatrix() {
        double [][] matrix = {
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1},
        };
        return matrix;
    }
    private void scale(double [][] matrix, double x, double y, double z) {
        double [] xyz = {x, y, x};
        for (int i = 0; i < xyz.length; i++)
            for (int j = 0; j < matrix[i].length; j++)
                matrix[i][j] *= xyz[i];
    }
    private void translate(double [][] matrix, double x, double y, double z) {
        matrix[0][3] += x;
        matrix[1][3] += y;
        matrix[2][3] += z;
    }
    private void rotate(double[][] m, double g, Axis axis) {
        double radians = Math.toRadians(g);
        double sin = Math.sin(radians);
        double cos = Math.cos(radians);
        double i0,i1,i2;
        switch (axis) {
            case X:
                for (int i = 0; i < 3; i++) {
                    i0 = m[0][i];
                    i1 =           cos * m[1][i] - sin * m[2][i];
                    i2 =           sin * m[1][i] + cos * m[2][i];
                    m[0][i] = i0;
                    m[1][i] = i1;
                    m[2][i] = i2;
                }
                break;
            case Y:
                for (int i = 0; i < 3; i++) {
                    i0 =  cos * m[0][i] +           sin * m[2][i];
                    i1 =                  m[1][i];
                    i2 = -sin * m[0][i] +           cos * m[2][i];
                    m[0][i] = i0;
                    m[1][i] = i1;
                    m[2][i] = i2;
                }
                break;
            case Z:
                for (int i = 0; i < 3; i++) {
                    i0 =  cos * m[0][i] - sin * m[1][i];
                    i1 =  sin * m[0][i] + cos * m[1][i];
                    i2 =                                  m[2][i];
                    m[0][i] = i0;
                    m[1][i] = i1;
                    m[2][i] = i2;
                }
                break;
        }
    }
}
