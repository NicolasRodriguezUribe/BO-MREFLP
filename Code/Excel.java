import Structures.SolutionPositionsCosts;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;

public class Excel {
    SortedArrayList paretoOptimal = new SortedArrayList();
    SortedArrayList results = new SortedArrayList();
    LocalSearch localSearch = new LocalSearch();
    Constructive constructive = new Constructive();
    ConstructiveB constructiveB = new ConstructiveB();
    Common common = new Common();

    static int row = 0;
    static int facilitiesRow = 0;
    static int c = 1;
    static int currentRow = 1;
    static int mhcRow = 1;
    static int crRow = 2;
    static int timeRow = 3;
    static int tableRow = 1;
    static int tableCell = 2;
    static int defaultIt = 100;
    static int[] iterations = {100};
    static int IT = 10;
    //static String[] heuristics = {"C8"};
    //static String[] heuristics = {"C0", "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8"};
    static String[] heuristics = {"C1", "C2", "C3", "C4"};
    //static String[] heuristics = {"C2", "C4", "C8"};
    static double[] alphas = {0.25, 0.5, 0.75, 1};
    //static double[] alphas = {0.5};

    static String filename = "src\\main\\resources\\0Example.xlsx";
    static FileInputStream inputStream;

    //State
    //static String[] instancesNames = {"previous6", "previous8", "previous12", "previous15"};
    //Test
//    static String[] instancesNames = {"O-10_t", "S-12_t", "N-15_t", "O-20_t", "S-25_t", "Y-40_t", "Y-50_t", "Y-60_t"};
    //static String[] instancesNames = {"O-10_t", "S-12_t", "N-15_t", "O-20_t", "S-25_t"};
    //static String[] instancesNames = {"O-10_t", "S-12_t", "N-15_t", "O-20_t", "S-25_t", "Y-40_t", "Y-50_t"};
    static String[] instancesNames = {"N-15_t"};
    //static String[] instancesNames = {"O-10_t","S-12_t", "N-15_t"};

    //ALL
    //String[] instancesNames = {"A-10-10", "A-10-20", "A-10-30", "A-10-40", "A-10-50", "A-10-60", "A-10-70", "A-10-80", "A-10-90", "O-10_t", "Y-10_t", "A-12-10", "A-12-20", "A-12-30", "A-12-40", "A-12-50", "A-12-60", "A-12-70", "A-12-80", "A-12-90",  "S-12_t", "Y-12_t", "S-14_t", "Y-14_t", "N-15_t",  "O-15_t",  "S-15_t", "Y-15_t", "A-20-10", "A-20-20", "A-20-30", "A-20-40", "A-20-50", "A-20-60", "A-20-70", "A-20-80", "A-20-90", "N-20_t", "O-20_t", "S-20_t", "Y-20_t", "A-25-10", "A-25-20", "A-25-30", "A-25-40", "A-25-50", "A-25-60", "A-25-70", "A-25-80", "A-25-90", "S-25_t", "Y-25_t", "Y-30_t", "Y-35_t", "Y-40_t", "Y-45_t", "Y-50_t", "Y-60_t"};

