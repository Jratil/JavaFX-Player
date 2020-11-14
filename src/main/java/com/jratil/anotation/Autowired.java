package com.jratil.anotation;

import java.lang.annotation.*;

/**
 * @author wenjunjun9
 * @created 2020/9/6 8:49:03
 * @description
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Autowired {
    String value() default "";
}
