/**Class that contains static methods for vector arithmetic
 * 
 * 
 *
 */
public class ExtendedMath {
    /**

    @return dot product of two vectors represented as arrays
    PRECONDITION: arrays are of equal size

     */

    public static double dotProduct(double[] v1, double[] v2) {
        int total = 0;
        for (int i = 0; i < v1.length; i++) {
            total+=(v1[i]*v2[i]);
        }
        return total;
    }
    /**
    @return cross product of two vectors represented as arrays
    PRECONDITION: both arrays are of length 3

     */
    public static double[] crossProduct(double[] v1, double[] v2){
        double[] array = new double[3];
        if(v1.length!=3||v2.length!=3){
            return array;
        }
        array[0]=v1[1]*v2[2]-v1[2]*v2[1];
        array[1]=v1[2]*v2[0]-v1[0]*v2[2];
        array[2]=v1[0]*v2[1]-v1[1]*v2[0];
        return array;
    }

}
