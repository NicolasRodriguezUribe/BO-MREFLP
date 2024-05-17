import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Instance {
    int numFacilities;
    ArrayList<Float> widths = new ArrayList<>();
    ArrayList<ArrayList<Integer>> weights = new ArrayList<>();

    public int getNumFacilities() {
        return numFacilities;
    }

    public ArrayList<Float> getWidths() {
        return widths;
    }

    public ArrayList<ArrayList<Integer>> getWeights() {
        return weights;
    }

    public Instance(String filename, int spaces) {
        ArrayList<String> widthsTemp = new ArrayList<>();
        String auxWidth = "";
        try
        {
            BufferedReader in = new BufferedReader(new FileReader(filename));
            String line;

            int currentLine = 0;

            while ((line = in.readLine()) != null) {

                if (currentLine == 0)
                    numFacilities = Integer.parseInt(line.toString());
                else if (currentLine == 1)
                    auxWidth = line;
                else if (currentLine == 2){}

                else {
                    widthsTemp.add((line));
                }
                currentLine++;
            }
            in.close();

            numFacilities += spaces;

            String[] widthsAux = auxWidth.split(" ");

            for (String aux : widthsAux) {
                widths.add(Float.parseFloat(aux));
            }

            for(int i=0; i<spaces;i++)
                widths.add(0.5f);

            //mirar si puede no ser O(n^2)
            for(String currentLineWeight : widthsTemp)
            {
                String[] weightAux = currentLineWeight.split(" ");
                ArrayList<Integer> weightsListTemp = new ArrayList<>();
                for (String aux : weightAux) {
                    weightsListTemp.add(Integer.parseInt(aux));
                }

                for(int s = 0; s < spaces; s++)
                    weightsListTemp.add(0);

                weights.add(weightsListTemp);
            }

            for(int i = 0; i < spaces; i++)
            {
                ArrayList<Integer> weightsListTemp = new ArrayList<>();
                for (int j = 0; j < numFacilities;j++)
                {
                    weightsListTemp.add(0);
                }
                weights.add(weightsListTemp);
            }
        }

        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public Instance(String filename, double spaceFactor) {
        int spaces = 0;
        ArrayList<String> widthsTemp = new ArrayList<>();
        String auxWidth = "";
        try
        {
            BufferedReader in = new BufferedReader(new FileReader(filename));
            String line;

            int currentLine = 0;

            while ((line = in.readLine()) != null) {

                if (currentLine == 0)
                    numFacilities = Integer.parseInt(line.toString());
                else if (currentLine == 1)
                    auxWidth = line;
                else if (currentLine == 2){}

                else {
                    widthsTemp.add((line));
                }
                currentLine++;
            }
            in.close();
            spaces = numFacilities*(int)spaceFactor;
            numFacilities += spaces;

            String[] widthsAux = auxWidth.split(" ");

            for (String aux : widthsAux) {
                widths.add(Float.parseFloat(aux));
            }

            for(int i=0; i<spaces;i++)
                widths.add(0.5f);

            //mirar si puede no ser O(n^2)
            for(String currentLineWeight : widthsTemp)
            {
                String[] weightAux = currentLineWeight.split(" ");
                ArrayList<Integer> weightsListTemp = new ArrayList<>();
                for (String aux : weightAux) {
                    weightsListTemp.add(Integer.parseInt(aux));
                }

                for(int s = 0; s < spaces; s++)
                    weightsListTemp.add(0);

                weights.add(weightsListTemp);
            }

            for(int i = 0; i < spaces; i++)
            {
                ArrayList<Integer> weightsListTemp = new ArrayList<>();
                for (int j = 0; j < numFacilities;j++)
                {
                    weightsListTemp.add(0);
                }
                weights.add(weightsListTemp);
            }
        }

        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
