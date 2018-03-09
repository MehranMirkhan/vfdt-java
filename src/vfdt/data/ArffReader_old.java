package vfdt.data;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Reads .arff files
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 03
 */
public class ArffReader_old {
    protected FileReader fileReader;
    protected LineNumberReader lnr;
    protected Integer classIndex;
    protected DatasetInfo datasetInfo;

    public ArffReader_old(String fileName, Integer classIndex, Integer bufferSize) throws FileNotFoundException {
        this.fileReader = new FileReader(fileName);
        if (bufferSize == null)
            this.lnr = new LineNumberReader(this.fileReader);
        else
            this.lnr = new LineNumberReader(this.fileReader, bufferSize);
        this.classIndex = classIndex;
        this.datasetInfo = new DatasetInfo().classIndex(classIndex);
    }

    public ArffReader_old(String fileName, Integer classIndex) throws FileNotFoundException {
        this(fileName, classIndex, null);
    }

    public ArffReader_old(String fileName) throws FileNotFoundException {
        this(fileName, null, null);
    }

    public void close() throws IOException {
        this.lnr.close();
    }

    public DatasetInfo getDatasetInfo() {
        return datasetInfo;
    }

    /**
     * Reads the header and builds DatasetInfo.
     */
    public void init() throws IOException {
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
        while ((line = lnr.readLine()) != null) {
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
        this.datasetInfo.setAttributeInfo(attInfo.toArray(new AttributeInfo[0]));
        this.lnr.setLineNumber(-2);
    }

    protected class ArffIterator implements Iterator<Instance> {
        private DatasetInfo datasetInfo;
        private LineNumberReader lnr;
        private String line;

        public ArffIterator(DatasetInfo datasetInfo, LineNumberReader lnr) throws IOException {
            this.datasetInfo = datasetInfo;
            this.lnr = lnr;
            this.line = lnr.readLine();
        }

        @Override
        public boolean hasNext() {
            return this.line == null;
        }

        @Override
        public Instance next() {
            String temp = this.line;
            try {
                this.line = this.lnr.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                return parseLine(temp);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected Instance parseLine(String line) throws Exception {
            int n_atts = this.datasetInfo.getNumAttributes();
            AttributeInfo[] attsInfo = this.datasetInfo.getAttributeInfo();
            Attribute[] atts = new Attribute[n_atts];
            String[] values = line.split("\\s*,\\s*");
            assert values.length == n_atts;
            for (int i=0; i<n_atts; i++) {
                // Create Attribute
                AttributeInfo attInfo = attsInfo[i];
                switch (attInfo.getType()) {
                    case NOMINAL:
                        atts[i] = new Attribute<String>(attInfo, values[i]);
                        break;
                    case NUMERICAL:
                        atts[i] = new Attribute<Double>(attInfo, Double.parseDouble(values[i]));
                        break;
                    default:
                        throw new Exception("Attribute type not recognized.");
                }
            }
            return new Instance(atts);
        }
    }

    public ArffIterator iterator() throws IOException {
        return new ArffIterator(this.datasetInfo, this.lnr);
    }

    public int getInstanceIndex() {
        return this.lnr.getLineNumber();
    }

    // todo: Read parts of file (train/test split)
}
