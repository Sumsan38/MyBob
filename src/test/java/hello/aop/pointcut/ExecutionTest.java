package hello.aop.pointcut;

import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ExecutionTest {

    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    Method helloMethod;

    @BeforeEach
    public void init() throws NoSuchMethodException {
        helloMethod = MemberServiceImpl.class.getMethod("hello", String.class);
    }

    @Test
    void printMethod(){
        // public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        log.info("helloMethod={}", helloMethod);
    }

    /**
     * 이 아래부터 execution으로 시작하는 포인트컷 표현식은 이 메서드 정보를 매칭해서 포인트컷 대상을 찾아낸다.
     * execution(modifiers-pattern?
     *          ret-type-pattern
     *          declaring-type-pattern?name-pattern(param-pattern)
     *          throws-pattern?)
     * execution(접근제어자? 반환타입 선언타입?메서드이름(파라미터) 예외?)
     *      메소드 실행 조인 포인트를 매칭한다.
     *      ?는 생략할 수 있다.
     *      * 같은 패턴을 지정할 수 있다
     */

    /*
    1. 가장 정확한 포인트 컷
    MemberServiceImpl.hello(String) 메서드와 가장 정확하게 모든 내용이 매칭되는 표현식
     */
    @Test
    void exactMatch(){
        //public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        pointcut.setExpression("execution(public String hello.aop.member.MemberServiceImpl.hello(String))");
        //접근제어자?: public
        //반환타입: String
        //선언타입?: hello.aop.member.MemberServiceImpl
        //메서드이름: hello
        //파라미터: (String)
        //예외?: 생략
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /*
    2. 가장 많이 생략한 포인트컷
     */
    @Test
    void allMatch(){
        pointcut.setExpression("execution(* *(..))");
        //접근제어자?: 생략
        //반환타입: *
        //선언타입?: 생략
        //메서드이름: *
        //파라미터: (..)
        //예외?: 없음
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /*
    3. 메서드 이름 매칭 관련 포인트컷
     */
    @Test
    void nameMatch(){
        pointcut.setExpression("execution(* hello(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
    @Test
    void nameMatchStar1(){
        pointcut.setExpression("execution(* hel*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
    @Test
    void nameMatcherFalse(){
        pointcut.setExpression("execution(* nono(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    /*
    4. 패키지 매칭 관련 포인트컷
     */
    @Test
    void packageExactMatch1(){
        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.hello(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
    @Test
    void packageExactMatch2(){
        pointcut.setExpression("execution(* hello.aop.member.*.*(..))");
        // hello.aop.member.*(1).*(2)
        // (1): 타입
        // (2): 메서드 이름
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
    @Test
    void packageExactMatchFalse(){
        pointcut.setExpression("execution(* hello.aop.*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }
    @Test
    void packageMatchSubPackage1(){
        pointcut.setExpression("execution(* hello.aop.member..*.*(..))");
        // 패키지에서 . , .. 의 차이를 이해해야 한다.
        // . : 정확하게 해당 위치의 패키지
        // .. : 해당 위치의 패키지와 그 하위 패키지도 포함
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
    @Test
    void packageMatchSubPackage2(){
        pointcut.setExpression("execution(* hello.aop..*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

}
