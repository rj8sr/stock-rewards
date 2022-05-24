package com.way.stock.rewards.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.util.StopWatch.TaskInfo;

/**
 * Logging Aspect Log method entry and exit Log method parameters and return
 * argument. Execution time
 */
@Component
@Aspect
@Order(1000) // This is to make sure initialize late ,otherwise it will cause issues with
// @PostConstruct annotations.
public class LoggingAspect {

	private static final long THRESOLDEXECUTIONTIME = 1000;

	Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

	@Pointcut("execution(* com.way.stock.rewards.*.*.*(..))")
	protected void allMethod() {
		// allMethod()
	}

	/**
	 * Log execution time Log return parameters.
	 *
	 * @param proceedingJoinPoint Proceeding Join Point.
	 * @return object
	 * @throws Throwable using superclass of all errors and exceptions
	 */
	@Around("allMethod()")
	public Object logMethodAround(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

		return logExecutionTime(proceedingJoinPoint);

	}

	/**
	 * Log the method params being send back.
	 *
	 * @param proceedingJoinPoint Proceeding Join Point.
	 * @return Object the method is throwing.
	 * @throws Throwable Exception the method throws.
	 */
	private Object logMethodOutputParams(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

		return proceedingJoinPoint.proceed();

	}

	/**
	 * Log the execution time. Called if trace is enabled.
	 *
	 * @param proceedingJoinPoint Proceeding Join Point.
	 * @return Object returned by the method.
	 * @throws Throwable Exception thrown by method.
	 */
	private Object logExecutionTime(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start(proceedingJoinPoint.toShortString());
		boolean isExceptionThrown = false;
		try {
			return logMethodOutputParams(proceedingJoinPoint);
		} catch (Throwable ex) {
			isExceptionThrown = true;
			// log execption here.
			throw ex;
		} finally {
			stopWatch.stop();
			TaskInfo taskInfo = stopWatch.getLastTaskInfo();

			// Log the method's profiling result
			String profileMessage = "Method " + taskInfo.getTaskName() + ": Execution Time: " + taskInfo.getTimeMillis()
					+ " ms" + (isExceptionThrown ? " (thrown Exception)" : "");
			if (taskInfo.getTimeMillis() > LoggingAspect.THRESOLDEXECUTIONTIME) {
				logger.warn(profileMessage);
			} else {
				logger.debug(profileMessage);
			}
		}

	}

}
