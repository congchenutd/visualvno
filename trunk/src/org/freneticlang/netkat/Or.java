package org.freneticlang.netkat;

public class Or implements Predicate {
    private Predicate left, right;

    public Or(Predicate left, Predicate right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return ("(" + left + " or " + right + ")");
    }
}
