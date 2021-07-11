package com.adlun.minirpc.consumer.annotation;

import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Autowired
public @interface RpcReference {

    String registryType() default "ZOOKEEPER";
    String registryAddr() default "127.0.0.1:2181";
    String serviceVersion() default "1.0";
    String serviceGroup() default "default";
    long timeout() default 10_000;


}
