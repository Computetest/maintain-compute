package sqlserver;

import java.sql.ResultSet;
import java.util.Date;

public class Compute {
	Getpoint du = new Getpoint();
	public float totaltime = 24;
	Date date = new Date();
	java.sql.Date sql_date = new java.sql.Date(date.getTime());
	//计划停运系数
	public void POF()
	{
		String sql = "select RprTime from input_data_windturbine_tag";
		float RPrTime = du.executeselectsql(sql, 0);
		//float[] POF = new float[RPrTime.length];
		//insert 输入表中
		float POF = RPrTime/totaltime;
		String insertsql = "insert into windturbine_reliability_day (Date,PlnOfLnFactor) values ('"+sql_date+"',"+POF+")";
		du.executeSQL(insertsql,1);
	}
	
	//非计划停运系数
	public void UOF()
	{
		String sql = "select FaultTime from input_data_windturbine_tag";
		float[] FaultTime = du.executeSelectSQLtoFloat(sql, 0);
		float UOF = 0f;
		for(int i =0;i<FaultTime.length;i++)
		{
			UOF += FaultTime[i];
		}
		UOF = UOF/totaltime;
		String insertsql = "insert into windturbine_reliability_day (Date,UnPlnOfLnFactor) values ('"+sql_date+"',"+UOF+")";
		du.executeSQL(insertsql,1);
	}
	
	//可用系数
	public void AF()
	{
		String sql = "select FaultTime from input_data_windturbine_tag";
		float FaultTime = du.executeselectsql(sql, 0);
		String sql1 = "select RprTime from input_data_windturbine_tag";
		float RPrTime = du.executeselectsql(sql1, 0);
		float AF = (24-FaultTime-RPrTime)/totaltime;
		String insertsql = "insert into windturbine_reliability_day (Date,AvaRatio) values ('"+sql_date+"',"+AF+")";
		du.executeSQL(insertsql,1);
	}
	
	//运行系数
	public void SF()
	{
		String sql = "select GenTime from input_data_windturbine_tag";//发电时间
		float[] NmGenTime = du.executeSelectSQLtoFloat(sql, 0);
		float SF = 0f;
		for(int i=0;i<NmGenTime.length;i++)
		{
			SF += NmGenTime[i];
		}
		SF = SF/totaltime;
		String insertsql = "insert into windturbine_reliability_day (Date,OptRatio) values ('"+sql_date+"',"+SF+")";
		du.executeSQL(insertsql,1);
	}
	
	//利用系数
	public void UTF()
	{
		String sql = "select NmGenTime from input_data_windturbine_tag";//正常发电时间
		float[] NmGenTime = du.executeSelectSQLtoFloat(sql, 0);
		float SF = 0f;
		for(int i=0;i<NmGenTime.length;i++)
		{
			SF += NmGenTime[i];
		}
		SF = SF/totaltime;
		String insertsql = "insert into windturbine_reliability_day (Date,OptRatio) values ('"+sql_date+"',"+SF+")";
		du.executeSQL(insertsql,1);
	}
	
	//非计划停运率
	public void UOR()
	{
		String sql = "select FaultTime from input_data_windturbine_tag";//故障时间
		float FT = du.executeselectsql(sql, 0);
		String sql1 = "select RprTime from input_data_windturbine_tag";//检修时间
		float RT = du.executeselectsql(sql1, 0);
		float UOR = FT/(24-RT);
		String insertsql = "insert into windturbine_reliability_day (Date,UnPLnOfLnRatio) values ('"+sql_date+"',"+UOR+")";
		du.executeSQL(insertsql,1);
	}
	 
	public void UOOR()
	{
		String sql = "select FaultTime from input_data_windturbine_tag";//故障时间
		float FT = du.executeselectsql(sql, 0);
		String sql1 = "select RprTime from input_data_windturbine_tag";//检修时间
		float RT = du.executeselectsql(sql1, 0);
		float t = 24-FT-RT;//可用小时
		String sql2 ="select FaultCnt from input_data_windturbine_tag";//故障次数
		float Fa = du.executeselectsql(sql2, 0);
		float UOOR = Fa*8760/t;
		String insertsql = "insert into windturbine_reliability_day (Date,UnPLnOfLnChaRatio) values ('"+sql_date+"',"+UOOR+")";
		du.executeSQL(insertsql,1);
	}
	
	public void EXR()
	{
		String sql = "select NmGenTime from input_data_windturbine_tag";//正常发电时间
		float NT = du.executeselectsql(sql, 0);
		String sql1 = "select FaultTime from input_data_windturbine_tag";//故障时间
		float FT = du.executeselectsql(sql1, 0);
		String sql2 = "select RprTime from input_data_windturbine_tag";//检修时间
		float RT = du.executeselectsql(sql2, 0);
		float t = 24-FT-RT;//可用小时
		float EXR = NT/t;
		String insertsql = "insert into windturbine_reliability_day (Date,ExpRatio) values ('"+sql_date+"',"+EXR+")";
		du.executeSQL(insertsql,1);
	}
	
	public void CAH()
	{
		String sql = "select NmGenTime from input_data_windturbine_tag";//正常发电时间
		float NT = du.executeselectsql(sql, 0);
		String sql1 = "select FaultTime from input_data_windturbine_tag";//故障时间
		float FT = du.executeselectsql(sql1, 0);
		String sql2 = "select RprTime from input_data_windturbine_tag";//检修时间
		float RT = du.executeselectsql(sql2, 0);
		float t = 24-FT-RT;//可用小时
		
		String sql3 = "select RprCnt from input_data_windturbine_tag";//计划停运次数，检修
		float Rp = du.executeselectsql(sql3, 0);
		String sql4 = "select FaultCnt from input_data_windturbine_tag";//非计划停运次数,故障
		float Fc = du.executeselectsql(sql4, 0);
		float CAH = t/(Rp+Fc);
		String insertsql = "insert into windturbine_reliability_day (Date,AvgSusAvaHor) values ('"+sql_date+"',"+CAH+")";
		du.executeSQL(insertsql,1);
	}
	public void MTBF()
	{
		String sql = "select NmGenTime from input_data_windturbine_tag";//正常发电时间
		float NT = du.executeselectsql(sql, 0);
		String sql1 = "select FaultTime from input_data_windturbine_tag";//故障时间
		float FT = du.executeselectsql(sql1, 0);
		String sql2 = "select RprTime from input_data_windturbine_tag";//检修时间
		float RT = du.executeselectsql(sql2, 0);
		float t = 24-FT-RT;//可用小时
		
		String sql4 = "select FaultCnt from input_data_windturbine_tag";//非计划停运次数,故障
		float Fc = du.executeselectsql(sql4, 0);
		float MTBF = t/Fc;
		String insertsql = "insert into windturbine_reliability_day (Date,AvgNoFltAvaHor) values ('"+sql_date+"',"+MTBF+")";
		du.executeSQL(insertsql,1);
	}
}
