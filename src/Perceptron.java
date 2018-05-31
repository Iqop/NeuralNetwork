import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class Perceptron {
    List<Arc> input_links;
    List<Arc> output_links;

    Function<List<Double>, Double> input_function;
    Function<Double, Double> activation_function;

    static double absoluteError = 0;
    static double learningRate = .2;

    Perceptron(Function<List<Double>, Double> input_function, Function<Double, Double> activation_function) {
        this.input_function = input_function;
        this.activation_function = activation_function;
        input_links = new LinkedList<>();
        output_links = new LinkedList<>();
    }

    void add_input_arc(Arc arc) {
        input_links.add(arc);
    }

    void add_output_arc(Arc arc) {
        output_links.add(arc);
    }

    void calc_output() {
        double sumProduct = 0;
        int count = 0;                      // real answer to the problem
        LinkedList<Double> list = new LinkedList<>();
        for (Arc arc : input_links) {
            list.add(arc.value * arc.weight);
            sumProduct += arc.value * arc.weight;
            count++;
        }
        double guess = Analysis.activateFunction(sumProduct);

        // ajustar pesos dos arcos
        if (count % 2 == 1 && guess < 1) {
            Analysis.train(1, guess,input_links);
        } else if (count % 2 == 0 && guess > 0) {
            Analysis.train(0, guess,input_links);
        }
        else {
            // guess is correct
        }

        double value = input_function.apply(list);
        double output = activation_function.apply(value);
    }

}