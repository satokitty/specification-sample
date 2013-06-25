package org.satokit.sample.spec;

import org.apache.commons.lang.Validate;

/**
 * 仕様オブジェクトを複数組み合わせるビルダクラス.
 * @param <T> 仕様検証対象の型
 */
public class SpecificationBuilder<T> {
    /** 現在保持している仕様オブジェクト */
    private Specification<T> baseSpec;

    /**
     * 仕様オブジェクトを与えてインスタンスを生成する.
     * @param baseSpec ベースになる仕様オブジェクト
     * @throws IllegalArgumentException {@code spec == null}
     */
    private SpecificationBuilder(Specification<T> baseSpec) {
        Validate.notNull(baseSpec, "baseSpec must not be null.");
        this.baseSpec = baseSpec;
    }

    /**
     * 初期状態で与えられた仕様と{@code spec}の論理積を取る仕様オブジェクトを持つビルダを返却する.
     *
     * @param spec 論理積を取る仕様オブジェクト
     * @return {@code baseSpec && spec}を初期状態とするビルダインスタンス
     * @throws IllegalArgumentException {@code spec == null}
     */
    public SpecificationBuilder<T> and(Specification<T> spec) {
        Validate.notNull(spec, "spec must not be null.");
        return new SpecificationBuilder<T>(new AndSpecification<T>(baseSpec, spec));
    }

    /**
     * 初期状態で与えられた仕様と{@code spec}の論理和を取る仕様オブジェクトを持つビルダを返却する.
     * @param spec 論理和を取る仕様オブジェクト
     * @return {@code baseSpec || spec}を初期状態とするビルダインスタンス
     * @throws IllegalArgumentException {@code spec == null}
     */
    public SpecificationBuilder<T> or(Specification<T> spec) {
        Validate.notNull(spec, "spec must not be null.");
        return new SpecificationBuilder<T>(new OrSpecification<T>(baseSpec, spec));
    }

    /**
     * コンストラクタで与えられた仕様オブジェクトを返却する.
     * @return コンストラクタで与えられた仕様オブジェクト.
     */
    public Specification<T> build() {
        return baseSpec;
    }
}
