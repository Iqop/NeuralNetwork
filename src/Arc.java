import java.util.Random;

public class Arc {
    Perceptron origin;
    double value;
    double weight;

    Arc(Perceptron origin,double value){
        this.origin = origin;
        this.value = value;
        Random r = new Random();
        this.weight = -1 + 2*r.nextDouble();
    }
}
