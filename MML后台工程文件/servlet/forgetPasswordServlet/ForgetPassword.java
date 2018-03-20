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
		System.out.println("��֧��Get����");
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
		
		String req = stringBuilder.toString(); //������ƴ�ӳ�Ϊһ���ַ���
		
		System.out.println(req);
	    
		//��һ������ȡ�ͻ��˷��������󣬻ָ���Json��ʽ
		JSONObject object = JSONObject.fromObject(req);
		String requestCode = object.getString("requestCode");
		JSONObject requestParam = object.getJSONObject("requestParam");
		
	/*	�ڶ�������jsonת��Ϊ������ݽṹ������ҵ�������ɽ��*/
		
		//ƴ��SQL��ԃ�Z��
		 
		String sql = String.format("select * from %s where user_phone = '%s'",
				DBNames.Table_User,
				requestParam.getString("user_phone"));
		System.out.println(sql);
		
		//�Զ�������Ϣ��
		
		CommonResponse res = new CommonResponse();
		try{
			ResultSet resultSet = DatabaseUtil.query(sql);
			if(resultSet.next())
			{
				if(resultSet.getString("user_phone").equals(requestParam.getString("user_phone")))
				{
					
					 res.setResult("0", URLEncoder.encode("���ֻ�ע��","utf-8"));
				}
			}else{/*
				String sqlInsertUser =String.format("insert into %s  (user_phone) values ('%s')",
					DBNames.Table_User,
					requestParam.getString("user_phone"));
				System.out.println(sqlInsertUser);
				int result = DatabaseUtil.update(sqlInsertUser);
				if(result ==1)
				{
					res.setResult("0",URLEncoder.encode("ע��chenggong","utf-8"));
				}else{
				    res.setResult("101", URLEncoder.encode("ע��ʧ��","utf-8"));
				}*/
				res.setResult("100",URLEncoder.encode("δע�ᣬ����ע��","utf-8"));
			}
		
		
		
		
	}catch (SQLException e) {
		res.setResult("300", "���ݿ��ѯ����");
		e.printStackTrace();
	}
		String resStr = JSONObject.fromObject(res).toString();
		System.out.println(resStr);
		response.getWriter().append(resStr).flush();
	}
	



}
