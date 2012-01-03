package com.saltaku.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.PARAMETER)
public @interface APIParam {
	public String description() default "";
	public String defaultValue() default "";
	public boolean isMandatory() default true;
}
