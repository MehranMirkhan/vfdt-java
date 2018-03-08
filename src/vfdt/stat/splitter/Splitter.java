package vfdt.stat.splitter;

import vfdt.measure.gain.Gain;
import vfdt.tree.Decision;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 07
 */
public interface Splitter {
    public Double getSplitGain() throws Exception;
    public Decision getDecision();
}
