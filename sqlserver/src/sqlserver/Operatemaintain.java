package sqlserver;

//import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;


public class Operatemaintain {
	Getpoint du = new Getpoint();
	CalFunc cf = new CalFunc();
	public float totaltime = 24;
	Date date = new Date();
	java.sql.Date sql_date = new java.sql.Date(date.getTime());
	
	public static void main(String args[])
	{
//		CalFunc cf = new CalFunc();
//		System.out.println(cf.calStartDate);
		Operatemaintain t = new Operatemaintain();
		t.TbAvaRatio();
		
	}
	//风电机组可利用率
	public void TbAvaRatio()
	{
		System.out.println(cf.calStartDate);
		String sql = "select FaultTime from input_data_windturbine_tag"; //where Date in (sql_date)";//故障小时
		String sql1 = "select RprTime from input_data_windturbine_tag";// where Date in (sql_date)";//检修时间
		float[] FaultTime = du.executeSelectSQLtoFloat(sql, 0);
		float[] RprTime = du.executeSelectSQLtoFloat(sql1, 0);
		
		float[] TbAvaRatio = new float[RprTime.length];
		
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
//		date = new java.sql.Date(System.currentTimeMillis());
		
		float Tb =0f,Fas=0f,Rps=0f;
		for(int i=0;i<RprTime.length;i++)
		{
			Fas += FaultTime[i];
			Rps += RprTime[i];
		}
		Tb = 1-(Fas+Rps)/totaltime;
		//插入风电机组可利用率和日期
		String insertsql = "insert into windturbine_service_day (Date,TbAvaRatio)values("+"'"+sql_date+"',"+Tb+")";
		du.executeSQL(insertsql,1);
	}
	
	//风电场可利用率
	public void PltAvaRatio()
	{
		String sql = "select FaultTime from input_data_windturbine_tag"; //故障时间
		String sql1 = "select RprTime from input_data_windturbine_tag";//检修时间
		float[] FaultTime = du.executeSelectSQLtoFloat(sql, 0);
		float[] RprTime = du.executeSelectSQLtoFloat(sql1, 0);
		
		float pl=0f,Fas=0f,Rps=0f;
		for(int i=0;i<RprTime.length;i++)
		{
			Fas += FaultTime[i];
			Rps += RprTime[i];
		}
		pl = 1-(Fas+Rps)/totaltime;
		String insertsql = "insert into plant_service_day (Date,PltAvaRatio)values("+"'"+sql_date+"',"+pl+")";
		du.executeSQL(insertsql,1);
	}

	//风机故障率
	public void TbFtRatio()
	{
		String sql = "select FltOffGrdTime from intermediate_data_windturbine";
		float[] FC = du.executeSelectSQLtoFloat(sql, 0);
		float tb = FC[0];
		
		String insertsql = "insert into windturbine_service_day (Date,TbFtRatio) values ("+"'"+sql_date+"',"+tb+")";
		du.executeSQL(insertsql,1);
	}

	//风电场故障率
	public void PlFtRatio()
	{
		String sql = "select FaultCnt from input_data_windturbine_tag";
		float[] Fl = du.executeSelectSQLtoFloat(sql, 0);//风电场故障停机次数
		String sql1 = "select TbCnt from static_plant";
		float[] TbCnt = du.executeSelectSQLtoFloat(sql1, 0);
		
		float WindfaultCt =0;
		for(int i=0;i<Fl.length;i++)
		{
			WindfaultCt +=Fl[i];
		}
		//风电场故障率
		float PlFtRatio = WindfaultCt/TbCnt[0];
		String insertSql="insert into plant_service_day (Date,TbFtRatio) values ("+"'"+sql_date+"',"+PlFtRatio+")";
		du.executeSQL(insertSql, 1); 
	}
	
	//风电机组平均故障停机时间
	public void TbAvgFtTm()
	{
		String sql = "select PltFtStpTm from input_data_windturbine";//风电机组故障停机时间
		float Plt = du.executeselectsql(sql, 0);
		String sql1 = "select FaultCnt from input_data_windturbine";//故障次数
		float fcs =du.executeselectsql(sql1, 0);
		float Tb = Plt/fcs;
		String insertsql = "insert into windturbine_service_day (Date,TbAvgFtTm) VALUES('"+sql_date+"',"+Tb+")";
		du.executeSQL(insertsql,1);
	}
	
	//风电场平均故障停机时间 
	public void PTbAvgFtTm()
	{
		String sql = "select FaultCnt from input_data_windturbine";//故障次数
		float FC = du.executeselectsql(sql, 0);
		String sql1 = "select PltFtStpTm from input_data_windturbine";//故障停机时间
		float Pl = du.executeselectsql(sql1, 0);
		float Tb = Pl/FC;
		String insertsql = "insert into plant_service_day (Date,TbAvgFtTm) VALUES('"+sql_date+"',"+Tb+")";
		du.executeSQL(insertsql,1);
	}
	
