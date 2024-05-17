import Structures.PositionDistance;
import Structures.SolutionPositionsCosts;

import java.util.ArrayList;
import java.util.Comparator;

public class SortedArrayList {

    public ArrayList<SolutionPositionsCosts> getSortedArrayList() {
        return sortedArrayList;
    }

    ArrayList<SolutionPositionsCosts> sortedArrayList = new ArrayList<>();
    Evaluation evaluation = new Evaluation();
    boolean changes = false;

    public boolean add(SolutionPositionsCosts x) {
        changes = false;
        if (sortedArrayList.isEmpty()) {
            sortedArrayList.add(x);
            changes = true;
        } else {
            if (evaluation.dominated(x, sortedArrayList.get(0))) {
                sortedArrayList.set(0, x);
                changes = true;
                while (1 < sortedArrayList.size() && evaluation.dominated(x, sortedArrayList.get(1))) {
                    sortedArrayList.remove(1);
                }
            } else if (x.getCostSolutionO1() < sortedArrayList.get(0).getCostSolutionO1() && x.getCostSolutionO2() > sortedArrayList.get(0).getCostSolutionO2()) {
                sortedArrayList.add(0, x);
                changes = true;
            } else if (x.getCostSolutionO1() > sortedArrayList.get(0).getCostSolutionO1() && x.getCostSolutionO2() < sortedArrayList.get(0).getCostSolutionO2()) {
                int iterator = 1;
                int iteratorNext = iterator + 1;
                while (sortedArrayList.size() > iteratorNext && x.getCostSolutionO1() >= sortedArrayList.get(iteratorNext).getCostSolutionO1()) {
                    iterator++;
                    iteratorNext++;
                }

                if (sortedArrayList.size() > 1) {
                    if (x.getCostSolutionO1() < sortedArrayList.get(iterator).getCostSolutionO1() && x.getCostSolutionO2() > sortedArrayList.get(iterator).getCostSolutionO2()) {
                        sortedArrayList.add(iterator, x);
                        changes = true;
                    }

                    if (evaluation.dominated(x, sortedArrayList.get(iterator))) {
                        sortedArrayList.set(iterator, x);
                        changes = true;

                        while (iteratorNext < sortedArrayList.size() && evaluation.dominated(x, sortedArrayList.get(iteratorNext)))
                            sortedArrayList.remove(iteratorNext);
                    }

                    if (x.getCostSolutionO1() > sortedArrayList.get(iterator).getCostSolutionO1() && x.getCostSolutionO2() < sortedArrayList.get(iterator).getCostSolutionO2()) {
                        sortedArrayList.add(iteratorNext, x);
                        changes = true;
                        while (iteratorNext + 1 < sortedArrayList.size() && evaluation.dominated(x, sortedArrayList.get(iteratorNext + 1)))
                            sortedArrayList.remove(iteratorNext + 1);
                    }

                } else {
                    sortedArrayList.add(x);
                    changes = true;
                }
            }
        }
        return changes;
    }

    @Override
    public boolean equals(Object obj) {
        SortedArrayList s1 = (SortedArrayList) obj;
        boolean equals = true;
        for (int i = 0; i < s1.sortedArrayList.size(); i++) {
            if (!s1.sortedArrayList.get(i).equals(this.sortedArrayList.get(i))) {
                equals = false;
                break;
            }
        }
        return equals;
    }

