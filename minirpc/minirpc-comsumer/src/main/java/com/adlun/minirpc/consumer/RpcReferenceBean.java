package com.adlun.minirpc.consumer;

import com.adlun.minirpc.registry.RegistryType;
import com.adlun.minirpc.registry.factory.RegistryServiceFactory;
import com.adlun.minirpc.registry.service.IRegistryService;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

public class RpcReferenceBean implements FactoryBean<Object> {
    @Setter
    private Class<?> interfaceClass;
    @Setter
    private String serviceVersion;
    @Setter
    private String registryType;
    @Setter
    private String registryAddr;
    @Setter
    private String serviceGroup;
    @Setter
    private long timeout;

    private Object object;

    @Override
    public Object getObject() throws Exception {
        return object;
    }

    @Override
    public Class<?> getObjectType() {
        return interfaceClass;
    }

    public void init() throws Exception {
        IRegistryService registryService = RegistryServiceFactory.getInstance(RegistryType.valueOf(this.registryType), this.registryAddr);
        this.object = Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new RpcInvokerProxy(serviceGroup, serviceVersion, timeout, registryService));
    }
}
