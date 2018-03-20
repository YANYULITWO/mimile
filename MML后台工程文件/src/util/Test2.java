package util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Test2 {

	public static void main(String[] args) throws SQLException {
		String  DBName = "spdetail";
		
	//	Connection  con = DatabaseUtil.getConnection();
		// TODO Auto-generated method stub
		String[] s2 = {"name","price","pp","xh","ssnf","ssyf","jsys","jscd","jskd","jshd","jszl","srfs","jscz","czxt","czxtbb","cpupp","cpupl","cpuhs","cpuxh","skjlx","zdzcsimksl","simklx","fourgwl","threegtwogwl","rom","ram","cck","zdcckzrl","zpmcc","fbl","pmxsmd","pmczlx","qzsxt","sxtsl","hzsxt","dcrl","dcsfkcx","cdq","sjcsjk","nfc","ejjklx","cdjklx","zwsb","gps","tly","cygn","sxtgqdx"};
		for(int i =1; i<=188; i++)
			
		{   String s3 ="";
		    String s4 ="";
			for(int j =0; j<s2.length; j++)
			{
				String str =String.format("select %s from spdetail where id = '%s' ", s2[j],i) ;
				ResultSet strtmp = DatabaseUtil.query(str);
				
				while (strtmp.next()) {
				 s4 =strtmp.getString(s2[j]);
				}
				if(s4==null)
				{
					s4="";
				}
				s3+=s4;
			}
		    System.out.println(s3);
		    String s5 = String.format("update  %s set wholeinfo ='%s' where id = '%s' ",DBName,s3,i);
		    System.out.println(s5);
		    DatabaseUtil.update(s5);
		}
	}

}
