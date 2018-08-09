package com.myscheduler.service;

import com.myscheduler.model.Module;
import com.myscheduler.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by Pierre on 2016-04-27.
 */
@Service
public class ModuleServiceImpl implements ModuleService {
    private ModuleRepository moduleRepository;

    @Autowired
    public void setModuleRepository(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    private JdbcTemplate jdbc;

    @Autowired
    public void getJdbcTemplate(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Iterable<Module> getAllModules() {
        return moduleRepository.findAll();
    }

    @Override
    public Optional<Module> getModuleById(Integer id) {
        return moduleRepository.findById(id);
    }

    @Override
    public void updateModulesStatus(String status, String x10Address) {
        jdbc.update("update modules set status=? where Concat(addresslet,addressnum)= ?", status, x10Address);
    }

    @Override
    public void updateModulesStatusAllOff() {
        jdbc.update("update modules set status='off'");
    }

    @Override
    public void updateModulesStatusAllLightsOn() {
        jdbc.update("update modules set status='on' where all_lights_on='yes'");

    }

}
