import Structures.Pareto_aux;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.uma.jmetal.qualityindicator.impl.*;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.front.util.FrontNormalizer;
import org.uma.jmetal.util.front.util.FrontUtils;
import org.uma.jmetal.util.point.util.PointSolution;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Metrics {
    String pathGlobal = "src\\main\\data_metrics";
    String[] paths = new String[]{
            "dominating",
            "15_30",
            "30_15"
    };

    boolean[] minimizing = new boolean[]{true, false};

    public static void prepareFronts(boolean[] minimizing, String pathGlobal, String[] paths, String pathJmetal, String[] pathsOut, String instanceName, String pathOutRef) {
        Pareto_aux pareto = new Pareto_aux(minimizing);
        for (int i = 0; i < paths.length; i++) {
            Pareto_aux p = new Pareto_aux(minimizing);
            p.loadFromFile(pathGlobal + "/" + paths[i] + "/" + instanceName);
            p.saveToFile(pathJmetal + "/" + pathsOut[i] + "/" + instanceName);
            pareto.loadFromFile(pathGlobal + "/" + paths[i] + "/" + instanceName);
        }
        pareto.saveToFile(pathOutRef);
    }

    public static void evaluateDir(ArrayList<Long> times) throws IOException {
//        String fileName = "src//main//resources//0Metrics.xlsx";
        String fileName = "src\\main\\resources\\0Metrics.xlsx";
        FileInputStream inputStream = new FileInputStream(fileName);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheet("RawData");
        int currentRow = 1;

//        String pathGlobal = "src//main//resources//data_metrics";
        String pathGlobal = "src\\main\\resources\\data_metrics";
        String[] paths = new String[]{
                "0",
                "1",
                "2",
                "3",
                "NSGAII",
                "SPEA2"
        };

        boolean[] minimizing = new boolean[]{true, true};
        String pathJMetal = "./test/jmetal";
        new File(pathJMetal).mkdirs();

        try (PrintWriter pw = new PrintWriter("final_report_analysis.csv")) {
            String matching = "";
            String headers = "INSTANCE";
            for (int i = 0; i < paths.length; i++) {
                matching += paths[i] + ";" + (i + 1) + "\n";
                headers += ";CV REF-" + (i + 1) + ";HV " + (i + 1) + ";EPS " + (i + 1) + ";GD " + (i + 1) + ";SIZE " + (i + 1) + ";SPREAD " + (i + 1);
            }
            pw.println(matching);
            pw.println(headers);
            String[] files = new File(pathGlobal + "/" + paths[0]).list((dir, name) -> name.endsWith(".txt"));
            for (String instanceName : files) {
                try {
                    System.out.println(instanceName);
                    pw.print(instanceName + ";");
                    String pathOutRefSet = pathJMetal + "/" + instanceName.replace(".txt", "") + "_reference.txt";
                    /*
                    Creates the reference set and multiplies by -1 those objectives that are minimizing
                     */
                    prepareFronts(minimizing, pathGlobal, paths, pathJMetal, paths, instanceName, pathOutRefSet);
                    Front frontRef = new ArrayFront(pathOutRefSet);
                    Front[] fronts = new Front[paths.length];
                    for (int i = 0; i < paths.length; i++) {
                        fronts[i] = new ArrayFront(pathJMetal + "/" + paths[i] + "/" + instanceName);
                    }

                    /*
                    NORMALIZATION
                     */
                    FrontNormalizer frontNormalizer = new FrontNormalizer(frontRef);
                    Front normRef = frontNormalizer.normalize(frontRef);
                    List<PointSolution> solsRef = FrontUtils.convertFrontToSolutionList(normRef);
                    Front[] normFronts = new Front[fronts.length];
                    List<PointSolution>[] sols = new List[fronts.length];
                    for (int i = 0; i < normFronts.length; i++) {
                        normFronts[i] = frontNormalizer.normalize(fronts[i]);
                        sols[i] = FrontUtils.convertFrontToSolutionList(normFronts[i]);
                    }

                    SetCoverage coverage = new SetCoverage();
                    Hypervolume<PointSolution> hypervolume = new PISAHypervolume<>(normRef);
                    Epsilon<PointSolution> epsilon = new Epsilon<>(normRef);
                    GenerationalDistance<PointSolution> gd = new GenerationalDistance<>();
                    InvertedGenerationalDistance<PointSolution> igd = new InvertedGenerationalDistance<>();
                    InvertedGenerationalDistancePlus<PointSolution> igdp = new InvertedGenerationalDistancePlus<>();
                    GeneralizedSpread<PointSolution> sp = new GeneralizedSpread<>();

                    XSSFRow xssfRow;
                    if (sheet.getRow(currentRow) != null)
                        xssfRow = sheet.getRow(currentRow);
                    else
                        xssfRow = sheet.createRow(currentRow);

                    int cell = 1;
                    Cell instance = xssfRow.createCell(cell);
                    instance.setCellValue(instanceName);
                    cell++;
                    for (int i = 0; i < sols.length; i++) {
                        Cell covRow = xssfRow.createCell(cell);
                        cell++;
                        Cell hvRow = xssfRow.createCell(cell);
                        cell++;
                        Cell epsRow = xssfRow.createCell(cell);
                        cell++;
                        Cell gdValRow = xssfRow.createCell(cell);
                        cell++;
                        Cell igdValRow = xssfRow.createCell(cell);
                        cell++;
                        Cell igdpValRow = xssfRow.createCell(cell);
                        cell++;
                        Cell sizeRow = xssfRow.createCell(cell);
                        cell++;
                        Cell spValRow = xssfRow.createCell(cell);
                        cell++;
                        Cell timeRow = xssfRow.createCell(cell);
                        cell++;

                        double cov = coverage.evaluate(solsRef, sols[i]);
                        double hv = hypervolume.evaluate(sols[i]);
                        double eps = epsilon.evaluate(sols[i]);
                        double gdVal = gd.generationalDistance(fronts[i], normRef);
                        double igdVal = igd.invertedGenerationalDistance(fronts[i], normRef);
                        double igdpVal = igdp.invertedGenerationalDistancePlus(fronts[i], normRef);
                        double spVal = sp.generalizedSpread(fronts[i], normRef);
                        pw.print(cov + ";" + hv + ";" + eps + ";" + gdVal + ";" + sols[i].size() + ";" + spVal + ";");

                        covRow.setCellValue(cov);
                        hvRow.setCellValue(hv);
                        epsRow.setCellValue(eps);
                        gdValRow.setCellValue(gdVal);
                        igdValRow.setCellValue(igdVal);
                        igdpValRow.setCellValue(igdpVal);
                        sizeRow.setCellValue(sols[i].size());
                        spValRow.setCellValue(spVal);
                        //timeRow.setCellValue(times.remove(0));
                        //timeRow.setCellValue(0);
                    }
                    currentRow++;

                    XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
                    FileOutputStream outputStream = new FileOutputStream(fileName);
                    workbook.write(outputStream);

                } catch (JMetalException e) {
                    System.err.println("ERROR EN INSTANCIA: " + instanceName);
                }
                pw.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void evaluateComparison_state() throws IOException {
        String fileName = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "0State.xlsx";
        FileInputStream inputStream = new FileInputStream(fileName);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheet("RawData");
        int currentRow = 1;

        String pathGlobal = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "state_of_the_art";
        String[] paths = new String[]{
                "0",
                "1",
                "2",
                "3",
                "NSBBO",
                "NSGA-II"
        };

        boolean[] minimizing = new boolean[]{true, true};
        String pathJMetal = "./test/jmetal";
        new File(pathJMetal).mkdirs();

        try (PrintWriter pw = new PrintWriter("final_report_analysis.csv")) {
            String matching = "";
            String headers = "INSTANCE";
            for (int i = 0; i < paths.length; i++) {
                matching += paths[i] + ";" + (i + 1) + "\n";
                headers += ";CV REF-" + (i + 1) + ";HV " + (i + 1) + ";EPS " + (i + 1) + ";GD " + (i + 1) + ";SIZE " + (i + 1) + ";SPREAD " + (i + 1);
            }
            pw.println(matching);
            pw.println(headers);
            String[] files = new File(pathGlobal + "/" + paths[0]).list((dir, name) -> name.endsWith(".txt"));
            for (String instanceName : files) {
                try {
                    System.out.println(instanceName);
                    pw.print(instanceName + ";");
                    String pathOutRefSet = pathJMetal + "/" + instanceName.replace(".txt", "") + "_reference.txt";
                    /*
                    Creates the reference set and multiplies by -1 those objectives that are minimizing
                     */
                    prepareFronts(minimizing, pathGlobal, paths, pathJMetal, paths, instanceName, pathOutRefSet);
                    Front frontRef = new ArrayFront(pathOutRefSet);
                    Front[] fronts = new Front[paths.length];
                    for (int i = 0; i < paths.length; i++) {
                        fronts[i] = new ArrayFront(pathJMetal + "/" + paths[i] + "/" + instanceName);
                    }

                    /*
                    NORMALIZATION
                     */
                    FrontNormalizer frontNormalizer = new FrontNormalizer(frontRef);
                    Front normRef = frontNormalizer.normalize(frontRef);
                    List<PointSolution> solsRef = FrontUtils.convertFrontToSolutionList(normRef);
                    Front[] normFronts = new Front[fronts.length];
                    List<PointSolution>[] sols = new List[fronts.length];
                    for (int i = 0; i < normFronts.length; i++) {
                        normFronts[i] = frontNormalizer.normalize(fronts[i]);
                        sols[i] = FrontUtils.convertFrontToSolutionList(normFronts[i]);
                    }

                    SetCoverage coverage = new SetCoverage();
                    Hypervolume<PointSolution> hypervolume = new PISAHypervolume<>(normRef);
                    Epsilon<PointSolution> epsilon = new Epsilon<>(normRef);
                    GenerationalDistance<PointSolution> gd = new GenerationalDistance<>();
                    InvertedGenerationalDistance<PointSolution> igd = new InvertedGenerationalDistance<>();
                    InvertedGenerationalDistancePlus<PointSolution> igdp = new InvertedGenerationalDistancePlus<>();
                    GeneralizedSpread<PointSolution> sp = new GeneralizedSpread<>();

                    XSSFRow xssfRow;
                    if (sheet.getRow(currentRow) != null)
                        xssfRow = sheet.getRow(currentRow);
                    else
                        xssfRow = sheet.createRow(currentRow);

                    int cell = 1;
                    Cell instance = xssfRow.createCell(cell);
                    instance.setCellValue(instanceName);
                    cell++;
                    for (int i = 0; i < sols.length; i++) {
                        Cell covRow = xssfRow.createCell(cell);
                        cell++;
                        Cell hvRow = xssfRow.createCell(cell);
                        cell++;
                        Cell epsRow = xssfRow.createCell(cell);
                        cell++;
                        Cell gdValRow = xssfRow.createCell(cell);
                        cell++;
                        Cell igdValRow = xssfRow.createCell(cell);
                        cell++;
                        Cell igdpValRow = xssfRow.createCell(cell);
                        cell++;
                        Cell sizeRow = xssfRow.createCell(cell);
                        cell++;
                        Cell spValRow = xssfRow.createCell(cell);
                        cell++;
                        Cell timeRow = xssfRow.createCell(cell);
                        cell++;

                        double cov = coverage.evaluate(solsRef, sols[i]);
                        double hv = hypervolume.evaluate(sols[i]);
                        double eps = epsilon.evaluate(sols[i]);
                        double gdVal = gd.generationalDistance(fronts[i], normRef);
                        double igdVal = igd.invertedGenerationalDistance(fronts[i], normRef);
                        double igdpVal = igdp.invertedGenerationalDistancePlus(fronts[i], normRef);
                        double spVal = sp.generalizedSpread(fronts[i], normRef);
                        pw.print(cov + ";" + hv + ";" + eps + ";" + gdVal + ";" + sols[i].size() + ";" + spVal + ";");

                        covRow.setCellValue(cov);
                        hvRow.setCellValue(hv);
                        epsRow.setCellValue(eps);
                        gdValRow.setCellValue(gdVal);
                        igdValRow.setCellValue(igdVal);
                        igdpValRow.setCellValue(igdpVal);
                        sizeRow.setCellValue(sols[i].size());
                        spValRow.setCellValue(spVal);
//                        timeRow.setCellValue(times.remove(0));
                        timeRow.setCellValue(0);
                    }
                    currentRow++;

                    XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
                    FileOutputStream outputStream = new FileOutputStream(fileName);
                    workbook.write(outputStream);

                } catch (JMetalException e) {
                    System.err.println("ERROR EN INSTANCIA: " + instanceName);
                }
                pw.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void evaluateDir_original() throws FileNotFoundException {
        String pathGlobal = "src\\main\\resources\\data_metrics";
        String[] paths = new String[]{
                "0",
                "1",
                "2",
                "3",
        };

        boolean[] minimizing = new boolean[]{true, true};
        String pathJMetal = "references";
        new File(pathJMetal).mkdirs();

        try (PrintWriter pw = new PrintWriter("src\\main\\resources\\data_metrics\\final_report_analysis.csv")) {
            String matching = "";
            String headers = "INSTANCE";
            for (int i = 0; i < paths.length; i++) {
                matching += paths[i] + ";" + (i + 1) + "\n";
                headers += ";CV REF-" + (i + 1) + ";HV " + (i + 1) + ";EPS " + (i + 1) + ";GD " + (i + 1) + ";IGD " + (i + 1) + ";IGD+ " + (i + 1) + ";SIZE " + (i + 1) + ";SPREAD " + (i + 1);
            }
            pw.println(matching);
            pw.println(headers);
            String[] files = new File(pathGlobal + "/" + paths[0]).list((dir, name) -> name.endsWith(".txt"));
            for (String instanceName : files) {
                try {
                    System.out.println(instanceName);
                    pw.print(instanceName + ";");
                    String pathOutRefSet = pathJMetal + "/" + instanceName.replace(".txt", "") + "_reference.txt";
                    /*
                    Creates the reference set and multiplies by -1 those objectives that are minimizing
                     */
                    prepareFronts(minimizing, pathGlobal, paths, pathJMetal, paths, instanceName, pathOutRefSet);
                    Front frontRef = new ArrayFront(pathOutRefSet);
                    Front[] fronts = new Front[paths.length];
                    for (int i = 0; i < paths.length; i++) {
                        fronts[i] = new ArrayFront(pathJMetal + "/" + paths[i] + "/" + instanceName);
                    }

                    /*
                    NORMALIZATION
                     */
                    FrontNormalizer frontNormalizer = new FrontNormalizer(frontRef);
                    Front normRef = frontNormalizer.normalize(frontRef);
                    List<PointSolution> solsRef = FrontUtils.convertFrontToSolutionList(normRef);
                    Front[] normFronts = new Front[fronts.length];
                    List<PointSolution>[] sols = new List[fronts.length];
                    for (int i = 0; i < normFronts.length; i++) {
                        normFronts[i] = frontNormalizer.normalize(fronts[i]);
                        sols[i] = FrontUtils.convertFrontToSolutionList(normFronts[i]);
                    }

                    SetCoverage coverage = new SetCoverage();
                    Hypervolume<PointSolution> hypervolume = new PISAHypervolume<>(normRef);
                    Epsilon<PointSolution> epsilon = new Epsilon<>(normRef);
                    GenerationalDistance<PointSolution> gd = new GenerationalDistance<>();
                    InvertedGenerationalDistance<PointSolution> igd = new InvertedGenerationalDistance<>();
                    InvertedGenerationalDistancePlus<PointSolution> igdp = new InvertedGenerationalDistancePlus<>();
                    GeneralizedSpread<PointSolution> sp = new GeneralizedSpread<>();

                    for (int i = 0; i < sols.length; i++) {
                        double cov = coverage.evaluate(solsRef, sols[i]);
                        double hv = hypervolume.evaluate(sols[i]);
                        double eps = epsilon.evaluate(sols[i]);
                        double gdVal = gd.generationalDistance(fronts[i], normRef);
                        double igdVal = igd.invertedGenerationalDistance(fronts[i], normRef);
                        double igdpVal = igdp.invertedGenerationalDistancePlus(fronts[i], normRef);
                        double spVal = sp.generalizedSpread(fronts[i], normRef);
                        pw.print(cov + ";" + hv + ";" + eps + ";" + gdVal + ";" + igdVal + ";" + igdpVal + ";" + sols[i].size() + ";" + spVal + ";");
                    }
                } catch (JMetalException e) {
                    System.err.println("ERROR EN INSTANCIA: " + instanceName);
                }
                pw.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double getMetric() throws FileNotFoundException {
        double metric_value = 0.0;
        String pathGlobal = "src\\main\\data_metrics\\";
        String[] paths = new String[]{"dominating", "15_30", "30_15",};

        boolean[] minimizing = new boolean[]{true, true}; //Revisar
        String pathJMetal = "src\\main\\data_metrics\\jmetal";
        new File(pathJMetal).mkdirs();

        String[] files = new File(pathGlobal + "/" + paths[0]).list((dir, name) -> name.endsWith(".txt"));
        for (String instanceName : files) {
            try {
                System.out.println(instanceName);
                String pathOutRefSet = pathJMetal + "/" + instanceName.replace(".txt", "") + "_reference.txt";

                prepareFronts(minimizing, pathGlobal, paths, pathJMetal, paths, instanceName, pathOutRefSet);
                Front frontRef = new ArrayFront(pathOutRefSet);
                Front[] fronts = new Front[paths.length];
                for (int i = 0; i < paths.length; i++) {
                    fronts[i] = new ArrayFront(pathJMetal + "/" + paths[i] + "/" + instanceName);
                }

                FrontNormalizer frontNormalizer = new FrontNormalizer(frontRef);
                Front normRef = frontNormalizer.normalize(frontRef);
                List<PointSolution> solsRef = FrontUtils.convertFrontToSolutionList(normRef);
                Front[] normFronts = new Front[fronts.length];
                List<PointSolution>[] sols = new List[fronts.length];
                for (int i = 0; i < normFronts.length; i++) {
                    normFronts[i] = frontNormalizer.normalize(fronts[i]);
                    sols[i] = FrontUtils.convertFrontToSolutionList(normFronts[i]);
                }

                SetCoverage coverage = new SetCoverage();
                Hypervolume<PointSolution> hypervolume = new PISAHypervolume<>(normRef);
                Epsilon<PointSolution> epsilon = new Epsilon<>(normRef);
                GenerationalDistance<PointSolution> gd = new GenerationalDistance<>();
                InvertedGenerationalDistance igd = new InvertedGenerationalDistance<>();
                InvertedGenerationalDistancePlus igdp = new InvertedGenerationalDistancePlus<>();
                GeneralizedSpread<PointSolution> sp = new GeneralizedSpread<>();
                for (int i = 0; i < sols.length; i++) {
                    double cov = coverage.evaluate(solsRef, sols[i]);
                    double hv = hypervolume.evaluate(sols[i]);
                    double eps = epsilon.evaluate(sols[i]);
                    double gdVal = gd.generationalDistance(fronts[i], normRef);
                    double spVal = sp.generalizedSpread(fronts[i], normRef);
                }

            } catch (JMetalException e) {
                System.err.println("ERROR EN INSTANCIA: " + instanceName);
            }
        }
        return metric_value;
    }

    public double getMetric_iRace(int ls) throws FileNotFoundException {
        double metric_value = 0.0;
        String pathGlobal = "./";
        String[] paths = new String[]{"dominating", "crowdMono", "crowdMixed",};

        boolean[] minimizing = new boolean[]{true, true}; //Revisar
        String pathJMetal = "jmetal";
        new File(pathJMetal).mkdirs();

        String[] files = new File(pathGlobal + "/" + paths[0]).list((dir, name) -> name.endsWith(".txt"));
        for (String instanceName : files) {
            try {
                String pathOutRefSet = pathJMetal + "/" + instanceName.replace(".txt", "") + "_reference.txt";

                prepareFronts(minimizing, pathGlobal, paths, pathJMetal, paths, instanceName, pathOutRefSet);
                Front frontRef = new ArrayFront(pathOutRefSet);
                Front[] fronts = new Front[paths.length];
                for (int i = 0; i < paths.length; i++) {
                    fronts[i] = new ArrayFront(pathJMetal + "/" + paths[i] + "/" + instanceName);
                }

                FrontNormalizer frontNormalizer = new FrontNormalizer(frontRef);
                Front normRef = frontNormalizer.normalize(frontRef);
                List<PointSolution> solsRef = FrontUtils.convertFrontToSolutionList(normRef);
                Front[] normFronts = new Front[fronts.length];
                List<PointSolution>[] sols = new List[fronts.length];
                for (int i = 0; i < normFronts.length; i++) {
                    normFronts[i] = frontNormalizer.normalize(fronts[i]);
                    sols[i] = FrontUtils.convertFrontToSolutionList(normFronts[i]);
                }

                SetCoverage coverage = new SetCoverage();
                Hypervolume<PointSolution> hypervolume = new PISAHypervolume<>(normRef);
                Epsilon<PointSolution> epsilon = new Epsilon<>(normRef);
                GenerationalDistance<PointSolution> gd = new GenerationalDistance<>();
                GeneralizedSpread<PointSolution> sp = new GeneralizedSpread<>();
                for (int i = 0; i < sols.length; i++) {
                    double cov = coverage.evaluate(solsRef, sols[i]);
                    //System.out.println(cov);
                    double hv = hypervolume.evaluate(sols[i]);
                    double eps = epsilon.evaluate(sols[i]);
                    double gdVal = gd.generationalDistance(fronts[i], normRef);
                    double spVal = sp.generalizedSpread(fronts[i], normRef);
                }
                metric_value = coverage.evaluate(solsRef, sols[ls]); //Revisar
            } catch (JMetalException e) {
                System.err.println("ERROR EN INSTANCIA: " + instanceName);
            }
        }
        return metric_value;
    }

    public double getHyperVolume(String instanceNameDir) throws FileNotFoundException {
        Front frontRef = new ArrayFront(instanceNameDir);
        FrontNormalizer frontNormalizer = new FrontNormalizer(frontRef);
        Front normRef = frontNormalizer.normalize(frontRef);
        List<PointSolution> solsRef = FrontUtils.convertFrontToSolutionList(normRef);
        Hypervolume<PointSolution> hypervolume = new PISAHypervolume<>(normRef);
        return (-1 * hypervolume.evaluate(solsRef));
    }

    public static void evaluateWithTimes(String route, ArrayList<Long> times, String[] instances) throws IOException {
//        String fileName = "src\\main\\resources\\0Metrics.xlsx";
        String fileName = "src\\main\\resources\\data\\0\\0State.xlsx";
        FileInputStream inputStream = new FileInputStream(fileName);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheet("RawData");
        int currentRow = 1;


//        String pathGlobal = "src//main//resources//data_metrics";
        String[] paths = new String[]{
                "0",
                "1",
                "2",
                "3",
                "NSBBO",
                "NSGA-II",
        };


        boolean[] minimizing = new boolean[]{true, true};
        String pathJMetal = "./test/jmetal";
        new File(pathJMetal).mkdirs();

        try (PrintWriter pw = new PrintWriter("final_report_analysis.csv")) {
            String matching = "";
            String headers = "INSTANCE";
            for (int i = 0; i < paths.length; i++) {
                matching += paths[i] + ";" + (i + 1) + "\n";
                headers += ";CV REF-" + (i + 1) + ";HV " + (i + 1) + ";EPS " + (i + 1) + ";GD " + (i + 1) + ";SIZE " + (i + 1) + ";SPREAD " + (i + 1);
            }
            pw.println(matching);
            pw.println(headers);
            //String[] files = new File(route + "/" + paths[0]).list((dir, name) -> name.endsWith(".txt"));
            for (String instanceName : instances) {
                try {
                    instanceName += ".txt";
                    System.out.println(instanceName);
                    pw.print(instanceName + ";");
                    String pathOutRefSet = pathJMetal + "/" + instanceName.replace(".txt", "") + "_reference.txt";
                    /*
                    Creates the reference set and multiplies by -1 those objectives that are minimizing
                     */
                    prepareFronts(minimizing, route, paths, pathJMetal, paths, instanceName, pathOutRefSet);
                    Front frontRef = new ArrayFront(pathOutRefSet);
                    Front[] fronts = new Front[paths.length];
                    for (int i = 0; i < paths.length; i++) {
                        fronts[i] = new ArrayFront(pathJMetal + "/" + paths[i] + "/" + instanceName);
                    }

                    /*
                    NORMALIZATION
                     */
                    FrontNormalizer frontNormalizer = new FrontNormalizer(frontRef);
                    Front normRef = frontNormalizer.normalize(frontRef);
                    List<PointSolution> solsRef = FrontUtils.convertFrontToSolutionList(normRef);
                    Front[] normFronts = new Front[fronts.length];
                    List<PointSolution>[] sols = new List[fronts.length];
                    for (int i = 0; i < normFronts.length; i++) {
                        normFronts[i] = frontNormalizer.normalize(fronts[i]);
                        sols[i] = FrontUtils.convertFrontToSolutionList(normFronts[i]);
                    }

                    SetCoverage coverage = new SetCoverage();
                    Hypervolume<PointSolution> hypervolume = new PISAHypervolume<>(normRef);
                    Epsilon<PointSolution> epsilon = new Epsilon<>(normRef);
                    GenerationalDistance<PointSolution> gd = new GenerationalDistance<>();
                    InvertedGenerationalDistance<PointSolution> igd = new InvertedGenerationalDistance<>();
                    InvertedGenerationalDistancePlus<PointSolution> igdp = new InvertedGenerationalDistancePlus<>();
                    GeneralizedSpread<PointSolution> sp = new GeneralizedSpread<>();

                    XSSFRow xssfRow;
                    if (sheet.getRow(currentRow) != null)
                        xssfRow = sheet.getRow(currentRow);
                    else
                        xssfRow = sheet.createRow(currentRow);

                    int cell = 1;
                    Cell instance = xssfRow.createCell(cell);
                    instance.setCellValue(instanceName);
                    cell++;
                    for (int i = 0; i < sols.length; i++) {
                        Cell covRow = xssfRow.createCell(cell);
                        cell++;
                        Cell hvRow = xssfRow.createCell(cell);
                        cell++;
                        Cell epsRow = xssfRow.createCell(cell);
                        cell++;
                        Cell gdValRow = xssfRow.createCell(cell);
                        cell++;
                        Cell igdValRow = xssfRow.createCell(cell);
                        cell++;
                        Cell igdpValRow = xssfRow.createCell(cell);
                        cell++;
                        Cell sizeRow = xssfRow.createCell(cell);
                        cell++;
                        Cell spValRow = xssfRow.createCell(cell);
                        cell++;
                        Cell timeRow = xssfRow.createCell(cell);
                        cell++;

                        double cov = coverage.evaluate(solsRef, sols[i]);
                        double hv = hypervolume.evaluate(sols[i]);
                        double eps = epsilon.evaluate(sols[i]);
                        double gdVal = gd.generationalDistance(fronts[i], normRef);
                        double igdVal = igd.invertedGenerationalDistance(fronts[i], normRef);
                        double igdpVal = igdp.invertedGenerationalDistancePlus(fronts[i], normRef);
                        double spVal = sp.generalizedSpread(fronts[i], normRef);
                        pw.print(cov + ";" + hv + ";" + eps + ";" + gdVal + ";" + sols[i].size() + ";" + spVal + ";");

                        covRow.setCellValue(cov);
                        hvRow.setCellValue(hv);
                        epsRow.setCellValue(eps);
                        gdValRow.setCellValue(gdVal);
                        igdValRow.setCellValue(igdVal);
                        igdpValRow.setCellValue(igdpVal);
                        sizeRow.setCellValue(sols[i].size());
                        spValRow.setCellValue(spVal);
//                        if (i < 4)
//                            timeRow.setCellValue(times.remove(0));

                        timeRow.setCellValue(0);
                    }
                    currentRow++;

                    XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
                    //FileOutputStream outputStream = new FileOutputStream(route + File.separator + "0Metrics.xlsx");
                    FileOutputStream outputStream = new FileOutputStream(route + File.separator + "0State.xlsx");
                    workbook.write(outputStream);

                } catch (JMetalException e) {
                    System.err.println("ERROR EN INSTANCIA: " + instanceName);
                }
                pw.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Method to read time from files
    public static ArrayList<Long> readTimes(String path, String[] instances) {
        ArrayList<Long> times = new ArrayList<>();
        for (String instance : instances) {
            for (int i = 0; i < 4; i++) {
                String filePath = path + File.separator + i + File.separator + instance + "_time.txt";
                try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                    String firstLine = reader.readLine();
                    times.add(Long.parseLong(firstLine));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return times;
    }

    public static void evaluateFormulas(String route) throws IOException {
        String fileName = route + File.separator + "0Metrics.xlsx";
        String sheetName = "Overview"; // replace with your sheet name

        try (FileInputStream fis = new FileInputStream(fileName);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

            for (Row row : sheet) {
                for (Cell cell : row) {
                    if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
                        evaluator.evaluateFormulaCell(cell);
                    }
                }
            }
            evaluator.evaluateAll();
            try (FileOutputStream fos = new FileOutputStream(fileName)) {
                workbook.write(fos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}