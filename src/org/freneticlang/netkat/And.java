package org.freneticlang.netkat;

public class And implements Predicate {
    private Predicate left, right;

    public And(Predicate left, Predicate right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return ("(" + left + " and " + right + ")");
    }
}
