package vfdt;

import java.io.FileReader;
import java.io.LineNumberReader;

public class Main {
    public static void f1() {
        String fn = "D:/temp/sample.csv";
        int bs = 1000;
        try {
            FileReader fr = new FileReader(fn);
            LineNumberReader lnr = new LineNumberReader(fr, bs);
            long startTime = System.currentTimeMillis();
            String line;
            lnr.setLineNumber(2);
            while ((line = lnr.readLine()) != null) {
                System.out.println(line);
                break;
            }
            long elapsed = System.currentTimeMillis() - startTime;
            System.out.println("Elapsed time: " + elapsed + " ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void f2() {

    }

    public static void main(String[] args) {
        f2();
    }
}
