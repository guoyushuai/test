package com.gys.aop;

import org.aspectj.lang.ProceedingJoinPoint;

public class MyAspect {

    public void beforeAdvice() {
        System.out.println("前置通知");
    }

    public void afterAdvice() {
        System.out.println("后置通知");
    }

    public void exceptionAdvice() {
        System.out.println("异常通知");
    }

    public void finallyAdvice() {
        System.out.println("最终通知");
    }


    public void aroundAdvice(ProceedingJoinPoint joinPoint) {

        try {
            System.out.println("前置通知");
            Object result = joinPoint.proceed();//目标对象方法的执行
            System.out.println("后置通知");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            System.out.println("异常通知");
        } finally {
            System.out.println("最终通知");
        }

    }


}
