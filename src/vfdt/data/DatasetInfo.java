package vfdt.data;

/**
 * Information about dataset.
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Feb 28
 */
public class DatasetInfo {
    private AttributeInfo[] attributeInfo;
    private int classIndex;

    public DatasetInfo(AttributeInfo... attinfo) {
        this.attributeInfo = attinfo;
    }

    public DatasetInfo(int classIndex, AttributeInfo... attinfo) {
        this.classIndex = classIndex;
        this.attributeInfo = attinfo;
    }
}
