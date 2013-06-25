package org.satokit.sample.spec

import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import spock.lang.Ignore
import spock.lang.Unroll

@RunWith(Enclosed)
class OrSpecificationSpec {
    static class InstantiationSpec extends spock.lang.Specification {
        static final spec = new TautologySpecification<DummyInput>()

        def "2つの仕様オブジェクトを与えてインスタンスを生成する"() {
            expect:
            new OrSpecification<DummyInput>(spec, spec) != null
        }

        def "いずれか片方でもnullの場合IllegalArgumentExceptionをスローする"() {
            when:
            new OrSpecification<DummyInput>(spec1, spec2)

            then:
            def e = thrown(IllegalArgumentException)
            e.message == message

            where:
            spec1 | spec2 || message
            null  | spec  || "left must not be null."
            spec  | null  || "right must not be null."
            null  | null  || "left must not be null."
        }
    }

    static class BasicSpec extends spock.lang.Specification {
        static final tautology = new TautologySpecification<DummyInput>()
        static final contradiction = new ContradictionSpecification<DummyInput>()

        static final input = new DummyInput()

        @Unroll
        def "#spec1 or #spec2 = #expect"() {
            given:
            def sut = new OrSpecification<DummyInput>(spec1, spec2)

            expect:
            sut.isSatisfied(input) == expect

            where:
            spec1         | spec2         || expect
            contradiction | contradiction || false
            contradiction | tautology     || true
            tautology     | contradiction || true
            tautology     | tautology     || true
        }

        def "isSatisfiedメソッドにnullを与えるとIllegalArgumentExceptionをスローする"() {
            given:
            def sut = new OrSpecification<DummyInput>(tautology, tautology)

            when:
            sut.isSatisfied(null)

            then:
            def e = thrown(IllegalArgumentException)
            e.message == "target must not be null."
        }

        @Ignore("Mockだと||の短絡評価が効かない")
        def "左辺が仕様を満たす場合、短絡評価される"() {
            given:
            def left = Mock(Specification)
            def right = Mock(Specification)

            left.isSatisfied(_) >> true
            right.isSatisfied(_) >> true

            and:
            def sut = new OrSpecification<DummyInput>(left, right)

            when:
            sut.isSatisfied(input)

            then:
            1 * left.isSatisfied(input)
            0 * right.isSatisfied(input)
        }

        def "OrSpecificationはネストできる"() {
            given:
            def spec1 = new OrSpecification<DummyInput>(spec1left, spec1right)
            def spec2 = new OrSpecification<DummyInput>(spec2left, spec2right)

            and:
            def sut = new OrSpecification<DummyInput>(spec1, spec2)

            expect:
            sut.isSatisfied(input) == expect

            where:
            spec1left     | spec1right    || spec2left | spec2right        || expect
            tautology     | contradiction || contradiction | contradiction || true
            contradiction | contradiction || contradiction | tautology     || true
            contradiction | contradiction || contradiction | contradiction || false
        }
    }
}
