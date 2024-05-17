import Structures.SolutionPositionsCosts;

import java.util.ArrayList;
import java.util.Collections;

public class LocalSearch {
    Common common = new Common();
    public boolean dominatedHybridLocalSearchInterchange(SolutionPositionsCosts solution, ArrayList<ArrayList<Integer>> mhcs, ArrayList<ArrayList<Integer>> crs, SortedArrayList paretoOptimals) {
        Evaluation evaluation = new Evaluation();
        Common common = new Common();
        ArrayList<Integer> I = new ArrayList<>(solution.getSolution().size());
        ArrayList<Integer> J = new ArrayList<>();//cambiado
        SolutionPositionsCosts bestSolution = new SolutionPositionsCosts(common.CopyArrayList(solution.getSolution()), common.CopyPositions(solution.getPositions()), solution.getCostSolutionO1(), solution.getCostSolutionO2());
        SolutionPositionsCosts currentsol = new SolutionPositionsCosts(common.CopyArrayList(solution.getSolution()), common.CopyPositions(solution.getPositions()), solution.getCostSolutionO1(), solution.getCostSolutionO2());

        boolean changes = false;
        boolean improve = true;

        for (int i = 0; i < currentsol.getSolution().size(); i++) {
            I.add(i);
        }

        while (improve) {
            improve = false;

            Collections.shuffle(I, RandomSingleton.getInstance().getRnd());
            for (int i : I) {
                for (int jj = 0; jj < currentsol.getSolution().get(i).size(); jj++) {
                    J.add(jj);
                }
                Collections.shuffle(J, RandomSingleton.getInstance().getRnd());
                for (int j : J) {
                    int u = currentsol.getSolution().get(i).get(j);
                    for (int k = 0; k < solution.getSolution().size(); k++) {
                        for (int l = 0; l < solution.getSolution().get(k).size(); l++) {
                            int v = currentsol.getSolution().get(k).get(l);
                            currentsol.getSolution().get(i).set(j, v);
                            currentsol.getSolution().get(k).set(l, u);
                            SolutionPositionsCosts cost = evaluation.Evaluation(currentsol.getSolution(), mhcs, crs);
                            if (evaluation.dominated(cost, bestSolution)) {
                                bestSolution.setCostSolutionO1(cost.getCostSolutionO1());
                                bestSolution.setCostSolutionO2(cost.getCostSolutionO2());
                                bestSolution.setSolution(common.CopyArrayList(cost.getSolution()));
                                improve = true;
                                changes = true;
                            } else if (!evaluation.dominated(bestSolution, cost)) {
                                if (paretoOptimals.add(common.CopySolutionPositionCosts(cost)))
                                    changes = true;
                            }

                            currentsol.getSolution().get(i).set(j, u);
                            currentsol.getSolution().get(k).set(l, v);
                        }
                    }
                    if (improve) {
                        currentsol.setSolution(common.CopyArrayList(bestSolution.getSolution()));
                        currentsol.setCostSolutionO1(bestSolution.getCostSolutionO1());
                        currentsol.setCostSolutionO2(bestSolution.getCostSolutionO2());
                        break;
                    }
                }
                J.clear();
                if (improve)
                    break;
            }
        }
        paretoOptimals.add(bestSolution);
        return changes;
    }

