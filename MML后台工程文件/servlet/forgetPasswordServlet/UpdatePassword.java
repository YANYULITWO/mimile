package forgetPasswordServlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLEncoder;
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
 * Servlet implementation class UpdatePassword
 */
@WebServlet(description = "更新密码", urlPatterns = { "/updatePassword" })
public class UpdatePassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdatePassword() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		System.out.println("不支持DoGet方法");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		  BufferedReader bufferedReader = request.getReader();
		StringBuilder stringBuilder = new StringBuilder();
		   String line = null;
		 while((line =bufferedReader.readLine())!=null )
		 {
			 stringBuilder.append(line);
			 
		 }
		 String reqtemp =stringBuilder.toString();
		 System.out.println(reqtemp);
		// JSONObject object = JSONObject.fromObject(req);
		 JSONObject object = JSONObject.fromObject(reqtemp);
			String requestCode = object.getString("requestCode");
			JSONObject requestParam = object.getJSONObject("requestParam");
		 String updatePwd  = String.format("update %s set user_password = '%s' where user_phone ='%s'", 
				 DBNames.Table_User,
				 requestParam.getString("user_password"),
				 requestParam.getString("user_phone"));
		System.out.println(updatePwd );
		 CommonResponse response2 = new CommonResponse();
		 try{
		 if(DatabaseUtil.update(updatePwd) ==1)
		 {
			 response2.setResult("0", URLEncoder.encode("更改密码成功","utf-8"));
		 }
		 else {
			 response2.setResult("105",URLEncoder.encode("更改密码失败","utf-8"));
		 }
			
	}catch (SQLException e) {
		response2.setResult("300", "数据库查询错误");
		e.printStackTrace();
	}
		String resStr = JSONObject.fromObject(response2).toString();
		System.out.println(resStr);
		response.getWriter().append(resStr).flush();

}
}
