package com.eservice.eumowy.annotation

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Omit {

    /** Ommit cmd --> process */
    boolean inSave() default true;

    /** Ommit process --> cmd*/
    boolean inPopulate() default false;
}