    public SortedArrayList reducedParetoFrontier(SortedArrayList paretoOptimal, int frontierSize) {
        Common common = new Common();
        SortedArrayList aux = common.CopySortedArrayListSolution(paretoOptimal);
        ArrayList<PositionDistance> positionDistances = new ArrayList<>();
        double distanceMHC, distanceCR, distance;

        while (aux.sortedArrayList.size() > frontierSize) {
            for (int i = 1; i < aux.sortedArrayList.size() - 1; i++) {
                distanceMHC = (aux.sortedArrayList.get(i + 1).getCostSolutionO1() - aux.sortedArrayList.get(i - 1).getCostSolutionO1()) / (aux.sortedArrayList.get(aux.sortedArrayList.size() - 1).getCostSolutionO1() - (aux.sortedArrayList.get(0).getCostSolutionO1()));
                distanceCR = ((aux.sortedArrayList.get(i + 1).getCostSolutionO2() - aux.sortedArrayList.get(i - 1).getCostSolutionO2()) / (aux.sortedArrayList.get(0).getCostSolutionO2() - (aux.sortedArrayList.get(aux.sortedArrayList.size() - 1).getCostSolutionO2()))) * -1;
                distance = distanceCR + distanceMHC;
                PositionDistance positionDistance = new PositionDistance(i, distance);
                positionDistances.add(positionDistance);
            }
            PositionDistance positionDistance = new PositionDistance(0, Integer.MAX_VALUE);
            positionDistances.add(positionDistance);
            positionDistance = new PositionDistance(aux.sortedArrayList.size() - 1, Integer.MAX_VALUE);
            positionDistances.add(positionDistance);
            //De menor a mayor
            positionDistances.sort((Comparator) (o1, o2) -> {

                Double x1 = ((PositionDistance) o1).getDistance();
                Double x2 = ((PositionDistance) o2).getDistance();
                int sComp = x1.compareTo(x2);

                if (sComp != 0) {
                    return sComp;
                }
                return x1.compareTo(x2);
            });

            int last = positionDistances.size() - 1;
            int lastPosition = positionDistances.get(last).getPosition();
            aux.sortedArrayList.remove(lastPosition);
            positionDistances.clear();
        }

        return aux;
    }

    public ArrayList<SolutionPositionsCosts> selectSolution(SortedArrayList paretoOptimal, ArrayList<PositionDistance> cds, String order) {
        Common common = new Common();
        ArrayList<SolutionPositionsCosts> aux = new ArrayList<>(1);
        int l = cds.size();

        cds.sort((Comparator) (o1, o2) -> {

            Double x1 = ((PositionDistance) o1).getDistance();
            Double x2 = ((PositionDistance) o2).getDistance();
            int sComp = x2.compareTo(x1);

            if (sComp != 0) {
                return sComp;
            }
            return x1.compareTo(x2);
        });
        if (order.equals("desc")) { //De mayor a menor
            int position = cds.get(l-1).getPosition();
            SolutionPositionsCosts temp = paretoOptimal.getSortedArrayList().get(position);
            aux.add(common.CopySolutionPositionCosts(temp));        }
        else if (order.equals("asc")) { //De menor a mayor
            int position = cds.get(2).getPosition();
            SolutionPositionsCosts temp = paretoOptimal.getSortedArrayList().get(position);
            aux.add(common.CopySolutionPositionCosts(temp));
        }
        return aux;
    }

    public ArrayList<PositionDistance> crowding_distance_assignment(SortedArrayList paretoOptimal) {
        Common common = new Common();
        SortedArrayList aux = common.CopySortedArrayListSolution(paretoOptimal);
        ArrayList<PositionDistance> crowding_distances = new ArrayList<>(paretoOptimal.getSortedArrayList().size());
        double distanceMHC, distanceCR, distance;
        int l = aux.getSortedArrayList().size();

        for (int i = 0; i < l; i++) {
            PositionDistance positionDistance = new PositionDistance(i, 0);
            crowding_distances.add(positionDistance);
        }

        crowding_distances.get(0).setDistance(Integer.MAX_VALUE);
        crowding_distances.get(l - 1).setDistance(Integer.MAX_VALUE);

        double fMax_MHC = aux.getSortedArrayList().get(l - 1).getCostSolutionO1();
        double fMin_MHC = aux.getSortedArrayList().get(0).getCostSolutionO1();

        double fMax_CR = aux.getSortedArrayList().get(0).getCostSolutionO2();
        double fMin_CR = aux.getSortedArrayList().get(l - 1).getCostSolutionO2();

        for (int i = 1; i < l - 1; i++) {
            distanceMHC = (aux.getSortedArrayList().get(i + 1).getCostSolutionO1() - aux.getSortedArrayList().get(i - 1).getCostSolutionO1()) / (fMax_MHC - fMin_MHC);
            distanceCR = (aux.getSortedArrayList().get(i + 1).getCostSolutionO2() - aux.getSortedArrayList().get(i - 1).getCostSolutionO2()) / (fMax_CR - fMin_CR);
            distance = distanceMHC - distanceCR;
            crowding_distances.get(i).setPosition(i);
            crowding_distances.get(i).setDistance(distance);
        }
        return crowding_distances;
    }
}