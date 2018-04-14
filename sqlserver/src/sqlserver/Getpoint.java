package sqlserver;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Getpoint {
	private static String driver;
	private static String url;
	private static String username;
	private static String password; 
	//public Connection conn;
	// 用于连接opplant数据库
	private static String driver2;
	private static String url2;
	private static String username2;
	private static String password2;
	public static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
	static Calendar cal = Calendar.getInstance();
	public static int day_of_month = cal.get(Calendar.DAY_OF_MONTH);
	static int deadline = day_of_month-1;
	public static String GN = "3";
	static {
		Properties prop = new Properties();
		InputStream in;
		try {
			in = new FileInputStream("src\\config.properties");
			prop.load(in);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		driver = prop.getProperty("driver");
		url = prop.getProperty("url");
		username = prop.getProperty("username");
		password = prop.getProperty("password");

		driver2 = prop.getProperty("driver2");
		url2 = prop.getProperty("url2");
		username2 = prop.getProperty("username2");
		password2 = prop.getProperty("password2");
		// driver="com.mysql.jdbc.Driver";
		// url="jdbc:mysql://127.0.0.1:3306/TestDB";
		// username="root";
		// password="123456";
	}
    //连接sql数据库
	public static Connection open() {
		// 加载数据库驱动
		try {
			Class.forName(driver);
			System.out.println("db connection success---");
			return DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
	//连接openplant数据库
	public static Connection open2() {
		// 加载数据库驱动
		try {
			Class.forName(driver2);
			System.out.println("opdb connection success---");
			return DriverManager.getConnection(url2, username2, password2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public static void close(Connection conn) {
		if (conn != null)
			try {
				conn.close();
				System.out.println("db close success---");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	/** ***********执行sql语句，用于insert、update语句 ***************/
	/**
	 * 从指定参数得到一个连接对象
	 * 
	 * @param sql
	 *            sql语句
	 * @param sqltype
	 *            数据库类型，0为sqlserver,1位openplant
	 */
	protected void executeSQL(String sql, int sqltype) {
		Connection conn;
		if (sqltype == 1) {
			conn = open();
		} else {
			conn = open2();
		}
		try {
			System.out.println("sql:" + sql);

			Statement st = conn.createStatement();
			boolean success = st.execute(sql);
			if (success) {
				System.out.println("操作成功");
			} else {
				System.out.println("error");
			}
			st.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(conn);
		}
	}

	/** ***********执行select sql语句 ***************/
	/**
	 * 从指定参数得到一个连接对象
	 * 
	 * @param sql
	 *            sql语句
	 * @param sqltype
	 *            数据库类型，0为sqlserver,1位openplant 返回值为ResultSet类型
	 */
	public ResultSet executeSelectSQLtoRS(String sql, int sqltype) {
		ResultSet rs = null;
		Connection conn;
		if (sqltype == 0) {
			conn = open();
		} else {
			conn = open2();
		}
		System.out.println("sql:" + sql);
		try {
			Statement st = conn.createStatement();
			rs = st.executeQuery(sql);
			// rs.close();
			// st.close();
			// conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;

	}

	/** ***********执行select sql语句 ***************/
	/**
	 * 从指定参数得到一个连接对象
	 * 
	 * @param sql
	 *            sql语句
	 * @param sqltype
	 *            数据库类型，0为sqlserver,1位openplant 返回值为Float数组
	 */
	protected float[] executeSelectSQLtoFloat(String sql, int sqltype) {
		Connection conn;
		if (sqltype == 0) {
			conn = open();
		} else {
			conn = open2();
		}
		ArrayList<Float> valueList = new ArrayList<Float>();
		float[] value ={9999999};
		try {
			System.out.println("sql:" + sql);
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			/** -----获得列数---------- */
			// ResultSetMetaData rsmd = rs.getMetaData();
			// int columnCount=rsmd.getColumnCount();
			while (rs.next()) {
				valueList.add(rs.getFloat(1));
			}
			rs.close();
			st.close();
			conn.close();
		} catch (Exception e) {
			System.out.println("wrong");
			e.printStackTrace();
		}
		
			for (int i = 0; i < valueList.size(); i++) {
				value[i] = valueList.get(i);
			}

		
		return value;

	}

	/** ***********执行select sql语句 ***************/
	/**
	 * 从指定参数得到一个连接对象
	 * 
	 * @param sql
	 *            sql语句
	 * @param sqltype
	 *            数据库类型，0为sqlserver,1位openplant 返回值为String数组
	 */
	@SuppressWarnings("finally")
	protected String[] executeSelectSQLtoStrsz(String sql, int sqltype) {
		Connection conn;
		if (sqltype == 0) {
			conn = open();
		} else {
			conn = open2();
		}
		ArrayList<String> nameList = new ArrayList<String>();
		String[] nameStr;
		try {
			System.out.println("sql:" + sql);
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			/** -----获得列数---------- */
			// ResultSetMetaData rsmd = rs.getMetaData();
			// int columnCount=rsmd.getColumnCount();
			while (rs.next()) {
				nameList.add(rs.getString(1));
			}
			rs.close();
			st.close();
			conn.close();

		} catch (Exception e) {
			System.out.println("wrong");
			e.printStackTrace();
		} finally {
			nameStr = (String[]) nameList.toArray(new String[nameList.size()]);
			return nameStr;
		}
	}
	/** ***********执行select sql语句 ***************/
	/**
	 * 从指定参数得到一个连接对象
	 * 
	 * @param sql     sql语句
	 * @param sqltype    数据库类型，0为sqlserver,1位openplant 返回值为list
	 */
	protected List executeSelectSQLtoList(String sql, int sqltype) {
		Connection conn;
		if (sqltype == 0) {
			conn = open();
		} else {
			conn = open2();
		}
		List ls=new ArrayList();
		String[] nameStr;
		try {
			System.out.println("sql:" + sql);
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			/** -----获得列数---------- */
			if(rs==null){
				return Collections.EMPTY_LIST;
			}
			 ResultSetMetaData rsmd = rs.getMetaData();
			 int columnCount=rsmd.getColumnCount();
			while (rs.next()) {
				Map rowData=new HashMap();
				for(int i=1;i<=columnCount;i++){
					rowData.put(rsmd.getCatalogName(i), rs.getObject(i));
				}
				ls.add(rowData);
			}
			rs.close();
			st.close();
			conn.close();

		} catch (Exception e) {
			System.out.println("wrong");
			e.printStackTrace();
		} 
		return ls;
	}
	
	
	//update
	public void executeUpdateSQL(String sql,int sqltype)
	{
		Connection conn = null;
		if(sqltype == 0)
			conn = open();
		else if(sqltype ==1)
			conn = open2();
		
		sql = "update ? set ? =? where ?=?";//(表名，列名，列值，查询列名，查询列值)
		try {
			PreparedStatement pre = conn.prepareStatement(sql,sqltype);
			pre.setString(1,"表名");
			ResultSet rs = pre.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//执行sql查询，返回float和
	public float executeselectsql(String sql,int sqltype)
	{
		Connection conn = null;
		if(sqltype==0)
			conn =open();
		else if(sqltype==1)
			conn = open2();
		System.out.println("sql:" + sql);
		Statement st;
		float fs =0f;
		try {
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			int i=0;
			while(rs.next())
			{
				i++;
				fs += rs.getFloat(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fs;
	}
}
