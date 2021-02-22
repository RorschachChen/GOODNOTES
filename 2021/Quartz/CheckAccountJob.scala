import java.util.Calendar

import com.today.api.checkaccount.scala.enums.FlatFormTypeEnum
import com.today.api.checkaccount.scala.request.ReconciliationRequest
import com.today.api.checkaccount.scala.CheckAccountServiceClient
import com.today.service.financetask.job.define.{AbstractJob, JobEnum}
import org.quartz.JobExecutionContext

class CheckAccountJob extends AbstractJob{
  /**
    * get the api information
    *
    * @return (interface name, interface version, JobEnum)
    */
  override def getJobAndApiInfo(context: JobExecutionContext): (String, String, JobEnum) = {
    ("com.today.api.financetask.service.CloseAccountScheduleService", "1.0.0",  JobEnum.CHECK_ACCOUNT_PROCESS)
  }

  /**
    * start up the scheduled task
    *
    * @param context JobExecutionContext
    */
  override def run(context: JobExecutionContext): Unit = {
    val cal = Calendar.getInstance
    cal.add(Calendar.DATE, -1)
    new CheckAccountServiceClient().appReconciliation(new ReconciliationRequest(FlatFormTypeEnum.TODAY_APP,None))

  }
}
