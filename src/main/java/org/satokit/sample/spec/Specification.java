/*
 * Copyright (c) 2013. satokitty
 *
 * Licensed under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package org.satokit.sample.spec;

/**
 * 仕様を表現するインターフェイス.
 * @param <T> 仕様検証対象のオブジェクト
 */
public interface Specification<T> {
    /**
     * 対象のオブジェクトがこのオブジェクトが表現する仕様を満たしているか検証する.
     *
     * @param target 仕様検証対象のオブジェクト
     * @return {@code target}が仕様を満たしている場合{@code true}, 満たしていない場合{@code false}.
     */
    boolean isSatisfied(T target);
}
