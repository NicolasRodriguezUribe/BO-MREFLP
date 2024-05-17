import Structures.SolutionPositionsCosts;

import java.util.ArrayList;

public class Shake {
    Common common = new Common();

    public ArrayList<ArrayList<Integer>> shake_insert_random(SolutionPositionsCosts original, int k)
    {
        //System.out.println("Original: " + original.getSolution());
        ArrayList<ArrayList<Integer>> temp = common.CopyArrayList(original.getSolution());
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();
        int rowSize = temp.get(0).size();
        ArrayList<Integer> newSolution = new ArrayList<>();

        for (int currentK = 1; currentK <= k; currentK++ ) {
            int rowDel = RandomSingleton.getInstance().nextInt(temp.size());
            int columnDel = RandomSingleton.getInstance().nextInt(temp.get(rowDel).size());
            int rowIns = RandomSingleton.getInstance().nextInt(temp.size());
            int columnIns = RandomSingleton.getInstance().nextInt(temp.get(rowIns).size());

            for (ArrayList<Integer> integers : temp) {
                newSolution.addAll(integers);
            }

            int aux = newSolution.remove((rowDel * rowSize) + columnDel);
            newSolution.add((rowIns * rowSize) + columnIns, aux);

            for (ArrayList<Integer> integers : temp) {
                ArrayList<Integer> tempRow = new ArrayList<>();
                for (int j = 0; j < integers.size(); j++) {
                    tempRow.add(newSolution.remove(0));
                }
                result.add(tempRow);
            }
            temp.clear();
            temp = common.CopyArrayList(result);
            //System.out.println("Resultad: " + temp);
            //newSolution.clear();
            result.clear();
        }
        return temp;
    }
}
