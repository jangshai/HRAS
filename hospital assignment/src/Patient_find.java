import java.util.ArrayList;
import java.util.Random;

public class Patient_find {
	static ArrayList<Integer> createpatient = new ArrayList();
	static ArrayList<Integer> createambulance = new ArrayList();
	static ArrayList<Hospital> hospital = new ArrayList<>();
	static ArrayList<Hospital> hospitalHRAS  = new ArrayList<>();;
	
	static int nownum;
	static int patientnum;
	static int allcritical; //總重症人數
	static int ambulancenum; //此刻上救護車的人數
	static int allambulancenum ;  //所有上救護車的人數
	static ArrayList gethospital()
	{
		 hospital.add(new Hospital(0,1,4,40,100));
		 hospital.add(new Hospital(1,1,12,40,114));
		 hospital.add(new Hospital(2,1,20,40,113));
		 hospital.add(new Hospital(3,1,26,40,46));
		 hospital.add(new Hospital(4,2,1,20,175));
		 hospital.add(new Hospital(5,2,3,20,39));
		 hospital.add(new Hospital(6,2,4,20,29));
		 hospital.add(new Hospital(7,2,5,20,188));
		 hospital.add(new Hospital(8,2,5,20,60));
		 hospital.add(new Hospital(9,2,6,20,74));
		 hospital.add(new Hospital(10,2,7,20,100));
		 hospital.add(new Hospital(11,2,18,20,100));
		 hospital.add(new Hospital(12,2,22,20,100));
		 hospital.add(new Hospital(13,2,27,20,100));
		 
		 
		return hospital;
	}
	static ArrayList getHRAShospital()
	{
		 hospitalHRAS.add(new Hospital(0,1,4,40,100));
		 hospitalHRAS.add(new Hospital(1,1,12,40,114));
		 hospitalHRAS.add(new Hospital(2,1,20,40,113));
		 hospitalHRAS.add(new Hospital(3,1,26,40,46));
		 hospitalHRAS.add(new Hospital(4,2,2,40,175));
		 hospitalHRAS.add(new Hospital(5,2,3,40,39));
		 hospitalHRAS.add(new Hospital(6,2,4,20,29));
		 hospitalHRAS.add(new Hospital(7,2,5,20,188));
		 hospitalHRAS.add(new Hospital(8,2,5,20,60));
		 hospitalHRAS.add(new Hospital(9,2,6,20,74));
		 hospitalHRAS.add(new Hospital(10,2,7,20,100));
		 hospitalHRAS.add(new Hospital(11,2,18,20,100));
		 hospitalHRAS.add(new Hospital(12,2,22,20,100));
		 hospitalHRAS.add(new Hospital(13,2,27,20,100));
		 
		return hospitalHRAS;
	}
	static double patient_create_function() //傷患產生機制
	{
		Random ran=new Random(); 
		double patientnumber=0,b=1,c=Math.exp(-67),u;
		do{
			u=Math.random();
			b*=u;
			if(b>=c)
				patientnumber++;
		}while(b>=c);
		
		return patientnumber;
	}
	static double critical_create_function() //帕松分布   exp()內的值
	{
		Random ran=new Random(); 
		double patientnumber=0,b=1,c=Math.exp(-67),u;
		do{
			u=Math.random();
			b*=u;
			if(b>=c)
				patientnumber++;
		}while(b>=c);
		
		return patientnumber;
	}
     static ArrayList patient_main_function(int anum)
	{
		patientnum=anum;
		nownum=0;
		allcritical=0;
		int criticalpatientnum =0; 
		int seriouspatientnum =0;
		int normalpatientnum = 0;
		int nowpatientnum = 0 ;
		int a=0,b=0,c=0;
		while(nownum<patientnum)
		{
			if(nownum<patientnum)       //如果病人大概在預估範圍內 就不用產生病人了
			{
				criticalpatientnum =(int)critical_create_function(); 
				seriouspatientnum =(int)patient_create_function();
				normalpatientnum =(int)patient_create_function();
				if(nownum+criticalpatientnum>=patientnum)
				{
					criticalpatientnum=patientnum-nownum;
					//System.out.println("嚴重病人"+criticalpatientnum);
					seriouspatientnum=0;
					normalpatientnum=0;
					
				}
				else if(nownum+criticalpatientnum+seriouspatientnum>=patientnum)
				{
					seriouspatientnum=patientnum-nownum-criticalpatientnum;
					//System.out.println("中症病人"+seriouspatientnum);
					normalpatientnum=0;
				}
				else if(nownum+criticalpatientnum+seriouspatientnum+normalpatientnum>=patientnum)
				{
					normalpatientnum=patientnum-nownum-criticalpatientnum-seriouspatientnum;
					//System.out.println("輕症病人"+normalpatientnum);
				}
				a+=criticalpatientnum;
				b+=seriouspatientnum;
				c+=normalpatientnum;
				createpatient.add(criticalpatientnum);
				createpatient.add(seriouspatientnum);
				createpatient.add(normalpatientnum);
				
			}
			else 
			{
				criticalpatientnum =0; 
				seriouspatientnum =0;
				normalpatientnum =0;
			}
			nowpatientnum=criticalpatientnum+seriouspatientnum+normalpatientnum;
			nownum+=nowpatientnum;
			allcritical=allcritical+criticalpatientnum;
			//System.out.println(allcritical);
		}
		
		
		return createpatient;	
	}
    static int get_allcritical()
 	{
 		return allcritical;
 	}
	static int get_patientnum()
	{
		return patientnum;
	}
	static ArrayList ambulance_create_function(int patientnum)
	{
		allambulancenum=0;
		ambulancenum=0;
		while(allambulancenum<patientnum)
		{
			ambulancenum=(int)ambulance();
			if(allambulancenum+ambulancenum>patientnum)
			{
				ambulancenum=patientnum-allambulancenum;
			}
			createambulance.add(ambulancenum);  
			allambulancenum+=ambulancenum;
		}
		return createambulance;
	}
	public static double ambulance()
	{
		Random ran=new Random(); 
		double tempambulance=0,b=1,c=Math.exp(-4),u;
		do{
			u=Math.random();
			b*=u;
			if(b>=c)
				tempambulance++;
		}while(b>=c);
		
		return tempambulance;
	}
}
