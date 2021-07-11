package com.adlun.minirpc.registry.service;

import com.adlun.minirpc.core.common.ServiceMeta;
import org.apache.curator.x.discovery.ServiceInstance;

import java.util.Collection;

public interface ILoadBalancer {

    ServiceInstance<ServiceMeta> choose(Collection<ServiceInstance<ServiceMeta>> serviceInstances);

}