    public boolean dominatedHybridLocalSearchInterchangeEfficient(SolutionPositionsCosts solution, ArrayList<ArrayList<Integer>> mhcs, ArrayList<ArrayList<Integer>> crs, SortedArrayList paretoOptimals) {
        Evaluation evaluation = new Evaluation();
        Common common = new Common();
        ArrayList<Integer> I = new ArrayList<>(solution.getSolution().size());
        ArrayList<Integer> J = new ArrayList<>();
        SolutionPositionsCosts bestSolution = new SolutionPositionsCosts(common.CopyArrayList(solution.getSolution()), common.CopyPositions(solution.getPositions()), solution.getCostSolutionO1(), solution.getCostSolutionO2());
        SolutionPositionsCosts currentsol = new SolutionPositionsCosts(common.CopyArrayList(solution.getSolution()), common.CopyPositions(solution.getPositions()), solution.getCostSolutionO1(), solution.getCostSolutionO2());

        boolean changes = false;
        boolean improve = true;

        for (int i = 0; i < currentsol.getSolution().size(); i++) {
            I.add(i);
        }

        while (improve) {
            improve = false;

            Collections.shuffle(I, RandomSingleton.getInstance().getRnd());
            for (int i : I) {
                for (int jj = 0; jj < currentsol.getSolution().get(i).size(); jj++) {
                    J.add(jj);
                }
                Collections.shuffle(J, RandomSingleton.getInstance().getRnd());
                for (int j : J) {
                    int u = currentsol.getSolution().get(i).get(j);
                    for (int k = 0; k < solution.getSolution().size(); k++) {
                        for (int l = 0; l < solution.getSolution().get(k).size(); l++) {
                            int v = currentsol.getSolution().get(k).get(l);
                            currentsol.getSolution().get(i).set(j, v);
                            currentsol.getSolution().get(k).set(l, u);
                            ArrayList<Float> aux = deltas(currentsol, mhcs, crs, i, j, k, l);

                            currentsol.setCostSolutionO1(currentsol.getCostSolutionO1() + aux.get(0));
                            currentsol.setCostSolutionO2(currentsol.getCostSolutionO2() + aux.get(1));

                            if (evaluation.dominated(currentsol, bestSolution)) {
                                bestSolution.setCostSolutionO1(currentsol.getCostSolutionO1());
                                bestSolution.setCostSolutionO2(currentsol.getCostSolutionO2());
                                bestSolution.setSolution(common.CopyArrayList(currentsol.getSolution()));
                                changes = paretoOptimals.add(common.CopySolutionPositionCosts(bestSolution));
                                improve = true;
                            } else if (!evaluation.dominated(bestSolution, currentsol) && !evaluation.equals(bestSolution, currentsol)) {
                                if (paretoOptimals.add(common.CopySolutionPositionCosts(currentsol)))
                                    changes = true;
                            }

                            currentsol.getSolution().get(i).set(j, u);
                            currentsol.getSolution().get(k).set(l, v);
                            currentsol.setCostSolutionO1(currentsol.getCostSolutionO1() - aux.get(0));
                            currentsol.setCostSolutionO2(currentsol.getCostSolutionO2() - aux.get(1));
                        }
                    }
                    if (improve) {
                        currentsol.setSolution(common.CopyArrayList(bestSolution.getSolution()));
                        currentsol.setCostSolutionO1(bestSolution.getCostSolutionO1());
                        currentsol.setCostSolutionO2(bestSolution.getCostSolutionO2());
                        break;
                    }
                }
                J.clear();
                if (improve)
                    break;
            }
        }
        return changes;
    }

    public SolutionPositionsCosts hybridLocalSearchInterchange(SolutionPositionsCosts solution, ArrayList<ArrayList<Integer>> mhcs, ArrayList<ArrayList<Integer>> crs, String factor) {
        Evaluation evaluation = new Evaluation();
        Common common = new Common();
        ArrayList<Integer> I = new ArrayList<>(solution.getSolution().size());
        ArrayList<Integer> J = new ArrayList<>();//cambiado
        SolutionPositionsCosts bestSolution = new SolutionPositionsCosts(common.CopyArrayList(solution.getSolution()), common.CopyPositions(solution.getPositions()), solution.getCostSolutionO1(), solution.getCostSolutionO2());

        SolutionPositionsCosts currentsol = new SolutionPositionsCosts(common.CopyArrayList(solution.getSolution()), common.CopyPositions(solution.getPositions()), solution.getCostSolutionO1(), solution.getCostSolutionO2());


        boolean improve = true;

        for (int i = 0; i < currentsol.getSolution().size(); i++) {
            I.add(i);
        }

        while (improve) {
            improve = false;

            Collections.shuffle(I, RandomSingleton.getInstance().getRnd());
            for (int i : I) {
                for (int jj = 0; jj < currentsol.getSolution().get(i).size(); jj++) {
                    J.add(jj);
                }
                Collections.shuffle(J, RandomSingleton.getInstance().getRnd());
                for (int j : J) {
                    int u = currentsol.getSolution().get(i).get(j);
                    for (int k = 0; k < solution.getSolution().size(); k++) {
                        for (int l = 0; l < solution.getSolution().get(k).size(); l++) {
                            int v = currentsol.getSolution().get(k).get(l);
                            currentsol.getSolution().get(i).set(j, v);
                            currentsol.getSolution().get(k).set(l, u);
                            SolutionPositionsCosts cost = evaluation.Evaluation(currentsol.getSolution(), mhcs, crs);
                            if (factor.equals("mhc")) {
                                if (cost.getCostSolutionO1() < bestSolution.getCostSolutionO1()) {
                                    bestSolution.setCostSolutionO1(cost.getCostSolutionO1());
                                    bestSolution.setCostSolutionO2(cost.getCostSolutionO2());
                                    bestSolution.setSolution(common.CopyArrayList(cost.getSolution()));
                                    bestSolution.setPositions(common.CopyPositions(cost.getPositions()));
                                    improve = true;
                                }
                            } else if (factor.equals("cr")) {
                                if (cost.getCostSolutionO2() < bestSolution.getCostSolutionO2()) {
                                    bestSolution.setCostSolutionO1(cost.getCostSolutionO1());
                                    bestSolution.setCostSolutionO2(cost.getCostSolutionO2());
                                    bestSolution.setSolution(common.CopyArrayList(cost.getSolution()));
                                    bestSolution.setPositions(common.CopyPositions(cost.getPositions()));
                                    improve = true;
                                }
                            } else
                                System.out.println("Error exchange");

                            currentsol.getSolution().get(i).set(j, u);
                            currentsol.getSolution().get(k).set(l, v);
                        }
                    }

                    if (improve) {
                        currentsol.setSolution(common.CopyArrayList(bestSolution.getSolution()));
                        currentsol.setCostSolutionO1(bestSolution.getCostSolutionO1());
                        currentsol.setCostSolutionO2(bestSolution.getCostSolutionO2());
                        break;
                    }
                }
                J.clear();
                if (improve)
                    break;
            }
        }

        return bestSolution;
    }

