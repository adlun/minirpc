package com.adlun.minirpc.provider;


import com.adlun.minirpc.core.common.RpcProperties;
import com.adlun.minirpc.registry.RegistryType;
import com.adlun.minirpc.registry.factory.RegistryServiceFactory;
import com.adlun.minirpc.registry.service.IRegistryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
@EnableConfigurationProperties(RpcProperties.class)
@Slf4j
public class RpcProviderAutoConfiguration {

    @Resource
    private RpcProperties rpcProperties;

    @Bean
    public RpcProvider rpcProvider() {
        String registryTypeStr = rpcProperties.getRegistryType();
        RegistryType registryType;
        if (StringUtils.isBlank(registryTypeStr)) {
            log.warn("registry type is null, use zookeeper as default");
            registryType = RegistryType.ZOOKEEPER;
        } else {
            try {
                registryType = RegistryType.valueOf(registryTypeStr);
            } catch (Exception e) {
                registryType = RegistryType.ZOOKEEPER;
                log.warn("unrecognized registry type {}, use zookeeper as default");
            }

        }
        IRegistryService registryService = RegistryServiceFactory.getInstance(registryType, rpcProperties.getRegistryAddr());
        return new RpcProvider(rpcProperties.getServicePort(), registryService);
    }

}
