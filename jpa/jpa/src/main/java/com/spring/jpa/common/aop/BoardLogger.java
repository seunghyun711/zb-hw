package com.spring.jpa.common.aop;

import com.spring.jpa.logs.service.LogService;
import com.spring.jpa.user.entity.User;
import com.spring.jpa.user.model.UserLogin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class BoardLogger {
    private final LogService logService;

    @Around("execution(* com.spring.jpa..*.*Controller.detail(..))")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("# LoginLogger");
        log.info("Controller detail 호출 전");

        Object result = joinPoint.proceed();


        if (joinPoint.getSignature().getDeclaringTypeName().contains("ApiBoardController")
                && "detail".equals(joinPoint.getSignature().getName())) {
            StringBuilder sb = new StringBuilder();

            Object[] args = joinPoint.getArgs();
            for (Object x : args) {
                sb.append(x.toString());
            }
            sb.append("결과 : ");
            sb.append(result.toString());

            logService.add(sb.toString());
            log.info(sb.toString());
        }

        log.info("Controller detail 호출 후");

        return result;
    }
}
