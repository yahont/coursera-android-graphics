package com.bennyplo.graphics2d;

import android.graphics.Point;

import junit.framework.TestCase;

public class MyViewTest extends TestCase {

    public void testAffineTransformation() {
        Point[] points = {
                new Point(2, 4)
        };
        assertEquals(2, points[0].x);
        assertEquals(4, points[0].y);
        MyView.transform(
                points,
                new double[][]{
                        {1,0,2},
                        {0,1,4}
                });
        assertEquals(1, points.length);
        assertEquals(4, points[0].x);
        assertEquals(8, points[0].y);
    }
}