import Structures.SolutionPositionsCosts;

import java.io.*;
import java.util.*;

public class Executable {
    public static void main(String[] args) throws IOException {

        //All instances
//        String[] instancesName = {"A-10-10", "A-10-20", "A-10-30", "A-10-40", "A-10-50", "A-10-60", "A-10-70", "A-10-80", "A-10-90", "A-12-10", "A-12-20", "A-12-30", "A-12-40", "A-12-50", "A-12-60",
//                "A-12-70", "A-12-80", "A-12-90", "A-20-10", "A-20-20", "A-20-30", "A-20-40", "A-20-50", "A-20-60", "A-20-70", "A-20-80", "A-20-90", "A-25-10", "A-25-20", "A-25-30", "A-25-40",
//                "A-25-50", "A-25-60", "A-25-70", "A-25-80", "A-25-90", "N-15_t", "N-20_t", "O-10_t", "O-15_t", "O-20_t", "previous12", "previous15", "previous6", "previous8",  "S-12_t", "S-15_t",
//                "S-20_t", "S-25_t", "Y-10_t", "Y-12_t", "Y-15_t", "Y-20_t", "Y-25_t", "Y-30_t", "Y-35_t", "Y-40_t", "Y-45_t", "Y-50_t", "Y-60_t"};

        //OLD Test instances
        //String[] instancesName = {"previous6", "A-10-70", "A-12-70", "O-15_t", "Y-15_t", "A-20-50", "A-25-60", "Y-45_t", "Y-50_t"};
        //String[] instancesName = {"previous6", "previous8", "previous12", "previous15"};

        //New Test instances
//        String[] instancesName = {"previous6", "previous15", "A-10-70", "A-20-80", "A-20-30", "Y-12_t", "Y-25_t", "Y-40_t", "Y-60_t"};

        //Previous instances
        String[] instancesName = {"previous6", "previous8", "previous12", "previous15"};


//        String[] instancesName = {"A-10-70"};
//        String[] instancesName = {"Y-15_t"};
//        String[] instancesName = {"Y-60_t"};

//        Experiment1(instancesName);
        AllInstances(instancesName);
    }

    //ALL GRASP configurations
    public static void Experiment1(String[] instancesName) throws IOException {
        Common common = new Common();
        Metaheuristics metaheuristics = new Metaheuristics();

        double[] alpha = {0.43, 0.14, 0.44, 0.19};
        double[] pC1 = {0.03, 0.29, 0.03, 0.12};
        double[] pC2 = {0.46, 0.27, 0.44, 0.19};
        double[] pC3 = {0.32, 0.13, 0.33, 0.12};
        double[] pC4 = {0.19, 0.31, 0.2, 0.57};
        double[] beta = {0.92, 0.9, 0.92, 0.75};
        String[] order = {"MD", "MD", "MD", "MD"};
        int iterations = 100;

        //We launch 30 runs for each instance
        for (int i = 0; i < 30; i++) {
            for (String instanceName : instancesName) {
                Instance instanceMHC = new Instance("src\\main\\resources\\instancesMHC\\" + instanceName + ".txt", 0);
                Instance instanceCR = new Instance("src\\main\\resources\\instancesCR\\" + instanceName + "_CR.txt", 0);
                System.out.println("Run: " + i + " instance: " + instanceName);

                for (int aux = 0; aux < alpha.length; aux++) {
                    long init = System.nanoTime();
                    SortedArrayList results;
                    results = metaheuristics.GRASP(iterations, pC1[aux], pC2[aux], pC3[aux], pC4[aux], alpha[aux], beta[aux], order[aux], instanceMHC, instanceCR);
                    long end = System.nanoTime();//
                    long time = (end - init);// / (1000 * 1000 * 1000);

                    File folder = new File("src\\main\\resources\\data" + File.separator + i + File.separator + aux + File.separator);
                    folder.mkdirs();

                    common.CreateFile("src\\main\\resources\\data" + File.separator + i + File.separator + aux + File.separator + instanceName + ".txt", results);
                    common.CreateFileTime("src\\main\\resources\\data" + File.separator + i + File.separator + aux + File.separator + instanceName + "_time.txt", time);
                }
            }
        }
    }

