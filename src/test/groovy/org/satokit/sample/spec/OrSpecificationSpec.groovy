package org.satokit.sample.spec

import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith

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
}
