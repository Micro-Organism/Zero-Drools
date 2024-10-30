package com.zero.drools.controller;

import com.zero.drools.domain.entity.SystemUserEntity;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/drools")
public class DroolsController {

    KieSession kieSession;

    @Autowired
    public DroolsController(KieSession kieSession) {
        this.kieSession = kieSession;
    }

    @PostMapping("/rule")
    public SystemUserEntity processRules(@RequestBody SystemUserEntity systemUserEntity) {
        kieSession.insert(systemUserEntity);
        kieSession.fireAllRules();
        return systemUserEntity;
    }

}
