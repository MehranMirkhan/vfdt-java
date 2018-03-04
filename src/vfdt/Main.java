package vfdt;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        String[] lines = {
                "% Some comments",
                "@ReLation M3hr4n",
                "@aTTribute a0 numeric",
                "@aTTribute a1 numeric",
                "@aTTribute class {c0, c1}",
                "@data"
        };
        String[] regex = {
                "(?i)@relation\\s+(\\w+)",
                "(?i)@attribute\\s+(\\w+)\\s+(.+)"
        };
        for (String line : lines) {
            if (line.charAt(0) == '%')
                continue;
            if (line.matches(regex[0])) {
                Matcher m = Pattern.compile(regex[0]).matcher(line);
                m.matches();
                System.out.println("Dataset name = " + m.group(1));
            } else if (line.matches(regex[1])) {
                Matcher m = Pattern.compile(regex[1]).matcher(line);
                m.matches();
                String attName = m.group(1);
                String attSpec = m.group(2);
                if (attSpec.toLowerCase().equals("numeric"))
                    System.out.println("attribute " + attName + " is numeric.");
                else {
                    System.out.println("attribute " + attName + " is nominal:");
                    String[] values = attSpec.split("\\W+");
                    for (String value : values)
                        if (!value.isEmpty())
                            System.out.println(value);
                }
            }
        }
//        String[] tokens;
//        // relation
//        tokens = lines[0].split("\\s+");
//        assert tokens[0].toLowerCase().equals("@relation");
//        System.out.println("Dataset name = " + tokens[1]);
//        // attributes
//        int i = 1;
//        String line;
//        do {
//            line = lines[i];
//            tokens = line.split("\\s+");
//            assert tokens[0].toLowerCase().equals("@attribute");
//            String attName = tokens[1];
//            String values = line.split("@\\w+\\s+\\w+\\s+")[0];
//            if (values.toLowerCase().equals("numeric")) {
//                System.out.println("attribute " + attName + " is numeric.");
//            } else {
//                System.out.println("attribute " + attName + " is nominal:");
//                for (String s : values.split("[{|}]"))
//            }
//            i++;
//        } while(!line.toLowerCase().equals("@data"));
    }

    public static void main(String[] args) {
        f2();
    }
}
