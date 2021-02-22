import java.text.SimpleDateFormat
import java.util.Date
 
import org.quartz.{CronScheduleBuilder, CronTrigger, JobBuilder, JobKey, TriggerBuilder, TriggerKey, _}
import org.quartz.impl.StdSchedulerFactory
 
import scala.collection.JavaConverters._
 
/**
  * 定时任务管理类
  *
  * @author BarryWang create at 2018/5/11 14:22
  * @version 0.0.1
  */
object QuartzManager {
  private val stdSchedulerFactory = new StdSchedulerFactory
  private val JOB_GROUP_NAME = "JOB_GROUP_NAME"
  private val TRIGGER_GROUP_NAME = "TRIGGER_NAME"
 
  /**
    * 根据指定格式(yyyy-MM-dd HH:mm:ss)时间字符串添加定时任务，使用默认的任务组名，触发器名，触发器组名
    * @param jobName  任务名
    * @param time     时间设置，参考quartz说明文档
    * @param jobClass 任务类名
    */
  def addJobByTime(jobName: String, time: String, jobClass: Class[_ <: Job]) : Unit = {
    QuartzManager.addJobByTime(jobName, time, jobClass, Map("1"->"otherData"))
  }
 
  /**
    * 根据指定时间(java.util.Date)添加定时任务，使用默认的任务组名，触发器名，触发器组名
    *
    * @param jobName 任务名
    * @param date 日期
    * @param jobClass 任务类名
    */
  def addJobByDate(jobName: String, date: Date, jobClass: Class[_ <: Job]): Unit = {
    QuartzManager.addJobByDate(jobName, date, jobClass, Map("1"->"otherData"))
  }
 
  /**
    * 根据指定cron表达式添加定时任务，使用默认的任务组名，触发器名，触发器组名
    *
    * @param jobName 任务名
    * @param jobClass 任务类名
    * @param cron cron表达式
    */
  def addJobByCron(jobName: String, cron : String, jobClass: Class[_ <: Job]): Unit = {
    QuartzManager.addJobByCron(jobName, cron, jobClass, Map("1"->"otherData"))
  }
 
  /**
    * 函数描述: 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名
    * @param jobName    任务名
    * @param time       时间字符串, 格式为(yyyy-MM-dd HH:mm:ss)
    * @param jobClass   任务类名
    * @param paramsMap  定时器需要额外数据
    */
  def addJobByTime(jobName: String, time: String, jobClass: Class[_ <: Job], paramsMap: Map[_ <: String, _ <: AnyRef]): Unit = {
    addJobByTime(jobName, time, "yyyy-MM-dd HH:mm:ss", jobClass, paramsMap)
  }
 
  /**
    * 函数描述: 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名
    * @param jobName    任务名
    * @param time       时间设置，参考quartz说明文档
    * @param jobClass   任务类名
    * @param paramsMap  定时器需要额外数据
    */
  def addJobByTime(jobName: String, time: String, timePattern: String, jobClass: Class[_ <: Job], paramsMap: Map[_ <: String, _ <: AnyRef]): Unit = {
    val df = new SimpleDateFormat(timePattern)
    val cron = getCron(df.parse(time))
    addJobByCron(jobName, cron, jobClass, paramsMap)
  }
 
  /**
    * Description: 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名
    *
    * @param jobName 任务名
    * @param date 日期
    * @param cls 任务
    * @param paramsMap  定时器需要额外数据
    */
  def addJobByDate(jobName: String, date: Date, cls: Class[_ <: Job], paramsMap: Map[_ <: String, _ <: AnyRef]): Unit = {
      val cron = getCron(date)
      addJobByCron(jobName, cron, cls, paramsMap)
  }
 
  /**
    * 函数描述: 根据cron表达式添加定时任务(默认触发器组名及任务组名)
    * @param jobId            任务ID
    * @param cron             时间设置 表达式，参考quartz说明文档
    * @param jobClass         任务的类
    * @param paramsMap        可变参数需要进行传参的值
    */
  def addJobByCron(jobId: String, cron: String, jobClass: Class[_ <: Job], paramsMap: Map[_ <: String, _ <: AnyRef]): Unit = {
    addJob(jobId, cron, jobClass, paramsMap, JOB_GROUP_NAME, TRIGGER_GROUP_NAME)
  }
 
  /**
    * 函数描述: 根据cron表达式添加定时任务
    * @param jobId            任务ID
    * @param cron             时间设置 表达式，参考quartz说明文档
    * @param jobClass         任务的类类型  eg:TimedMassJob.class
    * @param paramsMap        可变参数需要进行传参的值
    * @param jobGroupName     任务组名
    * @param triggerGroupName 触发器组名
    */
  def addJob(jobId: String, cron: String, jobClass: Class[_ <: Job], paramsMap: Map[_ <: String, _ <: AnyRef],
             jobGroupName: String, triggerGroupName: String): Unit = {
      val scheduler = stdSchedulerFactory.getScheduler
      // 任务名，任务组，任务执行类
      val jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobId, jobGroupName).build
       //设置参数
      jobDetail.getJobDataMap.putAll(paramsMap.asJava)
 
