package org.satokit.sample.spec;

/**
 * 2つの仕様オブジェクトの論理積を表現するオブジェクト.
 * @param <T> 仕様検証対象のオブジェクト型
 */
public class AndSpecification<T> implements Specification<T> {
    private Specification<T> left;
    private Specification<T> right;

    @Override
    public boolean isSatisfied(T target) {
        return false;  // TODO
    }
}
