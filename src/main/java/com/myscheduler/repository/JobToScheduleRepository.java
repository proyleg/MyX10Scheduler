package com.myscheduler.repository;

import com.myscheduler.model.JobToSchedule;
import com.myscheduler.model.StatusType;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Pierre on 2016-03-11.
 */
@Repository
public interface JobToScheduleRepository extends PagingAndSortingRepository<JobToSchedule,Long> {
    Iterable<JobToSchedule> findByMonday(Boolean monday);
    Iterable<JobToSchedule> findByMondayAndStatus(Boolean monday, StatusType status);
    Iterable<JobToSchedule> findByTuesday(Boolean tuesday);
    Iterable<JobToSchedule> findByTuesdayAndStatus(Boolean monday, StatusType status);
    Iterable<JobToSchedule> findByWednesday(Boolean wednesday);
    Iterable<JobToSchedule> findByWednesdayAndStatus(Boolean monday, StatusType status);
    Iterable<JobToSchedule> findByThursday(Boolean thursday);
    Iterable<JobToSchedule> findByThursdayAndStatus(Boolean monday, StatusType status);
    Iterable<JobToSchedule> findByFriday(Boolean friday);
    Iterable<JobToSchedule> findByFridayAndStatus(Boolean monday, StatusType status);
    Iterable<JobToSchedule> findBySaturday(Boolean saturday);
    Iterable<JobToSchedule> findBySaturdayAndStatus(Boolean monday, StatusType status);
    Iterable<JobToSchedule> findBySunday(Boolean sunday);
    Iterable<JobToSchedule> findBySundayAndStatus(Boolean monday, StatusType status);

}
