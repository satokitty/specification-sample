package org.satokit.sample.spec;

/**
 * 仕様オブジェクトを複数組み合わせるビルダクラス.
 * @param <T> 仕様検証対象の型
 */
public class SpecificationBuilder<T> {
    public SpecificationBuilder<T> and(Specification<T> spec) {
        return null;
    }

    public SpecificationBuilder<T> or(Specification<T> spec) {
        return null;
    }

    public Specification<T> build() {
        return null;
    }
}
