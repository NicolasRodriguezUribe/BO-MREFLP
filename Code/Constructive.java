import Structures.Candidate;
import Structures.SolutionPositionsCosts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Constructive {


    public SolutionPositionsCosts C0_Random(ArrayList<ArrayList<Integer>> weights, ArrayList<ArrayList<Integer>> crs, int n, int m) {
        ArrayList<ArrayList<Integer>> solution = new ArrayList<>(n);
        ArrayList<Integer> facilities = new ArrayList<>();
        Evaluation evaluation = new Evaluation();

        for (int aux = 0; aux < n * m; aux++)
            facilities.add(aux);

        Collections.shuffle(facilities, RandomSingleton.getInstance().getRnd());

        for (int i = 0; i < n; i++) {
            solution.add(new ArrayList<>());
        }

        for (int nAux = 0; nAux < n; nAux++) {
            for (int mAux = 0; mAux < m; mAux++) {
                solution.get(nAux).add(facilities.remove(0));
            }
        }
        return evaluation.Evaluation(solution, weights, crs);
    }

    public SolutionPositionsCosts C1_GR(ArrayList<ArrayList<Integer>> mhc, ArrayList<ArrayList<Integer>> crs, int m, int n, double alpha) {
        Common common = new Common();
        Evaluation evaluation = new Evaluation();
        ArrayList<Integer> facilities = new ArrayList<>(n);

        //Initial random layout
        ArrayList<ArrayList<Integer>> solution = InitialRandomSolution(n, m, facilities);

        while (!facilities.isEmpty()) {
            ArrayList<Candidate> CL = new ArrayList<>(facilities.size());
            ArrayList<Candidate> RCL = new ArrayList<>(facilities.size());

            //We try a random facility that belongs to facilities in each all the possible solutions
            for (int facilityIterated = 0; facilityIterated < facilities.size(); facilityIterated++) {
                for (int row = 0; row < m ; row++) {
                    if(solution.get(row).size() < n) {
                        for (int position = 0; position < solution.get(row).size(); position++) {
                            ArrayList<ArrayList<Integer>> currentSol = common.CopyArrayList(solution);
                            currentSol.get(row).add(position, facilities.get(facilityIterated));
                            SolutionPositionsCosts feasibleSolution = evaluation.Evaluation(currentSol, mhc, crs);
                            CL.add(new Candidate(facilityIterated, feasibleSolution));
                        }
                        ArrayList<ArrayList<Integer>> currentSol = common.CopyArrayList(solution);
                        currentSol.get(row).add(facilities.get(facilityIterated));
                        SolutionPositionsCosts feasibleSolution = evaluation.Evaluation(currentSol, mhc, crs);
                        CL.add(new Candidate(facilityIterated, feasibleSolution));
                    }
                }
            }

            //From lowest to highest
            sortCandidatesMHC(CL);

            //Calculation of the threshold
            double temp = CL.get(0).getSolution().getCostSolutionO1() * alpha + CL.get(CL.size() - 1).getSolution().getCostSolutionO1() * (1 - alpha);
            double threshold = (double) Math.round(temp * 100) / 100;

            //Get the RCL
            for (Candidate aux : CL) {
                if (aux.getSolution().getCostSolutionO1() <= threshold)
                    RCL.add(aux);
                else
                    break;
            }

            Candidate aux = RCL.get(RandomSingleton.getInstance().nextInt(RCL.size()));
            solution = aux.getSolution().getSolution();
            facilities.remove(aux.getIndex());
        }
        return evaluation.Evaluation(solution, mhc, crs);
    }

    public SolutionPositionsCosts C2_RG(ArrayList<ArrayList<Integer>> mhc, ArrayList<ArrayList<Integer>> crs, int n, int m, double alpha) {
        Common common = new Common();
        Evaluation evaluation = new Evaluation();
        ArrayList<Integer> facilities = new ArrayList<>(n);

        //Initial random layout
        ArrayList<ArrayList<Integer>> solution = InitialRandomSolution(n, m, facilities);

        while (!facilities.isEmpty()) {
            ArrayList<Candidate> CL = new ArrayList<>(facilities.size());
            ArrayList<Candidate> RCL = new ArrayList<>(facilities.size());

            //We try a random facility in facilities in each all the possible solutions
            for (int facilityIterated = 0; facilityIterated < facilities.size(); facilityIterated++) {
                for (int row = 0; row < m; row++) {
                    if (solution.get(row).size() < n) {
                        for (int position = 0; position < solution.get(row).size(); position++) {
                            ArrayList<ArrayList<Integer>> currentSol = common.CopyArrayList(solution);
                            currentSol.get(row).add(position, facilities.get(facilityIterated));
                            SolutionPositionsCosts feasibleSolution = evaluation.Evaluation(currentSol, mhc, crs);
                            CL.add(new Candidate(facilityIterated, feasibleSolution));
                        }
                        ArrayList<ArrayList<Integer>> currentSol = common.CopyArrayList(solution);
                        currentSol.get(row).add(facilities.get(facilityIterated));
                        SolutionPositionsCosts feasibleSolution = evaluation.Evaluation(currentSol, mhc, crs);
                        CL.add(new Candidate(facilityIterated, feasibleSolution));
                    }
                }
            }

            //Get the RCL
            for (int a = 0; a < CL.size() * alpha; a++) {
                Candidate aux = CL.get(RandomSingleton.getInstance().nextInt(CL.size()));
                RCL.add(aux);
            }

            //From lowest to highest
            sortCandidatesMHC(RCL);

            Candidate aux = RCL.get(0);
            solution = aux.getSolution().getSolution();
            facilities.remove(aux.getIndex());
        }
        return evaluation.Evaluation(solution, mhc, crs);
    }

    public SolutionPositionsCosts C3_GR(ArrayList<ArrayList<Integer>> mhc, ArrayList<ArrayList<Integer>> crs, int m, int n, double alpha) {
        Common common = new Common();
        Evaluation evaluation = new Evaluation();
        ArrayList<Integer> facilities = new ArrayList<>(n);

        //Initial random layout
        ArrayList<ArrayList<Integer>> solution = InitialRandomSolution(n, m, facilities);

        while (!facilities.isEmpty()) {
            ArrayList<Candidate> CL = new ArrayList<>(facilities.size());
            ArrayList<Candidate> RCL = new ArrayList<>(facilities.size());

            //We try a random facility in facilities in each all the possible solutions
            for (int facilityIterated = 0; facilityIterated < facilities.size(); facilityIterated++) {
                for (int row = 0; row < m; row++) {
                    if (solution.get(row).size() < n) {
                        for (int position = 0; position < solution.get(row).size(); position++) {
                            ArrayList<ArrayList<Integer>> currentSol = common.CopyArrayList(solution);
                            currentSol.get(row).add(position, facilities.get(facilityIterated));
                            SolutionPositionsCosts feasibleSolution = evaluation.Evaluation(currentSol, mhc, crs);
                            CL.add(new Candidate(facilityIterated, feasibleSolution));
                        }
                        ArrayList<ArrayList<Integer>> currentSol = common.CopyArrayList(solution);
                        currentSol.get(row).add(facilities.get(facilityIterated));
                        SolutionPositionsCosts feasibleSolution = evaluation.Evaluation(currentSol, mhc, crs);
                        CL.add(new Candidate(facilityIterated, feasibleSolution));
                    }
                }
            }

            //From lowest to highest
            sortCandidatesCR(CL);

            //Calculation of the threshold
            double temp = CL.get(0).getSolution().getCostSolutionO2() * alpha + CL.get(CL.size() - 1).getSolution().getCostSolutionO2() * (1 - alpha);
            double threshold = (double) Math.round(temp * 100) / 100;

            //Get the RCL
            for (Candidate aux : CL) {
                if (aux.getSolution().getCostSolutionO2() <= threshold)
                    RCL.add(aux);
                else
                    break;
            }

            Candidate aux = RCL.get(RandomSingleton.getInstance().nextInt(RCL.size()));
            solution = aux.getSolution().getSolution();
            facilities.remove(aux.getIndex());
        }
        return evaluation.Evaluation(solution, mhc, crs);
    }

    public SolutionPositionsCosts C4_RG(ArrayList<ArrayList<Integer>> mhc, ArrayList<ArrayList<Integer>> crs, int n, int m, double alpha) {
        Common common = new Common();
        Evaluation evaluation = new Evaluation();
        ArrayList<Integer> facilities = new ArrayList<>(n);

        //Initial random layout
        ArrayList<ArrayList<Integer>> solution = InitialRandomSolution(n, m, facilities);

        while (!facilities.isEmpty()) {
            ArrayList<Candidate> CL = new ArrayList<>(facilities.size());
            ArrayList<Candidate> RCL = new ArrayList<>(facilities.size());

            //We try a random facility in facilities in each all the possible solutions
            for (int facilityIterated = 0; facilityIterated < facilities.size(); facilityIterated++) {
                for (int row = 0; row < m; row++) {
                    if (solution.get(row).size() < n) {
                        for (int position = 0; position < solution.get(row).size(); position++) {
                            ArrayList<ArrayList<Integer>> currentSol = common.CopyArrayList(solution);
                            currentSol.get(row).add(position, facilities.get(facilityIterated));
                            SolutionPositionsCosts feasibleSolution = evaluation.Evaluation(currentSol, mhc, crs);
                            CL.add(new Candidate(facilityIterated, feasibleSolution));
                        }
                        ArrayList<ArrayList<Integer>> currentSol = common.CopyArrayList(solution);
                        currentSol.get(row).add(facilities.get(facilityIterated));
                        SolutionPositionsCosts feasibleSolution = evaluation.Evaluation(currentSol, mhc, crs);
                        CL.add(new Candidate(facilityIterated, feasibleSolution));
                    }
                }
            }

            //Get the RCL
            for (int a = 0; a < CL.size() * alpha; a++) {
                Candidate aux = CL.get(RandomSingleton.getInstance().nextInt(CL.size()));
                RCL.add(aux);
            }

            //From lowest to highest
            sortCandidatesCR(RCL);

            Candidate aux = RCL.get(0);
            solution = aux.getSolution().getSolution();
            facilities.remove(aux.getIndex());
        }
        return evaluation.Evaluation(solution, mhc, crs);
    }

    private ArrayList<ArrayList<Integer>> InitialRandomSolution(int n, int m, ArrayList<Integer> facilities) {
        ArrayList<ArrayList<Integer>> initialRandomSolution = new ArrayList<>();

        for (int i = 0; i < m; i++)
            initialRandomSolution.add(new ArrayList<>(m));

        for (int i = 0; i < n * m; i++)
            facilities.add(i);

        Collections.shuffle(facilities, RandomSingleton.getInstance().getRnd());

        for (int auxM = 0; auxM < m; auxM++) {
            initialRandomSolution.get(auxM).add(facilities.remove(0));
            initialRandomSolution.get(auxM).add(facilities.remove(0));
        }

        return initialRandomSolution;
    }

    private static void sortCandidatesMHC(ArrayList<Candidate> candidates) {
        candidates.sort((Comparator) (o1, o2) -> {
            Double x1 = ((Candidate) o1).getSolution().getCostSolutionO1();
            Double x2 = ((Candidate) o2).getSolution().getCostSolutionO1();

            int sComp = x1.compareTo(x2);
            if (sComp != 0) {
                return sComp;
            }
            return x2.compareTo(x1);
        });
    }

    private static void sortCandidatesCR(ArrayList<Candidate> candidates) {
        candidates.sort((Comparator) (o1, o2) -> {
            Double x1 = ((Candidate) o1).getSolution().getCostSolutionO2();
            Double x2 = ((Candidate) o2).getSolution().getCostSolutionO2();

            int sComp = x1.compareTo(x2);
            if (sComp != 0) {
                return sComp;
            }
            return x2.compareTo(x1);
        });
    }

}
