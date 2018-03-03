package vfdt;

import vfdt.data.Attribute;
import vfdt.data.Instance;

public class Main {
    public static void main(String[] args) {
        Attribute<Integer> a0 = new Attribute<>(5);
        Attribute<String> a1 = new Attribute<>("c1");
        Attribute[] atts = {a0, a1};
        Instance i = new Instance(atts);
        i.print();
    }
}
