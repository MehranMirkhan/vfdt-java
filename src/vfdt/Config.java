package vfdt;

import java.util.HashMap;

/**
 * Stores the hyperparameters of the project.
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 07
 */
public class Config {
    private static Config ins = new Config();

    public static Config ins() {
        return ins;
    }

    private enum Module {
        DATA, MEASURE, ML, STAT, TREE
    }

    private HashMap<Module, Object> modules;

    private Config() {
        modules = new HashMap<>();
    }

    public Object getModule(Module module) {
        return modules.get(module);
    }

    public void setModule(Module module, Object config) {
        modules.put(module, config);
    }

    // todo: Create a config class for each module.
}