    public SolutionPositionsCosts hybridLocalSearchInterchangeEfficient(SolutionPositionsCosts solution, ArrayList<ArrayList<Integer>> mhcs, ArrayList<ArrayList<Integer>> crs, String factor) {
        Common common = new Common();
        ArrayList<Integer> I = new ArrayList<>(solution.getSolution().size());
        ArrayList<Integer> J = new ArrayList<>();//cambiado
        SolutionPositionsCosts bestSolution = new SolutionPositionsCosts(common.CopyArrayList(solution.getSolution()), common.CopyPositions(solution.getPositions()), solution.getCostSolutionO1(), solution.getCostSolutionO2());
        ;
        SolutionPositionsCosts currentsol = new SolutionPositionsCosts(common.CopyArrayList(solution.getSolution()), common.CopyPositions(solution.getPositions()), solution.getCostSolutionO1(), solution.getCostSolutionO2());
        ;

        boolean improve = true;

        for (int i = 0; i < currentsol.getSolution().size(); i++) {
            I.add(i);
        }

        while (improve) {
            improve = false;

            Collections.shuffle(I, RandomSingleton.getInstance().getRnd());
            for (int i : I) {
                for (int jj = 0; jj < currentsol.getSolution().get(i).size(); jj++) {
                    J.add(jj);
                }
                Collections.shuffle(J, RandomSingleton.getInstance().getRnd());
                for (int j : J) {
                    int u = currentsol.getSolution().get(i).get(j);
                    for (int k = 0; k < solution.getSolution().size(); k++) {
                        for (int l = 0; l < solution.getSolution().get(k).size(); l++) {
                            int v = currentsol.getSolution().get(k).get(l);
                            currentsol.getSolution().get(i).set(j, v);
                            currentsol.getSolution().get(k).set(l, u);
                            ArrayList<Float> deltas = deltas(currentsol, mhcs, crs, i, j, k, l);
                            currentsol.setCostSolutionO1(currentsol.getCostSolutionO1() + deltas.get(0));
                            currentsol.setCostSolutionO2(currentsol.getCostSolutionO2() + deltas.get(1));
                            //Structures.SolutionPositionsCosts cost = evaluation.Evaluation(currentsol.getSolution(), mhcs, crs);
                            if (factor.equals("mhc")) {
                                if (currentsol.getCostSolutionO1() < bestSolution.getCostSolutionO1()) {
                                    bestSolution.setCostSolutionO1(currentsol.getCostSolutionO1());
                                    bestSolution.setCostSolutionO2(currentsol.getCostSolutionO2());
                                    bestSolution.setSolution(common.CopyArrayList(currentsol.getSolution()));
                                    bestSolution.setPositions(common.CopyPositions(currentsol.getPositions()));
                                    improve = true;
                                }
                            } else if (factor.equals("cr")) {
                                if (currentsol.getCostSolutionO2() < bestSolution.getCostSolutionO2()) {
                                    bestSolution.setCostSolutionO1(currentsol.getCostSolutionO1());
                                    bestSolution.setCostSolutionO2(currentsol.getCostSolutionO2());
                                    bestSolution.setSolution(common.CopyArrayList(currentsol.getSolution()));
                                    bestSolution.setPositions(common.CopyPositions(currentsol.getPositions()));
                                    improve = true;
                                }
                            } else
                                System.out.println("ERROR Interchange");

                            currentsol.getSolution().get(i).set(j, u);
                            currentsol.getSolution().get(k).set(l, v);
                            currentsol.setCostSolutionO1(currentsol.getCostSolutionO1() - deltas.get(0));
                            currentsol.setCostSolutionO2(currentsol.getCostSolutionO2() - deltas.get(1));
                        }
                    }
                    if (improve) {
                        currentsol.setSolution(common.CopyArrayList(bestSolution.getSolution()));
                        currentsol.setCostSolutionO1(bestSolution.getCostSolutionO1());
                        currentsol.setCostSolutionO2(bestSolution.getCostSolutionO2());
                        break;
                    }
                }
                J.clear();
                if (improve)
                    break;
            }
        }

        return bestSolution;
    }

