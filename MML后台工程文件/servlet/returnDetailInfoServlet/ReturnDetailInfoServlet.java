package returnDetailInfoServlet;

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

import com.sun.jmx.snmp.SnmpStringFixed;

import beans.CommonResponse;
import constants.DBNames;
import constants.VariableNumber;
import net.sf.json.JSONObject;
import util.DatabaseUtil;

/**
 * Servlet implementation class ReturnDetailInfoServlet
 */
@WebServlet(description = "返回商品的详细信息", urlPatterns = { "/returnDetailInfoServlet" })
public class ReturnDetailInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReturnDetailInfoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("不支持doGet");
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
			// requestCode暂时用不上
			// 注意下边用到的2个字段名称requestCode、requestParam要和客户端CommonRequest封装时候的名字一致
			String requestCode = object.getString("requestCode");
			JSONObject requestParam = object.getJSONObject("requestParam");
	       VariableNumber.SPID =URLDecoder.decode(requestParam.getString("sp_id"),"utf-8");
	       VariableNumber.Userphone = URLDecoder.decode(requestParam.getString("user_phone"),"utf-8");
	       System.out.println(VariableNumber.Userphone);
	       if(!(VariableNumber.Userphone=="")){
	       String sqlExit = String.format("select * from %s where user_phone = %s and sp_Id = %s", DBNames.Table_History,VariableNumber.Userphone,VariableNumber.SPID);
		   System.out.println(sqlExit);
		      try {
					ResultSet set = DatabaseUtil.query(sqlExit);
					System.out.println(set);
					if(set.next()){
					
					      String deleteInfo = String.format("delete from %s where user_phone = '%s' and sp_ID = '%s'", DBNames.Table_History,VariableNumber.Userphone,VariableNumber.SPID );
					      
					      int  number = DatabaseUtil.update(deleteInfo);
					      if(!(number == 0))
					      {
					    	  System.out.println("删除重复记录成功");
					      }else{
					    	  System.out.println("删除重复记录失败");
					      }
					}
				} catch (SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
		    
	       //生成随机数，显示在主页面
	       String insertHistorysql  = String.format("insert  into %s (user_phone, sp_Id ) values('%s','%s')", DBNames.Table_History,VariableNumber.Userphone,VariableNumber.SPID);
		      System.out.println(insertHistorysql);
		       try {
				int inser = DatabaseUtil.update(insertHistorysql);
				if(inser == 1)
				{
					System.out.println("数据插入成功");
				}else{
					System.out.println("数据插入失败");
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		      
	       String sql = String.format("select name,img,price,pp,xh,ssnf,ssyf,jsys,jscd,jskd,jshd,jszl,srfs,jscz,czxt,czxtbb,cpupp,cpupl,cpuhs,cpuxh,skjlx,zdzcsimksl,simklx,fourgwl,threegtwogwl,rom,ram,cck,zdcckzrl,zpmcc,fbl,pmxsmd,pmczlx,qzsxt,sxtsl,hzsxt,dcrl,dcsfkcx,cdq,sjcsjk,nfc,ejjklx,cdjklx,zwsb,gps,tly,cygn,sxtgqdx from %s where id = '%s'",DBNames.Table_SP,VariableNumber.SPID);
			CommonResponse res = new CommonResponse();
			try {
				ResultSet result = DatabaseUtil.query(sql);
				// 数据库查询操作
				while (result.next()) {
					HashMap<String, String> map = new HashMap<>();
					//System.out.println(result.getString("sp_name"));
					map.put("name", result.getString("name"));
					map.put("price", result.getString("price"));
					map.put("img", result.getString("img"));	
					map.put("pp", result.getString("pp"));
					map.put("xh", result.getString("xh"));
					map.put("ssnf", result.getString("ssnf"));
					map.put("ssyf", result.getString("ssyf"));
					map.put("jsys", result.getString("jsys"));
					map.put("jscd", result.getString("jscd"));
					map.put("jskd", result.getString("jskd"));
					//这是10个字段
					map.put("jshd", result.getString("jshd"));
					map.put("jszl", result.getString("jszl"));
					map.put("srfs", result.getString("srfs"));
					map.put("jscz", result.getString("jscz"));
					map.put("czxt", result.getString("czxt"));
					map.put("czxtbb", result.getString("czxtbb"));
					map.put("cpupp", result.getString("cpupp"));
					map.put("cpupl", result.getString("cpupl"));
					map.put("cpuhs", result.getString("cpuhs"));
					map.put("cpuxh", result.getString("cpuxh"));
					//这是20个字段
					map.put("skjlx", result.getString("skjlx"));
					map.put("zdzcsimksl", result.getString("zdzcsimksl"));
					map.put("simklx", result.getString("simklx"));
					map.put("fourgwl", result.getString("fourgwl"));
					map.put("threegtwogwl", result.getString("threegtwogwl"));
					map.put("rom", result.getString("rom"));
					map.put("ram", result.getString("ram"));
					map.put("cck", result.getString("cck"));
					map.put("zdcckzrl", result.getString("zdcckzrl"));
					map.put("zpmcc", result.getString("zpmcc"));
					//这是第30个字段
					map.put("fbl", result.getString("fbl"));
					map.put("pmxsmd", result.getString("pmxsmd"));
					map.put("pmczlx", result.getString("pmczlx"));
					map.put("qzsxt", result.getString("qzsxt"));
					map.put("sxtsl", result.getString("sxtsl"));
					map.put("hzsxt", result.getString("hzsxt"));
					map.put("dcrl", result.getString("dcrl"));
					map.put("dcsfkcx", result.getString("dcsfkcx"));
					map.put("cdq", result.getString("cdq"));
					map.put("sjcsjk", result.getString("sjcsjk"));
					//这是第40个字段
					map.put("nfc", result.getString("nfc"));
					map.put("ejjklx",result.getString("ejjklx"));
					map.put("cdjklx",result.getString("cdjklx"));
					map.put("zwsb",result.getString("zwsb"));
					map.put("gps",result.getString("gps"));
					map.put("tly",result.getString("tly"));
					map.put("cygn",result.getString("cygn"));
					map.put("sxtgqdx",result.getString("sxtgqdx"));
					//这是第48个字段


				   // map.put("sp_picture", ImageUtil.imageToBase64(result.getString("sp_picture")));
					//map.put("price", result.getString("price"));
					Set<Map.Entry<String, String>> entry = map.entrySet();

					/*for(Map.Entry<String, String> e : entry) {
					System.out.println("key:"+e.getKey()+"-value:"+e.getValue());
					}*/
					res.addListItem(map);
					/*for(HashMap<String, String> e : res.getArrayList()) {
						System.out.println("key:"+e.get("pp"));
					}*/
				}
				res.setResult("0",URLEncoder.encode("成功返回","utf-8") ); // 这个不能忘了，表示业务结果正确
			} catch (SQLException e) {
				res.setResult("300", URLEncoder.encode("数据库查询错误","utf-8"));
				e.printStackTrace();
			}

			String resStr = JSONObject.fromObject(res).toString();
			System.out.println(resStr);
			response.getWriter().append(resStr).flush();
	       }else{
	    	   String sql = String.format("select name,img,price,pp,xh,ssnf,ssyf,jsys,jscd,jskd,jshd,jszl,srfs,jscz,czxt,czxtbb,cpupp,cpupl,cpuhs,cpuxh,skjlx,zdzcsimksl,simklx,fourgwl,threegtwogwl,rom,ram,cck,zdcckzrl,zpmcc,fbl,pmxsmd,pmczlx,qzsxt,sxtsl,hzsxt,dcrl,dcsfkcx,cdq,sjcsjk,nfc,ejjklx,cdjklx,zwsb,gps,tly,cygn,sxtgqdx from %s where id = '%s'",DBNames.Table_SP,VariableNumber.SPID);
				CommonResponse res = new CommonResponse();
				try {
					ResultSet result = DatabaseUtil.query(sql);
					// 数据库查询操作
					while (result.next()) {
						HashMap<String, String> map = new HashMap<>();
						//System.out.println(result.getString("sp_name"));
						map.put("name", result.getString("name"));
						map.put("price", result.getString("price"));
						map.put("img", result.getString("img"));	
						map.put("pp", result.getString("pp"));
						map.put("xh", result.getString("xh"));
						map.put("ssnf", result.getString("ssnf"));
						map.put("ssyf", result.getString("ssyf"));
						map.put("jsys", result.getString("jsys"));
						map.put("jscd", result.getString("jscd"));
						map.put("jskd", result.getString("jskd"));
						//这是10个字段
						map.put("jshd", result.getString("jshd"));
						map.put("jszl", result.getString("jszl"));
						map.put("srfs", result.getString("srfs"));
						map.put("jscz", result.getString("jscz"));
						map.put("czxt", result.getString("czxt"));
						map.put("czxtbb", result.getString("czxtbb"));
						map.put("cpupp", result.getString("cpupp"));
						map.put("cpupl", result.getString("cpupl"));
						map.put("cpuhs", result.getString("cpuhs"));
						map.put("cpuxh", result.getString("cpuxh"));
						//这是20个字段
						map.put("skjlx", result.getString("skjlx"));
						map.put("zdzcsimksl", result.getString("zdzcsimksl"));
						map.put("simklx", result.getString("simklx"));
						map.put("fourgwl", result.getString("fourgwl"));
						map.put("threegtwogwl", result.getString("threegtwogwl"));
						map.put("rom", result.getString("rom"));
						map.put("ram", result.getString("ram"));
						map.put("cck", result.getString("cck"));
						map.put("zdcckzrl", result.getString("zdcckzrl"));
						map.put("zpmcc", result.getString("zpmcc"));
						//这是第30个字段
						map.put("fbl", result.getString("fbl"));
						map.put("pmxsmd", result.getString("pmxsmd"));
						map.put("pmczlx", result.getString("pmczlx"));
						map.put("qzsxt", result.getString("qzsxt"));
						map.put("sxtsl", result.getString("sxtsl"));
						map.put("hzsxt", result.getString("hzsxt"));
						map.put("dcrl", result.getString("dcrl"));
						map.put("dcsfkcx", result.getString("dcsfkcx"));
						map.put("cdq", result.getString("cdq"));
						map.put("sjcsjk", result.getString("sjcsjk"));
						//这是第40个字段
						map.put("nfc", result.getString("nfc"));
						map.put("ejjklx",result.getString("ejjklx"));
						map.put("cdjklx",result.getString("cdjklx"));
						map.put("zwsb",result.getString("zwsb"));
						map.put("gps",result.getString("gps"));
						map.put("tly",result.getString("tly"));
						map.put("cygn",result.getString("cygn"));
						map.put("sxtgqdx",result.getString("sxtgqdx"));
						//这是第48个字段


					   // map.put("sp_picture", ImageUtil.imageToBase64(result.getString("sp_picture")));
						//map.put("price", result.getString("price"));
						Set<Map.Entry<String, String>> entry = map.entrySet();

						/*for(Map.Entry<String, String> e : entry) {
						System.out.println("key:"+e.getKey()+"-value:"+e.getValue());
						}*/
						res.addListItem(map);
						/*for(HashMap<String, String> e : res.getArrayList()) {
							System.out.println("key:"+e.get("pp"));
						}*/
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


