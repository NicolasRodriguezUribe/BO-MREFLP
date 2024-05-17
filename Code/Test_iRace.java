import Structures.SolutionPositionsCosts;

import java.io.*;
import java.util.ArrayList;

public class Test_iRace {
    static Constructive constructive = new Constructive();
    static LocalSearch ls = new LocalSearch();
    static Common common = new Common();
    static SortedArrayList pareto = new SortedArrayList();
    static Metrics metrics = new Metrics();
    static int iterations = 100;
    public static void main(String[] args) throws IOException {
        String instancesName = args[1];
        double alpha = Double.parseDouble(args[5]);
        double pC1 = Double.parseDouble(args[7]);
        double pC2 = Double.parseDouble(args[9]);
        double pC3 = Double.parseDouble(args[11]);
        double pC4 = Double.parseDouble(args[13]);
        double beta = Double.parseDouble(args[15]);
        String order = args[17];

        Instance instanceMHC = new Instance(instancesName, 0);

        String[] fileDirectory = instancesName.split("/");
        String[] nameFile = fileDirectory[fileDirectory.length - 1].split("\\.");
        StringBuilder url_CR = new StringBuilder();
        for (int i = 0; i < fileDirectory.length - 2; i++) {
            url_CR.append(fileDirectory[i]);
            url_CR.append("/");
        }

        url_CR.append("Instances_CR/");
        url_CR.append(nameFile[0]);
        url_CR.append("_CR.txt");

        Instance instanceCR = new Instance(String.valueOf(url_CR), 0);

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

        SortedArrayList results;
        if(order.equals("DM"))
            results = ls.combiningDM(pareto, instanceMHC.getWeights(), instanceCR.getWeights(), beta);
        else
            results = ls.combiningMD(pareto, instanceMHC.getWeights(), instanceCR.getWeights(), beta);

        common.CreateFile(nameFile[0], results);
        System.out.println(metrics.getHyperVolume(nameFile[0]));
    }
}
