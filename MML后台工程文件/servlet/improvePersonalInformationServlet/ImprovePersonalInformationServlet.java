package improvePersonalInformationServlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
 * Servlet implementation class ImprovePersonalInformationServlet
 */
@WebServlet(description = "���Ƹ�����Ϣ", urlPatterns = { "/improvePersonalInformationServlet" })
public class ImprovePersonalInformationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ImprovePersonalInformationServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("��֧��doGet ����");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 BufferedReader read = request.getReader();
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = read.readLine()) != null) {
				sb.append(line);
			}
			String req = sb.toString();
			System.out.println(req);
			JSONObject object = JSONObject.fromObject(req);
			// requestCode��ʱ�ò���
			// ע���±��õ���2���ֶ�����requestCode��requestParamҪ�Ϳͻ���CommonRequest��װʱ�������һ��
			String requestCode = object.getString("requestCode");
			JSONObject requestParam = object.getJSONObject("requestParam");
	       String user_phone=URLDecoder.decode(requestParam.getString("user_phone"),"utf-8");
	       //�������������ʾ����ҳ��
	       String user_name=URLDecoder.decode(requestParam.getString("user_name"),"utf-8");
	       String user_sex = URLDecoder.decode(requestParam.getString("user_sex"),"utf-8");
	       String user_birthday= URLDecoder.decode(requestParam.getString("user_birthday"),"utf-8");
	       String address= URLDecoder.decode(requestParam.getString("address"),"utf-8");
	       String sql = String.format("update %s set user_name = %s ,user_sex =%s,user_birthday = %s, user_address = %s where user_phone = %s", DBNames.Table_User,user_name,user_sex,user_birthday,address);
	      
	       
	       System.out.println(sql);
	    	   
			
			
	       CommonResponse response2 = new CommonResponse();
			 try{
			 if(DatabaseUtil.update(sql) ==1)
			 {
				 response2.setResult("0", URLEncoder.encode("�����û���Ϣ�ɹ�","utf-8"));
			 }
			 else {
				 response2.setResult("105",URLEncoder.encode("�����û���Ϣʧ��","utf-8"));
			 }
				
		}catch (SQLException e) {
			response2.setResult("300", "���ݿ��ѯ����");
			e.printStackTrace();
		}
			String resStr = JSONObject.fromObject(response2).toString();
			System.out.println(resStr);
			response.getWriter().append(resStr).flush();

	}
	}