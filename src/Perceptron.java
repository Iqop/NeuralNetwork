import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class Perceptron {
    List<Arc> input_links;
    List<Arc> output_links;

    Function<List<Double>,Double> input_function;
    Function<Double,Double> activation_function;

    Perceptron(Function<List<Double>,Double> input_function, Function<Double,Double> activation_function){
        this.input_function = input_function;
        this.activation_function = activation_function;
        input_links = new LinkedList<>();
        output_links = new LinkedList<>();
    }

    void add_input_arc(Arc arc){
        input_links.add(arc);
    }
    void add_output_arc(Arc arc){
        output_links.add(arc);
    }

    void calc_output(){
        LinkedList<Double> list = new LinkedList<>();
        for(Arc arc : input_links){
            list.add(arc.value * arc.weight);
        }
        double value = input_function.apply(list);
        double output = activation_function.apply(value);
    }

}