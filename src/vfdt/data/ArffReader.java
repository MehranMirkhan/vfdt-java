package vfdt.data;

import vfdt.util.Pair;

import java.io.*;
import java.util.ArrayList;
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

    private final String      fileName;
    private final DatasetInfo datasetInfo;

    public ArffReader(String fileName) {
        this.fileName = fileName;
        datasetInfo = new DatasetInfo();
    }

    public DatasetInfo getDatasetInfo() {
        return datasetInfo;
    }

    @Override
    public DatasetInfo analyze() throws IOException {
        FileReader               fileReader = new FileReader(fileName);
        BufferedReader           reader     = new BufferedReader(fileReader);
        ArrayList<AttributeInfo> attInfo    = new ArrayList<>();
        String[] regex = {
                "^[%].*",
                "(?i)@relation\\s+(\\w+)",
                "(?i)@attribute\\s+(\\w+)\\s+(.+)",
                "(?i)@data"
        };
        Pattern[] patterns = new Pattern[regex.length];
        for (int i = 0; i < regex.length; i++)
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
                    att.setValues(values.toArray(new String[values.size()]));
                attInfo.add(att);
                continue;
            }
            // data
            m = patterns[3].matcher(line);
            if (m.matches())
                break;
        }
        datasetInfo.setAttributeInfo(attInfo.toArray(new AttributeInfo[attInfo.size()]));
        reader.close();
        return datasetInfo;
    }

    @Override
    public DatasetIterator onePass() throws IOException {
        return new ArffIterator(datasetInfo, fileName, 1, null);
    }

    @Override
    public DatasetIterator onePass(IndexCondition indexCondition) throws IOException {
        return new ArffIterator(datasetInfo, fileName, 1, indexCondition);
    }

    @Override
    public DatasetIterator epochs(int numEpochs) throws IOException {
        return new ArffIterator(datasetInfo, fileName, numEpochs, null);
    }

    @Override
    public DatasetIterator epochs(int numEpochs, IndexCondition indexCondition) throws IOException {
        return new ArffIterator(datasetInfo, fileName, numEpochs, indexCondition);
    }


    protected class ArffIterator implements DatasetIterator {
        private final DatasetInfo    datasetInfo;
        private final String         fileName;
        private final int            numEpochs;
        private final IndexCondition indexCondition;

        private LineNumberReader          lnr;
        private String                    line;
        private int                       currentEpoch;
        private Instance                  instance;
        private Attribute<String>         label;
        private int                       numAtts;
        private Pair<Instance, Attribute> pair;

        public ArffIterator(DatasetInfo datasetInfo, String fileName, int numEpochs, IndexCondition indexCondition) throws IOException {
            this.datasetInfo = datasetInfo;
            this.fileName = fileName;
            this.numEpochs = numEpochs;
            this.indexCondition = indexCondition;

            currentEpoch = 1;
            AttributeInfo[] attsInfo = this.datasetInfo.getAttributeInfo();
            label = new Attribute<String>(datasetInfo.getClassAttribute(), null);
            if (datasetInfo.getClassIndex() == null)
                numAtts = attsInfo.length;
            else
                numAtts = attsInfo.length - 1;
            Attribute[] atts = new Attribute[numAtts];
            for (int i = 0; i < attsInfo.length; i++) {
                if (datasetInfo.getClassIndex() == null || i != datasetInfo.getClassIndex())
                    atts[i] = new Attribute<>(attsInfo[i], null);
            }
            instance = new Instance(atts);
            pair = new Pair<>(instance, label);
            reset();
        }

        @Override
        public void close() throws IOException {
            lnr.close();
        }

        private void reset() throws IOException {
            lnr = new LineNumberReader(new FileReader(fileName));
            line = lnr.readLine();
            skip();
            hasNext();
        }

        /**
         * Skips header part of the file.
         */
        private void skip() throws IOException {
            while (line != null) {
                if (line.trim().toLowerCase().equals("@data")) {
                    updateLine();
                    lnr.setLineNumber(0);
                    break;
                }
                updateLine();
            }
        }

        @Override
        public boolean hasNext() {
            try {
                while (indexCondition != null && !indexCondition.isValid(lnr.getLineNumber())) {
                    updateLine();
                    if (line == null)
                        return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return line != null;
        }

        @Override
        public Pair<Instance, Attribute> next() {
            try {
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

        void updateLine() throws IOException {
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

        Pair<Instance, Attribute> parseLine(String line) throws Exception {
            AttributeInfo[] attsInfo = this.datasetInfo.getAttributeInfo();
//            String[]        values   = line.split("\\s*,\\s*");
            String[]        values   = line.split(",");
            for (int i = 0; i < values.length; i++) {
                String v = values[i].trim();
                // Create Attribute
                AttributeInfo attInfo = attsInfo[i];
                if (i == datasetInfo.getClassIndex()) {
                    label.setValue(v);
                } else {
                    if (attInfo.isNumerical())
                        instance.getAttribute(i).setValue(Double.parseDouble(v));
                    else
                        instance.getAttribute(i).setValue(v);
                }
            }
            return pair;
        }
    }
}
