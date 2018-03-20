package returnOrderServlet;

import java.io.BufferedReader;
import java.io.IOException;
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
 * Servlet implementation class PayServlet
 */
@WebServlet("/returnOrderServlet")
public class ReturnOrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReturnOrderServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("不支持doGet方法");
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
		
		//拼接SQL查詢語
		 if(requestParam.getString("payResult").equals("success"))
		 {
			 	String sql = String.format("select order_number,sp_name,sp_brand,sp_color,sp_price,order_quantity,total_price,order_time from orders where order_number = ' %s' left join user ON user.user_ID = orders.user_ID left join sp on orders.sp_ID = sp.sp_ID",
				requestParam.getString("order_number"));
			 	System.out.println(sql);
		
		//自定义结果信息类
		
			 	CommonResponse res = new CommonResponse();
				try {
					ResultSet result = DatabaseUtil.query(sql);
					// 数据库查询操作
					while (result.next()) {
						HashMap<String, String> map = new HashMap<>();
					
						map.put("order_number", result.getString("order_number"));
						map.put("sp_name", String.valueOf(result.getString(("sp_name"))));
						map.put("sp_brand", result.getString("sp_brand"));			
					   // map.put("sp_picture", ImageUtil.imageToBase64(result.getString("sp_picture")));
						map.put("sp_color", String.valueOf(result.getString("sp_color")));
						map.put("sp_price",String.valueOf(result.getString("order_quantity")));
						map.put("order_quantity", String.valueOf(result.getString("order_quantity")));
						map.put("total_price", String.valueOf(result.getString("total_price")));
						map.put("order_time", String.valueOf(result.getString("order_time")));
						
						Set<Map.Entry<String, String>> entry = map.entrySet();

						for(Map.Entry<String, String> e : entry) {
						System.out.println("key:"+e.getKey()+"-value:"+e.getValue());
						}
						res.addListItem(map);
						for(HashMap<String, String> e : res.getArrayList()) {
							System.out.println("key:"+e.get("sp_name"));
						}
					}
					res.setResult("0",URLEncoder.encode("成功返回","utf-8") ); // 这个不能忘了，表示业务结果正确
				} catch (SQLException e) {
					res.setResult("300", URLEncoder.encode("数据库查询错误","utf-8"));
					e.printStackTrace();
				}

				String resStr = JSONObject.fromObject(res).toString();
				System.out.println(resStr);
				response.getWriter().append(resStr).flush();
			
			}


}
}
