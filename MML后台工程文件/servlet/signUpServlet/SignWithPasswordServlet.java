


package signUpServlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.spi.DirStateFactory.Result;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.log.Log;

import beans.CommonResponse;
import constants.DBNames;
import net.sf.json.JSONObject;
import util.DatabaseUtil;
import util.LogUtil;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet(description = "注册", urlPatterns = { "/signWithPasswordServlet" })
public class SignWithPasswordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public SignWithPasswordServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("不支持GET方法;");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		BufferedReader read = request.getReader();
		StringBuilder stringBuilder = new StringBuilder();
		String line = null;
		while((line = read.readLine())!=null)
		{
			stringBuilder.append(line);   
		}
		
		String req = stringBuilder.toString(); //将请求拼接成为一个字符串
		
		System.out.println(req);
	    
		//第一步，获取客户端发来的请求，恢复其Json格式
		JSONObject object = JSONObject.fromObject(req);
		String requestCode = object.getString("requestCode");
		JSONObject requestParam = object.getJSONObject("requestParam");
		
	/*	第二步，将json转化为别的数据结构，进行业务处理，生成结果*/
		
		//拼接SQL查詢語句
		 
		String sql = String.format("select * from %s where user_phone = '%s'",
				DBNames.Table_User,
				requestParam.getString("user_phone"));
		System.out.println(sql);
		
		//自定义结果信息类
		
		CommonResponse res = new CommonResponse();
		try{
			ResultSet resultSet = DatabaseUtil.query(sql);
			if(resultSet.next())
			{
				if(resultSet.getString("user_phone").equals(requestParam.getString("user_phone")))
				{
					
					 res.setResult("100", URLEncoder.encode("该手机已经注册","utf-8"));
				}
			}else{
				String sqlInsertUser =String.format("insert into %s  (user_phone,user_password) values ('%s','%s')",
					DBNames.Table_User,
					requestParam.getString("user_phone"),
					requestParam.getString("user_password"));
				System.out.println(sqlInsertUser);
				int result = DatabaseUtil.update(sqlInsertUser);
				if(result ==1)
				{
					res.setResult("0",URLEncoder.encode("注册成功","utf-8"));
				}else{
				    res.setResult("101", URLEncoder.encode("注册失败","utf-8"));
				}
			}
		
		
		
		
	}catch (SQLException e) {
		res.setResult("300", "数据库查询错误");
		e.printStackTrace();
	}
		String resStr = JSONObject.fromObject(res).toString();
		System.out.println(resStr);
		response.getWriter().append(resStr).flush();
	}
}
	
	
