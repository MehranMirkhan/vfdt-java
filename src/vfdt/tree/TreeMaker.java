package vfdt.tree;

import com.sun.istack.internal.NotNull;
import vfdt.data.Attribute;
import vfdt.data.AttributeInfo;
import vfdt.data.DatasetInfo;
import vfdt.data.Instance;
import vfdt.ml.StopCriterion;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Random;

/**
 * Creates a random decision tree to synthesize data.
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Apr 05
 */
public abstract class TreeMaker {

    public static class SimpleDecisionTree extends DecisionTree {

        SimpleDecisionTree(DatasetInfo datasetInfo) {
            super(datasetInfo);
        }

        public DatasetInfo getDatasetInfo() {
            return datasetInfo;
        }

        public String draw() {
            return super.print(true) + getRoot().toString();
        }

        @Override
        public void learn(Instance instance, Attribute label, StopCriterion stopCriterion) {
        }

        @Override
        public String classify(Instance instance) {
            NodeLeaf leaf = sortDown(instance);
            return leaf.classify(instance);
        }
    }

    public static SimpleDecisionTree createTree(Random rand,
                                                Integer numAttributes, Integer numClasses,
                                                Double minAttValue, Double maxAttValue,
                                                Integer minHeight, Integer maxHeight,
                                                Double leafProb, long seed) {
        // Init tree
        DatasetInfo datasetInfo = createDatasetInfo(numAttributes, numClasses);
        SimpleDecisionTree tree = new SimpleDecisionTree(datasetInfo);
        // Create the root
        DecisionNode root = makeDecisionNode(rand, datasetInfo, minAttValue, maxAttValue);
        root.setHeight(1);
        extendNode(rand, datasetInfo, minAttValue, maxAttValue,
                minHeight, maxHeight, leafProb, root, 1);
        tree.setRoot(root);
        return tree;
    }

    private static void extendNode(Random rand, DatasetInfo datasetInfo,
                                   Double minAttValue, Double maxAttValue,
                                   Integer minHeight, Integer maxHeight,
                                   Double leafProb, @NotNull Node node, int height) {
        if (!(node instanceof NodeLeaf)) {
            Node leftChild, rightChild;
            if (height < minHeight - 1) {   // Children must be decision nodes
                leftChild = makeDecisionNode(rand, datasetInfo, minAttValue, maxAttValue);
                extendNode(rand, datasetInfo, minAttValue, maxAttValue,
                        minHeight, maxHeight, leafProb, leftChild, height + 1);
                rightChild = makeDecisionNode(rand, datasetInfo, minAttValue, maxAttValue);
                extendNode(rand, datasetInfo, minAttValue, maxAttValue,
                        minHeight, maxHeight, leafProb, rightChild, height + 1);
            } else if (height == maxHeight - 1) {   // Children must be leaf nodes
                leftChild = makeLeafNode(rand, datasetInfo);
                rightChild = makeLeafNode(rand, datasetInfo);
            } else {
                boolean shouldLeftBeLeaf = rand.nextDouble() < leafProb;
                if (shouldLeftBeLeaf)
                    leftChild = makeLeafNode(rand, datasetInfo);
                else {
                    leftChild = makeDecisionNode(rand, datasetInfo, minAttValue, maxAttValue);
                    extendNode(rand, datasetInfo, minAttValue, maxAttValue,
                            minHeight, maxHeight, leafProb, leftChild, height + 1);
                }
                boolean shouldRightBeLeaf = rand.nextDouble() < leafProb;
                if (shouldRightBeLeaf)
                    rightChild = makeLeafNode(rand, datasetInfo);
                else {
                    rightChild = makeDecisionNode(rand, datasetInfo, minAttValue, maxAttValue);
                    extendNode(rand, datasetInfo, minAttValue, maxAttValue,
                            minHeight, maxHeight, leafProb, rightChild, height + 1);
                }
            }
            leftChild.setParent(node);
            leftChild.setHeight(height + 1);
            rightChild.setParent(node);
            rightChild.setHeight(height + 1);
            node.setChildren(leftChild, rightChild);
        }
    }

    private static NodeLeaf makeLeafNode(Random rand, DatasetInfo datasetInfo) {
        NodeLeaf leaf = new NodeLeaf(datasetInfo);
        int label = rand.nextInt(datasetInfo.getNumClasses());
        leaf.getClassCounts().increment(label);
        return leaf;
    }

    private static DecisionNode makeDecisionNode(Random rand, DatasetInfo datasetInfo,
                                                 Double minAttValue, Double maxAttValue) {
        int attIndex = rand.nextInt(datasetInfo.getNumAttributes() - 1);
        Double decisionValue = rand.nextDouble() * (maxAttValue - minAttValue) + minAttValue;
        return new DecisionNode(
                datasetInfo.getAttributeInfo()[attIndex],
                new DecisionNumeric(decisionValue));
    }

    private static DatasetInfo createDatasetInfo(Integer numAttributes, Integer numClasses) {
        DatasetInfo datasetInfo = new DatasetInfo();
        AttributeInfo[] attributeInfos = new AttributeInfo[numAttributes + 1];
        for (int i = 0; i < numAttributes; i++)
            attributeInfos[i] = new AttributeInfo(AttributeInfo.AttributeType.NUMERICAL).name("a" + i);
        String[] classValues = new String[numClasses];
        for (int i = 0; i < numClasses; i++)
            classValues[i] = "c" + i;
        attributeInfos[numAttributes] = new AttributeInfo(AttributeInfo.AttributeType.NOMINAL)
                .name("class").values(classValues);
        datasetInfo.setAttributeInfo(attributeInfos);
        datasetInfo.setClassIndex(numAttributes);
        return datasetInfo;
    }

    public static void generateDataset(Random rand, SimpleDecisionTree tree,
                                       Double minAttValue, Double maxAttValue,
                                       String filePath,
                                       long numInstances) throws IOException {
        DatasetInfo datasetInfo = tree.getDatasetInfo();
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        // Write arff header
        writer.write("@relation " + datasetInfo.getDatasetName() + "\n\n");
        for (int i = 0; i < datasetInfo.getAttributeInfo().length; i++) {
            AttributeInfo attInfo = datasetInfo.getAttributeInfo()[i];
            String attDesc;
            if (attInfo.isNumerical())
                attDesc = "numeric";
            else {
                attDesc = "{";
                for (int j = 0; j < attInfo.getValues().length; j++) {
                    attDesc += attInfo.getValues()[j];
                    if (j != attInfo.getValues().length - 1)
                        attDesc += ", ";
                }
                attDesc += "}";
            }
            writer.write("@attribute " + attInfo.getName() + " " + attDesc + "\n");
        }
        writer.write("\n@data\n");
        Attribute[] atts = new Attribute[datasetInfo.getNumAttributes() - 1];
        for (int j = 0; j < atts.length; j++) {
            atts[j] = new Attribute<Double>(datasetInfo.getAttributeInfo()[j], null);
        }
        DecimalFormat df = new DecimalFormat("0.#####");
        Instance instance = new Instance(atts);
        for (int i = 0; i < numInstances; i++) {
            for (int j = 0; j < atts.length; j++) {
                Double value = rand.nextDouble() * (maxAttValue - minAttValue) + minAttValue;
                atts[j].setValue(value);
            }
            String label = tree.classify(instance);
            String line = "";
            for (int j = 0; j < atts.length; j++) {         // ASSUMING classIndex = -1
                line += df.format(atts[j].getValue()) + ", ";
            }
            line += label + "\n";
            writer.write(line);
        }
        writer.close();
    }
}
