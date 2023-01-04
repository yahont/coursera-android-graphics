package com.bennyplo.android_mooc_graphics_3d;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class MyView extends View {
    private enum Axis { X, Y, Z }
    private Paint redPaint; //paint object for drawing the lines
    private Coordinate[]cube_vertices;//the vertices of a 3D cube
    private Coordinate[]draw_cube_vertices;//the vertices for drawing a 3D cube
    public MyView(Context context) {
        super(context, null);
        final MyView thisview=this;
        //create the paint object
        redPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        redPaint.setStyle(Paint.Style.STROKE);//Stroke
        redPaint.setColor(Color.RED);
        redPaint.setStrokeWidth(2);
        //create a 3D cube
        cube_vertices = new Coordinate[8];
        cube_vertices[0] = new Coordinate(-1, -1, -1, 1);
        cube_vertices[1] = new Coordinate(-1, -1, 1, 1);
        cube_vertices[2] = new Coordinate(-1, 1, -1, 1);
        cube_vertices[3] = new Coordinate(-1, 1, 1, 1);
        cube_vertices[4] = new Coordinate(1, -1, -1, 1);
        cube_vertices[5] = new Coordinate(1, -1, 1, 1);
        cube_vertices[6] = new Coordinate(1, 1, -1, 1);
        cube_vertices[7] = new Coordinate(1, 1, 1, 1);
        draw_cube_vertices=rotate(cube_vertices, 80, Axis.Z);
        draw_cube_vertices=rotate(draw_cube_vertices, 30, Axis.Y);
        draw_cube_vertices=translate(draw_cube_vertices,2,2,2);
        draw_cube_vertices=scale(draw_cube_vertices, 40,40,40);
        //draw_cube_vertices=rotate(draw_cube_vertices, 45, Axis.Y);
        //draw_cube_vertices=rotate(draw_cube_vertices, 45, Axis.X);
        thisview.invalidate();//update the view
    }

    private  void DrawLinePairs(Canvas canvas, Coordinate[] vertices, int start, int end, Paint paint)
    {//draw a line connecting 2 points
        //canvas - canvas of the view
        //points - array of points
        //start - index of the starting point
        //end - index of the ending point
        //paint - the paint of the line
        canvas.drawLine((int)vertices[start].x,(int)vertices[start].y,(int)vertices[end].x,(int)vertices[end].y,paint);
    }

    private void DrawCube(Canvas canvas)
    {//draw a cube on the screen
        DrawLinePairs(canvas, draw_cube_vertices, 0, 1, redPaint);
        DrawLinePairs(canvas, draw_cube_vertices, 1, 3, redPaint);
        DrawLinePairs(canvas, draw_cube_vertices, 3, 2, redPaint);
        DrawLinePairs(canvas, draw_cube_vertices, 2, 0, redPaint);
        DrawLinePairs(canvas, draw_cube_vertices, 4, 5, redPaint);
        DrawLinePairs(canvas, draw_cube_vertices, 5, 7, redPaint);
        DrawLinePairs(canvas, draw_cube_vertices, 7, 6, redPaint);
        DrawLinePairs(canvas, draw_cube_vertices, 6, 4, redPaint);
        DrawLinePairs(canvas, draw_cube_vertices, 0, 4, redPaint);
        DrawLinePairs(canvas, draw_cube_vertices, 1, 5, redPaint);
        DrawLinePairs(canvas, draw_cube_vertices, 2, 6, redPaint);
        DrawLinePairs(canvas, draw_cube_vertices, 3, 7, redPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //draw objects on the screen
        super.onDraw(canvas);
        DrawCube(canvas);//draw a cube onto the screen
    }
    //*********************************
    //matrix and transformation functions
    public double []GetIdentityMatrix()
    {//return an 4x4 identity matrix
        double [] matrix = {
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1,
        };
        return matrix;
    }
    public Coordinate Transformation(Coordinate vertex,double []matrix)
    {//affine transformation with homogeneous coordinates
        //i.e. a vector (vertex) multiply with the transformation matrix
        // vertex - vector in 3D
        // matrix - transformation matrix
        Coordinate result=new Coordinate();
        result.x=matrix[0]*vertex.x+matrix[1]*vertex.y+matrix[2]*vertex.z+matrix[3];
        result.y=matrix[4]*vertex.x+matrix[5]*vertex.y+matrix[6]*vertex.z+matrix[7];
        result.z=matrix[8]*vertex.x+matrix[9]*vertex.y+matrix[10]*vertex.z+matrix[11];
        result.w=matrix[12]*vertex.x+matrix[13]*vertex.y+matrix[14]*vertex.z+matrix[15];
        return result;
    }
    public Coordinate[]Transformation(Coordinate []vertices,double []matrix)
    {   //Affine transform a 3D object with vertices
        // vertices - vertices of the 3D object.
        // matrix - transformation matrix
        Coordinate []result=new Coordinate[vertices.length];
        for (int i=0;i<vertices.length;i++)
        {
           result[i]=Transformation(vertices[i],matrix);
           result[i].Normalise();
        }
        return result;
    }
    //***********************************************************
    //Affine transformation
    public Coordinate []translate(Coordinate []vertices,double tx,double ty,double tz)
    {
        double []matrix=GetIdentityMatrix();
        matrix[3]=tx;
        matrix[7]=ty;
        matrix[11]=tz;
        return Transformation(vertices,matrix);
    }
    private Coordinate[]scale(Coordinate []vertices,double sx,double sy,double sz)
    {
        double []matrix=GetIdentityMatrix();
        matrix[0]=sx;
        matrix[5]=sy;
        matrix[10]=sz;
        return Transformation(vertices,matrix);
    }
    private Coordinate[] rotate(Coordinate[] vertices, double g, Axis axis) {
        double radians = Math.toRadians(g);
        double[] matrix = GetIdentityMatrix();
        switch (axis) {
            case X:
                matrix[5]  =  Math.cos(radians);
                matrix[6]  = -Math.sin(radians);
                matrix[9]  =  Math.sin(radians);
                matrix[10] =  Math.cos(radians);
                break;
            case Y:
                matrix[0]  =  Math.cos(radians);
                matrix[2]  =  Math.sin(radians);
                matrix[8]  = -Math.sin(radians);
                matrix[10] =  Math.cos(radians);
                break;
            case Z:
                matrix[0] =  Math.cos(radians);
                matrix[1] = -Math.sin(radians);
                matrix[4] =  Math.sin(radians);
                matrix[5] =  Math.cos(radians);
                break;
        }
        return Transformation(vertices, matrix);
    }

}