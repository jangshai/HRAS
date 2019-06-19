import java.util.Comparator;

public class Comparatorpatient implements Comparator{

	@Override
	public int compare(Object obj0, Object obj1) {
		// TODO Auto-generated method stub
		Patient patient0=(Patient)obj0;
		 Patient patient1=(Patient)obj1;

		   //首先比较年龄，如果年龄相同，则比较名字

		  int flag=patient0.geta().compareTo(patient1.geta());
		  if(flag==0){
		   return patient0.geta().compareTo(patient1.geta());
		  }else{
		   return flag;
		  } 
		 }
		
	}


