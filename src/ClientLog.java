import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class ClientLog {

    protected List<String[]> choice;
    protected String[] title = "Номер, Количество".split(",");

    public ClientLog() {
        this.choice = new ArrayList<>();
    }

    public void log(int productNumb, int amount) {
        String[] choiceCsv = new String[2];
        choiceCsv[0] = String.valueOf(productNumb + 1);
        choiceCsv[1] = String.valueOf(amount);
        choice.add(choiceCsv);
    }

    public void exportAsCSV(File txtFile) {
        try (CSVWriter writer = new CSVWriter((new FileWriter(txtFile, true)), ',',
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END)) {
            if (txtFile.length() == 0) {
                writer.writeNext(title);
            }
            writer.writeAll(choice);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}