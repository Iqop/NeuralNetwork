import java.util.List;

public class Analysis {
    static double activateFunction(double value) {
        return 1 / (1 + Math.exp(-value));
    }

    static void train(double target, double guessedValue, List<Arc> inputs) {
        double error = Math.abs(target - guessedValue);
        for (Arc a : inputs) {
            a.weight = error * a.value * Perceptron.learningRate;
        }
    }
}
