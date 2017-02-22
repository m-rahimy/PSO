package ir.mrgkrahimy.pso_implementation;

/**
 * Created by vincent on 2/21/2017.
 */
public class CostFunctions {


    public static float sphere(float[] pos) {
        float sum = 0;
        for (float p : pos) {
            sum += Math.pow(p, 2);
        }
        return sum;
    }
}
