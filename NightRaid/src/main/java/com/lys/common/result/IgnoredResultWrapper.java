package com.lys.common.result;


import java.lang.annotation.*;

/**
 * @author liao
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface IgnoredResultWrapper {
}
