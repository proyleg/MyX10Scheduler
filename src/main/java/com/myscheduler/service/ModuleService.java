package com.myscheduler.service;

import com.myscheduler.model.Module;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by Pierre on 2016-04-27.
 */
@Service
public interface ModuleService {
    Iterable<Module> getAllModules();

    Optional<Module> getModuleById(Integer id);

    void updateModulesStatus(String status, String x10Address);

    void updateModulesStatusAllOff();

    void updateModulesStatusAllLightsOn();

}
