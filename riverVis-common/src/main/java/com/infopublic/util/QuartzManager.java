package com.infopublic.util;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/** 定时任务管理类 
 */
public class QuartzManager {  
	@Autowired
//	static SchedulerFactoryBean schedulerFactoryBean;   //使用spring中配置的bean
    private static SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();  //创建一个SchedulerFactory工厂实例
    private static String JOB_GROUP_NAME = "BROAD_JOBGROUP_NAME";  					//任务组
    private static String TRIGGER_GROUP_NAME = "BROAD_TRIGGERGROUP_NAME";  			//触发器组
  
    /**添加一个定时任务，使用默认的任务组名，触发器名，触发器组名  
     * @param jobName 任务名
     * @param cls 任务
     * @param time 时间设置，参考quartz说明文档
     */
    public static void addJob(String jobName, Class<? extends Job> cls, String time) {
        try {  
            Scheduler sched = gSchedulerFactory.getScheduler();  										//通过SchedulerFactory构建Scheduler对象
            JobDetail jobDetail= JobBuilder.newJob(cls).withIdentity(jobName,JOB_GROUP_NAME).build();	//用于描叙Job实现类及其他的一些静态信息，构建一个作业实例
        	CronTrigger trigger = (CronTrigger) TriggerBuilder
        			.newTrigger()	 																	//创建一个新的TriggerBuilder来规范一个触发器
    				.withIdentity(jobName, TRIGGER_GROUP_NAME)											//给触发器起一个名字和组名
    				.withSchedule(CronScheduleBuilder.cronSchedule(time))// 触发器时间设定
    				.build();
            sched.scheduleJob(jobDetail, trigger);  
            if (!sched.isShutdown()) {  
                sched.start();  	  // 启动  
            }  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
    
    /**添加一个定时任务，使用默认的任务组名，触发器名，触发器组名  （带参数）
     * @param jobName 任务名
     * @param cls 任务
     * @param time 时间设置，参考quartz说明文档
     */
    public static void addJob(String jobName, Class<? extends Job> cls, String time, Map<String,Object> parameter) {
        try {  
            Scheduler sched = gSchedulerFactory.getScheduler();  //通过SchedulerFactory构建Scheduler对象
            JobDetail jobDetail= JobBuilder.newJob(cls).withIdentity(jobName,JOB_GROUP_NAME).build();	//用于描叙Job实现类及其他的一些静态信息，构建一个作业实例
            jobDetail.getJobDataMap().put("parameterList", parameter);								//传参数
        	CronTrigger trigger = (CronTrigger) TriggerBuilder
        			.newTrigger()	 																	//创建一个新的TriggerBuilder来规范一个触发器
    				.withIdentity(jobName, TRIGGER_GROUP_NAME)											//给触发器起一个名字和组名
    				.withSchedule(CronScheduleBuilder.cronSchedule(time))
    				.build();
            sched.scheduleJob(jobDetail, trigger);  
            if (!sched.isShutdown()) {  
                sched.start();  	  // 启动  
            }  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
  
    /**添加一个定时任务 
     * @param jobName	任务名 
     * @param jobGroupName	任务组名 
     * @param triggerName	触发器名 
     * @param triggerGroupName	触发器组名 
     * @param jobClass	任务 
     * @param time	时间设置，参考quartz说明文档 
     */
    public static void addJob(String jobName, String jobGroupName,  
            String triggerName, String triggerGroupName, Class<? extends Job> jobClass,
            String time) {  
        try {  
            Scheduler sched = gSchedulerFactory.getScheduler();
            JobDetail jobDetail= JobBuilder.newJob(jobClass).withIdentity(jobName,jobGroupName).build();// 任务名，任务组，任务执行类
            CronTrigger trigger = (CronTrigger) TriggerBuilder     // 触发器
    				.newTrigger()
    				.withIdentity(triggerName, triggerGroupName)
    				.withSchedule(CronScheduleBuilder.cronSchedule(time))
    				.build();
            sched.scheduleJob(jobDetail, trigger);
            if (!sched.isShutdown()) {  
                sched.start();  	  // 启动  
            } 
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
    
    /**添加一个定时任务  （带参数）
     * @param jobName	任务名 
     * @param jobGroupName	任务组名 
     * @param triggerName	触发器名 
     * @param triggerGroupName	触发器组名 
     * @param jobClass	任务 
     * @param time	时间设置，参考quartz说明文档 
     */
    public static void addJob(String jobName, String jobGroupName,  
            String triggerName, String triggerGroupName, Class<? extends Job> jobClass,
            String time, Map<String,Object> parameter) {  
        try {  
            Scheduler sched = gSchedulerFactory.getScheduler();
            JobDetail jobDetail= JobBuilder.newJob(jobClass).withIdentity(jobName,jobGroupName).build();// 任务名，任务组，任务执行类
            jobDetail.getJobDataMap().put("parameterList", parameter);								//传参数
            CronTrigger trigger = (CronTrigger) TriggerBuilder     // 触发器
    				.newTrigger()
    				.withIdentity(triggerName, triggerGroupName)
    				.withSchedule(CronScheduleBuilder.cronSchedule(time))
    				.build();
            sched.scheduleJob(jobDetail, trigger);
            if (!sched.isShutdown()) {  
                sched.start();  	  // 启动  
            } 
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    } 
    /** 
     * @Description: 修改一个任务的触发时间 使用默认的任务组名，触发器名，触发器组名
     *  
     * @param jobName 
     * @param cron   时间设置，参考quartz说明文档   
     */  
    public static void modifyJobTime(String jobName,  String cron) {  
        try {  
            Scheduler sched = gSchedulerFactory.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, TRIGGER_GROUP_NAME);
            CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerKey);
            if (trigger == null) {  
                return;  
            }  

            String oldTime = trigger.getCronExpression();  
            if (!oldTime.equalsIgnoreCase(cron)) { 
                /** 方式一 ：调用 rescheduleJob 开始 */
                // 触发器  
                TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
                // 触发器名,触发器组  
                triggerBuilder.withIdentity(jobName, TRIGGER_GROUP_NAME);
                triggerBuilder.startNow();
                // 触发器时间设定  
                triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
                // 创建Trigger对象
                trigger = (CronTrigger) triggerBuilder.build();
                // 方式一 ：修改一个任务的触发时间
                sched.rescheduleJob(triggerKey, trigger);
                /** 方式一 ：调用 rescheduleJob 结束 */

                /** 方式二：先删除，然后在创建一个新的Job  */
                //JobDetail jobDetail = sched.getJobDetail(JobKey.jobKey(jobName, jobGroupName));  
                //Class<? extends Job> jobClass = jobDetail.getJobClass();  
                //removeJob(jobName, jobGroupName, triggerName, triggerGroupName);  
                //addJob(jobName, jobGroupName, triggerName, triggerGroupName, jobClass, cron); 
                /** 方式二 ：先删除，然后在创建一个新的Job */
            }  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  

    /** 
     * @Description: 移除一个任务 
     *  
     * @param jobName 
     * @param jobGroupName 
     * @param triggerName 
     * @param triggerGroupName 
     */  
    public static void removeJob(String jobName, String jobGroupName,  
            String triggerName, String triggerGroupName) {  
        try {  
            Scheduler sched = gSchedulerFactory.getScheduler();

            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);

            sched.pauseTrigger(triggerKey);// 停止触发器  
            sched.unscheduleJob(triggerKey);// 移除触发器  
            sched.deleteJob(JobKey.jobKey(jobName, jobGroupName));// 删除任务
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
    /**
     * 检测job任务是否存在  使用默认的任务组名
     * @param jobName
     * @return
     */
    public static Boolean checkExists(String jobName) {
    	 try {
    		 Scheduler sched = gSchedulerFactory.getScheduler();
			 return sched.checkExists(JobKey.jobKey(jobName, JOB_GROUP_NAME)) ;
		} catch (SchedulerException e) {
			 throw new RuntimeException(e);  
		}
    }
    /**
     * 启动所有定时任务 
     */
    public static void startJobs() {  
        try {  
            Scheduler sched = gSchedulerFactory.getScheduler();
            sched.start();  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
  
    /**
     * 关闭所有定时任务 
     */
    public static void shutdownJobs() {  
        try {  
            Scheduler sched = gSchedulerFactory.getScheduler();
            if (!sched.isShutdown()) {  
                sched.shutdown();  
            }  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
}  