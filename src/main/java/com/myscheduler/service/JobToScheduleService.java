package com.myscheduler.service;

import com.myscheduler.model.JobToSchedule;
import org.springframework.stereotype.Service;

/**
 * Created by Pierre on 2016-04-11.
 */
@Service
public interface JobToScheduleService {
    JobToSchedule getJobToScheduleById(Long id);

    Iterable<JobToSchedule> getAllJobToSchedule();

    Iterable<JobToSchedule> getJobToScheduleMonday();

    Iterable<JobToSchedule> getJobToScheduleTuesday();

    Iterable<JobToSchedule> getJobToScheduleWednesday();

    Iterable<JobToSchedule> getJobToScheduleThursday();

    Iterable<JobToSchedule> getJobToScheduleFriday();

    Iterable<JobToSchedule> getJobToScheduleSaturday();

    Iterable<JobToSchedule> getJobToScheduleSunday();

    JobToSchedule saveJobToSchedule(JobToSchedule jobToSchedule);

    void deleteJobToScheduleById(Long id);
}
