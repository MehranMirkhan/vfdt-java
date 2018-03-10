package vfdt.ml;

import vfdt.data.Attribute;
import vfdt.data.Instance;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 08
 */
public interface Classifier {
    void learn(Instance instance, Attribute label) throws Exception;

    String classify(Instance instance);
}