    public void Experiment2(String[] instancesName) throws IOException {
        Common common = new Common();
        Metaheuristics metaheuristics = new Metaheuristics();

        double[] alpha = {0.5};
        double[] pC1 = {0.3};
        double[] pC2 = {0.2};
        double[] pC3 = {0.3};
        double[] pC4 = {0.2};
        double[] beta = {0};

        String[] order = {"DM"};
        int iterations = 100;
        ArrayList<Long> times = new ArrayList<>();

        for (String instanceName : instancesName) {
            Instance instanceMHC = new Instance("src\\main\\resources" + File.separator + instanceName + ".txt", 0);
            Instance instanceCR = new Instance("src\\main\\resources" + File.separator + instanceName + "_CR.txt", 0);
            System.out.println(instanceName);

            for (int aux = 0; aux < alpha.length; aux++) {
                long init = System.nanoTime();
                SortedArrayList resultsGRASP;
                SortedArrayList resultsBVNS;
                SortedArrayList results;

                results = metaheuristics.GRASP(iterations, pC1[aux], pC2[aux], pC3[aux], pC4[aux], alpha[aux], beta[aux], order[aux], instanceMHC, instanceCR);

                long end = System.nanoTime();
                long time = (end - init) / (1000 * 1000);

                times.add(time);
                common.CreateFile("src\\main\\resources\\data_metrics\\Experiment2\\GRASP" + File.separator + aux + File.separator + instanceName + ".txt", results);


                metaheuristics.BVNS(results, 1, instanceMHC.getWeights(), instanceCR.getWeights());

                common.CreateFile("src\\main\\resources\\data_metrics\\Experiment2\\GRASP" + File.separator + aux + File.separator + instanceName + ".txt", results);
            }
        }
        Metrics.evaluateDir(times);
    }

    public static void Experiment3(String[] instancesName) throws IOException {
        Common common = new Common();
        Metaheuristics metaheuristics = new Metaheuristics();

        double[] alpha = {0.5};
        double[] pC1 = {0.3};
        double[] pC2 = {0.2};
        double[] pC3 = {0.3};
        double[] pC4 = {0.2};
        double[] beta = {0};

        String[] order = {"DM"};
        int iterations = 100;
        ArrayList<Long> times = new ArrayList<>();

        for (String instanceName : instancesName) {
            Instance instanceMHC = new Instance("src\\main\\resources\\instancesMHC" + File.separator + instanceName + ".txt", 0);
            Instance instanceCR = new Instance("src\\main\\resources\\instancesCR" + File.separator + instanceName + "_CR.txt", 0);
            System.out.println(instanceName);

            for (int aux = 0; aux < alpha.length; aux++) {
                long init = System.nanoTime();
                SortedArrayList results;

                results = metaheuristics.GRASP_LSD(iterations, pC1[aux], pC2[aux], pC3[aux], pC4[aux], alpha[aux], beta[aux], order[aux], instanceMHC, instanceCR);
                metaheuristics.BVNS_v2(results, 0.1, instanceMHC.getWeights(), instanceCR.getWeights());


                long end = System.nanoTime();
                long time = (end - init) / (1000 * 1000);
                times.add(time);
            }
        }
    }

