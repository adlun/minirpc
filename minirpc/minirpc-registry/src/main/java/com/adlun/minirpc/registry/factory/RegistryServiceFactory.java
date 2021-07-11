package com.adlun.minirpc.registry.factory;

import com.adlun.minirpc.registry.RegistryType;
import com.adlun.minirpc.registry.service.IRegistryService;
import com.adlun.minirpc.registry.service.impl.ZookeeperRegistryService;

public class RegistryServiceFactory {

    public static IRegistryService getInstance(RegistryType registryType, String registryAddr) {
        switch (registryType) {
            case ZOOKEEPER:
                return new ZookeeperRegistryService(registryAddr);
        }
        return null;
    }

}
