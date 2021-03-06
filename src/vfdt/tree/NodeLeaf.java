package vfdt.tree;

import vfdt.data.Attribute;
import vfdt.data.DatasetInfo;
import vfdt.data.Instance;
import vfdt.measure.Counts;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 08
 */
public class NodeLeaf extends NodeBase {
    private Counts      classCounts;
    private final DatasetInfo datasetInfo;

    public NodeLeaf(DatasetInfo datasetInfo) {
        this.datasetInfo = datasetInfo;
        this.classCounts = new Counts(datasetInfo.getNumClasses());
    }

    public NodeLeaf() {
        // Used only for test
        classCounts = null;
        datasetInfo = null;
    }

    public DatasetInfo getDatasetInfo() {
        return datasetInfo;
    }

    public void setClassCounts(Counts classCounts) {
        this.classCounts = classCounts;
    }

    @Override
    public Node children(Node... children) {
        return this;
    }

    @Override
    public void setChildren(Node... children) {
    }

    @Override
    public int getSubHeight() {
        return 1;
    }

    @Override
    public void replaceChild(Node nodeOld, Node nodeNew) throws NoSuchFieldException {
        throw new NullPointerException("This node has no children.");
    }

    @Override
    public NodeLeaf sortDown(Instance instance) {
        return this;
    }

    public Counts getClassCounts() {
        return classCounts;
    }

    public SplitInfo learn(Instance instance, Attribute label) throws Exception {
        Integer index = label.getValueIndex();
        classCounts.increment(index);
        return null;
    }

    public String classify(Instance instance) {
        Integer index = classCounts.getIndexOfMax();
        return datasetInfo.getClassAttribute().getValues()[index];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<getHeight()-1; i++)
            sb.append("|\t");
        sb.append("Label: ").append(classify(null)).append('\n');
        return sb.toString();
    }
}