    //We choose alpha for the GRASP
    public void constructiveI() throws IOException {

        inputStream = new FileInputStream(filename);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

        XSSFSheet sheet = workbook.createSheet("Constructive I " + iterations[0]);
        currentRow = 31;

        for (String instancesName : instancesNames) {
            Instance instanceMHC = new Instance("src\\main\\resources" + File.separator + instancesName + ".txt", 0);
            Instance instanceCR = new Instance("src\\main\\resources" + File.separator + instancesName + "_CR.txt", 0);
            System.out.println(instancesName);

            //Fix the number of rows and the number of facilities in each row
            Topology(instanceCR.numFacilities);
            for (int it : iterations) {
                XSSFRow xssfRow = sheet.createRow(currentRow);
                Cell instance = xssfRow.createCell(0);
                instance.setCellValue(instancesName);
                //boolean first = true;
                int max = 0;

                ArrayList<SortedArrayList> bucket = new ArrayList<>();
                SortedArrayList finalPareto = new SortedArrayList();
                for (Double alpha : alphas) {
                    for (String heuristic : heuristics) {
                        long startTime = System.nanoTime();
                        switch (heuristic) {
                            case "C0":
                                for (int i = 0; i < it; i++) {
                                    SolutionPositionsCosts result = constructive.C0_Random(instanceMHC.weights, instanceCR.weights, row, facilitiesRow);
                                    paretoOptimal.add(result);
                                    //System.out.println("MHC: "+result.getCostSolutionO1()+"  CR: "+result.getCostSolutionO2());
                                }
                                break;

                            case "C1":
                                for (int i = 0; i < it; i++) {
                                    if (alpha == 1)
                                        alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                                    SolutionPositionsCosts result = constructive.C1_GR(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                                    paretoOptimal.add(result);
                                }
                                //System.out.println(paretoOptimal.sortedArrayList.size());
                                break;

                            case "C2":
                                for (int i = 0; i < it; i++) {
                                    if (alpha == 1)
                                        alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                                    SolutionPositionsCosts result = constructive.C2_RG(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                                    paretoOptimal.add(result);
                                }
                                break;

                            case "C3":
                                for (int i = 0; i < it; i++) {
                                    if (alpha == 1)
                                        alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                                    SolutionPositionsCosts result = constructive.C3_GR(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                                    paretoOptimal.add(result);
                                }
                                break;

                            case "C4":
                                for (int i = 0; i < it; i++) {
                                    if (alpha == 1)
                                        alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                                    SolutionPositionsCosts result = constructive.C4_RG(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                                    paretoOptimal.add(result);
                                }
                                break;
                        }
                        // SortedArrayList results = paretoOptimal.reducedParetoFrontier(paretoOptimal, 4);
                        long endTime = System.nanoTime();
                        long totalTime = endTime - startTime;
                        double elapsedTimeInSecond = (double) totalTime / 1_000_000_000;
                        int auxCurrentRow = currentRow;
                        for (SolutionPositionsCosts aux : paretoOptimal.sortedArrayList) {
                            finalPareto.add(aux); //all de solutions from all the constructive
                            if (sheet.getRow(currentRow) != null)
                                xssfRow = sheet.getRow(currentRow);
                            else
                                xssfRow = sheet.createRow(currentRow);
                            Cell mhc = xssfRow.createCell(mhcRow);
                            Cell cr = xssfRow.createCell(crRow);
                            Cell time = xssfRow.createCell(timeRow);

                            mhc.setCellValue(aux.getCostSolutionO1());
                            cr.setCellValue(aux.getCostSolutionO2());
                            time.setCellValue(elapsedTimeInSecond);
                            currentRow++;
                        }

                        //We change the constructive
                        mhcRow += 3;
                        crRow += 3;
                        timeRow += 3;
                        currentRow = auxCurrentRow;

                        bucket.add(common.CopySortedArrayListSolution(paretoOptimal));
                        if (paretoOptimal.sortedArrayList.size() > max)
                            max = paretoOptimal.sortedArrayList.size();
                        paretoOptimal.sortedArrayList.clear();
                    }
                    currentRow += max;
                    mhcRow = 1;
                    crRow = 2;
                    timeRow = 3;

                }
                for (SolutionPositionsCosts aux : finalPareto.sortedArrayList) {
                    if (sheet.getRow(currentRow) != null)
                        xssfRow = sheet.getRow(currentRow);
                    else
                        xssfRow = sheet.createRow(currentRow);
                    Cell mhc = xssfRow.createCell(mhcRow);
                    Cell cr = xssfRow.createCell(crRow);

                    mhc.setCellValue(aux.getCostSolutionO1());
                    cr.setCellValue(aux.getCostSolutionO2());
                    currentRow++;
                }

                //Check if is the Set
                ArrayList<Integer> bucketParetoPoints = new ArrayList<>();
                for (SortedArrayList aux : bucket) {
                    int paretoPoints = 0;
                    for (SolutionPositionsCosts sol : aux.sortedArrayList) {
                        if (finalPareto.sortedArrayList.contains(sol))
                            paretoPoints++;
                    }
                    bucketParetoPoints.add(paretoPoints);
                }

                for (Integer aux : bucketParetoPoints) {
                    if (sheet.getRow(tableRow) != null)
                        xssfRow = sheet.getRow(tableRow);
                    else
                        xssfRow = sheet.createRow(tableRow);
                    Cell mhc = xssfRow.createCell(tableCell);
                    mhc.setCellValue(aux);
                    tableCell++;
                }
                tableCell++;
                Cell size = xssfRow.createCell(tableCell);
                size.setCellValue(finalPareto.sortedArrayList.size());

                tableRow++;
                tableCell = 2;

                currentRow++;
            }
            try {
                XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
                FileOutputStream outputStream = new FileOutputStream(filename);
                workbook.write(outputStream);
            } catch (Exception e) {
                System.out.println("Error with the file: " + e);
            }
        }
    }

    //We check the quality of the constructive.
    public void constructiveII(double alpha) throws IOException {

        inputStream = new FileInputStream(filename);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

        XSSFSheet sheet = workbook.createSheet("Constructive II " + iterations[0]);
        currentRow = 31;

        for (String instancesName : instancesNames) {
            ArrayList<SortedArrayList> bucket = new ArrayList<>();
            SortedArrayList finalPareto = new SortedArrayList();

            Instance instanceMHC = new Instance("src\\main\\resources" + File.separator + instancesName + ".txt", 0);
            Instance instanceCR = new Instance("src\\main\\resources" + File.separator + instancesName + "_CR.txt", 0);
            System.out.println(instancesName);

            //Fix the number of rows and the number of facilities in each row
            Topology(instanceCR.numFacilities);
            for (int it : iterations) {
                XSSFRow xssfRow = sheet.createRow(currentRow);
                Cell instance = xssfRow.createCell(0);
                instance.setCellValue(instancesName);
                //boolean first = true;
                int max = 0;
                for (String heuristic : heuristics) {
                    long startTime = System.nanoTime();
                    switch (heuristic) {
                        case "C0":
                            for (int i = 0; i < it; i++) {
                                SolutionPositionsCosts result = constructive.C0_Random(instanceMHC.weights, instanceCR.weights, row, facilitiesRow);
                                paretoOptimal.add(result);
                            }
                            break;

                        case "C1":
                            for (int i = 0; i < it; i++) {
                                SolutionPositionsCosts result = constructive.C1_GR(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                                paretoOptimal.add(result);
                            }
                            break;

                        case "C2":
                            for (int i = 0; i < it; i++) {
                                SolutionPositionsCosts result = constructive.C2_RG(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                                paretoOptimal.add(result);
                            }
                            break;

                        case "C3":
                            for (int i = 0; i < it; i++) {
                                SolutionPositionsCosts result = constructive.C3_GR(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                                paretoOptimal.add(result);
                            }
                            break;

                        case "C4":
                            for (int i = 0; i < it; i++) {
                                SolutionPositionsCosts result = constructive.C4_RG(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                                paretoOptimal.add(result);
                            }
                            break;
                    }

                    long endTime = System.nanoTime();
                    long totalTime = endTime - startTime;
                    double elapsedTimeInSecond = (double) totalTime / 1_000_000_000;
                    int auxCurrentRow = currentRow;
                    for (SolutionPositionsCosts aux : paretoOptimal.sortedArrayList) {
                        finalPareto.add(aux); //all de solutions from all the constructive
                        if (sheet.getRow(currentRow) != null)
                            xssfRow = sheet.getRow(currentRow);
                        else
                            xssfRow = sheet.createRow(currentRow);
                        Cell mhc = xssfRow.createCell(mhcRow);
                        Cell cr = xssfRow.createCell(crRow);
                        Cell time = xssfRow.createCell(timeRow);

                        mhc.setCellValue(aux.getCostSolutionO1());
                        cr.setCellValue(aux.getCostSolutionO2());
                        time.setCellValue(elapsedTimeInSecond);
                        currentRow++;
                    }

                    //We change the constructive
                    mhcRow += 3;
                    crRow += 3;
                    timeRow += 3;
                    currentRow = auxCurrentRow;

                    bucket.add(common.CopySortedArrayListSolution(paretoOptimal));
                    if (paretoOptimal.sortedArrayList.size() > max)
                        max = paretoOptimal.sortedArrayList.size();
                    paretoOptimal.sortedArrayList.clear();
                }

                for (SolutionPositionsCosts aux : finalPareto.sortedArrayList) {
                    if (sheet.getRow(currentRow) != null)
                        xssfRow = sheet.getRow(currentRow);
                    else
                        xssfRow = sheet.createRow(currentRow);
                    Cell mhc = xssfRow.createCell(mhcRow);
                    Cell cr = xssfRow.createCell(crRow);

                    mhc.setCellValue(aux.getCostSolutionO1());
                    cr.setCellValue(aux.getCostSolutionO2());
                    currentRow++;
                }

                //Check if is the Set
                ArrayList<Integer> bucketParetoPoints = new ArrayList<>();
                for (SortedArrayList aux : bucket) {
                    int paretoPoints = 0;
                    for (SolutionPositionsCosts sol : aux.sortedArrayList) {
                        if (finalPareto.sortedArrayList.contains(sol))
                            paretoPoints++;
                    }
                    bucketParetoPoints.add(paretoPoints);
                }

                for (Integer aux : bucketParetoPoints) {
                    if (sheet.getRow(tableRow) != null)
                        xssfRow = sheet.getRow(tableRow);
                    else
                        xssfRow = sheet.createRow(tableRow);
                    Cell mhc = xssfRow.createCell(tableCell);
                    mhc.setCellValue(aux);
                    tableCell++;
                }

                tableRow++;
                tableCell = 1;
                currentRow += max;
                mhcRow = 1;
                crRow = 2;
                timeRow = 3;
                currentRow++;
            }
            try {
                XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
                FileOutputStream outputStream = new FileOutputStream(filename);
                workbook.write(outputStream);
            } catch (Exception e) {
                System.out.println("Error with the file: " + e);
            }
        }
    }

    public void constructiveIII(String[] args) throws IOException {

        inputStream = new FileInputStream(filename);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

        XSSFSheet sheet = workbook.createSheet("Constructive III 500");
        currentRow = 31;

        for (String instancesName : instancesNames) {
            ArrayList<SortedArrayList> bucket = new ArrayList<>();
            SortedArrayList finalPareto = new SortedArrayList();

            Instance instanceMHC = new Instance("src\\main\\resources" + File.separator + instancesName + ".txt", 0);
            Instance instanceCR = new Instance("src\\main\\resources" + File.separator + instancesName + "_CR.txt", 0);
            System.out.println(instancesName);

            //Fix the number of rows and the number of facilities in each row
            Topology(instanceCR.numFacilities);
            for (int it : iterations) {
                XSSFRow xssfRow = sheet.createRow(currentRow);
                Cell instance = xssfRow.createCell(0);
                instance.setCellValue(instancesName);
                //boolean first = true;
                int max = 0;
                for (String heuristic : heuristics) {
                    long startTime = System.nanoTime();
                    switch (heuristic) {
                        case "C0":
                            for (int i = 0; i < it; i++) {
                                SolutionPositionsCosts result = constructive.C0_Random(instanceMHC.weights, instanceCR.weights, row, facilitiesRow);
                                paretoOptimal.add(result);
                                //System.out.println("MHC: "+result.getCostSolutionO1()+"  CR: "+result.getCostSolutionO2());
                            }
                            break;

                        case "C1":
                            for (int i = 0; i < it; i++) {
                                SolutionPositionsCosts result = constructiveB.C1_GR(instanceMHC.weights, instanceCR.weights, row, facilitiesRow);
                                paretoOptimal.add(result);
                            }
                            //System.out.println(paretoOptimal.sortedArrayList.size());
                            break;

                        case "C2":
                            for (int i = 0; i < it; i++) {
                                //Random alpha for the constructive
                                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                                SolutionPositionsCosts result = constructiveB.C2_RG(instanceMHC.weights, instanceCR.weights, row, facilitiesRow);
                                paretoOptimal.add(result);
                            }
                            break;

                        case "C3":
                            for (int i = 0; i < it; i++) {
                                SolutionPositionsCosts result = constructiveB.C3_GR(instanceMHC.weights, instanceCR.weights, row, facilitiesRow);
                                paretoOptimal.add(result);
                            }
                            break;

                        case "C4":
                            for (int i = 0; i < it; i++) {
                                SolutionPositionsCosts result = constructiveB.C4_RG(instanceMHC.weights, instanceCR.weights, row, facilitiesRow);
                                paretoOptimal.add(result);
                            }
                            break;

                        case "C5": //C1+C3
                            for (int i = 0; i < it / 2; i++) {
                                //Random alpha for the constructive
                                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                                SolutionPositionsCosts result = constructive.C1_GR(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                                paretoOptimal.add(result);
                            }

                            for (int i = 0; i < it / 2; i++) {
                                //Random alpha for the constructive
                                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                                SolutionPositionsCosts result = constructive.C3_GR(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                                paretoOptimal.add(result);
                            }
                            break;

                        case "C6": //C1+C4
                            for (int i = 0; i < it / 2; i++) {
                                //Random alpha for the constructive
                                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                                SolutionPositionsCosts result = constructive.C1_GR(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                                paretoOptimal.add(result);
                            }

                            for (int i = 0; i < it / 2; i++) {
                                //Random alpha for the constructive
                                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                                SolutionPositionsCosts result = constructive.C4_RG(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                                paretoOptimal.add(result);
                            }
                            break;

                        case "C7": //C2+C3
                            for (int i = 0; i < it / 2; i++) {//Random alpha for the constructive
                                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                                SolutionPositionsCosts result = constructive.C2_RG(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                                paretoOptimal.add(result);
                            }

                            for (int i = 0; i < it / 2; i++) {
                                //Random alpha for the constructive
                                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                                SolutionPositionsCosts result = constructive.C3_GR(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                                paretoOptimal.add(result);
                            }
                            break;

                        case "C8": //C2+C4
                            for (int i = 0; i < it / 2; i++) {
                                //Random alpha for the constructive
                                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                                SolutionPositionsCosts result = constructive.C2_RG(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                                paretoOptimal.add(result);
                            }

                            for (int i = 0; i < it / 2; i++) {
                                //Random alpha for the constructive
                                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                                SolutionPositionsCosts result = constructive.C4_RG(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                                paretoOptimal.add(result);
                            }
                            break;

                    }
                    // SortedArrayList results = paretoOptimal.reducedParetoFrontier(paretoOptimal, 4);
                    long endTime = System.nanoTime();
                    long totalTime = endTime - startTime;
                    double elapsedTimeInSecond = (double) totalTime / 1_000_000_000;
                    int auxCurrentRow = currentRow;
                    for (SolutionPositionsCosts aux : paretoOptimal.sortedArrayList) {
                        finalPareto.add(aux); //all de solutions from all the constructive
                        if (sheet.getRow(currentRow) != null)
                            xssfRow = sheet.getRow(currentRow);
                        else
                            xssfRow = sheet.createRow(currentRow);
                        Cell mhc = xssfRow.createCell(mhcRow);
                        Cell cr = xssfRow.createCell(crRow);
                        Cell time = xssfRow.createCell(timeRow);

                        mhc.setCellValue(aux.getCostSolutionO1());
                        cr.setCellValue(aux.getCostSolutionO2());
                        time.setCellValue(elapsedTimeInSecond);
                        currentRow++;
                    }

                    //We change the constructive
                    mhcRow += 3;
                    crRow += 3;
                    timeRow += 3;
                    currentRow = auxCurrentRow;

                    bucket.add(common.CopySortedArrayListSolution(paretoOptimal));
                    if (paretoOptimal.sortedArrayList.size() > max)
                        max = paretoOptimal.sortedArrayList.size();
                    paretoOptimal.sortedArrayList.clear();
                }

                for (SolutionPositionsCosts aux : finalPareto.sortedArrayList) {
                    if (sheet.getRow(currentRow) != null)
                        xssfRow = sheet.getRow(currentRow);
                    else
                        xssfRow = sheet.createRow(currentRow);
                    Cell mhc = xssfRow.createCell(mhcRow);
                    Cell cr = xssfRow.createCell(crRow);

                    mhc.setCellValue(aux.getCostSolutionO1());
                    cr.setCellValue(aux.getCostSolutionO2());
                    currentRow++;
                }

                //Check if is the Set
                ArrayList<Integer> bucketParetoPoints = new ArrayList<>();
                for (SortedArrayList aux : bucket) {
                    int paretoPoints = 0;
                    for (SolutionPositionsCosts sol : aux.sortedArrayList) {
                        if (finalPareto.sortedArrayList.contains(sol))
                            paretoPoints++;
                    }
                    bucketParetoPoints.add(paretoPoints);
                }

                for (Integer aux : bucketParetoPoints) {
                    if (sheet.getRow(tableRow) != null)
                        xssfRow = sheet.getRow(tableRow);
                    else
                        xssfRow = sheet.createRow(tableRow);
                    Cell mhc = xssfRow.createCell(tableCell);
                    mhc.setCellValue(aux);
                    tableCell++;
                }

                tableRow++;
                tableCell = 1;
                currentRow += max;
                mhcRow = 1;
                crRow = 2;
                timeRow = 3;
                currentRow++;
            }
            try {
                XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
                FileOutputStream outputStream = new FileOutputStream(filename);
                workbook.write(outputStream);
            } catch (Exception e) {
                System.out.println("Error with the file: " + e);
            }
        }
    }

    public void constructiveIV(double perC1, double perC2, double perC3, double perC4) throws IOException {

        inputStream = new FileInputStream(filename);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

        XSSFSheet sheet = workbook.createSheet("Constructive IV " + iterations[0]);
        currentRow = 31;

        for (String instancesName : instancesNames) {
            Instance instanceMHC = new Instance("src\\main\\resources" + File.separator + instancesName + ".txt", 0);
            Instance instanceCR = new Instance("src\\main\\resources" + File.separator + instancesName + "_CR.txt", 0);
            System.out.println(instancesName);

            //Fix the number of rows and the number of facilities in each row
            Topology(instanceCR.numFacilities);
            for (int it : iterations) {
                XSSFRow xssfRow = sheet.createRow(currentRow);
                Cell instance = xssfRow.createCell(0);
                instance.setCellValue(instancesName);
                //boolean first = true;
                int max = 0;

                ArrayList<SortedArrayList> bucket = new ArrayList<>();
                SortedArrayList finalPareto = new SortedArrayList();
                for (String heuristic : heuristics) {
                    long startTime = System.nanoTime();
                    switch (heuristic) {
                        case "C0":
                            for (int i = 0; i < it; i++) {
                                SolutionPositionsCosts result = constructive.C0_Random(instanceMHC.weights, instanceCR.weights, row, facilitiesRow);
                                paretoOptimal.add(result);
                                //System.out.println("MHC: "+result.getCostSolutionO1()+"  CR: "+result.getCostSolutionO2());
                            }
                            break;

                        case "C1":
                            for (int i = 0; i < it * perC1; i++) {
                                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                                SolutionPositionsCosts result = constructive.C1_GR(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                                paretoOptimal.add(result);
                            }
                            //System.out.println(paretoOptimal.sortedArrayList.size());
                            break;

                        case "C2":
                            for (int i = 0; i < it * perC2; i++) {
                                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                                SolutionPositionsCosts result = constructive.C2_RG(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                                paretoOptimal.add(result);
                            }
                            break;

                        case "C3":
                            for (int i = 0; i < it * perC3; i++) {
                                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                                SolutionPositionsCosts result = constructive.C3_GR(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                                paretoOptimal.add(result);
                            }
                            break;

                        case "C4":
                            for (int i = 0; i < it * perC4; i++) {
                                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                                SolutionPositionsCosts result = constructive.C4_RG(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                                paretoOptimal.add(result);
                            }
                            break;
                    }
                    // SortedArrayList results = paretoOptimal.reducedParetoFrontier(paretoOptimal, 4);
                    long endTime = System.nanoTime();
                    long totalTime = endTime - startTime;
                    double elapsedTimeInSecond = (double) totalTime / 1_000_000_000;
                    int auxCurrentRow = currentRow;
                    for (SolutionPositionsCosts aux : paretoOptimal.sortedArrayList) {
                        finalPareto.add(aux); //all de solutions from all the constructive
                        if (sheet.getRow(currentRow) != null)
                            xssfRow = sheet.getRow(currentRow);
                        else
                            xssfRow = sheet.createRow(currentRow);
                        Cell mhc = xssfRow.createCell(mhcRow);
                        Cell cr = xssfRow.createCell(crRow);
                        Cell time = xssfRow.createCell(timeRow);

                        mhc.setCellValue(aux.getCostSolutionO1());
                        cr.setCellValue(aux.getCostSolutionO2());
                        time.setCellValue(elapsedTimeInSecond);
                        currentRow++;
                    }

                    //We change the constructive
                    mhcRow += 3;
                    crRow += 3;
                    timeRow += 3;
                    currentRow = auxCurrentRow;

                    bucket.add(common.CopySortedArrayListSolution(paretoOptimal));
                    if (paretoOptimal.sortedArrayList.size() > max)
                        max = paretoOptimal.sortedArrayList.size();
                    paretoOptimal.sortedArrayList.clear();
                }
                currentRow += max;
                mhcRow = 1;
                crRow = 2;
                timeRow = 3;


                for (SolutionPositionsCosts aux : finalPareto.sortedArrayList) {
                    if (sheet.getRow(currentRow) != null)
                        xssfRow = sheet.getRow(currentRow);
                    else
                        xssfRow = sheet.createRow(currentRow);
                    Cell mhc = xssfRow.createCell(mhcRow);
                    Cell cr = xssfRow.createCell(crRow);

                    mhc.setCellValue(aux.getCostSolutionO1());
                    cr.setCellValue(aux.getCostSolutionO2());
                    currentRow++;
                }

                //Check if is the Set
                ArrayList<Integer> bucketParetoPoints = new ArrayList<>();
                for (SortedArrayList aux : bucket) {
                    int paretoPoints = 0;
                    for (SolutionPositionsCosts sol : aux.sortedArrayList) {
                        if (finalPareto.sortedArrayList.contains(sol))
                            paretoPoints++;
                    }
                    bucketParetoPoints.add(paretoPoints);
                }

                for (Integer aux : bucketParetoPoints) {
                    if (sheet.getRow(tableRow) != null)
                        xssfRow = sheet.getRow(tableRow);
                    else
                        xssfRow = sheet.createRow(tableRow);
                    Cell mhc = xssfRow.createCell(tableCell);
                    mhc.setCellValue(aux);
                    tableCell++;
                }
                tableCell++;
                Cell size = xssfRow.createCell(tableCell);
                size.setCellValue(finalPareto.sortedArrayList.size());

                tableRow++;
                tableCell = 2;

                currentRow++;
            }
            try {
                XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
                FileOutputStream outputStream = new FileOutputStream(filename);
                workbook.write(outputStream);
            } catch (Exception e) {
                System.out.println("Error with the file: " + e);
            }
        }
    }

    //Dominated LS and ConstructiveA
    public ArrayList<SortedArrayList> lsDominated(double perC1, double perC2, double perC3, double perC4, int it) throws IOException {
        ArrayList<SortedArrayList> bucket = new ArrayList<>();

        inputStream = new FileInputStream(filename);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

        XSSFSheet sheet = workbook.createSheet("LSDominated " + iterations[0]);
        currentRow = 31;

        for (String instancesName : instancesNames) {
            Instance instanceMHC = new Instance("src\\main\\resources" + File.separator + instancesName + ".txt", 0);
            Instance instanceCR = new Instance("src\\main\\resources" + File.separator + instancesName + "_CR.txt", 0);
            System.out.println(instancesName);

            //Fix the number of rows and the number of facilities in each row
            Topology(instanceCR.numFacilities);

            XSSFRow xssfRow = sheet.createRow(currentRow);
            Cell instance = xssfRow.createCell(0);
            instance.setCellValue(instancesName);

            SortedArrayList finalPareto = new SortedArrayList();

            long startTime = System.nanoTime();

            //Constructive phase
            for (int i = 0; i < it * perC1; i++) {
                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                SolutionPositionsCosts result = constructive.C1_GR(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                paretoOptimal.add(result);
            }


            for (int i = 0; i < it * perC2; i++) {
                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                SolutionPositionsCosts result = constructive.C2_RG(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                paretoOptimal.add(result);
            }


            for (int i = 0; i < it * perC3; i++) {
                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                SolutionPositionsCosts result = constructive.C3_GR(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                paretoOptimal.add(result);
            }


            for (int i = 0; i < it * perC4; i++) {
                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                SolutionPositionsCosts result = constructive.C4_RG(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                paretoOptimal.add(result);
            }

            //LS phase. Dominated LS
            boolean changes = true;

            while (changes) {
                changes = false;
                for (SolutionPositionsCosts temp : paretoOptimal.sortedArrayList) {
                    if (localSearch.dominatedHybridLocalSearchInterchange(temp, instanceMHC.weights, instanceCR.weights, results))
                        changes = true;
                }

                if (changes) {
                    paretoOptimal = common.CopySortedArrayListSolution(results);
                    results = common.CopySortedArrayListSolution(paretoOptimal);
                }
            }

            long endTime = System.nanoTime();
            long totalTime = endTime - startTime;
            double elapsedTimeInSecond = (double) totalTime / 1_000_000_000;

            for (SolutionPositionsCosts aux : paretoOptimal.sortedArrayList) {
                finalPareto.add(aux); //all de solutions from all the constructive
                if (sheet.getRow(currentRow) != null)
                    xssfRow = sheet.getRow(currentRow);
                else
                    xssfRow = sheet.createRow(currentRow);
                Cell mhc = xssfRow.createCell(mhcRow);
                Cell cr = xssfRow.createCell(crRow);
                Cell time = xssfRow.createCell(timeRow);

                mhc.setCellValue(aux.getCostSolutionO1());
                cr.setCellValue(aux.getCostSolutionO2());
                time.setCellValue(elapsedTimeInSecond);
                currentRow++;
            }
            //Next instance
            currentRow++;

            //To save data in a file
            common.CreateFile("C:\\Users\\nicor\\OneDrive - Universidad Rey Juan Carlos\\PhD\\Repositorio\\MOMetricsMavenInicial\\test\\dominancia\\" + instancesName + ".txt", paretoOptimal);
            bucket.add(common.CopySortedArrayListSolution(paretoOptimal));
            paretoOptimal.sortedArrayList.clear();
            results.sortedArrayList.clear();

            try {
                XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
                FileOutputStream outputStream = new FileOutputStream(filename);
                workbook.write(outputStream);
            } catch (Exception e) {
                System.out.println("Error with the file: " + e);
            }
        }
        return bucket;
    }

    //Dominated LS and ConstructiveA
    public ArrayList<SortedArrayList> lsDominatedEfficient(double perC1, double perC2, double perC3, double perC4, int it) throws IOException {
        ArrayList<SortedArrayList> bucket = new ArrayList<>();

        //inputStream = new FileInputStream(filename);
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("N-15");
        //XSSFSheet sheet = workbook.createSheet("O-10  " + iterations[0]);
        currentRow = 31;

        for (String instancesName : instancesNames) {
            Instance instanceMHC = new Instance("src\\main\\resources" + File.separator + instancesName + ".txt", 0);
            Instance instanceCR = new Instance("src\\main\\resources" + File.separator + instancesName + "_CR.txt", 0);
            System.out.println(instancesName);

            //Fix the number of rows and the number of facilities in each row
            Topology(instanceCR.numFacilities);

            XSSFRow xssfRow = sheet.createRow(currentRow);
            Cell instance = xssfRow.createCell(0);
            instance.setCellValue(instancesName);

            SortedArrayList finalPareto = new SortedArrayList();

            long startTime = System.nanoTime();

            //Constructive phase
            for (int i = 0; i < it * perC1; i++) {
                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                SolutionPositionsCosts result = constructive.C1_GR(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                paretoOptimal.add(result);
            }


            for (int i = 0; i < it * perC2; i++) {
                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                SolutionPositionsCosts result = constructive.C2_RG(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                paretoOptimal.add(result);
            }


            for (int i = 0; i < it * perC3; i++) {
                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                SolutionPositionsCosts result = constructive.C3_GR(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                paretoOptimal.add(result);
            }


            for (int i = 0; i < it * perC4; i++) {
                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                SolutionPositionsCosts result = constructive.C4_RG(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                paretoOptimal.add(result);
            }

            //LS phase. Dominated LS
            boolean changes = true;

            while (changes) {
                changes = false;
                for (SolutionPositionsCosts temp : paretoOptimal.sortedArrayList) {
                    if (localSearch.dominatedHybridLocalSearchInterchangeEfficient(temp, instanceMHC.weights, instanceCR.weights, results))
                        changes = true;
                }

                if (changes) {
                    paretoOptimal = common.CopySortedArrayListSolution(results);
                    results = common.CopySortedArrayListSolution(paretoOptimal);
                }
            }

            long endTime = System.nanoTime();
            long totalTime = endTime - startTime;
            double elapsedTimeInSecond = (double) totalTime / 1_000_000_000;

            for (SolutionPositionsCosts aux : paretoOptimal.sortedArrayList) {
                finalPareto.add(aux); //all de solutions from all the constructive
                if (sheet.getRow(currentRow) != null)
                    xssfRow = sheet.getRow(currentRow);
                else
                    xssfRow = sheet.createRow(currentRow);
                Cell mhc = xssfRow.createCell(mhcRow);
                Cell cr = xssfRow.createCell(crRow);
                Cell time = xssfRow.createCell(timeRow);

                mhc.setCellValue(aux.getCostSolutionO1());
                cr.setCellValue(aux.getCostSolutionO2());
                time.setCellValue(elapsedTimeInSecond);
                currentRow++;
            }
            //Next instance
            currentRow++;

            //To save data in a file
            common.CreateFile("src\\main\\data_metrics\\dominating" + File.separator + instancesName + ".txt", paretoOptimal);

            bucket.add(common.CopySortedArrayListSolution(paretoOptimal));
            paretoOptimal.sortedArrayList.clear();
            results.sortedArrayList.clear();

            try {
                XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
                FileOutputStream outputStream = new FileOutputStream(filename);
                workbook.write(outputStream);
            } catch (Exception e) {
                System.out.println("Error with the file: " + e);
            }
        }
        return bucket;
    }

    //Mono-objective LS and ConstructiveA
    public void lsMonoObjective(double perC1, double perC2, double perC3, double perC4, int it) throws IOException {

        inputStream = new FileInputStream(filename);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

        XSSFSheet sheet = workbook.createSheet("LSM " + iterations[0]);
        currentRow = 31;

        for (String instancesName : instancesNames) {
            Instance instanceMHC = new Instance("src\\main\\resources" + File.separator + instancesName + ".txt", 0);
            Instance instanceCR = new Instance("src\\main\\resources" + File.separator + instancesName + "_CR.txt", 0);
            System.out.println(instancesName);

            //Fix the number of rows and the number of facilities in each row
            Topology(instanceCR.numFacilities);

            XSSFRow xssfRow = sheet.createRow(currentRow);
            Cell instance = xssfRow.createCell(0);
            instance.setCellValue(instancesName);

            SortedArrayList finalPareto = new SortedArrayList();

            long startTime = System.nanoTime();

            for (int i = 0; i < it * perC1; i++) {
                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                SolutionPositionsCosts result = constructive.C1_GR(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                paretoOptimal.add(result);
            }

            for (int i = 0; i < it * perC2; i++) {
                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                SolutionPositionsCosts result = constructive.C2_RG(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                paretoOptimal.add(result);
            }

            for (int i = 0; i < it * perC3; i++) {
                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                SolutionPositionsCosts result = constructive.C3_GR(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                paretoOptimal.add(result);
            }

            for (int i = 0; i < it * perC4; i++) {
                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                SolutionPositionsCosts result = constructive.C4_RG(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                paretoOptimal.add(result);
            }


            boolean changes = true;

            while (changes) {
                changes = false;
                ArrayList<SolutionPositionsCosts> newSolutions = new ArrayList<>();
                for (SolutionPositionsCosts temp : paretoOptimal.sortedArrayList) {
                    SolutionPositionsCosts newSolution = localSearch.hybridLocalSearchInterchange(temp, instanceMHC.weights, instanceCR.weights, "mhc");
                    newSolutions.add(newSolution);
                    newSolution = localSearch.hybridLocalSearchInterchange(temp, instanceMHC.weights, instanceCR.weights, "cr");
                    newSolutions.add(newSolution);
                }
                results = common.CopySortedArrayListSolution(paretoOptimal);


                for (SolutionPositionsCosts temp : newSolutions) {
                    paretoOptimal.add(temp);
                }

                if (paretoOptimal.sortedArrayList.size() != results.sortedArrayList.size()) {
                    changes = true;
                } else {
                    for (int i = 0; i < paretoOptimal.sortedArrayList.size(); i++) {
                        if (!paretoOptimal.sortedArrayList.get(i).getSolution().equals(results.sortedArrayList.get(i).getSolution())) {
                            changes = true;
                            break;
                        }
                    }
                }
            }

            long endTime = System.nanoTime();
            long totalTime = endTime - startTime;
            double elapsedTimeInSecond = (double) totalTime / 1_000_000_000;
            for (SolutionPositionsCosts aux : paretoOptimal.sortedArrayList) {
                finalPareto.add(aux); //all de solutions from all the constructive
                if (sheet.getRow(currentRow) != null)
                    xssfRow = sheet.getRow(currentRow);
                else
                    xssfRow = sheet.createRow(currentRow);
                Cell mhc = xssfRow.createCell(mhcRow);
                Cell cr = xssfRow.createCell(crRow);
                Cell time = xssfRow.createCell(timeRow);

                mhc.setCellValue(aux.getCostSolutionO1());
                cr.setCellValue(aux.getCostSolutionO2());
                time.setCellValue(elapsedTimeInSecond);
                currentRow++;
            }
            currentRow++;
            paretoOptimal.sortedArrayList.clear();
            results.sortedArrayList.clear();

            try {
                XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
                FileOutputStream outputStream = new FileOutputStream(filename);
                workbook.write(outputStream);
            } catch (Exception e) {
                System.out.println("Error with the file: " + e);
            }
        }
    }

    //Mono-objective LS and ConstructiveA with intermediate solutions
    public void lsIntermediate(double perC1, double perC2, double perC3, double perC4, int it) throws IOException {

        inputStream = new FileInputStream(filename);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

        XSSFSheet sheet = workbook.createSheet("LS Intermediate " + iterations[0]);
        currentRow = 31;

        for (String instancesName : instancesNames) {
            Instance instanceMHC = new Instance("src\\main\\resources" + File.separator + instancesName + ".txt", 0);
            Instance instanceCR = new Instance("src\\main\\resources" + File.separator + instancesName + "_CR.txt", 0);
            System.out.println(instancesName);

            //Fix the number of rows and the number of facilities in each row
            Topology(instanceCR.numFacilities);

            XSSFRow xssfRow = sheet.createRow(currentRow);
            Cell instance = xssfRow.createCell(0);
            instance.setCellValue(instancesName);

            SortedArrayList finalPareto = new SortedArrayList();

            long startTime = System.nanoTime();

            for (int i = 0; i < it * perC1; i++) {
                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                SolutionPositionsCosts result = constructive.C1_GR(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                paretoOptimal.add(result);
            }

            for (int i = 0; i < it * perC2; i++) {
                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                SolutionPositionsCosts result = constructive.C2_RG(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                paretoOptimal.add(result);
            }

            for (int i = 0; i < it * perC3; i++) {
                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                SolutionPositionsCosts result = constructive.C3_GR(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                paretoOptimal.add(result);
            }

            for (int i = 0; i < it * perC4; i++) {
                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                SolutionPositionsCosts result = constructive.C4_RG(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                paretoOptimal.add(result);
            }

            for (SolutionPositionsCosts temp : paretoOptimal.sortedArrayList) {
                localSearch.hybridLocalSearchInterchangeHolderMHC(temp, instanceMHC.weights, instanceCR.weights, results);
                localSearch.hybridLocalSearchInterchangeHolderCR(temp, instanceMHC.weights, instanceCR.weights, results);
            }

            paretoOptimal = common.CopySortedArrayListSolution(results);

            boolean changes = true;

            while (changes) {
                changes = false;
                ArrayList<SolutionPositionsCosts> newSolutions = new ArrayList<>();
                for (SolutionPositionsCosts temp : paretoOptimal.sortedArrayList) {
                    SolutionPositionsCosts newSolution = localSearch.hybridLocalSearchInterchange(temp, instanceMHC.weights, instanceCR.weights, "mhc");
                    newSolutions.add(newSolution);
                    newSolution = localSearch.hybridLocalSearchInterchange(temp, instanceMHC.weights, instanceCR.weights, "cr");
                    newSolutions.add(newSolution);
                }
                results = common.CopySortedArrayListSolution(paretoOptimal);

                for (SolutionPositionsCosts temp : newSolutions) {
                    paretoOptimal.add(temp);
                }
                if (paretoOptimal.sortedArrayList.size() != results.sortedArrayList.size()) {
                    changes = true;
                } else {
                    for (int i = 0; i < paretoOptimal.sortedArrayList.size(); i++) {
                        if (!paretoOptimal.sortedArrayList.get(i).getSolution().equals(results.sortedArrayList.get(i).getSolution())) {
                            changes = true;
                            break;
                        }
                    }
                }
            }

            long endTime = System.nanoTime();
            long totalTime = endTime - startTime;
            double elapsedTimeInSecond = (double) totalTime / 1_000_000_000;
            for (SolutionPositionsCosts aux : paretoOptimal.sortedArrayList) {
                finalPareto.add(aux); //all de solutions from all the constructive
                if (sheet.getRow(currentRow) != null)
                    xssfRow = sheet.getRow(currentRow);
                else
                    xssfRow = sheet.createRow(currentRow);
                Cell mhc = xssfRow.createCell(mhcRow);
                Cell cr = xssfRow.createCell(crRow);
                Cell time = xssfRow.createCell(timeRow);

                mhc.setCellValue(aux.getCostSolutionO1());
                cr.setCellValue(aux.getCostSolutionO2());
                time.setCellValue(elapsedTimeInSecond);
                currentRow++;
            }

            currentRow++;

            paretoOptimal.sortedArrayList.clear();
            results.sortedArrayList.clear();

            try {
                XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
                FileOutputStream outputStream = new FileOutputStream(filename);
                workbook.write(outputStream);
            } catch (Exception e) {
                System.out.println("Error with the file: " + e);
            }
        }
    }

    //Dominated LS and ConstructiveA. Loop changed by iterations instead of a while loop
    public ArrayList<SortedArrayList> lsDominatedByIt(double perC1, double perC2, double perC3, double perC4, int it, int auxIT) throws IOException {
        IT = auxIT;
        ArrayList<SortedArrayList> bucket = new ArrayList<>();
        inputStream = new FileInputStream(filename);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

        XSSFSheet sheet = workbook.createSheet("LSD " + iterations[0] + " " + IT);
        currentRow = 31;

        for (String instancesName : instancesNames) {
            Instance instanceMHC = new Instance("src\\main\\resources" + File.separator + instancesName + ".txt", 0);
            Instance instanceCR = new Instance("src\\main\\resources" + File.separator + instancesName + "_CR.txt", 0);
            System.out.println(instancesName);

            //Fix the number of rows and the number of facilities in each row
            Topology(instanceCR.numFacilities);

            XSSFRow xssfRow = sheet.createRow(currentRow);
            Cell instance = xssfRow.createCell(0);
            instance.setCellValue(instancesName);

            SortedArrayList finalPareto = new SortedArrayList();

            long startTime = System.nanoTime();
            for (int i = 0; i < it * perC1; i++) {
                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                SolutionPositionsCosts result = constructive.C1_GR(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                paretoOptimal.add(result);
            }

            for (int i = 0; i < it * perC2; i++) {
                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                SolutionPositionsCosts result = constructive.C2_RG(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                paretoOptimal.add(result);
            }

            for (int i = 0; i < it * perC3; i++) {
                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                SolutionPositionsCosts result = constructive.C3_GR(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                paretoOptimal.add(result);
            }

            for (int i = 0; i < it * perC4; i++) {
                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                SolutionPositionsCosts result = constructive.C4_RG(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                paretoOptimal.add(result);
            }

            boolean changes;

            for (it = 0; it < IT; it++) {
                changes = false;
                for (SolutionPositionsCosts temp : paretoOptimal.sortedArrayList) {
                    if (localSearch.dominatedHybridLocalSearchInterchange(temp, instanceMHC.weights, instanceCR.weights, results))
                        changes = true;
                }

                if (changes) {
                    paretoOptimal = common.CopySortedArrayListSolution(results);
                    results = common.CopySortedArrayListSolution(paretoOptimal);
                }
            }

            long endTime = System.nanoTime();
            long totalTime = endTime - startTime;
            double elapsedTimeInSecond = (double) totalTime / 1_000_000_000;
            for (SolutionPositionsCosts aux : paretoOptimal.sortedArrayList) {
                finalPareto.add(aux); //all de solutions from all the constructive
                if (sheet.getRow(currentRow) != null)
                    xssfRow = sheet.getRow(currentRow);
                else
                    xssfRow = sheet.createRow(currentRow);
                Cell mhc = xssfRow.createCell(mhcRow);
                Cell cr = xssfRow.createCell(crRow);
                Cell time = xssfRow.createCell(timeRow);

                mhc.setCellValue(aux.getCostSolutionO1());
                cr.setCellValue(aux.getCostSolutionO2());
                time.setCellValue(elapsedTimeInSecond);
                currentRow++;
            }

            currentRow++;
            bucket.add(common.CopySortedArrayListSolution(paretoOptimal));
            paretoOptimal.sortedArrayList.clear();
            results.sortedArrayList.clear();

            try {
                XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
                FileOutputStream outputStream = new FileOutputStream(filename);
                workbook.write(outputStream);
            } catch (Exception e) {
                System.out.println("Error with the file: " + e);
            }
        }
        return bucket;
    }

    //Dominated LS and ConstructiveA. Loop changed by iterations instead of a while loop. An additional LS.
    public void lsDominatedByItMono(double perC1, double perC2, double perC3, double perC4, int it, int auxIT) throws IOException {
        IT = auxIT;
        inputStream = new FileInputStream(filename);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

        XSSFSheet sheet = workbook.createSheet("LSDIt II " + iterations[0] + " " + IT);
        currentRow = 31;

        for (String instancesName : instancesNames) {
            Instance instanceMHC = new Instance("src\\main\\resources" + File.separator + instancesName + ".txt", 0);
            Instance instanceCR = new Instance("src\\main\\resources" + File.separator + instancesName + "_CR.txt", 0);
            System.out.println(instancesName);

            //Fix the number of rows and the number of facilities in each row
            Topology(instanceCR.numFacilities);

            XSSFRow xssfRow = sheet.createRow(currentRow);
            Cell instance = xssfRow.createCell(0);
            instance.setCellValue(instancesName);

            SortedArrayList finalPareto = new SortedArrayList();

            long startTime = System.nanoTime();
            for (int i = 0; i < it * perC1; i++) {
                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                SolutionPositionsCosts result = constructive.C1_GR(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                paretoOptimal.add(result);
            }

            for (int i = 0; i < it * perC2; i++) {
                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                SolutionPositionsCosts result = constructive.C2_RG(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                paretoOptimal.add(result);
            }

            for (int i = 0; i < it * perC3; i++) {
                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                SolutionPositionsCosts result = constructive.C3_GR(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                paretoOptimal.add(result);
            }

            for (int i = 0; i < it * perC4; i++) {
                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                SolutionPositionsCosts result = constructive.C4_RG(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                paretoOptimal.add(result);
            }

            boolean changes;

            for (it = 0; it < IT; it++) {
                changes = false;
                for (SolutionPositionsCosts temp : paretoOptimal.sortedArrayList) {
                    if (localSearch.dominatedHybridLocalSearchInterchange(temp, instanceMHC.weights, instanceCR.weights, results))
                        changes = true;
                }

                if (changes) {
                    paretoOptimal = common.CopySortedArrayListSolution(results);
                    results = common.CopySortedArrayListSolution(paretoOptimal);
                }
            }

            changes = true;

            while (changes) {
                changes = false;
                ArrayList<SolutionPositionsCosts> newSolutions = new ArrayList<>();
                for (SolutionPositionsCosts temp : paretoOptimal.sortedArrayList) {
                    SolutionPositionsCosts newSolution = localSearch.hybridLocalSearchInterchange(temp, instanceMHC.weights, instanceCR.weights, "mhc");
                    newSolutions.add(newSolution);
                    newSolution = localSearch.hybridLocalSearchInterchange(temp, instanceMHC.weights, instanceCR.weights, "cr");
                    newSolutions.add(newSolution);
                }
                results = common.CopySortedArrayListSolution(paretoOptimal);

                for (SolutionPositionsCosts temp : newSolutions) {
                    paretoOptimal.add(temp);
                }

                if (paretoOptimal.sortedArrayList.size() != results.sortedArrayList.size()) {
                    changes = true;
                } else {
                    for (int i = 0; i < paretoOptimal.sortedArrayList.size(); i++) {
                        if (!paretoOptimal.sortedArrayList.get(i).getSolution().equals(results.sortedArrayList.get(i).getSolution())) {
                            changes = true;
                            break;
                        }
                    }
                }
            }

            long endTime = System.nanoTime();
            long totalTime = endTime - startTime;
            double elapsedTimeInSecond = (double) totalTime / 1_000_000_000;

            for (SolutionPositionsCosts aux : paretoOptimal.sortedArrayList) {
                finalPareto.add(aux); //all de solutions from all the constructive
                if (sheet.getRow(currentRow) != null)
                    xssfRow = sheet.getRow(currentRow);
                else
                    xssfRow = sheet.createRow(currentRow);
                Cell mhc = xssfRow.createCell(mhcRow);
                Cell cr = xssfRow.createCell(crRow);
                Cell time = xssfRow.createCell(timeRow);

                mhc.setCellValue(aux.getCostSolutionO1());
                cr.setCellValue(aux.getCostSolutionO2());
                time.setCellValue(elapsedTimeInSecond);
                currentRow++;
            }
            currentRow++;

            paretoOptimal.sortedArrayList.clear();
            results.sortedArrayList.clear();

            try {
                XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
                FileOutputStream outputStream = new FileOutputStream(filename);
                workbook.write(outputStream);
            } catch (Exception e) {
                System.out.println("Error with the file: " + e);
            }
        }
    }

    //Dominated LS and ConstructiveA. Loop changed by iterations instead of a while loop. An additional LS.
    public void extendedDominatedLS(ArrayList<SortedArrayList> bucket) throws IOException {
        inputStream = new FileInputStream(filename);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

        XSSFSheet sheet = workbook.createSheet("LE" + iterations[0]);
        currentRow = 31;

        for (String instancesName : instancesNames) {
            Instance instanceMHC = new Instance("src\\main\\resources" + File.separator + instancesName + ".txt", 0);
            Instance instanceCR = new Instance("src\\main\\resources" + File.separator + instancesName + "_CR.txt", 0);
            System.out.println(instancesName);

            //Fix the number of rows and the number of facilities in each row
            Topology(instanceCR.numFacilities);

            XSSFRow xssfRow = sheet.createRow(currentRow);
            Cell instance = xssfRow.createCell(0);
            instance.setCellValue(instancesName);

            SortedArrayList finalPareto = new SortedArrayList();

            paretoOptimal = common.CopySortedArrayListSolution(bucket.get(0));
            bucket.remove(0);

            long startTime = System.nanoTime();

            boolean changes = true;

            while (changes) {
                changes = false;
                ArrayList<SolutionPositionsCosts> newSolutions = new ArrayList<>();
                for (SolutionPositionsCosts temp : paretoOptimal.sortedArrayList) {
                    SolutionPositionsCosts newSolution = localSearch.hybridLocalSearchInterchange(temp, instanceMHC.weights, instanceCR.weights, "mhc");
                    newSolutions.add(newSolution);
                    newSolution = localSearch.hybridLocalSearchInterchange(temp, instanceMHC.weights, instanceCR.weights, "cr");
                    newSolutions.add(newSolution);
                }
                results = common.CopySortedArrayListSolution(paretoOptimal);

                for (SolutionPositionsCosts temp : newSolutions) {
                    paretoOptimal.add(temp);
                }
                if (paretoOptimal.sortedArrayList.size() != results.sortedArrayList.size()) {
                    changes = true;
                } else {
                    for (int i = 0; i < paretoOptimal.sortedArrayList.size(); i++) {
                        if (!paretoOptimal.sortedArrayList.get(i).getSolution().equals(results.sortedArrayList.get(i).getSolution())) {
                            changes = true;
                            break;
                        }
                    }
                }
            }

            long endTime = System.nanoTime();
            long totalTime = endTime - startTime;
            double elapsedTimeInSecond = (double) totalTime / 1_000_000_000;

            for (SolutionPositionsCosts aux : paretoOptimal.sortedArrayList) {
                finalPareto.add(aux); //all de solutions from all the constructive
                if (sheet.getRow(currentRow) != null)
                    xssfRow = sheet.getRow(currentRow);
                else
                    xssfRow = sheet.createRow(currentRow);
                Cell mhc = xssfRow.createCell(mhcRow);
                Cell cr = xssfRow.createCell(crRow);
                Cell time = xssfRow.createCell(timeRow);

                mhc.setCellValue(aux.getCostSolutionO1());
                cr.setCellValue(aux.getCostSolutionO2());
                time.setCellValue(elapsedTimeInSecond);
                currentRow++;
            }

            currentRow++;

            paretoOptimal.sortedArrayList.clear();
            results.sortedArrayList.clear();

            try {
                XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
                FileOutputStream outputStream = new FileOutputStream(filename);
                workbook.write(outputStream);
            } catch (Exception e) {
                System.out.println("Error with the file: " + e);
            }
        }
    }

    //LS Combination
    public ArrayList<SortedArrayList> lsCombination(double perC1, double perC2, double perC3, double perC4, int it, int dominatedLoop, int mono_objectiveLoop) throws IOException {
        ArrayList<SortedArrayList> bucket = new ArrayList<>();

        inputStream = new FileInputStream(filename);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

        XSSFSheet sheet = workbook.createSheet("LCT " + iterations[0] + " " + dominatedLoop + " " + mono_objectiveLoop);
        currentRow = 31;

        for (String instancesName : instancesNames) {
            Instance instanceMHC = new Instance("src\\main\\resources" + File.separator + instancesName + ".txt", 0);
            Instance instanceCR = new Instance("src\\main\\resources" + File.separator + instancesName + "_CR.txt", 0);
            System.out.println(instancesName);

            //Fix the number of rows and the number of facilities in each row
            Topology(instanceCR.numFacilities);

            XSSFRow xssfRow = sheet.createRow(currentRow);
            Cell instance = xssfRow.createCell(0);
            instance.setCellValue(instancesName);

            SortedArrayList finalPareto = new SortedArrayList();

            long startTime = System.nanoTime();
            for (int i = 0; i < it * perC1; i++) {
                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                SolutionPositionsCosts result = constructive.C1_GR(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                paretoOptimal.add(result);
            }

            for (int i = 0; i < it * perC2; i++) {
                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                SolutionPositionsCosts result = constructive.C2_RG(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                paretoOptimal.add(result);
            }

            for (int i = 0; i < it * perC3; i++) {
                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                SolutionPositionsCosts result = constructive.C3_GR(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                paretoOptimal.add(result);
            }

            for (int i = 0; i < it * perC4; i++) {
                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                SolutionPositionsCosts result = constructive.C4_RG(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                paretoOptimal.add(result);
            }

            boolean changes;

            for (int iterator = 0; iterator < ((dominatedLoop * instanceCR.numFacilities) / 100); iterator++) {
                changes = false;
                for (SolutionPositionsCosts temp : paretoOptimal.sortedArrayList) {
                    if (localSearch.dominatedHybridLocalSearchInterchange(temp, instanceMHC.weights, instanceCR.weights, results))
                        changes = true;
                }

                if (changes) {
                    paretoOptimal = common.CopySortedArrayListSolution(results);
                    results = common.CopySortedArrayListSolution(paretoOptimal);
                }
            }

            for (int iterator = 0; iterator < ((mono_objectiveLoop * instanceCR.numFacilities) / 100); iterator++) {
                ArrayList<SolutionPositionsCosts> newSolutions = new ArrayList<>();
                for (SolutionPositionsCosts temp : paretoOptimal.sortedArrayList) {
                    SolutionPositionsCosts newSolution = localSearch.hybridLocalSearchInterchange(temp, instanceMHC.weights, instanceCR.weights, "mhc");
                    newSolutions.add(newSolution);
                    newSolution = localSearch.hybridLocalSearchInterchange(temp, instanceMHC.weights, instanceCR.weights, "cr");
                    newSolutions.add(newSolution);
                }
                results = common.CopySortedArrayListSolution(paretoOptimal);

                for (SolutionPositionsCosts temp : newSolutions) {
                    paretoOptimal.add(temp);
                }
            }

            long endTime = System.nanoTime();
            long totalTime = endTime - startTime;
            double elapsedTimeInSecond = (double) totalTime / 1_000_000_000;

            for (SolutionPositionsCosts aux : paretoOptimal.sortedArrayList) {
                finalPareto.add(aux); //all de solutions from all the constructive
                if (sheet.getRow(currentRow) != null)
                    xssfRow = sheet.getRow(currentRow);
                else
                    xssfRow = sheet.createRow(currentRow);
                Cell mhc = xssfRow.createCell(mhcRow);
                Cell cr = xssfRow.createCell(crRow);
                Cell time = xssfRow.createCell(timeRow);

                mhc.setCellValue(aux.getCostSolutionO1());
                cr.setCellValue(aux.getCostSolutionO2());
                time.setCellValue(elapsedTimeInSecond);
                currentRow++;
            }

            currentRow++;

            //To save data in a file
            common.CreateFile("C:\\Users\\nicor\\OneDrive - Universidad Rey Juan Carlos\\PhD\\Repositorio\\MOMetricsMavenInicial\\test\\" + dominatedLoop + " " + mono_objectiveLoop + "\\" + instancesName + ".txt", paretoOptimal);
            bucket.add(common.CopySortedArrayListSolution(paretoOptimal));
            paretoOptimal.sortedArrayList.clear();
            results.sortedArrayList.clear();

            try {
                XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
                FileOutputStream outputStream = new FileOutputStream(filename);
                workbook.write(outputStream);
            } catch (Exception e) {
                System.out.println("Error with the file: " + e);
            }
        }
        return bucket;
    }

    //LS Combination
    public ArrayList<SortedArrayList> lsCombinationEfficient(double perC1, double perC2, double perC3, double perC4, int it, int dominatedLoop, int mono_objectiveLoop) throws IOException {
        ArrayList<SortedArrayList> bucket = new ArrayList<>();

        inputStream = new FileInputStream(filename);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

        XSSFSheet sheet = workbook.createSheet("LCE_previous " + iterations[0] + " " + dominatedLoop + " " + mono_objectiveLoop);
        currentRow = 31;

        for (String instancesName : instancesNames) {
            Instance instanceMHC = new Instance("src\\main\\resources" + File.separator + instancesName + ".txt", 0);
            Instance instanceCR = new Instance("src\\main\\resources" + File.separator + instancesName + "_CR.txt", 0);
            System.out.println(instancesName);

            //Fix the number of rows and the number of facilities in each row
            Topology(instanceCR.numFacilities);

            XSSFRow xssfRow = sheet.createRow(currentRow);
            Cell instance = xssfRow.createCell(0);
            instance.setCellValue(instancesName);

            SortedArrayList finalPareto = new SortedArrayList();

            long startTime = System.nanoTime();
            for (int i = 0; i < it * perC1; i++) {
                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                SolutionPositionsCosts result = constructive.C1_GR(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                paretoOptimal.add(result);
            }

            for (int i = 0; i < it * perC2; i++) {
                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                SolutionPositionsCosts result = constructive.C2_RG(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                paretoOptimal.add(result);
            }

            for (int i = 0; i < it * perC3; i++) {
                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                SolutionPositionsCosts result = constructive.C3_GR(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                paretoOptimal.add(result);
            }

            for (int i = 0; i < it * perC4; i++) {
                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                SolutionPositionsCosts result = constructive.C4_RG(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                paretoOptimal.add(result);
            }

            boolean changes;

            for (int iterator = 0; iterator < ((dominatedLoop * instanceCR.numFacilities) / 100); iterator++) {
                changes = false;
                for (SolutionPositionsCosts temp : paretoOptimal.sortedArrayList) {
                    if (localSearch.dominatedHybridLocalSearchInterchangeEfficient(temp, instanceMHC.weights, instanceCR.weights, results))
                        changes = true;
                }

                if (changes) {
                    paretoOptimal = common.CopySortedArrayListSolution(results);
                    results = common.CopySortedArrayListSolution(paretoOptimal);
                }
            }

            for (int iterator = 0; iterator < ((mono_objectiveLoop * instanceCR.numFacilities) / 100); iterator++) {
                ArrayList<SolutionPositionsCosts> newSolutions = new ArrayList<>();
                for (SolutionPositionsCosts temp : paretoOptimal.sortedArrayList) {
                    SolutionPositionsCosts newSolution = localSearch.hybridLocalSearchInterchangeEfficient(temp, instanceMHC.weights, instanceCR.weights, "mhc");
                    newSolutions.add(newSolution);
                    newSolution = localSearch.hybridLocalSearchInterchangeEfficient(temp, instanceMHC.weights, instanceCR.weights, "cr");
                    newSolutions.add(newSolution);
                }
                results = common.CopySortedArrayListSolution(paretoOptimal);

                for (SolutionPositionsCosts temp : newSolutions) {
                    paretoOptimal.add(temp);
                }
            }

            long endTime = System.nanoTime();
            long totalTime = endTime - startTime;
            double elapsedTimeInSecond = (double) totalTime / 1_000_000_000;

            for (SolutionPositionsCosts aux : paretoOptimal.sortedArrayList) {
                finalPareto.add(aux); //all de solutions from all the constructive
                if (sheet.getRow(currentRow) != null)
                    xssfRow = sheet.getRow(currentRow);
                else
                    xssfRow = sheet.createRow(currentRow);
                Cell mhc = xssfRow.createCell(mhcRow);
                Cell cr = xssfRow.createCell(crRow);
                Cell time = xssfRow.createCell(timeRow);

                mhc.setCellValue(aux.getCostSolutionO1());
                cr.setCellValue(aux.getCostSolutionO2());
                time.setCellValue(elapsedTimeInSecond);
                currentRow++;
            }

            currentRow++;

            //To save data in a file
            common.CreateFile("src\\main\\data_metrics\\" + dominatedLoop + "_" + mono_objectiveLoop + File.separator + instancesName + ".txt", paretoOptimal);
            bucket.add(common.CopySortedArrayListSolution(paretoOptimal));
            paretoOptimal.sortedArrayList.clear();
            results.sortedArrayList.clear();

            try {
                XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
                FileOutputStream outputStream = new FileOutputStream(filename);
                workbook.write(outputStream);
            } catch (Exception e) {
                System.out.println("Error with the file: " + e);
            }
        }
        return bucket;
    }

    public void bestParetoOptimal(ArrayList<ArrayList<SortedArrayList>> bucket) throws IOException {
        inputStream = new FileInputStream(filename);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

        XSSFSheet sheet = workbook.createSheet("Best");
        currentRow = 31;

        for (String instancesName : instancesNames) {
            XSSFRow xssfRow = sheet.createRow(currentRow);
            Cell instance = xssfRow.createCell(0);
            instance.setCellValue(instancesName);

            SortedArrayList finalPareto = new SortedArrayList();

            for (ArrayList<SortedArrayList> sortedArrayLists : bucket) {
                SortedArrayList aux = common.CopySortedArrayListSolution(sortedArrayLists.get(0));
                for (int j = 0; j < aux.sortedArrayList.size(); j++) {
                    paretoOptimal.add(aux.sortedArrayList.get(j));
                }
                sortedArrayLists.remove(0);
            }

            for (SolutionPositionsCosts aux : paretoOptimal.sortedArrayList) {
                finalPareto.add(aux); //all de solutions from all the constructive
                if (sheet.getRow(currentRow) != null)
                    xssfRow = sheet.getRow(currentRow);
                else
                    xssfRow = sheet.createRow(currentRow);
                Cell mhc = xssfRow.createCell(mhcRow);
                Cell cr = xssfRow.createCell(crRow);

                mhc.setCellValue(aux.getCostSolutionO1());
                cr.setCellValue(aux.getCostSolutionO2());
                currentRow++;
            }

            currentRow++;
            common.CreateFile("C:\\Users\\nicor\\OneDrive - Universidad Rey Juan Carlos\\PhD\\Repositorio\\MOMetricsMavenInicial\\test\\best_previo\\" + instancesName + ".txt", paretoOptimal);

            paretoOptimal.sortedArrayList.clear();
            results.sortedArrayList.clear();

            try {
                XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
                FileOutputStream outputStream = new FileOutputStream(filename);
                workbook.write(outputStream);
            } catch (Exception e) {
                System.out.println("Error with the file: " + e);
            }
        }
    }

    public void lsCompleteCombination(double perC1, double perC2, double perC3, double perC4, int it) throws IOException {
        inputStream = new FileInputStream(filename);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

        XSSFSheet sheet = workbook.createSheet("LC" + iterations[0] + " ");
        currentRow = 31;

        for (String instancesName : instancesNames) {
            Instance instanceMHC = new Instance("src\\main\\resources" + File.separator + instancesName + ".txt", 0);
            Instance instanceCR = new Instance("src\\main\\resources" + File.separator + instancesName + "_CR.txt", 0);
            System.out.println(instancesName);

            //Fix the number of rows and the number of facilities in each row
            Topology(instanceCR.numFacilities);

            XSSFRow xssfRow = sheet.createRow(currentRow);
            Cell instance = xssfRow.createCell(0);
            instance.setCellValue(instancesName);

            SortedArrayList finalPareto = new SortedArrayList();

            long startTime = System.nanoTime();
            for (int i = 0; i < it * perC1; i++) {
                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                SolutionPositionsCosts result = constructive.C1_GR(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                paretoOptimal.add(result);
            }

            for (int i = 0; i < it * perC2; i++) {
                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                SolutionPositionsCosts result = constructive.C2_RG(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                paretoOptimal.add(result);
            }

            for (int i = 0; i < it * perC3; i++) {
                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                SolutionPositionsCosts result = constructive.C3_GR(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                paretoOptimal.add(result);
            }

            for (int i = 0; i < it * perC4; i++) {
                double alpha = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
                SolutionPositionsCosts result = constructive.C4_RG(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                paretoOptimal.add(result);
            }

            //LS phase. Dominated LS
            boolean changesD = true;
            boolean changesM = true;
            boolean changes = true;

            while (changes) {
                changes = false;

                while (changesD) {
                    changesD = false;
                    for (SolutionPositionsCosts temp : paretoOptimal.sortedArrayList) {
                        if (localSearch.dominatedHybridLocalSearchInterchange(temp, instanceMHC.weights, instanceCR.weights, results)) {
                            changesD = true;
                            changes = true;
                        }
                    }

                    if (changesD) {
                        paretoOptimal = common.CopySortedArrayListSolution(results);
                        results = common.CopySortedArrayListSolution(paretoOptimal);
                    }
                }

                while (changesM) {
                    changesM = false;
                    ArrayList<SolutionPositionsCosts> newSolutions = new ArrayList<>();
                    for (SolutionPositionsCosts temp : paretoOptimal.sortedArrayList) {
                        SolutionPositionsCosts newSolution = localSearch.hybridLocalSearchInterchange(temp, instanceMHC.weights, instanceCR.weights, "mhc");
                        newSolutions.add(newSolution);
                        newSolution = localSearch.hybridLocalSearchInterchange(temp, instanceMHC.weights, instanceCR.weights, "cr");
                        newSolutions.add(newSolution);
                    }
                    results = common.CopySortedArrayListSolution(paretoOptimal);

                    for (SolutionPositionsCosts temp : newSolutions) {
                        paretoOptimal.add(temp);
                    }

                    if (paretoOptimal.sortedArrayList.size() != results.sortedArrayList.size()) {
                        changesM = true;
                        changes = true;
                    } else {
                        for (int i = 0; i < paretoOptimal.sortedArrayList.size(); i++) {
                            if (!paretoOptimal.sortedArrayList.get(i).getSolution().equals(results.sortedArrayList.get(i).getSolution())) {
                                changesM = true;
                                changes = true;
                                break;
                            }
                        }
                    }
                }
            }

            long endTime = System.nanoTime();
            long totalTime = endTime - startTime;
            double elapsedTimeInSecond = (double) totalTime / 1_000_000_000;

            for (SolutionPositionsCosts aux : paretoOptimal.sortedArrayList) {
                finalPareto.add(aux); //all de solutions from all the constructive
                if (sheet.getRow(currentRow) != null)
                    xssfRow = sheet.getRow(currentRow);
                else
                    xssfRow = sheet.createRow(currentRow);
                Cell mhc = xssfRow.createCell(mhcRow);
                Cell cr = xssfRow.createCell(crRow);
                Cell time = xssfRow.createCell(timeRow);

                mhc.setCellValue(aux.getCostSolutionO1());
                cr.setCellValue(aux.getCostSolutionO2());
                time.setCellValue(elapsedTimeInSecond);
                currentRow++;
            }

            currentRow++;

            paretoOptimal.sortedArrayList.clear();
            results.sortedArrayList.clear();

            try {
                XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
                FileOutputStream outputStream = new FileOutputStream(filename);
                workbook.write(outputStream);
            } catch (Exception e) {
                System.out.println("Error with the file: " + e);
            }
        }
    }

    public void lsR(String[] args) throws IOException {
        inputStream = new FileInputStream(filename);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

        XSSFSheet sheet = workbook.createSheet("LS " + iterations[0]);
        currentRow = 31;

        //Random alpha for the constructive
        double alphaAux = (RandomSingleton.getInstance().nextInt(9) + 1) * 0.1;
        alphas = new double[]{alphaAux};

        for (String instancesName : instancesNames) {

            Instance instanceMHC = new Instance("src\\main\\resources" + File.separator + instancesName + ".txt", 0);
            Instance instanceCR = new Instance("src\\main\\resources" + File.separator + instancesName + "_CR.txt", 0);
            System.out.println(instancesName);

            //Fix the number of rows and the number of facilities in each row
            Topology(instanceCR.numFacilities);
            for (int it : iterations) {
                for (double alpha : alphas) {
                    XSSFRow xssfRow = sheet.createRow(currentRow);
                    Cell instance = xssfRow.createCell(0);
                    instance.setCellValue(instancesName);
                    //boolean first = true;
                    int max = 0;
                    for (String heuristic : heuristics) {
                        long startTime = System.nanoTime();
                        switch (heuristic) {
                            case "C0":
                                for (int i = 0; i < it; i++) {
                                    SolutionPositionsCosts result = constructive.C0_Random(instanceMHC.weights, instanceCR.weights, row, facilitiesRow);
                                    paretoOptimal.add(result);
                                    //System.out.println("MHC: "+result.getCostSolutionO1()+"  CR: "+result.getCostSolutionO2());
                                }
                                break;

                            case "C1":
                                for (int i = 0; i < it; i++) {
                                    SolutionPositionsCosts result = constructive.C1_GR(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                                    paretoOptimal.add(result);
                                }
                                //System.out.println(paretoOptimal.sortedArrayList.size());
                                break;

                            case "C2":
                                for (int i = 0; i < it; i++) {
                                    SolutionPositionsCosts result = constructive.C2_RG(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                                    paretoOptimal.add(result);
                                }
                                break;

                            case "C3":
                                for (int i = 0; i < it; i++) {
                                    SolutionPositionsCosts result = constructive.C3_GR(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                                    paretoOptimal.add(result);
                                }
                                break;

                            case "C4":
                                for (int i = 0; i < it; i++) {
                                    SolutionPositionsCosts result = constructive.C4_RG(instanceMHC.weights, instanceCR.weights, row, facilitiesRow, alpha);
                                    paretoOptimal.add(result);
                                }
                                break;
                        }
                        boolean changes = true;
                        ArrayList<SortedArrayList> check = new ArrayList<>();
                        while (changes) {
                            changes = false;
                            for (SolutionPositionsCosts temp : paretoOptimal.sortedArrayList) {
                                if (localSearch.dominatedHybridLocalSearchInterchange(temp, instanceMHC.weights, instanceCR.weights, results))
                                    //if(localSearch.hybridLocalSearchInterchangeEfficient(temp, instanceMHC.weights, instanceCR.weights, results))
                                    changes = true;
                            }

                            if (changes) {
                                paretoOptimal = common.CopySortedArrayListSolution(results);
                                if (paretoOptimal.sortedArrayList.size() > 10) {
                                    paretoOptimal.reducedParetoFrontier(paretoOptimal, 10);
                                    if (!check.isEmpty() && check.contains(paretoOptimal))
                                        changes = false;

                                    results = common.CopySortedArrayListSolution(paretoOptimal);
                                    check.add(common.CopySortedArrayListSolution(paretoOptimal));
                                }
                            }
                        }

                        //System.out.println("Tamaño: "+results.sortedArrayList.size());
//                        if(results.sortedArrayList.size() > 10)
//                            results.reducedParetoFrontier(results, 10);

                        long endTime = System.nanoTime();
                        long totalTime = endTime - startTime;
                        double elapsedTimeInSecond = (double) totalTime / 1_000_000_000;
                        int auxCurrentRow = currentRow;
                        for (SolutionPositionsCosts aux : results.sortedArrayList) {
                            if (sheet.getRow(currentRow) != null)
                                xssfRow = sheet.getRow(currentRow);
                            else
                                xssfRow = sheet.createRow(currentRow);
                            Cell mhc = xssfRow.createCell(mhcRow);
                            Cell cr = xssfRow.createCell(crRow);
                            Cell time = xssfRow.createCell(timeRow);
//                            if(heuristic.equals("C4") && instancesName.equals("Y-50_t"))
//                                System.out.println(aux.getSolution());
                            mhc.setCellValue(aux.getCostSolutionO1());
                            cr.setCellValue(aux.getCostSolutionO2());
                            time.setCellValue(elapsedTimeInSecond);
                            currentRow++;
                        }
                        //We change the constructive
                        mhcRow += 3;
                        crRow += 3;
                        timeRow += 3;
                        currentRow = auxCurrentRow;

                        if (results.sortedArrayList.size() > max)
                            max = results.sortedArrayList.size();
                        paretoOptimal.sortedArrayList.clear();
                        results.sortedArrayList.clear();
                    }
                    currentRow += max;
                    mhcRow = 1;
                    crRow = 2;
                    timeRow = 3;
                }
                currentRow++;
            }

            try {
                FileOutputStream outputStream = new FileOutputStream(filename);
                workbook.write(outputStream);
            } catch (Exception e) {
                System.out.println("Error with the file: " + e);
            }
        }
    }

    public void Topology(int size) {
        switch (size) {
            case 6:
                row = 2;
                facilitiesRow = 3;
                c = 2;
                break;

            case 8:
                row = 2;
                facilitiesRow = 4;
                break;

            case 10:
                row = 2;
                facilitiesRow = 5;
                break;

            case 12:
                row = 3;
                facilitiesRow = 4;
                break;

            case 15:
                row = 3;
                facilitiesRow = 5;
                break;

            case 20://20 = 4 x 5, chen
                row = 4;
                facilitiesRow = 5;
                break;

            case 25:
                row = 5;
                facilitiesRow = 5;
                break;

            case 30:
                row = 5;
                facilitiesRow = 6;
                break;

            case 35:
                row = 5;
                facilitiesRow = 7;
                break;

            case 40:
                row = 5;
                facilitiesRow = 8;
                break;

            case 45:
                row = 5;
                facilitiesRow = 9;
                break;

            case 50:
                row = 5;
                facilitiesRow = 10;
                break;

            case 60:
                row = 6;
                facilitiesRow = 10;
                break;

        }
    }
}
