package com.myscheduler.repository;

import com.myscheduler.model.Module;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Pierre on 2016-04-27.
 */
@Repository
public interface ModuleRepository extends CrudRepository<Module, Integer>{
}