    public void hybridLocalSearchInterchangeHolderMHC(SolutionPositionsCosts solution, ArrayList<ArrayList<Integer>> mhcs, ArrayList<ArrayList<Integer>> crs, SortedArrayList paretoOptimals) {
        Evaluation evaluation = new Evaluation();
        Common common = new Common();
        ArrayList<Integer> I = new ArrayList<>(solution.getSolution().size());
        ArrayList<Integer> J = new ArrayList<>();//cambiado
        SolutionPositionsCosts bestSolution = new SolutionPositionsCosts(common.CopyArrayList(solution.getSolution()), common.CopyPositions(solution.getPositions()), solution.getCostSolutionO1(), solution.getCostSolutionO2());
        ;
        SolutionPositionsCosts currentsol = new SolutionPositionsCosts(common.CopyArrayList(solution.getSolution()), common.CopyPositions(solution.getPositions()), solution.getCostSolutionO1(), solution.getCostSolutionO2());
        ;

        boolean improve = true;

        for (int i = 0; i < currentsol.getSolution().size(); i++) {
            I.add(i);
        }

        while (improve) {
            improve = false;

            Collections.shuffle(I, RandomSingleton.getInstance().getRnd());
            for (int i : I) {
                for (int jj = 0; jj < currentsol.getSolution().get(i).size(); jj++) {
                    J.add(jj);
                }
                Collections.shuffle(J, RandomSingleton.getInstance().getRnd());
                for (int j : J) {
                    int u = currentsol.getSolution().get(i).get(j);
                    for (int k = 0; k < solution.getSolution().size(); k++) {
                        for (int l = 0; l < solution.getSolution().get(k).size(); l++) {
                            int v = currentsol.getSolution().get(k).get(l);
                            currentsol.getSolution().get(i).set(j, v);
                            currentsol.getSolution().get(k).set(l, u);
                            SolutionPositionsCosts cost = evaluation.Evaluation(currentsol.getSolution(), mhcs, crs);
                            if (cost.getCostSolutionO1() < bestSolution.getCostSolutionO1()) {
                                paretoOptimals.add(new SolutionPositionsCosts(common.CopyArrayList(currentsol.getSolution()), common.CopyPositions(currentsol.getPositions()), cost.getCostSolutionO1(), cost.getCostSolutionO2()));
                                bestSolution.setCostSolutionO1(cost.getCostSolutionO1());
                                bestSolution.setCostSolutionO2(cost.getCostSolutionO2());
                                bestSolution.setSolution(common.CopyArrayList(currentsol.getSolution()));
                                bestSolution.setPositions(common.CopyPositions(currentsol.getPositions()));
                                improve = true;
                            }

                            currentsol.getSolution().get(i).set(j, u);
                            currentsol.getSolution().get(k).set(l, v);

                            currentsol.setCostSolutionO1(currentsol.getCostSolutionO1());

                        }
                    }
                    if (improve) {
                        currentsol.setSolution(common.CopyArrayList(bestSolution.getSolution()));
                        currentsol.setCostSolutionO1(bestSolution.getCostSolutionO1());
                        break;
                    }
                }
                J.clear();
                if (improve)
                    break;
            }
        }
    }

