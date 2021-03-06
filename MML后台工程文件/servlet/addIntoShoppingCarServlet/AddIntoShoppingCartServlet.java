package addIntoShoppingCarServlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
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
import constants.VariableNumber;
import net.sf.json.JSONObject;
import util.DatabaseUtil;

/**
 * Servlet implementation class AddIntoShoppingCarServlet
 */
@WebServlet(description = "加入购物车", urlPatterns = { "/addIntoShoppingCartServlet" })
public class AddIntoShoppingCartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddIntoShoppingCartServlet() {
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
			VariableNumber.Userphone=URLDecoder.decode(requestParam.getString("user_phone"),"utf-8");
			VariableNumber.SPAMOUNT=URLDecoder.decode(requestParam.getString("sp_amount"),"utf-8");
			VariableNumber.SPID=URLDecoder.decode(requestParam.getString("sp_id"),"utf-8");
		   String type = URLDecoder.decode(requestParam.getString("type"),"utf-8");
	       System.out.println(requestParam.getString("user_phone"));
	       VariableNumber.SPID=URLDecoder.decode(requestParam.getString("sp_id"),"utf-8");
	       System.out.println(VariableNumber.SPID);
	       if(type.equals("1")){
	       String sqlExit = String.format("select * from %s where user_phone = %s and sp_Id = %s", DBNames.Table_ShoppingCar,VariableNumber.Userphone,VariableNumber.SPID);
	       int num =0;
		   System.out.println(sqlExit);
		      try {
					ResultSet set = DatabaseUtil.query(sqlExit);
					System.out.println(set);
					if(set.next()){
					      num = Integer.parseInt(set.getString("amount"));
					      String deleteInfo = String.format("delete from %s where user_phone = '%s' and sp_Id = '%s'", DBNames.Table_ShoppingCar,VariableNumber.Userphone,VariableNumber.SPID );
					      System.out.println(deleteInfo);
					      int amount = Integer.parseInt(VariableNumber.SPAMOUNT)+num;
					      VariableNumber.SPAMOUNT =""+amount;
					      
					      int  number = DatabaseUtil.update(deleteInfo);
					      if(!(number == 0))
					      {
					    	  System.out.println("删除原有购物车成功");
					      }else{
					    	  System.out.println("删除原有购物车失败");
					      }
					}
				} catch (SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
		      
		    
	       String sql = String.format("insert  into %s (user_phone, sp_Id,amount) values('%s','%s','%s')", DBNames.Table_ShoppingCar,VariableNumber.Userphone,VariableNumber.SPID,VariableNumber.SPAMOUNT);
	      
	       
	       System.out.println(sql);
	    
			
			
	       CommonResponse response2 = new CommonResponse();
			 try{
			 if(DatabaseUtil.update(sql) ==1)
			 {
				 response2.setResult("0", URLEncoder.encode("添加购物车成功","utf-8"));
			 }
			 else {
				 response2.setResult("105",URLEncoder.encode("添加购物车失败","utf-8"));
			 }
				
		}catch (SQLException e) {
			response2.setResult("300", "数据库查询错误");
			e.printStackTrace();
		}
			String resStr = JSONObject.fromObject(response2).toString();
			System.out.println(resStr);
			response.getWriter().append(resStr).flush();

	}else{
		 CommonResponse response2 = new CommonResponse();
		 String sql = String.format("delete from %s where user_phone = '%s' and sp_Id = '%s'", DBNames.Table_ShoppingCar,VariableNumber.Userphone,VariableNumber.SPID);
	      
	       
	       System.out.println(sql);
	    
			
			
	   
			 try{
			 if(DatabaseUtil.update(sql) ==1)
			 {
				 response2.setResult("0", URLEncoder.encode("添加购物车成功","utf-8"));
			 }
			 else {
				 response2.setResult("105",URLEncoder.encode("添加购物车失败","utf-8"));
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
}