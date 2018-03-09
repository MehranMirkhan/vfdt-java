package vfdt.data;

import vfdt.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 09
 */
public class ArffReader implements DatasetReader {

    protected final String fileName;
    protected DatasetInfo datasetInfo;

    public ArffReader(String fileName) {
        this.fileName = fileName;
        datasetInfo = new DatasetInfo();
    }

    public DatasetInfo getDatasetInfo() {
        return datasetInfo;
    }

    @Override
    public DatasetInfo analyze() throws IOException {
        FileReader fileReader = new FileReader(fileName);
        BufferedReader reader = new BufferedReader(fileReader);
        ArrayList<AttributeInfo> attInfo = new ArrayList<>();
        String[] regex = {
                "^[%].*",
                "(?i)@relation\\s+(\\w+)",
                "(?i)@attribute\\s+(\\w+)\\s+(.+)",
                "(?i)@data"
        };
        Pattern[] patterns = new Pattern[regex.length];
        for (int i=0; i<regex.length; i++)
            patterns[i] = Pattern.compile(regex[i]);
        String line;
        while ((line = reader.readLine()) != null) {
            Matcher m;
            // comment
            m = patterns[0].matcher(line);
            if (m.matches()) continue;
            // relation
            m = patterns[1].matcher(line);
            if (m.matches()) {
                this.datasetInfo.setDatasetName(m.group(1));
                continue;
            }
            // attribute
            m = patterns[2].matcher(line);
            if (m.matches()) {
                String attName = m.group(1);
                String attSpec = m.group(2);
                AttributeInfo.AttributeType type;
                ArrayList<String> values = new ArrayList<>();
                if (attSpec.toLowerCase().equals("numeric"))
                    type = AttributeInfo.AttributeType.NUMERICAL;
                else {
                    type = AttributeInfo.AttributeType.NOMINAL;
                    String[] valuesTemp = attSpec.split("\\W+");
                    for (String v : valuesTemp)
                        if (!v.isEmpty())
                            values.add(v);
                }
                AttributeInfo att = new AttributeInfo(type).name(attName);
                if (!values.isEmpty())
                    att.setValues(values.toArray(new String[0]));
                attInfo.add(att);
                continue;
            }
            // data
            m = patterns[3].matcher(line);
            if (m.matches())
                break;
        }
        datasetInfo.setAttributeInfo(attInfo.toArray(new AttributeInfo[0]));
        return datasetInfo;
    }

    @Override
    public Iterator<Pair<Instance, Attribute>> onePass() throws IOException {
        return new ArffIterator(datasetInfo, fileName, 1, null);
    }

    @Override
    public Iterator<Pair<Instance, Attribute>> onePass(IndexCondition indexCondition) throws IOException {
        return new ArffIterator(datasetInfo, fileName, 1, indexCondition);
    }

    @Override
    public Iterator<Pair<Instance, Attribute>> epochs(int numEpochs) throws IOException {
        return new ArffIterator(datasetInfo, fileName, numEpochs, null);
    }

    @Override
    public Iterator<Pair<Instance, Attribute>> epochs(int numEpochs, IndexCondition indexCondition) throws IOException {
        return new ArffIterator(datasetInfo, fileName, numEpochs, indexCondition);
    }



    protected class ArffIterator implements Iterator<Pair<Instance, Attribute>> {
        private final DatasetInfo datasetInfo;
        private final String fileName;
        private final int numEpochs;
        private final IndexCondition indexCondition;

        private LineNumberReader lnr;
        private String line;
        private int currentEpoch;

        public ArffIterator(DatasetInfo datasetInfo, String fileName, int numEpochs, IndexCondition indexCondition) throws IOException {
            this.datasetInfo = datasetInfo;
            this.fileName = fileName;
            this.numEpochs = numEpochs;
            this.indexCondition = indexCondition;

            currentEpoch = 1;

            reset();
        }

        private void reset() throws IOException {
            lnr = new LineNumberReader(new FileReader(fileName));
            line = lnr.readLine();
            skip();
        }

        /**
         * Skips header part of the file.
         */
        private void skip() {
            while (line != null)
                if (line.trim().toLowerCase().equals("@data")) {
                    lnr.setLineNumber(-2);
                    break;
                }
        }

        @Override
        public boolean hasNext() {
            return line == null;
        }

        @Override
        public Pair<Instance, Attribute> next() {
            try {
                while (!indexCondition.isValid(lnr.getLineNumber()))
                    updateLine();
                Pair<Instance, Attribute> data = parseLine(line);
                updateLine();
                return data;
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                lnr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void updateLine() throws IOException {
            line = lnr.readLine();
            // Check whether current epoch is ended
            boolean eof = line == null;
            if (eof) {      // We have reached the end of file.
                if (currentEpoch < numEpochs) {     // But still more epochs are remained.
                    currentEpoch += 1;
                    reset();
                }
            }
        }

        protected Pair<Instance, Attribute> parseLine(String line) throws Exception {
            AttributeInfo[] attsInfo = this.datasetInfo.getAttributeInfo();
            ArrayList<Attribute> atts = new ArrayList<>();
            Attribute label = null;
            String[] values = line.split("\\s*,\\s*");
            for (int i=0; i<values.length; i++) {
                // Create Attribute
                AttributeInfo attInfo = attsInfo[i];
                if (i == datasetInfo.getClassIndex()) {
                    label = new Attribute<String>(attInfo, values[i]);
                } else {
                    switch (attInfo.getType()) {
                        case NOMINAL:
                            atts.add(new Attribute<String>(attInfo, values[i]));
                            break;
                        case NUMERICAL:
                            atts.add(new Attribute<Double>(attInfo, Double.parseDouble(values[i])));
                            break;
                        default:
                            throw new Exception("Attribute type not recognized.");
                    }
                }
            }
            return new Pair<>(new Instance(atts.toArray(new Attribute[0])), label);
        }
    }
}
