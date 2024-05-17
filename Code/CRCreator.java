import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class CRCreator {
    public static void main(String[] args) throws IOException {
        //Up to 60
        String[] instancesNames = {"A-10-10", "A-10-20", "A-10-30", "A-10-40", "A-10-50", "A-10-60", "A-10-70", "A-10-80", "A-10-90", "A-12-10", "A-12-20", "A-12-30", "A-12-40", "A-12-50", "A-12-60", "A-12-70", "A-12-80", "A-12-90", "N-15_t", "O-10_t", "O-15_t", "S-12_t", "S-14_t", "S-15_t", "Y-10_t", "Y-12_t", "Y-14_t", "Y-15_t", "A-20-10", "A-20-20", "A-20-30", "A-20-40", "A-20-50", "A-20-60", "A-20-70", "A-20-80", "A-20-90", "N-20_t", "O-20_t", "S-20_t", "Y-20_t", "A-25-10", "A-25-20", "A-25-30", "A-25-40", "A-25-50", "A-25-60", "A-25-70", "A-25-80", "A-25-90", "S-25_t", "Y-25_t", "Y-30_t", "Y-35_t", "Y-40_t", "Y-45_t", "Y-50_t", "Y-60_t"};

        for (String instancesName : instancesNames) {
            Instance instanceCR = new Instance("src\\main\\resources" + File.separator + instancesName + ".txt", 0);
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add(String.valueOf(instanceCR.numFacilities));
            arrayList.add(System.lineSeparator());

            for (int i = 0; i < instanceCR.widths.size(); i++) {
                arrayList.add("1");
                arrayList.add(" ");
            }

            arrayList.add(System.lineSeparator());
            arrayList.add(System.lineSeparator());
            for (int i = 0; i < instanceCR.widths.size(); i++) {
                for (int j = 0; j <= i; j++) {
                    arrayList.add("0");
                    arrayList.add(" ");
                }

                for (int j = i + 1; j < instanceCR.widths.size(); j++) {
                    int min = -2;
                    int max = 5;
                    Random random = new Random();
                    int randomInt = (int) (random.nextFloat() * (max - min) + min);
                    arrayList.add(String.valueOf(randomInt));
                    arrayList.add(" ");
                }
                arrayList.add(System.lineSeparator());
            }

            FileWriter writer = new FileWriter("src\\main\\resources" + File.separator +instancesName + "_CR.txt");
            for (String str : arrayList) {
                writer.write(str);
            }
            writer.close();
        }
    }
}
