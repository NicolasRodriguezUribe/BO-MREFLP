import Structures.SolutionPositionsCosts;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class Common {

    public SortedArrayList CopySortedArrayListSolution(SortedArrayList originalSol) {
        SortedArrayList copySol = new SortedArrayList();
        for (SolutionPositionsCosts temp : originalSol.sortedArrayList) {
            SolutionPositionsCosts aux = CopySolutionPositionCosts(temp);
            copySol.add(aux);
        }
        return copySol;
    }

    public ArrayList<ArrayList<Integer>> CopySolution(ArrayList<ArrayList<Integer>> originalSol) {
        ArrayList<ArrayList<Integer>> copySol = new ArrayList<>(originalSol.size());
        for (ArrayList<Integer> integers : originalSol) {
            ArrayList<Integer> aux = new ArrayList<>(integers);
            copySol.add(aux);
        }
        return copySol;
    }

    public ArrayList<ArrayList<Float>> CopyPositions(ArrayList<ArrayList<Float>> originalSol) {
        ArrayList<ArrayList<Float>> copySol = new ArrayList<>(originalSol.size());
        for (ArrayList<Float> floats : originalSol) {
            ArrayList<Float> aux = new ArrayList<>(floats);
            copySol.add(aux);
        }
        return copySol;
    }

    public SolutionPositionsCosts CopySolutionPositionCosts(SolutionPositionsCosts solutionPositionsCosts) {
        Common common = new Common();
        return new SolutionPositionsCosts(common.CopySolution(solutionPositionsCosts.getSolution()), common.CopyPositions(solutionPositionsCosts.getPositions()), solutionPositionsCosts.getCostSolutionO1(), solutionPositionsCosts.getCostSolutionO2());
    }

    public ArrayList<Float> CopySingleArrayListF(ArrayList<Float> originalSol) {
        ArrayList<Float> copySol = new ArrayList<>(originalSol.size());
        copySol.addAll(originalSol);
        return copySol;
    }

    public ArrayList<ArrayList<String>> CopyArrayListS(ArrayList<ArrayList<String>> originalSol) {
        ArrayList<ArrayList<String>> copySol = new ArrayList<>(originalSol.size());
        for (int i = 0; i < originalSol.size(); i++) {
            ArrayList<String> aux = new ArrayList<>(originalSol.get(i));
            copySol.add(aux);
        }
        return copySol;
    }

    public ArrayList<ArrayList<Integer>> CopyArrayList(ArrayList<ArrayList<Integer>> originalSol) {
        ArrayList<ArrayList<Integer>> copySol = new ArrayList<>(originalSol.size());
        for (ArrayList<Integer> integers : originalSol) {
            ArrayList<Integer> aux = new ArrayList<>(integers);
            copySol.add(aux);
        }
        return copySol;
    }

    public void CreateFile(String route, SortedArrayList paretoOptimal) {
        try {
            FileWriter myWriter = new FileWriter(route);
            for (int i = 0 ; i < paretoOptimal.sortedArrayList.size(); i++)
            {
                myWriter.write(paretoOptimal.sortedArrayList.get(i).getCostSolutionO1() + " " + paretoOptimal.sortedArrayList.get(i).getCostSolutionO2() + "\n");
            }
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void CreateFileTime(String route, Long time) {
        try {
            FileWriter myWriter = new FileWriter(route);
            myWriter.write(time + "\n");
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public ArrayList<Integer> Topology(int size) {
        ArrayList topology = new ArrayList(2);

        switch (size) {
            case 6:
                topology.add(2);
                topology.add(3);
                //row = 2;
                //facilitiesRow = 3;
                break;

            case 8:
                topology.add(2);
                topology.add(4);
                //row = 2;
                //facilitiesRow = 4;
                break;

            case 10:
                topology.add(2);
                topology.add(5);
                //row = 2;
                //facilitiesRow = 5;
                break;

            case 12:
                topology.add(3);
                topology.add(4);
                //row = 3;
                //facilitiesRow = 4;
                break;

            case 15:
                topology.add(3);
                topology.add(5);
                //row = 3;
                //facilitiesRow = 5;
                break;

            case 20://20 = 4 x 5, chen
                topology.add(4);
                topology.add(5);
                //row = 4;
                //facilitiesRow = 5;
                break;

            case 25:
                topology.add(5);
                topology.add(5);
                //row = 5;
                //facilitiesRow = 5;
                break;

            case 30:
                topology.add(5);
                topology.add(6);
                //row = 5;
                //facilitiesRow = 6;
                break;

            case 35:
                topology.add(5);
                topology.add(7);
                //row = 5;
                //facilitiesRow = 7;
                break;

            case 40:
                topology.add(5);
                topology.add(8);
                //row = 5;
                //facilitiesRow = 8;
                break;

            case 45:
                topology.add(5);
                topology.add(9);
                //row = 5;
                //facilitiesRow = 9;
                break;

            case 50:
                topology.add(5);
                topology.add(10);
                //row = 5;
                //facilitiesRow = 10;
                break;

            case 60:
                topology.add(6);
                topology.add(10);
                //row = 6;
                //facilitiesRow = 10;
                break;

        }
        return topology;
    }
}