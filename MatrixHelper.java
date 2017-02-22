package ir.mrgkrahimy.pso_implementation;

/**
 * Created by vincent on 2/21/2017.
 */
public class MatrixHelper {


    public static float[] min(float[] position, int x) {
        int l = position.length;
        float[] res = new float[l];
        for (int i = 0; i < l; i++) {
            if (position[i] > x)
                res[i] = x;
            else res[i] = position[i];
        }
        return res;
    }

    public static float[] max(float[] position, int x) {
        int l = position.length;
        float[] res = new float[l];
        for (int i = 0; i < l; i++) {
            if (position[i] < x)
                res[i] = x;
            else res[i] = position[i];
        }
        return res;
    }

    public static float[] multArray(float[] arr, float n) {
        int l = arr.length;
        float[] res = new float[l];
        for (int i = 0; i < l; i++) {
            res[i] = arr[i] * n;
        }
        return res;
    }

    public static float[] subtractArray(float[] arr1, float[] arr2) {
        int l = arr1.length;
        float[] res = new float[l];
        for (int i = 0; i < l; i++) {
            res[i] = arr1[i] - arr2[i];
        }
        return res;
    }

    public static float[] addArrays(float[] arr1, float[] arr2, float[] arr3) {
        int l = arr1.length;
        float[] res = new float[l];
        for (int i = 0; i < l; i++) {
            res[i] = arr1[i] + arr2[i] + arr3[i];
        }
        return res;
    }

    public static float[] addArrays(float[] arr1, float[] arr2) {
        int l = arr1.length;
        float[] res = new float[l];
        for (int i = 0; i < l; i++) {
            res[i] = arr1[i] + arr2[i];
        }
        return res;
    }

    public static float calcVectorLength(float[] v) {
        float sum = 0;
        for (int i = 0; i < v.length; i++) {
            sum += Math.pow(v[i], 2);
        }
        return (float) Math.sqrt(sum);
    }

}
