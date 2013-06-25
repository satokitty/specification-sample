package org.satokit.sample.spec

class SpecificationBuilderSpec extends spock.lang.Specification {
    static final tautology = new TautologySpecification<DummyInput>()
    static final contradiction = new ContradictionSpecification<DummyInput>()
    static final input = new DummyInput()

    def "buildメソッドはコンストラクタに与えられたSpecificationオブジェクトを返却する"() {
        given:
        def sut = new SpecificationBuilder<DummyInput>(tautology)

        when:
        def actual = sut.build()

        then:
        actual == tautology
    }

    def "コンストラクタにnullを与えるとIllegalArgumentExceptionをスローする"() {
        when:
        new SpecificationBuilder<DummyInput>(null)

        then:
        def e = thrown(IllegalArgumentException)
        e.message == "baseSpec must not be null."
    }

    def "andメソッドは初期状態として与えられた仕様と引数の仕様の論理積を取るSpecificationオブジェクトを持つBuilderを返却する"() {
        given:
        def sut = new SpecificationBuilder<DummyInput>(initial)

        when:
        def actual = sut.and(param)

        then:
        def actualSpec = actual.build()
        actualSpec instanceof AndSpecification
        actualSpec.isSatisfied(input) == expect

        where:
        initial       | param         || expect
        tautology     | tautology     || true
        tautology     | contradiction || false
        contradiction | tautology     || false
        contradiction | contradiction || false
    }

    def "orメソッドは初期状態として与えられた仕様と引数の仕様の論理和を取るSpecificationオブジェクトを持つBuilderを返却する"() {
        given:
        def sut = new SpecificationBuilder<DummyInput>(initial)

        when:
        def actual = sut.or(param)

        then:
        def actualSpec = actual.build()
        actualSpec instanceof OrSpecification
        actualSpec.isSatisfied(input) == expect

        where:
        initial       | param         || expect
        tautology     | tautology     || true
        tautology     | contradiction || true
        contradiction | tautology     || true
        contradiction | contradiction || false
    }

    def "メソッドチェーンしてSpecificationオブジェクトを組み合わせる場合、完成した仕様オブジェクトは右辺から評価される"() {
        given:
        def sut = new SpecificationBuilder<DummyInput>(initial)

        when:
        def actual = sut.and(param1).or(param2).and(param3).build()

        then:
        actual.isSatisfied(input) == expect

        where:
        initial   | param1        | param2        | param3    || expect
        tautology | contradiction | tautology     | tautology || true
        tautology | contradiction | contradiction | tautology || false
    }

    def "andメソッドにnullを与えるとIllegalArgumentExceptionをスローする"() {
        given:
        def sut = new SpecificationBuilder<DummyInput>(tautology)

        when:
        sut.and(null)

        then:
        def e = thrown(IllegalArgumentException)
        e.message == "spec must not be null."
    }

    def "orメソッドにnullを与えるとIllegalArgumentExceptionをスローする"() {
        given:
        def sut = new SpecificationBuilder<DummyInput>(tautology)

        when:
        sut.or(null)

        then:
        def e = thrown(IllegalArgumentException)
        e.message == "spec must not be null."
    }
}
