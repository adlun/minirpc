package com.adlun.minirpc.provider.service.impl;

import com.adlun.minirpc.provider.annotation.RpcService;
import com.adlun.minirpc.serviceinterface.IToolService;
import org.apache.commons.lang3.StringUtils;

@RpcService(serviceInterface = IToolService.class)
public class ToolServiceImpl implements IToolService {
    @Override
    public String toUpperCase(String data) {
        return StringUtils.upperCase(data);
    }

    @Override
    public String toLowerCase(String data) {
        return StringUtils.lowerCase(data);
    }
}
