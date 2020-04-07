package com.lin.missyou.sample;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(LOLConfigurationSelector.class)
public @interface EnableLOLConfiguration {
}
