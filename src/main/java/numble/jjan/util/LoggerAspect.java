package numble.jjan.util;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggerAspect {

    @Around("execution(* avocado.moim..controller.*Controller.*(..)) || execution(* avocado.moim..service.*Impl.*(..)) || execution(* avocado.moim..repository.*Mapper.*(..))")
    public Object logPrint(ProceedingJoinPoint joinPoint) throws Throwable {
        String type = "";
        String name = joinPoint.getSignature().getDeclaringTypeName();
        if (name.contains("Controller")) {
            type = "Controller \t:  ";
        } else if (name.contains("Service")) {
            type = "ServiceImpl \t:  ";
        } else if (name.contains("Mapper")) {
            type = "Mapper \t\t:  ";
        }
        log.info("{}{}.{}()", type, name, joinPoint.getSignature().getName());
        return joinPoint.proceed();
    }
}
