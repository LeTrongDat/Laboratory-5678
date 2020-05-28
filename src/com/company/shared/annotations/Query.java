package com.company.shared.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(value = ElementType.METHOD)
public @interface Query {
    String value() default "";
    String name() default "";
}
