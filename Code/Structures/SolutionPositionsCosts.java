package Structures;

import java.util.ArrayList;

public class SolutionPositionsCosts {
    private ArrayList<ArrayList<Integer>> solution;
    private ArrayList<ArrayList<Float>> positions;
    private double mhc;
    private double cr;

    public ArrayList<ArrayList<Integer>> getSolution() {
        return solution;
    }

    public void setSolution(ArrayList<ArrayList<Integer>> solution) {
        this.solution = solution;
    }

    public ArrayList<ArrayList<Float>> getPositions() {
        return positions;
    }

    public void setPositions(ArrayList<ArrayList<Float>> positions) {
        this.positions = positions;
    }

    public double getCostSolutionO1() {
        return mhc;
    }

    public void setCostSolutionO1(double costSolutionO1) {
        this.mhc = costSolutionO1;
    }

    public double getCostSolutionO2() {
        return cr;
    }

    public void setCostSolutionO2(double costSolutionO2) {
        this.cr = costSolutionO2;
    }

    public SolutionPositionsCosts(ArrayList<ArrayList<Integer>> solution, ArrayList<ArrayList<Float>> positions, double costSolutionO1, double costSolutionO2) {
        this.solution = solution;
        this.positions = positions;
        this.mhc = costSolutionO1;
        this.cr = costSolutionO2;
    }

    @Override
    public boolean equals(Object obj) {
        SolutionPositionsCosts s1 = (SolutionPositionsCosts) obj;
        boolean equals = true;
        for (int i = 0; i < s1.getSolution().size(); i++) {
            if (!s1.getSolution().get(i).equals(this.getSolution().get(i))) {
                equals = false;
                break;
            }
        }
        return equals;
    }
}