    public static void Experiment4(String[] instancesName) throws IOException {
        Common common = new Common();
        Metaheuristics metaheuristics = new Metaheuristics();

//        double[] alpha = {0.5, 0.99, 1, 1};
//        double[] pC1 = {0.3, 0.55, 0.55, 1};
//        double[] pC2 = {0.2, 0.07, 0, 0};
//        double[] pC3 = {0.3, 0, 0, 0};
//        double[] pC4 = {0.2, 0.38, 0.45, 0};
//        double[] beta = {0, 0, 0, 0};
//        String[] order = {"DM", "DM", "DM", "DM"};

        double[] alpha = {0.56, 0.69, 0.65, 0.58};
        double[] pC1 = {0.48, 0.31, 0.44, 0.34};
        double[] pC2 = {0.02, 0.25, 0.12, 0.13};
        double[] pC3 = {0.27, 0.26, 0.31, 0.38};
        double[] pC4 = {0.23, 0.18, 0.13, 0.15};
        double[] beta = {0.04, 0.01, 0.02, 0.06};
        String[] order = {"DM", "DM", "MD", "DM"};
        int iterations = 100;
        ArrayList<Long> times = new ArrayList<>();

        for (String instanceName : instancesName) {
            Instance instanceMHC = new Instance("src\\main\\resources\\instancesMHC" + File.separator + instanceName + ".txt", 0);
            Instance instanceCR = new Instance("src\\main\\resources\\instancesCR" + File.separator + instanceName + "_CR.txt", 0);
            System.out.println(instanceName);

            for (int aux = 0; aux < alpha.length; aux++) {
                long init = System.nanoTime();
                SortedArrayList results, results2;

                results = metaheuristics.GRASP_LSD(iterations, pC1[aux], pC2[aux], pC3[aux], pC4[aux], alpha[aux], beta[aux], order[aux], instanceMHC, instanceCR);
                results2 = metaheuristics.BVNS_v2(results, 0.1, instanceMHC.getWeights(), instanceCR.getWeights());

                long end = System.nanoTime();
                //long time = (end - init) / (1000 * 1000 * 1000);
                long time = (end - init);// / (1000 * 1000 * 1000);

                times.add(time);
                common.CreateFile("src\\main\\resources\\data_metrics" + File.separator + aux + File.separator + instanceName + ".txt", results2);
            }
        }
        //Metrics.evaluateDir(times);
    }

    public static void LNCS(String[] instancesName) throws IOException {
        Common common = new Common();
        Metaheuristics metaheuristics = new Metaheuristics();
        Constructive constructive = new Constructive();
        LocalSearch ls = new LocalSearch();

        int iterations = 100;
        ArrayList<Long> times = new ArrayList<>();

        for (String instanceName : instancesName) {
            SortedArrayList results = new SortedArrayList();
            Instance instanceMHC = new Instance("src\\main\\resources\\instancesMHC" + File.separator + instanceName + ".txt", 0);
            Instance instanceCR = new Instance("src\\main\\resources\\instancesCR" + File.separator + instanceName + "_CR.txt", 0);
            System.out.println(instanceName);

            long init = System.nanoTime();

            ArrayList<Integer> elements = common.Topology(instanceCR.numFacilities);

            for (int i = 0; i < iterations*instanceMHC.numFacilities; i++) {
                SolutionPositionsCosts result = constructive.C0_Random(instanceMHC.getWeights(), instanceCR.getWeights(), elements.get(0), elements.get(1));
                results.add(result);
            }

            SortedArrayList tempResults = common.CopySortedArrayListSolution(results);
            boolean improve = true;
            while (improve) {
                improve = false;
                for (SolutionPositionsCosts temp : results.getSortedArrayList()) {
                    SolutionPositionsCosts crTemp = ls.hybridLocalSearchInterchange(temp, instanceMHC.getWeights(), instanceCR.getWeights(), "cr");
                    SolutionPositionsCosts mhcTemp = ls.hybridLocalSearchInterchange(temp, instanceMHC.getWeights(), instanceCR.getWeights(), "mhc");
                    if(tempResults.add(crTemp) || tempResults.add(mhcTemp)){
                        improve = true;
                    }
                }
                if (improve)
                    results = common.CopySortedArrayListSolution(tempResults);
            }

            SortedArrayList pareto = metaheuristics.BVNS(results, 0.2, instanceMHC.getWeights(), instanceCR.getWeights());
            common.CreateFile("src\\main\\resources\\LNCS" + File.separator + instanceName + ".txt", pareto);
            long end = System.nanoTime();
            long time = (end - init) / (1000 * 1000);
            times.add(time);
        }
        System.out.println(times);
    }

