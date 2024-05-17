package Structures;

public class PositionDistance {
    private int position;
    private double distance;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public PositionDistance(int position, double distance) {
        this.position = position;
        this.distance = distance;
    }
}
