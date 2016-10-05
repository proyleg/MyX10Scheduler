package com.myscheduler.dao;

import org.quartz.JobKey;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by Pierre on 2016-04-13.
 */
public class JobList {
    private JobKey jobKey;
    private String jobName;
    private String jobGroup;
    private Date nextFireTime;
    private String state;

    public JobKey getJobKey() {
        return jobKey;
    }

    public void setJobKey(JobKey jobKey) {
        this.jobKey = jobKey;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public Date getNextFireTime() {
        return nextFireTime;
    }

    public void setNextFireTime(Date nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public static final Comparator<JobList> JobListDateComparator = new Comparator<JobList>() {

        public int compare(JobList j1, JobList j2) {
            Date JobListDate1 = j1.getNextFireTime();
            Date JobListDate2 = j2.getNextFireTime();

            if (JobListDate1 == null || JobListDate2 == null)
                return 0;
            //ascending order
            return JobListDate1.compareTo(JobListDate2);

            //descending order
            //return JobListDate2.compareTo(JobListDate1);
        }};}
