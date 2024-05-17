package Structures;

public class Candidate {

    private int index;
    private SolutionPositionsCosts solution;

    public Candidate(int index, SolutionPositionsCosts solution) {
        this.index = index;
        this.solution = solution;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public SolutionPositionsCosts getSolution() {
        return solution;
    }

    public void setSolution(SolutionPositionsCosts solution) {
        this.solution = solution;
    }
}
