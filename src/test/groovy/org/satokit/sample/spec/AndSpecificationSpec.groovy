package org.satokit.sample.spec

import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import spock.lang.Unroll

@RunWith(Enclosed)
class AndSpecificationSpec {
    static class InstantiationSpec extends spock.lang.Specification {
        static final spec = new TautologySpecification<DummyInput>()

        def "2つの仕様オブジェクトを与えてインスタンスを生成する"() {
            given:
            def sut
            def spec1 = spec
            def spec2 = spec

            when:
            sut = new AndSpecification<DummyInput>(spec1, spec2)

            then:
            sut != null
        }

        def "いずれか片方でもnullの場合にIllegalArgumentExceptionをスロー"() {
            when:
            new AndSpecification<DummyInput>(spec1, spec2)

            then:
            def e = thrown(IllegalArgumentException)
            e.message == message

            where:
            spec1 | spec2 | message
            null  | spec  | "left must not be null."
            spec  | null  | "right must not be null."
            null  | null  | "left must not be null."
        }
    }

    static class BasicSpec extends spock.lang.Specification {
        static final tautology = new TautologySpecification<DummyInput>()
        static final contradiction = new ContradictionSpecification<DummyInput>()

        static final input = new DummyInput()

        @Unroll
        def "#spec1 and #spec2 = #expect"() {
            given:
            def sut = new AndSpecification<DummyInput>(spec1, spec2)

            expect:
            sut.isSatisfied(input) == expect

            where:
            spec1         | spec2         || expect
            contradiction | contradiction || false
            contradiction | tautology     || false
            tautology     | contradiction || false
            tautology     | tautology     || true
        }

        def "isSatisfiedメソッドにnullを与えるとIllegalArgumentExceptionをスローする"() {
            given:
            def sut = new AndSpecification(tautology, tautology)

            when:
            sut.isSatisfied(null)

            then:
            def e = thrown(IllegalArgumentException)
            e.message == "target must not be null."
        }

        def "左辺が仕様を満たさない場合、短絡評価される"() {
            given:
            def left = Mock(Specification)
            def right = Mock(Specification)

            left.isSatisfied(_) >> false
            right.isSatisfied(_) >> true

            and:
            def sut = new AndSpecification<DummyInput>(left, right)

            when:
            sut.isSatisfied(input)

            then:
            1 * left.isSatisfied(input)
            0 * right.isSatisfied(input)
        }

        def "AndSpecificationはネストできる"() {
            given:
            def spec1 = new AndSpecification<DummyInput>(spec1left, spec1right)
            def spec2 = new AndSpecification<DummyInput>(spec2left, spec2right)

            and:
            def sut = new AndSpecification<DummyInput>(spec1, spec2)

            expect:
            sut.isSatisfied(input) == expect

            where:
            spec1left     | spec1right || spec2left | spec2right    || expect
            tautology     | tautology  || tautology | contradiction || false
            contradiction | tautology  || tautology | tautology     || false
            tautology     | tautology  || tautology | tautology     || true
        }
    }
}
