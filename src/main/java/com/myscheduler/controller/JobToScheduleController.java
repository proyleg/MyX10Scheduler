package com.myscheduler.controller;

import com.myscheduler.job.JobScheduler;
import com.myscheduler.model.CommandType;
import com.myscheduler.model.JobToSchedule;
import com.myscheduler.model.Module;
import com.myscheduler.model.StatusType;
import com.myscheduler.service.JobListService;
import com.myscheduler.service.JobToScheduleService;
import com.myscheduler.service.ModuleService;
import org.quartz.SchedulerException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.Date;

import static com.myscheduler.job.JobScheduler.scheduleTheX10Job;
import static org.quartz.JobKey.jobKey;

/**
 * Created by Pierre on 2016-04-11.
 */
@Controller
public class JobToScheduleController {
    private static final String JOBS_LIST = "jobs";
    private static final String JOBS_SCHEDULED_LIST = "scheduledjobs";
    private static final String JOB_FORM = "jobform";
    private JobToScheduleService jobToScheduleService;
    private JobListService jobListService;
    private SchedulerFactoryBean scheduler;
    private JobScheduler jobScheduler;
    private ModuleService moduleService;

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

    @Autowired
    public void setJobScheduler(JobScheduler jobScheduler) {
        this.jobScheduler = jobScheduler;
    }

    @Autowired
    public void setModuleService(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    @RequestMapping(value = {"/", "/jobs"}, method = RequestMethod.GET)
    public String listJobs(Model model) {
        model.addAttribute("jobs", jobToScheduleService.getAllJobToSchedule());
        return JOBS_LIST;
    }

    @RequestMapping(value = "/scheduledjobs", method = RequestMethod.GET)
    public String listScheduledJobs(Model model) {
        model.addAttribute("jobs", jobListService.getJobList("ALL"));
        return JOBS_SCHEDULED_LIST;
    }

    @RequestMapping(value = "/scheduledjobs/reschedulejobs")
    public String rescheduleJobs(Model model) {
        jobScheduler.scheduleX10Jobs();
        jobScheduler.resumeX10Jobs();
        model.addAttribute("jobs", jobListService.getJobList("ALL"));
        return "redirect:/" + JOBS_SCHEDULED_LIST;
    }

    @RequestMapping(value = "/scheduledjobs/cancel/{jobName}")
    public String cancelScheduledJobs(@PathVariable String jobName, Model model) {
        try {
            scheduler.getScheduler().deleteJob(jobKey(jobName, "X10"));
        } catch (SchedulerException e) {
        }
        model.addAttribute("jobs", jobListService.getJobList("ALL"));
        return "redirect:/" + JOBS_SCHEDULED_LIST;
    }

    @RequestMapping("job/new")
    public String newJob(Model model) {
        model.addAttribute("jobToSchedule", new JobToSchedule());
        model.addAttribute("modules", getAllModules());
        return JOB_FORM;
    }

    @RequestMapping("job/edit/{id}")
    public String editJob(@PathVariable Long id, Model model) {
        model.addAttribute("jobToSchedule", jobToScheduleService.getJobToScheduleById(id));
        model.addAttribute("modules", getAllModules());
        return JOB_FORM;
    }

    @RequestMapping(value = "job", method = RequestMethod.POST)
    public String saveJob(@Valid JobToSchedule jobToSchedule, BindingResult bindingResult,Model model) {
        if (!jobToSchedule.getCommand().equals(CommandType.AllUnitsOff) & jobToSchedule.getModule()==null)
            bindingResult.addError(new ObjectError("JobToSchedule", "Combinaisaon de Commande et Module incompatible"));
        if (bindingResult.hasErrors()) {
            model.addAttribute("modules", getAllModules());
            return JOB_FORM;
        }
        jobToScheduleService.saveJobToSchedule(jobToSchedule);
        return "redirect:/" + JOBS_LIST;
    }

    private ArrayList<Module> getAllModules() {
        ArrayList<Module> modules = (ArrayList<Module>) moduleService.getAllModules();
        Module nullModule = new Module();
        nullModule.setId(null);
        modules.add(0, nullModule);
        return modules;
    }

    @RequestMapping("job/duplicate/{id}")
    public String duplicateJob(@PathVariable Long id, Model model) {
        JobToSchedule jobToScheduleFrom = jobToScheduleService.getJobToScheduleById(id);
        JobToSchedule jobToSchedule = new JobToSchedule();
        BeanUtils.copyProperties(jobToScheduleFrom, jobToSchedule);
        jobToSchedule.setIdJob(null);
        jobToSchedule.setVersion(0L);
        jobToSchedule.setStatus(StatusType.Inactive);
        jobToScheduleService.saveJobToSchedule(jobToSchedule);
        model.addAttribute("jobs", jobToScheduleService.getAllJobToSchedule());
        return JOBS_LIST;
    }

    @RequestMapping("job/dis-active/{id}")
    public String disableJob(@PathVariable Long id, Model model) {
        JobToSchedule jobToSchedule = jobToScheduleService.getJobToScheduleById(id);
        if (jobToSchedule.getStatus().equals(StatusType.Active)) {
            jobToSchedule.setStatus(StatusType.Inactive);
            try {
                scheduler.getScheduler().deleteJob(jobKey(jobToSchedule.getName() + "_" + jobToSchedule.getIdJob(), "X10"));
            } catch (SchedulerException e) {
            }
        } else
            jobToSchedule.setStatus(StatusType.Active);
        jobToScheduleService.saveJobToSchedule(jobToSchedule);
        model.addAttribute("jobs", jobToScheduleService.getAllJobToSchedule());
        return "redirect:/" + JOBS_LIST;
    }

    @RequestMapping("job/delete/{id}")
    public String deleteJob(@PathVariable Long id) {
        jobToScheduleService.deleteJobToScheduleById(id);
        return "redirect:/" + JOBS_LIST;
    }

    @RequestMapping("job/executenow/{id}")
    public String executeJobNow(@PathVariable Long id) {
        JobToSchedule jobToSchedule = jobToScheduleService.getJobToScheduleById(id);
        Date now = new Date(System.currentTimeMillis());
        String x10Address = "";
        if (jobToSchedule.getModule() != null) {
            x10Address = moduleService.getModuleById(jobToSchedule.getModule()).getAddresslet() +
                    moduleService.getModuleById(jobToSchedule.getModule()).getAddressnum();
        }
        scheduleTheX10Job(scheduler, jobToSchedule, x10Address, now, "now_");
        return "redirect:/" + JOBS_LIST;
    }

}
