/**
 * 对数据库操作的类
 */
package DBHelper;
import java.sql.*;

public class SqlHelper {

	PreparedStatement ps = null;
	ResultSet rs = null;
	Connection ct = null;
	
	String driver = "com.mysql.jdbc.Driver";
	String url="jdbc:mysql://localhost/hotel?user=root&password=12345";

	
	//构造函数初始化ct
	
	public boolean  update(String sql,String[]paras)
	{
		boolean b = true;
		try{
			Class.forName(driver);
			ct = DriverManager.getConnection(url);
			ps = ct.prepareStatement(sql);
			for(int i=0;i<paras.length;i++)
			{
				ps.setString(i+1, paras[i]);
			}
			if(ps.executeUpdate()!=1)
			{
				b = false;
			}		
		}catch(Exception ex){
			ex.printStackTrace();
			
		}finally{
			this.close();
		}
		return b;
	}
	
	
	public SqlHelper()
	{
		try{
			Class.forName(driver);
			ct = DriverManager.getConnection(url);
		//	ps = ct.prepareStatement(sql);
		//	rs = ps.executeQuery();
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			
		}	
	}
	
	public ResultSet query(String sql,String[] paras)//查询操作
	{
		try {
			ps = ct.prepareStatement(sql);
			//对sql的参数赋值
			for(int i = 0;i < paras.length;i++)
			{
				ps.setString(i+1, paras[i]);
			}
			rs = ps.executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rs;
	}
	public ResultSet query(String sql,String paras)//查询操作
	{
		try {
			ps = ct.prepareStatement(sql);
				ps.setString(1,paras);

			rs = ps.executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rs;
	}
	//关闭资源
	public void close()
	{
		try {
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
			if(ct!=null) ct.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
