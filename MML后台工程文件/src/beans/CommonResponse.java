package beans;

import java.awt.List;
import java.util.ArrayList;
import java.util.HashMap;

public class CommonResponse {
	
	private String resCode; //��Ӧ����
	private String resMsg;  //��Ӧ��Ϣ
    private HashMap<String, String> property;//��Ӧ����
    private ArrayList<HashMap<String, String>> arrayList;//��Ӧ��
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
