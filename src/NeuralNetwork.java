import java.util.List;

public class NeuralNetwork {

    public static final int NUMBER_OF_INPUT_NODES = 4;
    public static final int NUMBER_OF_HIDDEN_LAYER_NODES = 4;

    public static void main(String[] args) {
        Perceptron input[] = new Perceptron[NUMBER_OF_INPUT_NODES];
        Perceptron hidden_layer[] = new Perceptron[NUMBER_OF_HIDDEN_LAYER_NODES];
        Perceptron output = new Perceptron(NeuralNetwork::input_function, NeuralNetwork::activation_function);

        //Conjunto de treino 1
        int conjuntosTreino[][] = {{1, 1, 0, 0}, {1, 0, 0, 0}, {0, 0, 0, 0}, {1, 1, 1, 0}};
        int solucoes[] = {1, 0, 1, 0};

        //Constroi as conexoes
        buildNeuralNetwork(input, hidden_layer, output, conjuntosTreino);
        System.out.println();
    }

    static void buildNeuralNetwork(Perceptron input[], Perceptron hidden_layer[], Perceptron output, int conjuntosTreino[][]) {

        for (int i = 0; i < NUMBER_OF_INPUT_NODES; i++) {
            input[i] = new Perceptron(NeuralNetwork::input_function, NeuralNetwork::activation_function);
            input[i].add_input_arc(new Arc(null, conjuntosTreino[0][i], input[i]));

        }
        for (int i = 0; i < NUMBER_OF_HIDDEN_LAYER_NODES; i++) {
            hidden_layer[i] = new Perceptron(NeuralNetwork::input_function, NeuralNetwork::activation_function);
            //For each output_node from

            for (int j = 0; j < NUMBER_OF_INPUT_NODES; j++) {
                Arc connection = new Arc(input[j], 0, hidden_layer[i]);
                input[j].add_output_arc(connection);
                hidden_layer[i].add_input_arc(connection);
            }
            Arc outputConnection = new Arc(hidden_layer[i], 0, output);
            hidden_layer[i].add_output_arc(outputConnection);
            output.add_input_arc(outputConnection);

        }
    }


    public static double activation_function(double value) {
        return (double) 1 / (1 + Math.pow(Math.E, (-value)));
    }

    public static double input_function(List<Double> list) {
        int aggregation = 0;
        for (double val : list) {
            aggregation += val;
        }
        return aggregation;
    }
}
