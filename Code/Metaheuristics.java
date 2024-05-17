import Structures.PositionDistance;
import Structures.SolutionPositionsCosts;

import java.util.ArrayList;

public class Metaheuristics {
    Constructive constructive = new Constructive();
    LocalSearch ls = new LocalSearch();
    Common common = new Common();
    Shake shake = new Shake();
    Evaluation evaluation = new Evaluation();


    public SortedArrayList GRASP(int iterations, double pC1, double pC2, double pC3, double pC4, double alpha, double beta, String lsString, Instance instanceMHC, Instance instanceCR) {
        SortedArrayList pareto = new SortedArrayList();
        SortedArrayList results;
        ArrayList<Integer> data = common.Topology(instanceCR.numFacilities);
        int row = data.get(0);
        int facilitiesRow = data.get(1);

        for (int i = 0; i < iterations * pC1; i++) {
            SolutionPositionsCosts result = constructive.C1_GR(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
            pareto.add(result);
        }

        for (int i = 0; i < iterations * pC2; i++) {
            SolutionPositionsCosts result = constructive.C2_RG(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
            pareto.add(result);
        }

        for (int i = 0; i < iterations * pC3; i++) {
            SolutionPositionsCosts result = constructive.C3_GR(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
            pareto.add(result);
        }

        for (int i = 0; i < iterations * pC4; i++) {
            SolutionPositionsCosts result = constructive.C4_RG(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
            pareto.add(result);
        }

        if (lsString.equals("DM"))
            results = ls.combiningDM(pareto, instanceMHC.getWeights(), instanceCR.getWeights(), beta);
        else
            results = ls.combiningMD(pareto, instanceMHC.getWeights(), instanceCR.getWeights(), beta);

        return results;
    }

    public SortedArrayList GRASP_LSD(int iterations, double pC1, double pC2, double pC3, double pC4, double alpha, double beta, String lsString, Instance instanceMHC, Instance instanceCR) {
        SortedArrayList pareto = new SortedArrayList();
        ArrayList<Integer> data = common.Topology(instanceCR.numFacilities);
        int row = data.get(0);
        int facilitiesRow = data.get(1);

        //Constructive
        for (int i = 0; i < iterations * pC1; i++) {
            SolutionPositionsCosts result = constructive.C1_GR(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
            pareto.add(result);
        }

        for (int i = 0; i < iterations * pC2; i++) {
            SolutionPositionsCosts result = constructive.C2_RG(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
            pareto.add(result);
        }

        for (int i = 0; i < iterations * pC3; i++) {
            SolutionPositionsCosts result = constructive.C3_GR(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
            pareto.add(result);
        }

        for (int i = 0; i < iterations * pC4; i++) {
            SolutionPositionsCosts result = constructive.C4_RG(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
            pareto.add(result);
        }

        //Local search
        SortedArrayList aux = common.CopySortedArrayListSolution(pareto);
        boolean improve = true;
        while (improve) {
            improve = false;
            for (int i = 0; i < pareto.getSortedArrayList().size(); i++)
            {
                if(ls.dominatedHybridLocalSearchInterchangeEfficient(pareto.getSortedArrayList().get(0), instanceMHC.getWeights(), instanceCR.getWeights(), aux))
                    improve = true;
            }
            pareto = common.CopySortedArrayListSolution(aux);
        }
        return pareto;
    }

    public SortedArrayList BVNS(SortedArrayList initSols, double kMax_per, ArrayList<ArrayList<Integer>> mhc, ArrayList<ArrayList<Integer>> cr) {
        ArrayList<ArrayList<Integer>> shakenSol;
        SortedArrayList results = common.CopySortedArrayListSolution(initSols);
        int k = 1;

        while (k <= kMax_per * mhc.get(0).size()) {
            SolutionPositionsCosts temp = initSols.getSortedArrayList().get(RandomSingleton.getInstance().nextInt(initSols.getSortedArrayList().size()));
            shakenSol = shake.shake_insert_random(temp, k);
            SolutionPositionsCosts result = evaluation.Evaluation(shakenSol, mhc, cr);
            SolutionPositionsCosts crTemp = ls.hybridLocalSearchInterchange(result, mhc, cr, "cr");
            SolutionPositionsCosts mhcTemp = ls.hybridLocalSearchInterchange(result, mhc, cr, "mhc");
            if (results.add(crTemp) || results.add(mhcTemp)) {
                k = 1;
            } else
                k++;

        }
        return results;
    }

    public SortedArrayList BVNS_v2(SortedArrayList initSols, double kMax_per, ArrayList<ArrayList<Integer>> mhc, ArrayList<ArrayList<Integer>> cr) {
        SortedArrayList sortedArrayList = new SortedArrayList();
        ArrayList<ArrayList<Integer>> shakenSol;
        SortedArrayList aux = common.CopySortedArrayListSolution(initSols);
        SortedArrayList results = common.CopySortedArrayListSolution(initSols);
        int k = 1;

        if (initSols.getSortedArrayList().size() > 2)
            while (k <= (kMax_per * mhc.get(0).size())) {
                ArrayList<PositionDistance> cds = sortedArrayList.crowding_distance_assignment(results);
                ArrayList<SolutionPositionsCosts> selectedSolutions = sortedArrayList.selectSolution(results, cds, "asc");
                SolutionPositionsCosts temp = selectedSolutions.get(0);
                shakenSol = shake.shake_insert_random(temp, k);
                SolutionPositionsCosts sol = evaluation.Evaluation(shakenSol, mhc, cr);
                if (ls.dominatedHybridLocalSearchInterchangeEfficient(sol, mhc, cr, aux)) {
                    results = common.CopySortedArrayListSolution(aux);
                    k = 1;
                } else
                    k++;
            }
        return results;
    }

    public ArrayList<SortedArrayList> Test(int iterations, double pC1, double pC2, double pC3, double pC4, double alpha, Instance instanceMHC, Instance instanceCR) {
        SortedArrayList pareto = new SortedArrayList();

        ArrayList<Integer> data = common.Topology(instanceCR.numFacilities);
        int row = data.get(0);
        int facilitiesRow = data.get(1);

        for (int i = 0; i < iterations * pC1; i++) {
            SolutionPositionsCosts result = constructive.C1_GR(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
            pareto.add(result);
        }

        for (int i = 0; i < iterations * pC2; i++) {
            SolutionPositionsCosts result = constructive.C2_RG(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
            pareto.add(result);
        }

        for (int i = 0; i < iterations * pC3; i++) {
            SolutionPositionsCosts result = constructive.C3_GR(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
            pareto.add(result);
        }

        for (int i = 0; i < iterations * pC4; i++) {
            SolutionPositionsCosts result = constructive.C4_RG(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
            pareto.add(result);
        }

        SortedArrayList pareto_copy = common.CopySortedArrayListSolution(pareto);
        SortedArrayList results_DBLS = ls.DBLS(instanceMHC.getNumFacilities(), pareto, instanceMHC.getWeights(), instanceCR.getWeights());
        SortedArrayList results_AOLS = ls.AOLS(instanceMHC.getNumFacilities(), pareto_copy, instanceMHC.getWeights(), instanceCR.getWeights());

        ArrayList<SortedArrayList> results = new ArrayList<>();
        results.add(results_DBLS);
        results.add(results_AOLS);
        return results;
    }

    public ArrayList<SortedArrayList> GRASP_TEST(int iterations, double pC1, double pC2, double pC3, double pC4, double alpha, Instance instanceMHC, Instance instanceCR) {
        SortedArrayList pareto = new SortedArrayList();
        ArrayList<Integer> data = common.Topology(instanceCR.numFacilities);
        int row = data.get(0);
        int facilitiesRow = data.get(1);

        for (int i = 0; i < iterations * pC1; i++) {
            SolutionPositionsCosts result = constructive.C1_GR(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
            pareto.add(result);
        }

        for (int i = 0; i < iterations * pC2; i++) {
            SolutionPositionsCosts result = constructive.C2_RG(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
            pareto.add(result);
        }

        for (int i = 0; i < iterations * pC3; i++) {
            SolutionPositionsCosts result = constructive.C3_GR(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
            pareto.add(result);
        }

        for (int i = 0; i < iterations * pC4; i++) {
            SolutionPositionsCosts result = constructive.C4_RG(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
            pareto.add(result);
        }

        SortedArrayList pareto_copy = common.CopySortedArrayListSolution(pareto);
        SortedArrayList results_DBLS = ls.combiningDM(pareto, instanceMHC.getWeights(), instanceCR.getWeights(), 1);
        SortedArrayList results_AOLS = ls.combiningMD(pareto_copy, instanceMHC.getWeights(), instanceCR.getWeights(), 0);

        ArrayList<SortedArrayList> results = new ArrayList<>();
        results.add(results_DBLS);
        results.add(results_AOLS);
        return results;
    }
}
