package vfdt.measure.gain;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 04
 */
public interface Gain {
    Double measure(Split split) throws Exception;
}
