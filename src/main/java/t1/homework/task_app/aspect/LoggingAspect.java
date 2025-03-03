package t1.homework.task_app.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * Аспект логгирования.
 */
@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("@annotation(t1.homework.task_app.annotation.LogBefore)")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("Происходит вызов метода {} с параметрами {}.",
                joinPoint.getSignature().getName(), joinPoint.getArgs());
    }

    @AfterThrowing(pointcut = "@annotation(t1.homework.task_app.annotation.LogAfterThrowing)", throwing = "error")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
        logger.error("Метод {} выбросил исключение: {}",
                joinPoint.getSignature().getName(), error.getMessage());
    }

    @AfterReturning(pointcut = "@annotation(t1.homework.task_app.annotation.LogAfterReturning)", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        logger.info("Успешное завершение работы метода {}, результат: {}",
                joinPoint.getSignature().getName(), result);
    }

    @Around("@annotation(t1.homework.task_app.annotation.LogAround)")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        logger.info("Происходит вызов метода {} с параметрами {}",
                methodName, joinPoint.getArgs());
        try {
            Object result = joinPoint.proceed();
            stopWatch.stop();
            logger.info("Успешное завершение метода {} с результатом {} за {} секунд.",
                    methodName, result, stopWatch.getTotalTimeSeconds());
            return result;
        } catch (Exception e) {
            logger.error("Метод {} выбросил исключение: {}",
                    joinPoint.getSignature().getName(), e.getMessage());
            throw e;
        }
    }
}
