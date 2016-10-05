package com.myscheduler.job;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myscheduler.dao.JobList;
import com.myscheduler.model.JobToSchedule;
import com.myscheduler.service.JobListService;
import com.myscheduler.service.JobToScheduleService;
import com.myscheduler.service.ModuleService;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static java.time.LocalDate.now;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by Pierre on 2016-04-10.
 */
@Service("JobScheduler")
public class JobScheduler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private Date sunrise = null;
    private Date sunset = null;
    private Calendar askedSunriseSunset = null;
    private JobToScheduleService jobToScheduleService;
    @Value("${scheduler.security.offset:0}")
    int securityOffset;

    private SchedulerFactoryBean scheduler;
    private JobListService jobListService;
    private ModuleService moduleService;

    @Autowired
    public void setModuleService(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    @Autowired
    public void setJobToScheduleService(JobToScheduleService jobToScheduleService) {
        this.jobToScheduleService = jobToScheduleService;
    }

    @Autowired
    public void setJobListService(JobListService jobListService) {
        this.jobListService = jobListService;
    }

    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    public void setScheduler(SchedulerFactoryBean scheduler) {
        this.scheduler = scheduler;
    }

    public void scheduleX10Jobs() {
        logger.info("Executing JobScheduler scheduleX10Jobs");
        Calendar nowTime = Calendar.getInstance();
        if (askedSunriseSunset == null || getTimeDiff(nowTime.getTime(), askedSunriseSunset.getTime()) > 1)
            getSunriseSunset();

        Random randomObj = new Random();
        //noinspection ConstantConditions
        for (JobToSchedule jobToSchedule : getJobToSchedule()) {
            Calendar startTime = Calendar.getInstance();
            switch (jobToSchedule.getScheduleType()) {
                case Fix:
                    startTime.setTime(jobToSchedule.getScheduleTime());
                    break;
                case Sunrise:
                    if (sunrise == null) {
                        startTime.setTime(jobToSchedule.getScheduleTime());
                        break;
                    }
                    startTime.setTime(sunrise);
                    break;
                case Sunset:
                    if (sunset == null) {
                        startTime.setTime(jobToSchedule.getScheduleTime());
                        break;
                    }
                    startTime.setTime(sunset);
                    break;
            }
            startTime.set(Calendar.YEAR, nowTime.get(Calendar.YEAR));
            startTime.set(Calendar.MONTH, nowTime.get(Calendar.MONTH));
            startTime.set(Calendar.DAY_OF_MONTH, nowTime.get(Calendar.DAY_OF_MONTH));
            startTime.add(Calendar.MINUTE, Math.toIntExact(jobToSchedule.getOffset()));
            if (jobToSchedule.getSecurity())
                startTime.add(Calendar.MINUTE, randomObj.ints(-securityOffset, securityOffset).findFirst().getAsInt());
            if (startTime.before(nowTime)) continue;
            String x10Address = "";
            if (jobToSchedule.getModule() != null) {
                x10Address = moduleService.getModuleById(jobToSchedule.getModule()).getAddresslet() +
                        moduleService.getModuleById(jobToSchedule.getModule()).getAddressnum();
            }
            scheduleTheX10Job(scheduler, jobToSchedule, x10Address, startTime.getTime(), "");
        }
        printAllScheduledJobs();
    }

    public void resumeX10Jobs() {
        logger.info("Executing JobScheduler resumeX10Jobs");
        ArrayList<JobList> jobLists = (ArrayList<JobList>) jobListService.getJobList("PAUSED");
        if (jobLists.size() > 0) {
            String temperatureUrl = "http://api.geonames.org/findNearByWeatherJSON?lat=45.7621381&lng=-73.473165&username=proyleg";
            logger.info("Asking for Temperature");
            RestTemplate restTemplate = new RestTemplate();
            String temperature = "20";
            try {
                int i = 0;
                int retry = 4;
                do {
                    ResponseEntity<String> response = restTemplate.getForEntity(temperatureUrl, String.class);
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode root = mapper.readTree(response.getBody());
                    JsonNode weather = root.findPath("weatherObservation");
                    temperature = weather.path("temperature").asText();
                    logger.info("Result for Temperature " + temperature);
//                    logger.info("Debug -10.6 : " + response.toString());
                    i++;
//                    temperature = "-10.6";
                    if (!temperature.equals("-10.6") || i >= retry) break;
                    Thread.sleep(120 * 1000L);       // x seconds
                } while (i < retry);
            } catch (Exception e) {
                logger.error("Asking for Temperature failed");
            }
            for (JobList jobList : jobLists) {
                JobToSchedule jobToSchedule = jobToScheduleService.getJobToScheduleById(Long.valueOf(jobList.getJobName().split("_")[1]));
                if (jobToSchedule.getTemperature() == null || Float.valueOf(temperature) >= jobToSchedule.getTemperature()) {
                    try {
                        scheduler.getScheduler().resumeJob(jobList.getJobKey());
                        Thread.sleep(60 * 1000L);       // x seconds
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void getSunriseSunset() {
        String sunriseSunsetUrl = "http://api.geonames.org/timezoneJSON?lat=45.7621381&lng=-73.473165&username=proyleg";

        logger.info("Asking for Sunrise and Sunset time");
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(sunriseSunsetUrl, String.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            sunrise = formatter.parse(root.path("sunrise").asText());
            sunset = formatter.parse(root.path("sunset").asText());
            askedSunriseSunset = Calendar.getInstance();
            askedSunriseSunset.setTime(formatter.parse(root.path("time").asText()));
            for (JobToSchedule jobToSchedule : jobToScheduleService.getAllJobToSchedule()) {
                switch (jobToSchedule.getScheduleType()) {
                    case Fix:
                        continue;
                    case Sunrise:
                        jobToSchedule.setScheduleTime(sunrise);
                        jobToScheduleService.saveJobToSchedule(jobToSchedule);
                        break;
                    case Sunset:
                        jobToSchedule.setScheduleTime(sunset);
                        jobToScheduleService.saveJobToSchedule(jobToSchedule);
                        break;
                }
            }
        } catch (Exception e) {
            logger.error("Asking for Sunrise and Sunset time failed");
            sunrise = null;
            sunset = null;
            askedSunriseSunset = null;
        }
    }

    private Iterable<JobToSchedule> getJobToSchedule() {
        switch (now().getDayOfWeek()) {
            case MONDAY:
                return this.jobToScheduleService.getJobToScheduleMonday();
            case TUESDAY:
                return this.jobToScheduleService.getJobToScheduleTuesday();
            case WEDNESDAY:
                return this.jobToScheduleService.getJobToScheduleWednesday();
            case THURSDAY:
                return this.jobToScheduleService.getJobToScheduleThursday();
            case FRIDAY:
                return this.jobToScheduleService.getJobToScheduleFriday();
            case SATURDAY:
                return this.jobToScheduleService.getJobToScheduleSaturday();
            case SUNDAY:
                return this.jobToScheduleService.getJobToScheduleSunday();
        }
        return null;
    }

    private long getTimeDiff(Date dateOne, Date dateTwo) {
        long diff;
        long timeDiff = Math.abs(dateOne.getTime() - dateTwo.getTime());
        diff = TimeUnit.MILLISECONDS.toDays(timeDiff);
        return diff;
    }

    private void printAllScheduledJobs() {
        System.out.println("===========================   Scheduled jobs   ===========================");
        for (JobList jobList : jobListService.getJobList("ALL")) {
            System.out.println("[jobName] : " + jobList.getJobName() + " [groupName] : "
                    + jobList.getJobGroup() + " - " + jobList.getNextFireTime() + " - " + jobList.getState());
        }
    }

    public static void scheduleTheX10Job(SchedulerFactoryBean scheduler, JobToSchedule jobToSchedule, String x10Address, Date scheduledTime, String suffixe) {
        try {
            JobDetail job = newJob(X10Job.class)
                    .withIdentity(jobToSchedule.getName() + "_" + suffixe + jobToSchedule.getIdJob(), "X10")
                    .usingJobData("x10address", x10Address)
                    .usingJobData("command", jobToSchedule.getCommand().toString())
                    .usingJobData("multiSend", jobToSchedule.getMultiSend())
                    .build();
            Trigger trigger = newTrigger()
                    .withIdentity(jobToSchedule.getName() + jobToSchedule.getIdJob() + suffixe)
                    .startAt(scheduledTime)
                    .withSchedule(simpleSchedule())
                    .build();
            Set<Trigger> triggers = new HashSet<>();
            triggers.add(trigger);
            scheduler.getScheduler().scheduleJob(job, triggers, true);
            if (jobToSchedule.getTemperature() != null & suffixe.equals("")) {
                scheduler.getScheduler().pauseTrigger(trigger.getKey());
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

    }
}