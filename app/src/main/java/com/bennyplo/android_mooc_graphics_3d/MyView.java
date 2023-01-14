package com.bennyplo.android_mooc_graphics_3d;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.signum;
import static java.lang.Math.sin;

class MyView extends View {
    static class BodyPart {
        Coordinate [] v;
        Paint paint;
        BodyPart(Coordinate [] vertices, Paint paint) {
            this.v = vertices;
            this.paint = paint;
        }
    }
    private static final Coordinate [] cube_vertices = {
        new Coordinate(-1,  1,  1, 1),
        new Coordinate(-1, -1,  1, 1),
        new Coordinate( 1, -1,  1, 1),
        new Coordinate( 1,  1,  1, 1),
        new Coordinate(-1,  1, -1, 1),
        new Coordinate(-1, -1, -1, 1),
        new Coordinate( 1, -1, -1, 1),
        new Coordinate( 1,  1, -1, 1),
    };
    private final BodyPart trunk;
    private final BodyPart r_upper_arm;
    private final BodyPart r_forearm;
    private final BodyPart r_hand;
    private final BodyPart l_upper_arm;
    private final BodyPart l_forearm;
    private final BodyPart l_hand;
    private final BodyPart r_thigh;
    private final BodyPart r_lower_leg;
    private final BodyPart r_foot;
    private final BodyPart l_thigh;
    private final BodyPart l_lower_leg;
    private final BodyPart l_foot;

    private final BodyPart [] allParts;
    private double width = 200;
    private double height = 400;
    private double rotateAngle = 0;
    private double angleInc = 2;

