package com.company.shared.annotations;

import java.lang.annotation.*;

/**
 * Annotation used to mark command methods.
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandAnnotation {
    public String name() default "";
    public String usage() default "";
    public int param() default 0;
}
