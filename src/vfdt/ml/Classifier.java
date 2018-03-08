package vfdt.ml;

import vfdt.data.Instance;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 08
 */
public interface Classifier {
    public void learn(Instance instance);
    public Integer classify(Instance instance);
}
