package forgetPasswordServlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.CommonResponse;
import constants.DBNames;
import net.sf.json.JSONObject;
import util.DatabaseUtil;

/**
 * Servlet implementation class ForgetPassword
 */
@WebServlet("/forgetPassword")
public class ForgetPassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ForgetPassword() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		System.out.println("不支持Get方法");
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
					
					 res.setResult("0", URLEncoder.encode("该手机注册","utf-8"));
				}
			}else{/*
				String sqlInsertUser =String.format("insert into %s  (user_phone) values ('%s')",
					DBNames.Table_User,
					requestParam.getString("user_phone"));
				System.out.println(sqlInsertUser);
				int result = DatabaseUtil.update(sqlInsertUser);
				if(result ==1)
				{
					res.setResult("0",URLEncoder.encode("注册chenggong","utf-8"));
				}else{
				    res.setResult("101", URLEncoder.encode("注册失败","utf-8"));
				}*/
				res.setResult("100",URLEncoder.encode("未注册，请先注册","utf-8"));
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
