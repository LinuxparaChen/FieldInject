package com.chenzhanyang;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Description: 字段注入注解
 * Auther: chen_zhanyang.
 * Email: zhanyang.chen@gmail.com
 * Data: 2017/5/19.
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface FieldBind {
    String value() default "";
}
