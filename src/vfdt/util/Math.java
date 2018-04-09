package vfdt.util;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 12
 */
public class Math {
    public static Double[] solveEq(double a, double b, double c) {
        Double[] x = new Double[]{null, null};
        double delta = b*b - 4.*a*c;
        if (delta == 0)
            x[0] = -b / (2. * a);
        else if (delta > 0) {
            double sqrtDelta = java.lang.Math.sqrt(delta);
            double denom = 1. / (2. * a);
            x[0] = (-b + sqrtDelta) * denom;
            x[1] = (-b - sqrtDelta) * denom;
        }
        return x;
    }
}