    public void hybridLocalSearchInterchangeHolderCR(SolutionPositionsCosts solution, ArrayList<ArrayList<Integer>> mhcs, ArrayList<ArrayList<Integer>> crs, SortedArrayList paretoOptimals) {
        Evaluation evaluation = new Evaluation();
        Common common = new Common();
        ArrayList<Integer> I = new ArrayList<>(solution.getSolution().size());
        ArrayList<Integer> J = new ArrayList<>();//cambiado
        SolutionPositionsCosts bestSolution = new SolutionPositionsCosts(common.CopyArrayList(solution.getSolution()), common.CopyPositions(solution.getPositions()), solution.getCostSolutionO1(), solution.getCostSolutionO2());
        ;
        SolutionPositionsCosts currentsol = new SolutionPositionsCosts(common.CopyArrayList(solution.getSolution()), common.CopyPositions(solution.getPositions()), solution.getCostSolutionO1(), solution.getCostSolutionO2());
        ;

        boolean improve = true;

        for (int i = 0; i < currentsol.getSolution().size(); i++) {
            I.add(i);
        }

        while (improve) {
            improve = false;

            Collections.shuffle(I, RandomSingleton.getInstance().getRnd());
            for (int i : I) {
                for (int jj = 0; jj < currentsol.getSolution().get(i).size(); jj++) {
                    J.add(jj);
                }
                Collections.shuffle(J, RandomSingleton.getInstance().getRnd());
                for (int j : J) {
                    int u = currentsol.getSolution().get(i).get(j);
                    for (int k = 0; k < solution.getSolution().size(); k++) {
                        for (int l = 0; l < solution.getSolution().get(k).size(); l++) {
                            int v = currentsol.getSolution().get(k).get(l);
                            currentsol.getSolution().get(i).set(j, v);
                            currentsol.getSolution().get(k).set(l, u);
                            SolutionPositionsCosts cost = evaluation.Evaluation(currentsol.getSolution(), mhcs, crs);
                            if (cost.getCostSolutionO2() < bestSolution.getCostSolutionO2()) {
                                paretoOptimals.add(new SolutionPositionsCosts(common.CopyArrayList(currentsol.getSolution()), common.CopyPositions(currentsol.getPositions()), cost.getCostSolutionO1(), cost.getCostSolutionO2()));
                                bestSolution.setCostSolutionO1(cost.getCostSolutionO1());
                                bestSolution.setCostSolutionO2(cost.getCostSolutionO2());
                                bestSolution.setSolution(common.CopyArrayList(currentsol.getSolution()));
                                bestSolution.setPositions(common.CopyPositions(currentsol.getPositions()));
                                improve = true;
                            }

                            currentsol.getSolution().get(i).set(j, u);
                            currentsol.getSolution().get(k).set(l, v);

                            currentsol.setCostSolutionO1(currentsol.getCostSolutionO2());

                        }
                    }
                    if (improve) {
                        currentsol.setSolution(common.CopyArrayList(bestSolution.getSolution()));
                        currentsol.setCostSolutionO1(bestSolution.getCostSolutionO2());
                        break;
                    }
                }
                J.clear();
                if (improve)
                    break;
            }
        }
    }

    public ArrayList<Float> deltas(SolutionPositionsCosts solution, ArrayList<ArrayList<Integer>> mhc, ArrayList<ArrayList<Integer>> cr, int i, int j, int k, int l) {

        float deltaU_MHC = 0;
        float deltaV_MHC = 0;
        float deltaU_CR = 0;
        float deltaV_CR = 0;

        int m = solution.getSolution().size();
        int c = numColumns(solution);
        int u = solution.getSolution().get(i).get(j);
        int v = solution.getSolution().get(k).get(l);
        int currentFacility;
        int temp_U_Current_MHC;
        int temp_V_Current_MHC;
        int temp_U_Current_CR;
        int temp_V_Current_CR;

        for (int r = 0; r < m; r++) {
            for (int s = 0; s < c; s++) {
                currentFacility = solution.getSolution().get(r).get(s);
                if ((currentFacility != u) && (currentFacility != v)) {
                    if (u < currentFacility) {
                        temp_U_Current_MHC = mhc.get(u).get(currentFacility);
                        temp_U_Current_CR = cr.get(u).get(currentFacility);
                    } else {
                        temp_U_Current_MHC = mhc.get(currentFacility).get(u);
                        temp_U_Current_CR = cr.get(currentFacility).get(u);
                    }

                    if (v < currentFacility) {
                        temp_V_Current_MHC = mhc.get(v).get(currentFacility);
                        temp_V_Current_CR = cr.get(v).get(currentFacility);
                    } else {
                        temp_V_Current_MHC = mhc.get(currentFacility).get(v);
                        temp_V_Current_CR = cr.get(currentFacility).get(v);
                    }
                    int hamiltonian1 = Math.abs(i - r) + Math.abs(j - s);
                    int hamiltonian2 = Math.abs(k - r) + Math.abs(l - s);

                    deltaU_MHC += (temp_U_Current_MHC - temp_V_Current_MHC) * hamiltonian1;
                    deltaV_MHC += (temp_V_Current_MHC - temp_U_Current_MHC) * hamiltonian2;

                    deltaU_CR += (temp_U_Current_CR - temp_V_Current_CR) * hamiltonian1;
                    deltaV_CR += (temp_V_Current_CR - temp_U_Current_CR) * hamiltonian2;
                }
            }
        }
        ArrayList<Float> deltas = new ArrayList<>();
        deltas.add(deltaU_MHC + deltaV_MHC);
        deltas.add(deltaU_CR + deltaV_CR);
        return deltas;
    }

