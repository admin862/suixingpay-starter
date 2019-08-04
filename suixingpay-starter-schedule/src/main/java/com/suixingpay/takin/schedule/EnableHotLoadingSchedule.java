package com.suixingpay.takin.schedule;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

/**
 * 
 * @author renjinhao
 *
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
@Import(HotLoadingScheduleMarkerConfiguration.class)
public @interface EnableHotLoadingSchedule {

}
