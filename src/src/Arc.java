import java.util.Random;
public class Arc {
  Perceptron origin;
  Perceptron destination;
  double value;
  double weight;
  
  Arc(Perceptron origin,double value,Perceptron destination){
    this.origin = origin;
    this.destination = destination;
    this.value = value;
    Random r = new Random();
    this.weight = -1 + 2*r.nextDouble();
  }
  
  
  Arc(double value,Perceptron destination){
    this.origin = null;
    this.destination = destination;
    this.value = value;
    this.weight = 1;
  }
}
