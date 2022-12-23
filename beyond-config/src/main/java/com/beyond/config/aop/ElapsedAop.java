package com.beyond.config.aop;

import com.beyond.result.IResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.util.StopWatch;

/**
 * @author beyond
 * @since 2022/12/2
 */
@Aspect
public class ElapsedAop {

    @Pointcut("execution(* com.beyond..*.*Controller.*(..))")
    public void pointcut() {

    }

    @Around(value = "pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final Object object = joinPoint.proceed();
        stopWatch.stop();
        if (object instanceof IResult) {
            final IResult<?> result = (IResult<?>) object;
            result.setElapsed(stopWatch.getTotalTimeMillis());
        }
        return object;
    }
}
