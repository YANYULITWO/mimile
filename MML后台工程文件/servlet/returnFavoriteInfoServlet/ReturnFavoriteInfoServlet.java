package returnFavoriteInfoServlet;

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

import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import beans.CommonResponse;
import constants.DBNames;
import net.sf.json.JSONObject;
import util.DatabaseUtil;

/**
 * Servlet implementation class ReturnFavoriteInfoServlet
 */
@WebServlet(description = "�����ղؼ���Ϣ", urlPatterns = { "/returnFavoriteInfoServlet" })
public class ReturnFavoriteInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReturnFavoriteInfoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("��֧��doGet����");
		
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
			
			String user_phone = requestParam.getString("user_phone");
			/*String sql = String.format("select id, name,img,price,zpmcc from %s where %s >= %s and %s < %s", 
					DBNames.Table_SP,
					sqlType,
					lower,
					sqlType,
					upper);*/
				
			//System.out.println(sql);
			
			String sql = String.format("select b.id,b.name,b.img,b.zpmcc,b.price from %s a inner join %s b where a.sp_ID = b.ID and a.user_phone = '%s' order by a.id desc ",
					
					DBNames.Table_favorite,
					DBNames.Table_SP,
					user_phone);
			System.out.println(sql);
			
		
			CommonResponse res = new CommonResponse();
			try {
				ResultSet result = DatabaseUtil.query(sql);
				// ���ݿ��ѯ����
				while (result.next()) {
					HashMap<String, String> map = new HashMap<>();
					//System.out.println(result.getString("sp_name"));
					map.put("name", result.getString("name"));
					map.put("id", result.getString("id"));
					map.put("img", result.getString("img"));	
					map.put("zpmcc", result.getString("zpmcc"));	
				   // map.put("sp_picture", ImageUtil.imageToBase64(result.getString("sp_picture")));
					map.put("price", result.getString("price"));
					Set<Map.Entry<String, String>> entry = map.entrySet();

				/*	for(Map.Entry<String, String> e : entry) {
					System.out.println("key:"+e.getKey()+"-value:"+e.getValue());
					}*/
					res.addListItem(map);
				/*	for(HashMap<String, String> e : res.getArrayList()) {
						System.out.println("key:"+e.get("pp"));
					}*/
				}
				res.setResult("0",URLEncoder.encode("�ɹ�����","utf-8") ); // ����������ˣ���ʾҵ������ȷ
			} catch (SQLException e) {
				res.setResult("300", URLEncoder.encode("���ݿ��ѯ����","utf-8"));
				e.printStackTrace();
			}

			String resStr = JSONObject.fromObject(res).toString();
			System.out.println(resStr);
			response.getWriter().append(resStr).flush();
		
		}

	}


