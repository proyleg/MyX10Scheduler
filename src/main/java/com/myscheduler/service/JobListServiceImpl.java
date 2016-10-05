package com.myscheduler.service;

import com.myscheduler.dao.JobList;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.myscheduler.dao.JobList.JobListDateComparator;

/**
 * Created by Pierre on 2016-04-13.
 */
@Service
public class JobListServiceImpl implements JobListService {
    private SchedulerFactoryBean scheduler;

    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    public void setScheduler(SchedulerFactoryBean scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public Iterable<JobList> getJobList(String state) {
        List<JobList> jobLists = new ArrayList<>();

        try {
            for (String groupName : scheduler.getScheduler().getJobGroupNames()) {
                for (JobKey jobKey : scheduler.getScheduler().getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                    JobList jobList = new JobList();
                    String jobName = jobKey.getName();
                    String jobGroup = jobKey.getGroup();
                    //get job's trigger
                    @SuppressWarnings("unchecked")
                    List<Trigger> triggers = (List<Trigger>) scheduler.getScheduler().getTriggersOfJob(jobKey);
                    if (triggers.size()<1) continue;
                    String triggerState = scheduler.getScheduler().getTriggerState(triggers.get(0).getKey()).toString();
                    if (!(state.equals("ALL") || triggerState.equals(state))) continue;
                    jobList.setState(triggerState);
                    Date nextFireTime = triggers.get(0).getNextFireTime();
                    jobList.setJobKey(jobKey);
                    jobList.setJobName(jobName);
                    jobList.setJobGroup(jobGroup);
                    jobList.setNextFireTime(nextFireTime);
                    jobLists.add(jobList);
                }
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        Collections.sort(jobLists,JobListDateComparator);
        return jobLists;
    }
}
