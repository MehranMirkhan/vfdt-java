package vfdt.data;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Information about an attribute.
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Feb 23
 */
public class AttributeInfo {
    public enum AttributeType {
        NOMINAL, NUMERICAL
    }

    private String name = "Unknown";
    private String[] values = null;
    private AttributeType type;

    public AttributeInfo(AttributeType type) {
        this.type = type;
    }

    public AttributeInfo name(String name) {
        this.name = name;
        return this;
    }

    public AttributeInfo values(String... values) {
        this.values = values;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getValues() {
        return this.values;
    }

    public void setValues(String... values) {
        this.values = values;
    }

    public AttributeType getType() {
        return this.type;
    }

    public void setType(AttributeType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "AttributeInfo{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", values=" + Arrays.toString(values) +
                '}';
    }
}
