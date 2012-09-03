package test.example;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.hetty.jdbc.DbUtils;
import com.hetty.jdbc.HConnection;
import com.hetty.jdbc.HConnection.Transaction;

public class HelloImpl implements Hello {

	@Override
	public String hello(String name) {
		//throw new RuntimeException("Have Error!");
		System.out.println("Hello, "+name+"!");
		
		return "Hello1, "+name+"!";
	}

	@Override
	public User getUser(int id) {
		{
			User u=new User();
			u.setAge(1);
			u.setEmail("zhuzhsh@163.com");
			u.setId(1);
			u.setName("zhuzhsh");
			for(int i=0;i<5;i++){
				Role r=new Role();
				r.setName("role"+i);
				r.setDescription("role"+i);
				u.addRole(r);
			}
			return u;
		}
	}

	@Override
	public String getAppSecret(String key) {
		HConnection cnn=DbUtils.getConnection();
		Transaction tra=cnn.getTransaction();
		String secret=null;
		try {
			tra.begin();
			String sql="select * from bcs_apps where app_key=? limit 0,1";
			//Statement smt=cnn.createStatement();
			PreparedStatement ps=cnn.prepareStatement(sql);
			ps.setString(1, key);
			ResultSet rs=ps.executeQuery();
			
			while(rs.next()){
				secret=rs.getString("app_secret");
				//System.out.println(secret);
			}
			rs.close();
			tra.end();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				cnn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return secret;
	}

}
