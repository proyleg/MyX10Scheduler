package com.myscheduler.service;

import com.myscheduler.model.JobToSchedule;
import com.myscheduler.model.StatusType;
import com.myscheduler.repository.JobToScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by Pierre on 2016-04-11.
 */
@Service
public class JobToScheduleServiceImpl implements JobToScheduleService {
    private final JobToScheduleRepository jobToScheduleRepository;

    @Autowired
    public JobToScheduleServiceImpl(JobToScheduleRepository jobToScheduleRepository) {
        this.jobToScheduleRepository = jobToScheduleRepository;
    }

    @Override
    public Optional<JobToSchedule> getJobToScheduleById(Long id) {
        return jobToScheduleRepository.findById(id);
    }

    @Override
    public Iterable<JobToSchedule> getAllJobToSchedule() {
        return jobToScheduleRepository.findAll(Sort.by("name"));
    }

    @Override
    public Iterable<JobToSchedule> getJobToScheduleMonday() {
        return jobToScheduleRepository.findByMondayAndStatus(true, StatusType.Active);
    }

    @Override
    public Iterable<JobToSchedule> getJobToScheduleTuesday() {
        return jobToScheduleRepository.findByTuesdayAndStatus(true, StatusType.Active);
    }

    @Override
    public Iterable<JobToSchedule> getJobToScheduleWednesday() {
        return jobToScheduleRepository.findByWednesdayAndStatus(true, StatusType.Active);
    }

    @Override
    public Iterable<JobToSchedule> getJobToScheduleThursday() {
        return jobToScheduleRepository.findByThursdayAndStatus(true, StatusType.Active);
    }

    @Override
    public Iterable<JobToSchedule> getJobToScheduleFriday() {
        return jobToScheduleRepository.findByFridayAndStatus(true, StatusType.Active);
    }

    @Override
    public Iterable<JobToSchedule> getJobToScheduleSaturday() {
        return jobToScheduleRepository.findBySaturdayAndStatus(true, StatusType.Active);
    }

    @Override
    public Iterable<JobToSchedule> getJobToScheduleSunday() {
        return jobToScheduleRepository.findBySundayAndStatus(true, StatusType.Active);
    }

    @Override
    public JobToSchedule saveJobToSchedule(JobToSchedule jobToSchedule) {
        return jobToScheduleRepository.save(jobToSchedule);
    }

    @Override
    public void deleteJobToScheduleById(Long id) {
        jobToScheduleRepository.deleteById(id);
    }
}
