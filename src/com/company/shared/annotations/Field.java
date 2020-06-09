package com.company.shared.annotations;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Field {
    String type() default "";
    double max_value() default 1e18;
    double min_value() default -1e18;
    boolean ignore() default false;
}
