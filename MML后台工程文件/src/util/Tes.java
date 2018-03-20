package util;
public class Tes {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String s ="»ªÎª 1200";
		  String[] a = s.split("\\s+");
		  System.out.println(a.length);
	       String sql = "select name,img,price,zpmcc from DBNames.Table_SP where";
	       String temp = null;
	       for(int i = 0; i < a.length; i++)
	       {
	    	   String se =a[i];
	    	
	    	   if(temp ==null){
	    	   
	      
			
			  temp =  String.format(" concat((name,price,pp,xh,ssnf,ssyf,jsys,jscz,czxt,czxtbb,cpupp,cpupl,cpuhs,cpuxh,skjlx,zdzcsimksl,simklx,fourgwl,rom,ram,zpmcc,fbl,pmczlx,qzsxt,sxtsl,hzsxt,dcrl,nfc,zwsb,gps,tly,sxtgqdx) like '%%%s%%'",a[i]);
				
					
	    	   }else{
	 			  temp =  String.format(" and concat(name,price,pp,xh,ssnf,ssyf,jsys,jscd,jskd,jshd,jszl,srfs,jscz,czxt,czxtbb,cpupp,cpupl,cpuhs,cpuxh,skjlx,zdzcsimksl,simklx,fourgwl,threegtwogwl,rom,ram,cck,zdcckzrl,zpmcc,fbl,pmxsmd,pmczlx,qzsxt,sxtsl,hzsxt,dcrl,dcsfkcx,cdq,sjcsjk,nfc,ejjklx,cdjklx,zwsb,gps,tly,cygn,sxtgqdx) like '%%%s%%'",a[i]);

					
	    	   }
	    	   sql +=temp;
	    		   
	    	   }
				
				
			System.out.println(sql);
	}
	
	

}
