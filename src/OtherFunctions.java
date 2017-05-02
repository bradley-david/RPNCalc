import net.objecthunter.exp4j.function.Function;

/**
 * Created by s641173 on 11/8/2016.
 */
public class OtherFunctions {
    static class logb extends Function{
        public logb(){
            super("logb",2);
        }
        @Override
        public double apply(double...args){
            return Math.log(args[1])/Math.log(args[0]);
        }
    }


}