      val triggerBuilder = TriggerBuilder.newTrigger
      // 触发器名,触发器组
      //默认设置触发器名与任务ID相同
      val triggerName = jobId
      triggerBuilder.withIdentity(triggerName, triggerGroupName)
      triggerBuilder.startNow
      // 触发器时间设定
      triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron))
      // 创建Trigger对象
      val trigger = triggerBuilder.build.asInstanceOf[CronTrigger]
      // 调度容器设置JobDetail和Trigger
      scheduler.scheduleJob(jobDetail, trigger)
      // 启动
      if (!scheduler.isShutdown) scheduler.start()
  }
 
  /**
    * 函数描述: 修改一个任务的触发时间(使用默认的任务组名，触发器名，触发器组名)
    * @param cron 时间字符串
    */
  def modifyJobTime(jobId: String, cron: String, jobClass: Class[_ <: Job]): Unit = {
    modifyJobTime(jobId, cron, jobClass, Map("1"->"otherData"), JOB_GROUP_NAME, TRIGGER_GROUP_NAME)
  }
 
  /**
    * 函数描述: 修改一个任务的触发时间(使用默认的任务组名，触发器名，触发器组名)
    * @param cron 时间字符串
    */
  def modifyJobTime(jobId: String, cron: String, jobClass: Class[_ <: Job], paramsMap: Map[_ <: String, _ <: AnyRef]): Unit = {
    modifyJobTime(jobId, cron, jobClass, paramsMap, JOB_GROUP_NAME, TRIGGER_GROUP_NAME)
  }
 
  /**
    * 函数描述: 修改一个任务的触发时间
    * @param jobId 任务ID
    * @param cron cron表达式
    * @param jobClass 任务类名
    * @param paramsMap 其他参数
    * @param jobGroupName 任务组名
    * @param triggerGroupName 触发器组
    */
  def modifyJobTime(jobId: String, cron: String, jobClass: Class[_ <: Job], paramsMap: Map[_ <: String, _ <: AnyRef],
                    jobGroupName: String, triggerGroupName: String): Unit = {
    val scheduler = stdSchedulerFactory.getScheduler()
    //默认设置触发器名与任务ID相同
    val triggerName = jobId
    val triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName)
    var trigger = scheduler.getTrigger(triggerKey).asInstanceOf[CronTrigger]
    if (trigger != null) {
      removeJob(jobId)
    }
    addJob(jobId, cron, jobClass, paramsMap, jobGroupName, triggerGroupName)
  }
 
  /**
    * 函数描述: 移除一个任务(使用默认的任务组名，触发器名，触发器组名)
    * @param jobId 任务名称
    */
  def removeJob(jobId: String): Unit = {
    val scheduler = stdSchedulerFactory.getScheduler
    //默认设置触发器名与任务ID相同
    val triggerName = jobId
    val triggerKey = TriggerKey.triggerKey(triggerName, TRIGGER_GROUP_NAME)
    // 停止触发器
    scheduler.pauseTrigger(triggerKey)
    // 移除触发器
    scheduler.unscheduleJob(triggerKey)
    // 删除任务
    scheduler.deleteJob(JobKey.jobKey(jobId , JOB_GROUP_NAME))
  }
 
  /**
    * 函数描述: 移除一个任务
    * @param jobId 任务ID
    * @param jobGroupName 任务组
    * @param triggerName 触发器名称
    * @param triggerGroupName 触发器组名
    */
  def removeJob(jobId: String, jobGroupName: String, triggerName: String, triggerGroupName: String): Unit = {
    val scheduler = stdSchedulerFactory.getScheduler
    val triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName)
    // 停止触发器
    scheduler.pauseTrigger(triggerKey)
    // 移除触发器
    scheduler.unscheduleJob(triggerKey)
    // 删除任务
    scheduler.deleteJob(JobKey.jobKey(jobId , jobGroupName))
  }
 
  /**
    * 函数描述:启动所有定时任务
    */
  def startJobs(): Unit = {
      stdSchedulerFactory.getScheduler.start()
  }
 
  /**
    * 函数描述:关闭所有定时任务
    *
    */
  def shutdownJobs(): Unit = {
      val sched = stdSchedulerFactory.getScheduler
      if (!sched.isShutdown) sched.shutdown()
  }
 
  /**
    * 根据时间获取Cron表达式
    * @param date 日期
    * @return
    */
  def getCron(date: Date): String = {
    val dateFormat = "ss mm HH dd MM ? yyyy"
    formatDateByPattern(date, dateFormat)
  }
 
  /**
    * 日期格式转换
    * @param date 日期
    * @param dateFormat 格式
    * @return
    */
  def formatDateByPattern(date : Date, dateFormat : String): String = {
    val sdf = new SimpleDateFormat(dateFormat)
    sdf.format(date)
  }
}
