package org.satokit.sample.spec

/**
 * 恒真.
 */
class TautologySpecification<T> implements Specification<T> {

    @Override
    boolean isSatisfied(T target) {
        return true
    }
}

