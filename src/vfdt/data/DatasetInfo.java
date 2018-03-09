package vfdt.data;

/**
 * Information about dataset.
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Feb 28
 */
public class DatasetInfo {
    private Integer classIndex = null;
    private String datasetName = "Unknown";
    private AttributeInfo[] attributeInfo = null;

    public DatasetInfo attributeInfo(AttributeInfo... attributeInfo) {
        this.attributeInfo = attributeInfo;
        return this;
    }

    public DatasetInfo classIndex(Integer classIndex) {
        this.classIndex = classIndex;
        return this;
    }

    public DatasetInfo datasetName(String datasetName) {
        this.datasetName = datasetName;
        return this;
    }

    public Integer getClassIndex() {
        return classIndex;
    }

    public void setClassIndex(Integer classIndex) {
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

    public int getNumClasses() {
        AttributeInfo attClass = attributeInfo[classIndex];
        return attClass.getValues().length;
    }

    public Attribute getLabel(Instance instance) {
        if (getClassIndex() == null)
            return null;
        else {
            Attribute att = instance.getAttribute(getClassIndex());
            return att;
        }
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

    public AttributeInfo getClassAttribute() {
        return getAttributeInfo()[getClassIndex()];
    }
}
