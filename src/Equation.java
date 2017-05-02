import java.util.function.DoubleFunction;
import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

/**
 * Created by s641173 on 11/4/2016.
 */
public class Equation implements DoubleFunction<Double> {
    protected String eqn;
    protected RPNCalc obj;

    static ScriptEngineManager mgr = new ScriptEngineManager();
    static ScriptEngine engine = mgr.getEngineByName("JavaScript");

    public Equation(String s, RPNCalc obj) {
        eqn = sanitize(s);
        this.obj = obj;
    }

    public Double apply(double d) {
        try {
            return (Double) engine.eval(eqn.replaceAll("x", " " + d + " "));
        } catch (ScriptException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    protected String sanitize(String s){
        String str = s;
        //catch nat log
        str = str.replaceAll("ln","Math.log");

        //catch base 10 log
        str = str.replaceAll("ln","Math.log");

        if(obj.radians){
            //catch sine
            str = str.replaceAll("sin","Math.sin");

            //catch cosine
            str = str.replaceAll("cos","Math.cos");

            //catch tangent
            str = str.replaceAll("tan","Math.tan");
        }else {
            //catch sine
            while(str.contains(" sin(")){
                int x = str.indexOf((" sin("));
                String left;

            }

            //catch cosine
            str = str.replaceAll("ln","Math.log");

            //catch tangent
            str = str.replaceAll("ln","Math.log");
        }



        return str;
    }


}
