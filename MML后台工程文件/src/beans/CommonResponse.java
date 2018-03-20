package beans;

import java.awt.List;
import java.util.ArrayList;
import java.util.HashMap;

public class CommonResponse {
	
	private String resCode; //响应类型
	private String resMsg;  //响应消息
    private HashMap<String, String> property;//响应属性
    private ArrayList<HashMap<String, String>> arrayList;//响应表单
    public CommonResponse () {
    	super();
    	resCode="";
    	resMsg="";
    	property = new HashMap<String,String>();
    	arrayList = new ArrayList<HashMap<String, String>>();
		
	}
    
    public void setResult(String resCode ,String resMsg )
    {
    	this.resCode = resCode;
    	this.resMsg =resMsg;
    	
    }

	public String getResCode() {
		return resCode;
	}

	public void setResCode(String resCode) {
		this.resCode = resCode;
	}

	public String getResMsg() {
		return resMsg;
	}

	public void setResMsg(String resMsg) {
		this.resMsg = resMsg;
	}

	public HashMap<String, String> getProperty() {
		return property;
	}

	public ArrayList<HashMap<String, String>> getArrayList() {
		return arrayList;
	}
	
	public void addListItem(HashMap<String, String> map) {
		arrayList.add(map);
	}
	
    
   
    
    

}