    private int numColumns(SolutionPositionsCosts solution) {
        int columns = 0;
        for (int i = 0; i < solution.getSolution().size(); i++) {
            if (solution.getSolution().get(i).size() > columns)
                columns = solution.getSolution().get(i).size();
        }
        return columns;
    }

    public SortedArrayList combiningDM(SortedArrayList original, ArrayList<ArrayList<Integer>> mhc_weights, ArrayList<ArrayList<Integer>> cr_weights, double beta) {

        SortedArrayList results_DBLS;
        SortedArrayList results_AOLS;

        int betaIt = (int) Math.round(beta * cr_weights.size());
        int remainingIt = (int) Math.round((1-beta) * cr_weights.size());

        results_DBLS = DBLS(betaIt, original, mhc_weights, cr_weights);
        results_AOLS = AOLS(remainingIt, results_DBLS, mhc_weights, cr_weights);

        return results_AOLS;
    }

    public SortedArrayList combiningMD(SortedArrayList original, ArrayList<ArrayList<Integer>> mhc_weights, ArrayList<ArrayList<Integer>> cr_weights, double beta) {
        SortedArrayList results_DBLS;
        SortedArrayList results_AOLS;

        int betaIt = (int) Math.round(beta * cr_weights.size());
        int remainingIt = (int) Math.round((1-beta) * cr_weights.size());

        results_AOLS = AOLS(remainingIt, original, mhc_weights, cr_weights);
        results_DBLS = DBLS(betaIt, results_AOLS, mhc_weights, cr_weights);

        return results_DBLS;
    }

    public SortedArrayList DBLS(int it, SortedArrayList original, ArrayList<ArrayList<Integer>> mhc_weights, ArrayList<ArrayList<Integer>> cr_weights)
    {
        SortedArrayList paretoOptimal = common.CopySortedArrayListSolution(original);
        SortedArrayList results = common.CopySortedArrayListSolution(original);
        boolean changes;
        for (int iterator = 0; iterator < it; iterator++) {
            changes = false;
            for (SolutionPositionsCosts temp : paretoOptimal.getSortedArrayList()) {
                if (dominatedHybridLocalSearchInterchangeEfficient(temp, mhc_weights, cr_weights, results))
                    changes = true;
            }

            if (changes)
                paretoOptimal = common.CopySortedArrayListSolution(results);

        }
        return results;
    }

    public SortedArrayList AOLS (int it, SortedArrayList original, ArrayList<ArrayList<Integer>> mhc_weights, ArrayList<ArrayList<Integer>> cr_weights){
        SortedArrayList paretoOptimal = common.CopySortedArrayListSolution(original);
        for (int iterator = 0; iterator < it; iterator++) {
            ArrayList<SolutionPositionsCosts> newSolutions = new ArrayList<>();
            for (SolutionPositionsCosts temp : paretoOptimal.getSortedArrayList()) {
                SolutionPositionsCosts newSolution = hybridLocalSearchInterchangeEfficient(temp, mhc_weights, cr_weights, "mhc");
                newSolutions.add(newSolution);
                newSolution = hybridLocalSearchInterchangeEfficient(temp, mhc_weights, cr_weights, "cr");
                newSolutions.add(newSolution);
            }

            for (SolutionPositionsCosts temp : newSolutions) {
                paretoOptimal.add(temp);
            }
        }
        return paretoOptimal;
    }
}
