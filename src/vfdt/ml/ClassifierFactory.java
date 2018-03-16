package vfdt.ml;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 16
 */
public abstract class ClassifierFactory {
    public abstract Classifier build() throws Exception;
}