    public static void DBLS_vs_AOLS(String[] instancesName) throws IOException {
        Common common = new Common();
        Metaheuristics metaheuristics = new Metaheuristics();
        Metrics metrics = new Metrics();

        double[] alpha = {0.83};
        double[] pC1 = {0.25};
        double[] pC2 = {0.15};
        double[] pC3 = {0.38};
        double[] pC4 = {0.22};

        int iterations = 100;

        for (String instanceName : instancesName) {
            Instance instanceMHC = new Instance("src\\main\\resources\\instancesMHC" + File.separator + instanceName + ".txt", 0);
            Instance instanceCR = new Instance("src\\main\\resources\\instancesCR" + File.separator + instanceName + "_CR.txt", 0);
            System.out.println(instanceName);

            ArrayList<SortedArrayList> results = metaheuristics.Test(iterations, pC1[0], pC2[0], pC3[0], pC4[0], alpha[0], instanceMHC, instanceCR);

            common.CreateFile("src\\main\\resources\\test\\DBLS" + File.separator + instanceName + ".txt", results.get(0));
            common.CreateFile("src\\main\\resources\\test\\AOLS" + File.separator + instanceName + ".txt", results.get(1));

            System.out.println(metrics.getHyperVolume("src\\main\\resources\\test\\DBLS" + File.separator + instanceName + ".txt"));
            System.out.println(metrics.getHyperVolume("src\\main\\resources\\test\\AOLS" + File.separator + instanceName + ".txt"));
        }
    }

    public static void DBLS_vs_AOLS_v2(String[] instancesName) throws IOException {
        Common common = new Common();
        Metaheuristics metaheuristics = new Metaheuristics();
        Metrics metrics = new Metrics();

        double[] alpha = {0.83};
        double[] pC1 = {0.25};
        double[] pC2 = {0.15};
        double[] pC3 = {0.38};
        double[] pC4 = {0.22};

        int iterations = 100;

        for (String instanceName : instancesName) {
            Instance instanceMHC = new Instance("src\\main\\resources\\instancesMHC" + File.separator + instanceName + ".txt", 0);
            Instance instanceCR = new Instance("src\\main\\resources\\instancesCR" + File.separator + instanceName + "_CR.txt", 0);
            System.out.println(instanceName);

            ArrayList<SortedArrayList> results = metaheuristics.GRASP_TEST(iterations, pC1[0], pC2[0], pC3[0], pC4[0], alpha[0], instanceMHC, instanceCR);

            common.CreateFile("src\\main\\resources\\test\\DBLS" + File.separator + instanceName + ".txt", results.get(0));
            common.CreateFile("src\\main\\resources\\test\\AOLS" + File.separator + instanceName + ".txt", results.get(1));

            System.out.println(metrics.getHyperVolume("src\\main\\resources\\test\\DBLS" + File.separator + instanceName + ".txt"));
            System.out.println(metrics.getHyperVolume("src\\main\\resources\\test\\AOLS" + File.separator + instanceName + ".txt"));
        }
    }

    public static void AllInstances (String[] instances) throws IOException {
        ArrayList<Long> times;
        for (int i = 0; i < 30; i++) {
            System.out.println("Run: " + i);
//            times = Metrics.readTimes("src\\main\\resources\\data\\" + i, instances);
                Metrics.evaluateWithTimes("src\\main\\resources\\data\\" + i, null, instances);
//                Metrics.evaluateFormulas("src\\main\\resources\\data\\" + i);
        }
    }
}