package vfdt.ml;

import java.util.HashMap;

/**
 * Stores the hyperparameters of the project.
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 07
 */
public abstract class Config {

    private enum Module {
        DATA, MEASURE, ML, STAT, TREE
    }

    private static HashMap<Module, Object> modules = new HashMap<>();

    public static Object getModule(Module module) {
        return modules.get(module);
    }

    public static void setModule(Module module, Object config) {
        modules.put(module, config);
    }

    // todo: Create a config class for each module.
}
