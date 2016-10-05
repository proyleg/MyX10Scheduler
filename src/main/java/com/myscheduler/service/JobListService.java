package com.myscheduler.service;

import com.myscheduler.dao.JobList;
import org.springframework.stereotype.Service;

/**
 * Created by Pierre on 2016-04-13.
 */
@Service
public interface JobListService {
    Iterable<JobList> getJobList(String state);
}