	//风电机组平均故障容量
	public void TbAvgFtVlm()
	{
		String sql = "select TbCap from static_windturbine";//单机容量
		float[] Tb = du.executeSelectSQLtoFloat(sql,0);
		String sql1 ="select FaultTime from input_data_windturbine";//故障停机时间
		float[] FT = du.executeSelectSQLtoFloat(sql1, 0);
		float FTs=0f;
		for(int i=0;i<FT.length;i++)
			FTs += FT[i];
		float TbAvgFtVlm = FTs*Tb[0]/totaltime;
		String insertsql = "insert into windturbine_service_day (Date,TbAvgFtVlm ) VALUES('"+sql_date+"',"+TbAvgFtVlm+")";
		du.executeSQL(insertsql,1);
	}

	//风电场平均故障率
	public void PTbAvgFtVlm()
	{
		String sql = "select TbCap from static_windturbine";//单机容量
		float[] Tb = du.executeSelectSQLtoFloat(sql,0);
		String sql1 ="select FaultTime from input_data_windturbine";//故障停机时间
		float[] FT = du.executeSelectSQLtoFloat(sql1, 0);
		float FTs=0f;
		for(int i=0;i<FT.length;i++)
			FTs += FT[i];
		float TbAvgFtVlm = FTs*Tb[0]/totaltime;
//		String insertsql = "insert into plant_service_day (Date,TbAvgFtVlm ) VALUES('"+sql_date+"',"+TbAvgFtVlm+")";
//		du.executeSQL(insertsql,1);
	}
	
	//计划维护率
	public void TbPlnSvrRatio()
	{
		String sql = "select SvrStopCnt from input_data_windturbine_tag";
		float[] sv = du.executeSelectSQLtoFloat(sql, 0);
		float svs =0f;
		for(int i=0;i<sv.length;i++)
			svs += sv[i];
		String sql1 = "select distinct(Tbname)from input_data_windturbine_tag";
		float[] tb = du.executeSelectSQLtoFloat(sql1, 0);
		float TbPlnSvrRatio = svs/tb[0];
		String insertsql = "insert into windturbine_service_day (Date, TbPlnSvrRatio) VALUES('"+sql_date+"',"+TbPlnSvrRatio+")";
		du.executeSQL(insertsql,1);
	}

	//计划检修率
	public void TbPlnMntRatio()
	{
		String sql = "select RprCnt from input_data_windturbine_tag";
		float[] rp = du.executeSelectSQLtoFloat(sql, 0);
		float rps =0f;
		for(int i=0;i<rp.length;i++)
			rps += rp[i];
		String sql1 = "select distinct(Tbname)from input_data_windturbine_tag";
		float[] tb = du.executeSelectSQLtoFloat(sql1, 0);
		float TbPlnMntRatio = rps/tb[0];
		String insertsql = "insert into windturbine_service_day (Date, TbPlnMntRatio) VALUES('"+sql_date+"',"+TbPlnMntRatio+")";
		du.executeSQL(insertsql,1);
	}

	//计划停运率
	public void TbPlnOfLnRatio()
	{
		String sql = "select RprTime from input_data_windturbine_tag";
		float[] rp = du.executeSelectSQLtoFloat(sql, 0);
		float rps =0f;
		for(int i=0;i<rp.length;i++)
			rps += rp[i];
		float TbPlnOfLnRatio = rps/totaltime;
		String insertsql = "insert into windturbine_service_day (Date, TbPlnMntRatio) VALUES('"+sql_date+"',"+TbPlnOfLnRatio+")";
		du.executeSQL(insertsql,1);
	}
	
	//计划停机率
	public void TbPlnStpRatio()
	{
		String sql = "select RprCnt from input_data_windturbine_tag";//检修次数
		float[] rp = du.executeSelectSQLtoFloat(sql, 0);
		float rps =0f;
		for(int i=0;i<rp.length;i++)
			rps += rp[i];
		String sql1 = "select distinct(Tbname)from input_data_windturbine_tag";
		float[] tb = du.executeSelectSQLtoFloat(sql1, 0);
		float TbPlnStpRatio = rps/tb[0];
		String insertsql = "insert into windturbine_service_day (Date, TbPlnMntRatio) VALUES('"+sql_date+"',"+TbPlnStpRatio+")";
		du.executeSQL(insertsql,1);
	}

	//风机能量可利用率
	public void TbPwrAvlRatio()
	{
		String sql = "select PlnOffGrdWstEng from windturbine_power_day";//计划停运损失电量
		float[] pl = du.executeSelectSQLtoFloat(sql, 0);
		float pls = pl[0];
		String sql1 ="select FltOffGrdWstEng from windturbine_power_day";//故障损失电量
		float[] fl = du.executeSelectSQLtoFloat(sql1, 0);
		float fls = fl[0];
		String sql2 ="select Energy from windturbine_power_day";//风机发电量
		float[] en = du.executeSelectSQLtoFloat(sql2, 0);
		float ens = en[0];
		float TbPwrAvlRatio = ens/ens+fls+pls;
		String insertsql = "insert into windturbine_service_day (Date, TbPlnMntRatio) VALUES('"+sql_date+"',"+TbPwrAvlRatio+")";
		du.executeSQL(insertsql,0);
	}
	
	
 

}
