package io.kyligence.notebook.console.support;

import io.kyligence.notebook.console.exception.AccessDeniedException;
import io.kyligence.notebook.console.exception.ErrorCodeEnum;
import io.kyligence.notebook.console.util.WebUtils;

import io.kyligence.saas.iam.pojo.AuthInfo;
import io.kyligence.saas.iam.sdk.context.AuthContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PermissionChecker {

    @Around("@annotation(io.kyligence.notebook.console.support.Permission)")
    public Object check(ProceedingJoinPoint joinPoint) throws Throwable {
        AuthInfo userInfo = AuthContextHolder.getContext();
        if (userInfo == null) {
            throw new AccessDeniedException(ErrorCodeEnum.ACCESS_DENIED);
        }
        WebUtils.setCurrentLoginUser(userInfo.getUsername());
        return joinPoint.proceed();
        }
    }
