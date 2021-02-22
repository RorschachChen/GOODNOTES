import java.io.{PrintWriter, StringWriter}

import com.github.dapeng.core.helper.MasterHelper
import com.today.api.financetask.scala.enums.TScheduledTaskLogEnum
import com.today.service.financetask.action.sql.ScheduledTaskLogSql
import com.today.service.financetask.util.{AppContextHolder, Debug}
import org.quartz.{Job, JobExecutionContext}
import org.slf4j.LoggerFactory
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.support.TransactionTemplate

import scala.util.{Failure, Success, Try}

/**
  * the abstract class for job
  */
trait AbstractJob extends Job{
  /** 日志 */
  val logger = LoggerFactory.getLogger(getClass)

  /**
    * execute the job
    * @param context
    */
  override def execute(context: JobExecutionContext): Unit = {
    val jobAndApiInfo = getJobAndApiInfo(context)
    if (!MasterHelper.isMaster(jobAndApiInfo._1, jobAndApiInfo._2)) {
      logger.info(s"Can't select master to run the job ${jobAndApiInfo._3.jobId}: ${jobAndApiInfo._3.jobName}")
      return
    }

    val logId = ScheduledTaskLogSql.insertScheduledTaskLog(jobAndApiInfo._3)
    context.put("logId", logId)
    logger.info(s"Starting the job ${jobAndApiInfo._3.jobId}: ${jobAndApiInfo._3.jobName} ...")

    /**
      * 事物处理
      */
    val transactionTemplate: TransactionTemplate = AppContextHolder.getBean("transactionTemplate")
    transactionTemplate.execute((status: TransactionStatus) =>{
      Debug.reset()
      Try(Debug.trace(s"${jobAndApiInfo._3.jobId}:${jobAndApiInfo._3.jobName}")(run(context))) match
      {
        case Success(x) => {
          logger.info(s"Successfully execute the job ${jobAndApiInfo._3.jobId}: ${jobAndApiInfo._3.jobName}")
          successLog(logId)
        }
        case Failure(e) => {
          logger.error(s"Failure execute the job ${jobAndApiInfo._3.jobId}: ${jobAndApiInfo._3.jobName}", e)
          failureLog(logId, status, e)
        }
      }
      Debug.info()
    })
  }

  /**
    * get the api information
    * @return (interface name, interface version, JobEnum)
    */
  def getJobAndApiInfo(context: JobExecutionContext) : (String, String, JobEnum)

  /**
    * start up the scheduled task
    * @param context JobExecutionContext
    */
  def run(context: JobExecutionContext)

  /**
    * 成功日志记录
    * @param logId
    */
  def successLog(logId: Long): Unit ={
    ScheduledTaskLogSql.updateExportReportRecord(logId, TScheduledTaskLogEnum.SUCCESS, "Success")
  }

  /**
    * 失败日志记录
    * @param logId
    */
  def failureLog(logId: Long, status: TransactionStatus, e: Throwable): Unit ={
    status.setRollbackOnly()
    ScheduledTaskLogSql.updateExportReportRecord(logId, TScheduledTaskLogEnum.FAILURE, getExceptionStack(e))
  }

  /**
    *
    * 功能说明:在日志文件中 ，打印异常堆栈
    * @param e : Throwable
    * @return : String
    */
  def getExceptionStack(e: Throwable): String = {
    val errorsWriter = new StringWriter
    e.printStackTrace(new PrintWriter(errorsWriter))
    errorsWriter.toString
  }
}
