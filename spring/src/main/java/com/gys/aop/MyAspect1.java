package com.gys.aop;

import org.aspectj.lang.ProceedingJoinPoint;

public class MyAspect1 {

    public void beforeAdvice() {
        System.out.println("前置通知");
    }

    public void afterAdvice(Object result) {
        System.out.println("后置通知:" + result);
    }

    public void exceptionAdvice(Exception ex) {
        System.out.println("异常通知:" + ex.getMessage());
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
