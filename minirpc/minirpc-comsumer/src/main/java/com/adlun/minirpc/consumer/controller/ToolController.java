package com.adlun.minirpc.consumer.controller;

import com.adlun.minirpc.consumer.annotation.RpcReference;
import com.adlun.minirpc.serviceinterface.IToolService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tool")
public class ToolController {

    @RpcReference
    private IToolService toolService;

    @GetMapping("/upper/{data}")
    public String toUpperCase(@PathVariable String data){
        return toolService.toUpperCase(data);
    }


}
