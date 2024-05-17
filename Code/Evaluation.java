import Structures.SolutionPositionsCosts;

import java.util.*;

public class Evaluation {
    public SolutionPositionsCosts Evaluation(ArrayList<ArrayList<Integer>> solution, ArrayList<ArrayList<Integer>> weights, ArrayList<ArrayList<Integer>> crs) {
        Common common = new Common();
        double mhc = 0;
        double cr = 0;
        int m = solution.size();
        ArrayList<Float> rowsWidthAux = new ArrayList<>(m);
        ArrayList<ArrayList<Float>> rowsPositions = new ArrayList<>(m);

        for (int i = 0; i < solution.size(); i++)
        {
            for (int j = 0; j < solution.get(i).size(); j++)
            {
                for (int k = 0; k < solution.size(); k++)
                {
                    for (int l = 0; l < solution.get(k).size(); l++)
                    {
                        int index1 = solution.get(i).get(j);
                        int index2 = solution.get(k).get(l);
                        double manhattan = Math.abs(i-k)+Math.abs(l-j);
                        if (index1 < index2) {
                            mhc += manhattan * weights.get(index1).get(index2);
                            cr += manhattan * crs.get(index1).get(index2);
                        }
                    }
                }
            }
        }
        return new SolutionPositionsCosts(common.CopySolution(solution), common.CopyPositions(rowsPositions), mhc, cr);
    }

    public boolean dominated(SolutionPositionsCosts sol1, SolutionPositionsCosts sol2){
        return sol1.getCostSolutionO1() <= sol2.getCostSolutionO1() && sol1.getCostSolutionO2() < sol2.getCostSolutionO2() || sol1.getCostSolutionO1() < sol2.getCostSolutionO1() && sol1.getCostSolutionO2() <= sol2.getCostSolutionO2();
    }

    public boolean equals(SolutionPositionsCosts sol1, SolutionPositionsCosts sol2){
        return sol1.getCostSolutionO1() == sol2.getCostSolutionO1() && sol1.getCostSolutionO2() == sol2.getCostSolutionO2();
    }


}