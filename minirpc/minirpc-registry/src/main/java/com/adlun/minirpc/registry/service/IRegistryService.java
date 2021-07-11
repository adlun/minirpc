package com.adlun.minirpc.registry.service;

import com.adlun.minirpc.core.common.ServiceMeta;

public interface IRegistryService {

    void register(ServiceMeta serviceMeta) throws Exception;

    void unregister(ServiceMeta serviceMeta) throws Exception;

    ServiceMeta discovery(String serviceName, String serviceGroup, String serviceVersion) throws Exception;

    void destry() throws Exception;

}
