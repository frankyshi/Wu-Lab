import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.io.*;

public class Detrender {
    /**
     * Gets file name from user, reads data into an ArrayList, detrends it,
     * and outputs it to output file.
     *
     * @param args
     */
    public static void main(String[] args) {
        // Get input file name
        Scanner s = new Scanner(System.in);
        File in;
        do {
            System.out.print("Enter the input file name: ");
            String fileName = s.nextLine();
            in = new File(fileName);

            if (!in.exists()) {
                System.out.println("File does not exist!");
            }
        } while (!in.exists());

        // Read data from input
        ArrayList<HeartbeatPair> data = readFile(in);

        // Detrend data
        ArrayList<HeartbeatPair> detrendedData = detrend(data);

        // Get output file name
        File out;
        do {
            System.out.print("Enter the output file name: ");
            String fileName = s.nextLine();
            out = new File(fileName);

            if (!out.exists()) {
                System.out.println("File does not exist!");
            }
        } while (!out.exists());

        // Write data to output
        writeFile(out, detrendedData);
    }

    /**
     * Reads input file containing "time" and "current" and stores it in an ArrayList of HeartbeatPair.
     *
     * @param in Input file
     * @return ArrayList containing HeartbeatPairs read from input file
     */
    public static ArrayList<HeartbeatPair> readFile(File in) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(in));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("Reading data from " + in.getName() + "...");

        ArrayList<HeartbeatPair> data = new ArrayList<HeartbeatPair>();
        try {
            // Skip header
            for (int i = 0; i < 9; i++) {
                br.readLine();
            }

            // Read time and current
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                line = line.trim();
                String[] values = line.split("\\s+");

                double time = Double.parseDouble(values[0]);
                BigDecimal current = new BigDecimal(values[1]);
                data.add(new HeartbeatPair(time, current));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    /**
     * Detrends the data through differencing.
     *
     * @param data ArrayList that contains raw data (time, current)
     * @return ArrayList containing detrended data
     */
    public static ArrayList<HeartbeatPair> detrend(ArrayList<HeartbeatPair> data) {
        System.out.println("Detrending...");

        ArrayList<HeartbeatPair> detrendedData = new ArrayList<HeartbeatPair>();
        detrendedData.add(new HeartbeatPair(data.get(0).getTime(), new BigDecimal(0)));

        for (int i = 1; i < data.size(); i++) {
            HeartbeatPair prev = data.get(i - 1);
            HeartbeatPair curr = data.get(i);
            BigDecimal diff = curr.getCurrent().subtract(prev.getCurrent());
            detrendedData.add(new HeartbeatPair(curr.getTime(), diff));
        }

        System.out.println("Successfully detrended data!");

        return detrendedData;
    }

    /**
     * Writes detrended data to output file.
     *
     * @param out Output file
     */
    public static void writeFile(File out, ArrayList<HeartbeatPair> data) {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(out));
            pw.println("Time\t\tCurrent");
            for (int i = 0; i < data.size(); i++) {
                HeartbeatPair pair = data.get(i);
                DecimalFormat df = new DecimalFormat("0.000000E0");
                pw.printf("%.6f\t%s\n", pair.getTime(), df.format(pair.getCurrent()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Detrended data successfully printed to " + out.getName() + "!");
    }
}
