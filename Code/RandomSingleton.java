import java.util.Random;

public class RandomSingleton {
    private static RandomSingleton instance;
    private Random rnd;

    public Random getRnd() {
        return rnd;
    }

    public void setRnd(Random rnd) {
        this.rnd = rnd;
    }

    private RandomSingleton() {
        rnd = new Random();
        rnd.setSeed(123456);
        //rnd.setSeed(1234);
    }



    public static RandomSingleton getInstance() {
        if(instance == null) {
            instance = new RandomSingleton();
        }
        return instance;
    }

    public double nextDouble() {
        return rnd.nextDouble();
    }

    public int nextInt(int max) {
        return rnd.nextInt(max);
    }

    public int nextInt() {
        return rnd.nextInt();
    }

    public boolean nextBoolean()
    {
        return rnd.nextBoolean();
    }
}