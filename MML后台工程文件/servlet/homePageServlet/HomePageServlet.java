package homePageServlet;

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

import com.sun.org.apache.bcel.internal.generic.Select;

import beans.CommonResponse;
import constants.DBNames;
import jdk.internal.org.objectweb.asm.tree.IntInsnNode;
import net.sf.json.JSONObject;
import util.DatabaseUtil;

/**
 * Servlet implementation class HomePageServlet
 */
@WebServlet(description = "首页默认显示", urlPatterns = { "/homePageServlet" })
public class HomePageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HomePageServlet() {
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
	//生成不重复的ID
	public static int[] randomCommon(int min,int max, int n)
	{
		if(n>(max - min +1)|| max <min){
			return null;
		}
		int[] result = new int[n];
		int count = 0;
		while(count <n)
		{
			int num =(int)(Math.random()*(max-min))+min;
			boolean flag = true;
			for(int j =0; j <n; j++){
				if(num ==result[j]){
					flag = false;
					break;
				}
			}
			if(flag)
			{
				result[count] = num;
				count++;
			}
				
		}
		return result;
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

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
			// requestCode暂时用不上
			// 注意下边用到的2个字段名称requestCode、requestParam要和客户端CommonRequest封装时候的名字一致
			String requestCode = object.getString("requestCode");
			JSONObject requestParam = object.getJSONObject("requestParam");
	       //String string =URLDecoder.decode(requestParam.getString(""),"utf-8");
	       //生成随机数，显示在主页面
			
			CommonResponse res = new CommonResponse();
		
			
			try {
	      int[] SID = randomCommon(1, 188, 36);
	      for(int i = 0; i<SID.length; i++){
	       String sql = String.format("select id,name,img,zpmcc,price from %s where id = '%s'",DBNames.Table_SP,SID[i]);
	       
	       System.out.println(sql);
	    	   
	   	ResultSet result = DatabaseUtil.query(sql);
		
				// 数据库查询操作
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

					/*for(Map.Entry<String, String> e : entry) {
					System.out.println("key:"+e.getKey()+"-value:"+e.getValue());
					}*/
					res.addListItem(map);
					/*for(HashMap<String, String> e : res.getArrayList()) {
						System.out.println("key:"+e.get("pp"));
					}*/
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
