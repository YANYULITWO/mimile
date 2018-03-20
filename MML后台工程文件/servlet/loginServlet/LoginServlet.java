package loginServlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import beans.CommonRequest;
import beans.CommonResponse;
import constants.DBNames;
//import encrypt.DecryptUtil;
//import encrypt.EncryptUtil;
import net.sf.json.JSONObject;
import util.DatabaseUtil;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet(description = "��¼", urlPatterns = { "/loginServlet" })
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("��֧��GET����;");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		BufferedReader read = request.getReader();
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = read.readLine()) != null) {
			sb.append(line);
		}

//		String req = DecryptUtil.getEDSDecryptStr(sb.toString()); // ����ͻ��������ļ�����
		String req = sb.toString();
		System.out.println(req);
		
		// ��һ������ȡ �ͻ��� ���������󣬻ָ���Json��ʽ����>��Ҫ�ͻ��˷�����ʱҲ��װ��Json��ʽ
		JSONObject object = JSONObject.fromObject(req);
		// requestCode��ʱ�ò���
		// ע���±��õ���2���ֶ�����requestCode��requestParamҪ�Ϳͻ���CommonRequest��װʱ�������һ��
		String requestCode = object.getString("requestCode");
		JSONObject requestParam = object.getJSONObject("requestParam");
		

		// �ڶ�������Jsonת��Ϊ������ݽṹ����ʹ�û���ֱ��ʹ�ã��˴�ֱ��ʹ�ã�������ҵ�������ɽ��
		// ƴ��SQL��ѯ���
		String sql = String.format("SELECT * FROM %s WHERE user_phone='%s'", 
				DBNames.Table_User, 
				requestParam.getString("user_phone"));
		System.out.println(sql);

		// �Զ���Ľ����Ϣ��
		CommonResponse res = new CommonResponse();
		try {
			ResultSet result = DatabaseUtil.query(sql); // ���ݿ��ѯ����
//			result.getRow();
			
			if (result.next()) {
				if (result.getString("user_password").equals(requestParam.getString("user_password"))) {
					res.setResult("0", "��½�ɹ�");

				} else {
					res.setResult("100", URLEncoder.encode("��¼ʧ�ܣ��������","utf-8"));
				}
			} else {
				res.setResult("101", URLEncoder.encode("���˺�δע��","utf-8"));
			}
		} catch (SQLException e) {
			res.setResult("102", URLEncoder.encode("���ݿ��ѯ����","utf-8"));
			e.printStackTrace();
		}

		// ���������������װ��Json��ʽ׼�����ظ��ͻ��ˣ���ʵ�����紫��ʱ���Ǵ���json���ַ���
		// ������֮ǰ��String����һ����ֻ��Json�ṩ���ض����ַ���ƴ�Ӹ�ʽ
		// ��Ϊ�����JSon���õ�����ĵ�����JSon��������ǿ�󣬲�����Android�������Լ��ֶ�ת��ֱ�ӿ��Դ�Beanת��JSon��ʽ
		String resStr = JSONObject.fromObject(res).toString();
		
		System.out.println(resStr);
//		response.getWriter().append(EncryptUtil.getEDSEncryptStr(resStr)); // ���Զ��ַ������м��ܲ�������Ӧ�ĵ��˿ͻ��˾���Ҫ����
		response.getWriter().append(resStr).flush();
	}

}
