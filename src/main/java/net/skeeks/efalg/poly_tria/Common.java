package net.skeeks.efalg.poly_tria;

public class Common {

    public static void main(String[] args) {
        Line g1 = new Line(new Point(3, 2), new Vector(2, 5));
        Line g2 = new Line(new Point(4, 2), new Vector(-2, 2));

        Vector v1 = new Vector(0, 1);
        Vector v2 = new Vector(100, 0);


        Polygon polygon = new Polygon(        new Point(5, 4),new Point(4, 2),new Point(3, 4),new Point(1, 1),new Point(7, 1));
        // Polygon polygon = new Polygon(new Point(4, 2), new Point(5, 4),new Point(7, 1),new Point(1, 1),new Point(3, 4));
    }

    /**
     * Returns true if the points of the passed polygon are ordered in clockwise direction.
     */
    public static boolean isClockwise(Polygon polygon) {
        if (polygon.points.length < 3)
            throw new IllegalArgumentException("min size is 3");

        int sum = 0;
        Point currP, nextP;
        for(int i = 0; i < polygon.points.length; i++){
            currP = polygon.points[i];
            // vector product
            nextP = polygon.points[(i+1) % polygon.points.length];
            sum += (nextP.x - currP.x)*(nextP.y + currP.y);
        }
        // last 
        currP = polygon.points[polygon.points.length - 1];;
        nextP = polygon.points[0];
        // vector product
        sum += (nextP.x - currP.x)*(nextP.y - currP.y);
        return sum > 0;

    }

    /**
     * Returns the triangle area
     */
    public static double triangleArea(Vector v1, Vector v2) {
        // if((v1.x == 0 && v2.x == 0) || (v1.y == 0 && v2.y == 0)) return 0.0;
        return ((v1.x * v2.y) - (v1.y * v2.x)) / 2.0;
    }

    public static Point leftOrthVec(Vector v) {
        return new Vector(-v.x, v.y);
    }

    public static Point rightOrtVec(Vector v) {
        return new Vector(v.x, -v.y);
    }

    public static DPoint intersectionPoint(Line l1, Line l2) {
        int det = det(l1.dirVec.x, -l2.dirVec.x, l1.dirVec.y, -l2.dirVec.y);

        if (det == 0) {
            return null;
        }

        // matrix multiplication
        int alphaBeforeD = (-l2.dirVec.y * (l2.start.x - l1.start.x)) + (l2.dirVec.x * (l2.start.y - l1.start.y));
        int betaBeforeD = (-l1.dirVec.y * ((l2.start.x - l1.start.x))) + (l1.dirVec.x * (l2.start.y - l1.start.y));

        // apply the det
        double alpha = alphaBeforeD * (1.0 / det);
        double beta = betaBeforeD * (1.0 / det);

        DPoint point1 = new DPoint(l1.start.x + alpha * l1.dirVec.x, l1.start.y + alpha * l1.dirVec.y);

        DPoint point2 = new DPoint(l2.start.x + beta * l2.dirVec.x, l2.start.y + beta * l2.dirVec.y);

        assert Math.abs(point1.x - point2.x) < Math.pow(10, -10);
        assert Math.abs(point1.y - point2.y) < Math.pow(10, -10);

        return point1;
    }

    public static int det(int a, int b, int c, int d) {
        return (a * d) - (c * b);
    }

    public static int[] vecProd(int[][] m1, int[] m2) {
        return new int[] { m1[0][0] * m2[0] + m1[0][1] * m2[1], m1[1][0] * m2[0] + m1[1][1] * m2[1] };
    }

    public static Vector makeVec(Point p1, Point p2) {
        return new Vector(p2.x - p1.x, p2.y - p1.y);
    }

    public static class Point {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "[" + x + ", " + y + "]";
        }
    }

    public static class DPoint {
        double x;
        double y;

        public DPoint(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "[" + x + ", " + y + "]";
        }
    }

    public static class Vector extends Point {
        public Vector(int x, int y) {
            super(x, y);
        }
    }

    public static class Line {
        public Point start;
        public Vector dirVec;

        public Line(Point start, Vector dirVec) {
            this.start = start;
            this.dirVec = dirVec;
        }
    }

    public static class LineSegment extends Line {
        public LineSegment(Point start, Point end) {
            super(start, makeVec(start, end));
        }
    }

    public static class Polygon {
        Point[] points;

        public Polygon(Point... points) {
            this.points = points;
        }
    }

}
