package com.adlun.minirpc.registry.service.impl;

import com.adlun.minirpc.core.common.ServiceMeta;
import com.adlun.minirpc.registry.service.IRegistryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.util.Collection;

@Slf4j
public class ZookeeperRegistryService implements IRegistryService {
    private static final int BASE_SLEEP_TIME_MS = 1_000;
    private static final int MAX_RETRIES = 3;
    private static final String ZK_BASE_PATH = "/minirpc";
    private final ServiceDiscovery<ServiceMeta> serviceDiscovery;

    public ZookeeperRegistryService(String registryAddr) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(MAX_RETRIES, BASE_SLEEP_TIME_MS);
        CuratorFramework client = CuratorFrameworkFactory.newClient(registryAddr, retryPolicy);
        client.start();
        JsonInstanceSerializer<ServiceMeta> serializer = new JsonInstanceSerializer<>(ServiceMeta.class);
        this.serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceMeta.class)
                .client(client)
                .serializer(serializer)
                .basePath(ZK_BASE_PATH)
                .build();
    }


    @Override
    public void register(ServiceMeta serviceMeta) throws Exception {
        ServiceInstance<ServiceMeta> serviceMetaServiceInstance = ServiceInstance.<ServiceMeta>builder()
                .name(StringUtils.joinWith("#", serviceMeta.getServiceName(), serviceMeta.getServiceVersion(), serviceMeta.getGroup()))
                .address(serviceMeta.getIpAddr())
                .port(serviceMeta.getPort())
                .payload(serviceMeta)
                .build();
        serviceDiscovery.registerService(serviceMetaServiceInstance);
    }

    @Override
    public void unregister(ServiceMeta serviceMeta) throws Exception {
        ServiceInstance<ServiceMeta> serviceMetaServiceInstance = ServiceInstance.<ServiceMeta>builder()
                .name(StringUtils.joinWith("#", serviceMeta.getServiceName(), serviceMeta.getServiceVersion(), serviceMeta.getGroup()))
                .address(serviceMeta.getIpAddr())
                .port(serviceMeta.getPort())
                .payload(serviceMeta)
                .build();
        serviceDiscovery.unregisterService(serviceMetaServiceInstance);
    }

    @Override
    public ServiceMeta discovery(String serviceName, String serviceVersion, String serviceGroup) throws Exception {
        Collection<ServiceInstance<ServiceMeta>> serviceInstances = serviceDiscovery.queryForInstances(StringUtils.joinWith("#", serviceName, serviceVersion, serviceGroup));
        ServiceInstance<ServiceMeta> metaServiceInstance = new RandomLoadBalancer().choose(serviceInstances);
        return metaServiceInstance.getPayload();
    }

    @Override
    public void destry() throws Exception {
        serviceDiscovery.close();
    }

}