    public MyView(Context context) {
        super(context, null);
        final Map<Integer,Paint> paints = new HashMap<>();
        for (int color : new int[]{Color.BLUE, Color.MAGENTA, Color.RED, Color.GREEN, Color.CYAN}) {
            Paint paint = new Paint();
            paint.setColor(color);
            paint.setStyle(Paint.Style.FILL);
            paint.setStrokeWidth(1);
            paints.put(color, paint);
        }

        BodyPart hip = new BodyPart(cube_vertices, paints.get(Color.MAGENTA));
        hip.v = scale(hip.v, 3, 1, 1);
        hip.v = translate(hip.v, 0, 1, 0);
        trunk = new BodyPart(cube_vertices, paints.get(Color.RED));
        trunk.v = scale(cube_vertices, hip.v[2].x, 4,1);
        trunk.v = translate(trunk.v, 0, hip.v[1].y - trunk.v[0].y, 0);
        BodyPart neck = new BodyPart(cube_vertices, paints.get(Color.MAGENTA));
        neck.v = scale(cube_vertices, 0.8, 0.4, 0.5);
        neck.v = translate(neck.v, 0, trunk.v[1].y - neck.v[0].y, 0);
        BodyPart head = new BodyPart(cube_vertices, paints.get(Color.BLUE));
        head.v = scale(cube_vertices, 1.5, 1.5,1);
        head.v = translate(head.v, 0, neck.v[1].y - head.v[0].y, 0);
        r_upper_arm = new BodyPart(cube_vertices, paints.get(Color.BLUE));
        r_upper_arm.v = scale(cube_vertices, 1, 1.8, 1);
        r_upper_arm.v = translate(r_upper_arm.v,
                trunk.v[1].x - r_upper_arm.v[2].x,
                trunk.v[1].y - r_upper_arm.v[1].y,0);
        l_upper_arm = new BodyPart(cube_vertices, paints.get(Color.BLUE));
        l_upper_arm.v = scale(cube_vertices, 1, 1.8, 1);
        l_upper_arm.v = translate( l_upper_arm.v,
                trunk.v[2].x - l_upper_arm.v[1].x,
                trunk.v[1].y - l_upper_arm.v[1].y,0);
        r_forearm = new BodyPart(cube_vertices, paints.get(Color.GREEN));
        r_forearm.v = scale(cube_vertices, 1,2.1,1);
        r_forearm.v = translate(r_forearm.v,
                trunk.v[1].x - r_forearm.v[2].x,
                r_upper_arm.v[0].y - r_forearm.v[1].y,0);
        l_forearm = new BodyPart(cube_vertices, paints.get(Color.GREEN));
        l_forearm.v = scale(cube_vertices, 1,2.1,1);
        l_forearm.v = translate(l_forearm.v,
                trunk.v[2].x - l_forearm.v[1].x,
                l_upper_arm.v[0].y - l_forearm.v[1].y,0);
        r_hand = new BodyPart(cube_vertices, paints.get(Color.CYAN));
        r_hand.v = scale(cube_vertices, 1, 0.5, 1.5);
        r_hand.v = translate(r_hand.v,
                trunk.v[1].x - r_hand.v[2].x,
                r_forearm.v[0].y - r_hand.v[1].y,
                r_hand.v[0].z - 1);
        l_hand = new BodyPart(cube_vertices, paints.get(Color.CYAN));
        l_hand.v = scale(cube_vertices, 1, 0.5, 1.5);
        l_hand.v = translate(l_hand.v,
                trunk.v[2].x - l_hand.v[1].x,
                l_forearm.v[0].y - l_hand.v[1].y,
                l_hand.v[0].z - 1);
        r_thigh = new BodyPart(cube_vertices, paints.get(Color.BLUE));
        r_thigh.v = scale(cube_vertices, 1, 2, 1);
        r_thigh.v = translate(r_thigh.v, trunk.v[0].x - r_thigh.v[0].x, hip.v[0].y - r_thigh.v[1].y, 0);
        l_thigh = new BodyPart(cube_vertices, paints.get(Color.BLUE));
        l_thigh.v = scale(cube_vertices, 1, 2, 1);
        l_thigh.v = translate(l_thigh.v, trunk.v[2].x - l_thigh.v[2].x, hip.v[0].y - l_thigh.v[1].y, 0);
        r_lower_leg = new BodyPart(cube_vertices, paints.get(Color.GREEN));
        r_lower_leg.v = scale(cube_vertices, 1, 2.2, 1);
        r_lower_leg.v = translate(r_lower_leg.v,
                r_thigh.v[0].x - r_lower_leg.v[0].x,
                r_thigh.v[0].y - r_lower_leg.v[1].y, 0);
        l_lower_leg = new BodyPart(cube_vertices, paints.get(Color.GREEN));
        l_lower_leg.v = scale(cube_vertices, 1, 2.2, 1);
        l_lower_leg.v = translate(l_lower_leg.v,
                l_thigh.v[0].x - l_lower_leg.v[0].x,
                l_thigh.v[0].y - l_lower_leg.v[1].y, 0);
        r_foot = new BodyPart(cube_vertices, paints.get(Color.RED));
        r_foot.v = scale(cube_vertices, 1, 0.7, 1.5);
        r_foot.v = translate(r_foot.v,
                r_lower_leg.v[0].x - r_foot.v[0].x,
                r_lower_leg.v[0].y - r_foot.v[1].y,
                r_foot.v[0].z - 1);
        l_foot = new BodyPart(cube_vertices, paints.get(Color.RED));
        l_foot.v = scale(cube_vertices, 1, 0.7, 1.5);
        l_foot.v = translate(l_foot.v,
                l_lower_leg.v[0].x - l_foot.v[0].x,
                l_lower_leg.v[0].y - l_foot.v[1].y,
                l_foot.v[0].z - 1);
        allParts = new BodyPart[] {
                r_upper_arm,
                r_forearm,
                r_hand,
                l_upper_arm,
                l_forearm,
                l_hand,
                head,
                neck,
                trunk,
                hip,
                r_thigh,
                r_lower_leg,
                r_foot,
                l_thigh,
                l_lower_leg,
                l_foot,
        };

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                invalidate();
            }
        };
        timer.scheduleAtFixedRate(task,100,50);
        invalidate();
    }

    private void drawFace(Canvas canvas, Coordinate[] vertices, int [] face, Paint paint) {
        Path path = new Path();
        path.moveTo((float) vertices[face[0]].x, (float) vertices[face[0]].y);
        path.lineTo((float) vertices[face[1]].x, (float) vertices[face[1]].y);
        path.lineTo((float) vertices[face[2]].x, (float) vertices[face[2]].y);
        path.lineTo((float) vertices[face[3]].x, (float) vertices[face[3]].y);
        path.close();
        canvas.drawPath(path, paint);
    }

    static int[][] face_vertices = {
            {0,1,2,3},
            {4,5,6,7},
            {0,1,5,4},
            {1,2,6,5},
            {2,3,7,6},
            {3,0,4,7},
    };
    private void drawBodyPart(Canvas canvas, Coordinate [] cube_vertices, Paint paint)
    {
        for(int [] face_v : face_vertices) {
            drawFace(canvas, cube_vertices, face_v, paint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        width = w;
        height = h;
    }

    private final double absHalfInc = Math.abs(angleInc / 2);

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        double scale_factor;
        if (width < height) {
            double x = trunk.v[2].x - trunk.v[1].x;
            double y = trunk.v[2].y - trunk.v[1].y;
            double z = trunk.v[2].z - trunk.v[1].z;
            scale_factor = width / 4.
                    / Math.sqrt(x*x + y*y + z*z); // width of unscaled trunk
        } else {
            double x = trunk.v[1].x - trunk.v[0].x;
            double y = trunk.v[1].y - trunk.v[0].y;
            double z = trunk.v[1].z - trunk.v[0].z;
            scale_factor = height / 4.
                    / Math.sqrt(x*x + y*y + z*z); // height of unscaled trunk
        }
        final double absRA = Math.abs(rotateAngle);
        if (absRA > absHalfInc && absRA < 30. - absHalfInc && (absRA < 15-absHalfInc || absRA >15+absHalfInc)) {
            double angle;
            if (absRA < 15. - absHalfInc) {
                angle = signum(rotateAngle) * signum(angleInc) * 8 * absHalfInc;
            } else {
                angle = -signum(rotateAngle) * signum(angleInc) * 8 * absHalfInc;
            }
            if (rotateAngle < 0) {
                step(angle, r_thigh, r_lower_leg, r_foot);
            } else {
                step(angle, l_thigh, l_lower_leg, l_foot);
            }
        }
        if (absRA > absHalfInc && absRA < 45.-absHalfInc && (absRA < 45.2-absHalfInc || absRA >45./2+absHalfInc)) {
            double angle;
            double halfR = (absRA > 45./2 ? absRA - 45./2 : absRA);
            if (halfR < 45./4 - absHalfInc) {
                angle = signum(rotateAngle) * signum(angleInc) * 4 * absHalfInc;
            } else {
                angle = -signum(rotateAngle) * signum(angleInc) * 4 * absHalfInc;
            }
            if (absRA > 45./2) {
                angle *= 1.3;
            }
            armUpDown(angle, new BodyPart[]{ r_upper_arm, r_forearm, r_hand });
            armUpDown(angle, new BodyPart[]{ l_upper_arm, l_forearm, l_hand });
        }
        if (absRA > 45. + absHalfInc) {
            angleInc = -signum(rotateAngle) * abs(angleInc);
        }
        BodyPart [] bp = new BodyPart[allParts.length];
        rotateAngle += angleInc;
        for (int i = 0; i < allParts.length; i++) {
            bp[i] = new BodyPart(allParts[i].v, allParts[i].paint);
            bp[i].v = scale(bp[i].v, scale_factor, scale_factor, scale_factor);
            bp[i].v = quaternionRotate(bp[i].v, rotateAngle, 0, 1, 0);
            bp[i].v = translate(bp[i].v, width/2, height/2, 0);
        }
        double angle = -rotateAngle / 2;
        if (rotateAngle < 0)
            rotateArm(angle, new BodyPart[]{ bp[0], bp[1], bp[2]}, 6);
        else
            rotateArm(angle, new BodyPart[]{ bp[3], bp[4], bp[5]}, 5);
        Arrays.sort(bp, (o1, o2) -> (int) signum(o1.v[7].z - o2.v[7].z));
        for (BodyPart bodyPart : bp)
            drawBodyPart(canvas, bodyPart.v, bodyPart.paint);
    }

    private void step(double angle, BodyPart thigh, BodyPart lower_leg, BodyPart foot) {
        double x = thigh.v[5].x;
        double y = thigh.v[5].y;
        double z = thigh.v[5].z;
        thigh.v = translate(thigh.v, -x, -y, -z);
        thigh.v = quaternionRotate(thigh.v, angle, 1, 0, 0);
        thigh.v = translate(thigh.v, x, y, z);
        x = thigh.v[4].x - lower_leg.v[5].x;
        y = thigh.v[4].y - lower_leg.v[5].y;
        z = thigh.v[4].z - lower_leg.v[5].z;
        lower_leg.v = translate(lower_leg.v, x, y, z);
        foot.v = translate(foot.v, x, y, z);
    }

    private void armUpDown(double angle, BodyPart [] arm_part) {
        double x = arm_part[0].v[1].x;
        double y = arm_part[0].v[1].y;
        double z = arm_part[0].v[1].z;
        for (BodyPart p : arm_part) {
            p.v = translate(p.v, -x, -y, -z);
            p.v = quaternionRotate(p.v, angle, 1, 0, 0);
            p.v = translate(p.v, x, y, z);
        }
        x = arm_part[1].v[1].x;
        y = arm_part[1].v[1].y;
        z = arm_part[1].v[1].z;
        for (int i = 1; i < arm_part.length; i++) {
            arm_part[i].v = translate(arm_part[i].v, -x, -y, -z);
            arm_part[i].v = quaternionRotate(arm_part[i].v, angle, 1, 0, 0);
            arm_part[i].v = translate(arm_part[i].v, x, y, z);
        }
    }

    private void rotateArm(double angle, BodyPart [] bp, int vertex) {
        double x = bp[0].v[vertex].x;
        double y = bp[0].v[vertex].y;
        double z = bp[0].v[vertex].z;
        for (BodyPart b : bp) {
            b.v = translate(b.v, -x, -y, -z);
            b.v = quaternionRotate(b.v, angle, 0, 0, 1);
            b.v = translate(b.v, x, y, z);
        }
    }

    public double [] identityMatrix()
    {
        return new double[] {
                1,0,0,0,
                0,1,0,0,
                0,0,1,0,
                0,0,0,1,
        };
    }
    public Coordinate Transformation(Coordinate vertex, double []matrix)
    {
        Coordinate result=new Coordinate();
        result.x=matrix[0]*vertex.x+matrix[1]*vertex.y+matrix[2]*vertex.z+matrix[3];
        result.y=matrix[4]*vertex.x+matrix[5]*vertex.y+matrix[6]*vertex.z+matrix[7];
        result.z=matrix[8]*vertex.x+matrix[9]*vertex.y+matrix[10]*vertex.z+matrix[11];
        result.w=matrix[12]*vertex.x+matrix[13]*vertex.y+matrix[14]*vertex.z+matrix[15];
        return result;
    }
    public Coordinate[]Transformation(Coordinate []vertices, double []matrix)
    {
        Coordinate [] result=new Coordinate[vertices.length];
        for (int i=0;i<vertices.length;i++)
        {
            result[i]=Transformation(vertices[i],matrix);
            result[i].Normalise();
        }
        return result;
    }
    public Coordinate [] translate(Coordinate []vertices, double tx, double ty, double tz)
    {
        double []matrix= identityMatrix();
        matrix[3]=tx;
        matrix[7]=ty;
        matrix[11]=tz;
        return Transformation(vertices,matrix);
    }
    public Coordinate [] quaternionRotate(Coordinate []vertices, double theta, int abtX, int abtY, int abtZ)
    {
        double w = cos(Math.toRadians(theta/2));
        double x = sin(Math.toRadians(theta/2)) * abtX;
        double y = sin(Math.toRadians(theta/2)) * abtY ;
        double z = sin(Math.toRadians(theta/2)) * abtZ;

        double []matrix = identityMatrix();
        matrix[0] = w*w + x*x - y*y - z*z;
        matrix[1] = 2*x*y - 2*w*z;
        matrix[2] = 2*x*z + 2*w*y;
        matrix[3] = 0;
        matrix[4] = 2*x*y + 2*w*z;
        matrix[5] = w*w + y*y - x*x - z*z;
        matrix[6] = 2*y*z - 2*w*x;
        matrix[7] = 0;
        matrix[8] = 2*x*z -2*w*y;
        matrix[9] = 2*y*z + 2*w*x;
        matrix[10] = w*w + z*z - x*x - y*y;
        matrix[11] = 0;
        matrix[12] = 0;
        matrix[13] = 0;
        matrix[14] = 0;
        matrix[15] = 1;

        return Transformation(vertices, matrix);
    }
    private Coordinate [] scale(Coordinate [] vertices, double sx, double sy, double sz)
    {
        double [] matrix= identityMatrix();
        matrix[0]=sx;
        matrix[5]=sy;
        matrix[10]=sz;
        return Transformation(vertices,matrix);
    }
}
