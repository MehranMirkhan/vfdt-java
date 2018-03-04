package vfdt.data;

/**
 * Information about dataset.
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Feb 28
 */
public class DatasetInfo {
    private int classIndex;
    private String datasetName;
    private AttributeInfo[] attributeInfo;

    public DatasetInfo() {
        this.classIndex = -1;
        this.datasetName = "Unknown";
        this.attributeInfo = null;
    }

    public DatasetInfo attributeInfo(AttributeInfo... attributeInfo) {
        this.attributeInfo = attributeInfo;
        return this;
    }

    public DatasetInfo classIndex(int classIndex) {
        this.classIndex = classIndex;
        return this;
    }

    public DatasetInfo datasetName(String datasetName) {
        this.datasetName = datasetName;
        return this;
    }

    public int getClassIndex() {
        return classIndex;
    }

    public void setClassIndex(int classIndex) {
        this.classIndex = classIndex;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public AttributeInfo[] getAttributeInfo() {
        return attributeInfo;
    }

    public void setAttributeInfo(AttributeInfo[] attributeInfo) {
        this.attributeInfo = attributeInfo;
    }

    public int getNumAttributes() {
        return this.attributeInfo.length;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Dataset name = ").append(this.datasetName).append("\n")
          .append("Class index = ").append(this.classIndex).append("\n");
        for (AttributeInfo attinfo : this.attributeInfo)
            sb.append(attinfo).append("\n");
        return sb.toString();
    }
}
