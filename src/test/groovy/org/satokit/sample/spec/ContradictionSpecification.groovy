package org.satokit.sample.spec

/**
 * 恒偽.
 */
class ContradictionSpecification<T> implements Specification<T> {
    @Override
    boolean isSatisfied(T target) {
        return false
    }

    @Override
    String toString() {
        return "false"
    }
}
