import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class NeuralNetwork {
  
  public static int NUMBER_OF_INPUT_NODES;
  public static  int NUMBER_OF_HIDDEN_LAYER_NODES;
  public static double learningRate =0.5;
  public static void main(String[] args) throws FileNotFoundException {
    Scanner trainingInfo = new Scanner(new File(args[0]));
    int conjuntosTreino[][]; //= {{0,0,0,0},{0,0,0,1},{0,0,1,0},{0,0,1,1},{0,1,0,0},{0,1,0,1},{0,1,1,0},{0,1,1,1},{1,0,0,0},{1,0,0,1},{1,0,1,0},{1,0,1,1},{1,1,0,0},{1,1,0,1},{1,1,1,0},{1,1,1,1}};
    int solucoes[];// = {0,1,1,0,1,0,0,1,1,0,0,1,0,1,1,0};
    /*

      Read from file
      Format:

      number of input nodes
      number of examples
      training set    solution

    */
    NUMBER_OF_INPUT_NODES = Integer.parseInt(trainingInfo.nextLine());
    int nTrainingSets = Integer.parseInt(trainingInfo.nextLine());
    conjuntosTreino = new int[nTrainingSets][NUMBER_OF_INPUT_NODES];
    solucoes = new int[nTrainingSets];

    for(int i=0;i<nTrainingSets;i++){
      for(int j=0;j<NUMBER_OF_INPUT_NODES;j++){
        conjuntosTreino[i][j] = trainingInfo.nextInt();
      }
      solucoes[i]= trainingInfo.nextInt();
    }

    trainingInfo.close();

    Scanner scan = new Scanner(System.in);
    System.out.print("Number of hidden-layer nodes: ");
    NUMBER_OF_HIDDEN_LAYER_NODES = scan.nextInt();

    Perceptron input[] = new Perceptron[NUMBER_OF_INPUT_NODES];
    Perceptron hidden_layer[] = new Perceptron[NUMBER_OF_HIDDEN_LAYER_NODES];
    Perceptron output = new Perceptron(NeuralNetwork::input_function,NeuralNetwork::activation_function);

    //Conjunto de treino 1
    scan.useLocale(Locale.US);
    System.out.print("What is the learning rate(\u03BB): ");
    learningRate = scan.nextDouble();
    
    //Constroi as conexoes
    buildNeuralNetwork(input,hidden_layer,output,conjuntosTreino);
    
    System.out.println("Building the Neural Network, please wait...");
    long initTime = System.nanoTime();

    for(int i=0;i<solucoes.length;i++) {
      double obtainedResponse;
      
      //Loads input from conjuntoTreino
      for(int j=0;j<NUMBER_OF_INPUT_NODES;j++){
        for(Arc arc: input[j].input_links){
          arc.value = conjuntosTreino[i][j];
        }
      }
      //Calcs outputs and backpropagates until the output differs 0.05 from the solution
      boolean first_access = true;
      do {
        
        obtainedResponse= calcOutput(input,hidden_layer,output);
        if (Math.abs(obtainedResponse - solucoes[i]) > 0.05){
          //Back Propagation
          
          output.sigma = output.output*(1 - output.output)*(obtainedResponse-solucoes[i]);
          output.bias.weight -= learningRate*output.sigma;
          for(int j=0;j<NUMBER_OF_HIDDEN_LAYER_NODES;j++){
            hidden_layer[j].sigma = (output.sigma*hidden_layer[j].output_links.get(0).weight)*(hidden_layer[j].output*(1-hidden_layer[j].output));
          }
          
          for(int j=0;j<NUMBER_OF_INPUT_NODES;j++){
            for(Arc arc : input[j].output_links){
              arc.weight -= learningRate*arc.destination.sigma*input[j].output;
            }
          }
          
          for(int j=0;j<NUMBER_OF_HIDDEN_LAYER_NODES;j++){
            for(Arc arc : hidden_layer[j].output_links){
              arc.weight -= learningRate*arc.destination.sigma*hidden_layer[j].output;
            }
            hidden_layer[j].bias.weight -= learningRate*hidden_layer[j].sigma;
          }
          first_access=false;
        }
      } while (Math.abs(obtainedResponse - solucoes[i]) > 0.05);
      if (!first_access){
        i=0;
      }
    }
    long endTime = System.nanoTime();
    
    System.out.println("Using \u03BB="+learningRate+" passed "+(endTime-initTime) +"ns");
    
    System.out.println("Training Done");
    System.out.println("Ready to receive inputs");
    System.out.print("Input example: ");

    for(int i=0;i<NUMBER_OF_INPUT_NODES-1;i++){
      System.out.print(conjuntosTreino[0][i]+", ");
    }
    System.out.println(conjuntosTreino[0][NUMBER_OF_INPUT_NODES-1]);

    System.out.print("Bits (comma separated): ");
    String querie;
    querie = scan.next();
    while(!querie.toLowerCase().equals("exit")){
      String data[] = querie.split(",");
      for(int i=0;i<data.length;i++){
        input[i].input_links.get(0).value = Integer.parseInt(data[i]);
      }
      double result = calcOutput(input,hidden_layer,output);
      
      if (result <0.5){
        System.out.println("Even");
      }else{
        System.out.println("Odd");
      }
      
      System.out.print("Bits (comma separated): ");
      querie=scan.next();
    }
    
  }
  
  static double calcOutput(Perceptron[] input,Perceptron[] hidden_layer,Perceptron output){
    for(int j=0;j<NUMBER_OF_INPUT_NODES;j++){
      input[j].calc_output();
    }
    for(int j=0;j<NUMBER_OF_HIDDEN_LAYER_NODES;j++){
      hidden_layer[j].calc_output();
    }
    output.calc_output();
    return output.output;
  }
  
  
  
  
  
  
  
  static void buildNeuralNetwork(Perceptron input[],Perceptron hidden_layer[],Perceptron output,int conjuntosTreino[][]){
    
    
    for(int i=0;i<NUMBER_OF_INPUT_NODES;i++){
      input[i]= new Perceptron(NeuralNetwork::input_function,NeuralNetwork::activation_function);
      input[i].add_input_arc(new Arc(0,input[i]));
      input[i].bias = new Arc(null,0, input[i]);
    }
    for(int i=0;i<NUMBER_OF_HIDDEN_LAYER_NODES;i++){
      hidden_layer[i] = new Perceptron(NeuralNetwork::input_function,NeuralNetwork::activation_function);
      hidden_layer[i].bias = new Arc(null,1, hidden_layer[i]);
      //For each output_node from
      for(int j=0;j<NUMBER_OF_INPUT_NODES;j++){
        Arc connection = new Arc(input[j],0,hidden_layer[i]);
        input[j].add_output_arc(connection);
        hidden_layer[i].add_input_arc(connection);
      }
      Arc outputConnection = new Arc(hidden_layer[i],0,output);
      hidden_layer[i].add_output_arc(outputConnection);
      output.add_input_arc(outputConnection);
    }
    output.bias= new Arc(null,1, output);
  }
  
  

    public static double activation_function(double value){
        return (double) 1 / (1+Math.pow(Math.E,(-value)));
    }
    public static double input_function(List<Double> list){
        double aggregation=0;
        for(double val: list){
            aggregation+=val;
        }
        return aggregation;
    }
}
