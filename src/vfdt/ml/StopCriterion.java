package vfdt.ml;

import vfdt.tree.DecisionTree;

public class StopCriterion {
    private Integer maxSize;

    public StopCriterion(Integer maxSize) {
        this.maxSize = maxSize;
    }

    public boolean shouldStop(Classifier model) {
        DecisionTree dt = (DecisionTree) model;
        if (maxSize != null)
            return dt.getNumNodes() >= maxSize;
        else
            return false;
    }
}
