package com.adlun.minirpc.registry.service.impl;

import com.adlun.minirpc.core.common.ServiceMeta;
import com.adlun.minirpc.registry.service.ILoadBalancer;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.curator.x.discovery.ServiceInstance;

import java.util.Collection;

public class RandomLoadBalancer implements ILoadBalancer {
    @Override
    public ServiceInstance<ServiceMeta> choose(Collection<ServiceInstance<ServiceMeta>> serviceInstances) {
        if (CollectionUtils.isEmpty(serviceInstances)) {
            return null;
        }
        return serviceInstances.stream().findFirst().orElse(null);
    }
}
