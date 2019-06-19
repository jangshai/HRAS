import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class Current_method {
	static int nownum; //到目前為止發現的傷患。
	static int time;   //時間
	static int ambulancenum; //此刻上救護車的人數
	static int surplusambulance = 0 ;
	static int allambulancenum ;  //所有上救護車的人數
	static int patientnum;
	static ArrayList<Patient> patient = new ArrayList<>(); 
	static ArrayList<Hospital> hospital = new ArrayList<>();
	static ArrayList<Medical> medical = new ArrayList<>();
	static ArrayList<Integer> createpatient;
	static ArrayList<Integer> createambulance;
	static ArrayList current_method(ArrayList patientlist,ArrayList ambulancelist)
	{
		 nownum=0;time=0;ambulancenum=0;surplusambulance = 0;allambulancenum=0;
		 createpatient=patientlist;
		 createambulance=ambulancelist;
		 hospital=Patient_find.gethospital();
		 patientnum=Patient_find.get_patientnum();
		 //System.out.println("現行patientnummmmmmmmmmmm:"+patientnum);
		 int a=0,b=0,c=0;
		 for(int temp=0;temp<createambulance.size();temp++)
		 {
			 //System.out.println("救護車current "+temp+": "+createambulance.get(temp));
		 }
		 while(allambulancenum<patientnum)
		 {
			int criticalpatientnum =0; 
			int seriouspatientnum =0;
			int normalpatientnum = 0;
			int nowpatientnum = 0 ;
			if(createpatient.size()>=3)
			{
				criticalpatientnum = createpatient.get(0);   //前三個  第一個重症 第二個中症 第三個輕症;
				seriouspatientnum = createpatient.get(1);
				normalpatientnum =  createpatient.get(2);
				createpatient.remove(0);
				createpatient.remove(0);
				createpatient.remove(0);
			}
			nowpatientnum=criticalpatientnum+seriouspatientnum+normalpatientnum;
			sorting(patient);	 //整理已經在現場的輕中重症
			
			//-----------隨機這時間的傷患 可能231223323112113221這樣
			Random thispatient = new Random();
			int allo_num=0,allo_cri=0,allo_ser=0,allo_nor=0;
			while(allo_num<nowpatientnum)
			{
			int thisnum = thispatient.nextInt(3)+1;    //0~2
			switch(thisnum){
			case 1:
				if(allo_cri<criticalpatientnum)
				{
					patient.add(new Patient(1,time));
					allo_cri++;
					allo_num++;
				}
			case 2:
				if(allo_ser<seriouspatientnum)
				{
					patient.add(new Patient(2,time));
					allo_ser++;
					allo_num++;
				}
			case 3:
				if(allo_nor<normalpatientnum)
				{
					patient.add(new Patient(3,time));
					allo_nor++;
					allo_num++;
				}
			}
			}
			int tempambulance=0;
			if(createambulance.size()>0)
			{
				tempambulance=createambulance.get(0);
				createambulance.remove(0);
			}
			ambulancenum = tempambulance+surplusambulance;   //加上一次剩餘的救護車
			surplusambulance=0;
			if(ambulancenum>=patient.size())    //救護車比人多  多的留給下一波的病人
			{
				surplusambulance = ambulancenum-patient.size();
				ambulancenum=patient.size();
			}
			a+=criticalpatientnum;
			b+=seriouspatientnum;
			c+=normalpatientnum;
				
			Hospital_Assignment(ambulancenum);   //現行方法
			//System.out.println(a+","+b+","+c);
			allambulancenum=allambulancenum+ambulancenum;	
			//System.out.println(ambulancenum+" allambulancenum"+allambulancenum);
			nownum+=nowpatientnum;
			time=time+1;
	     }
		 
		 return medical;
		 
	}
	public static void sorting(ArrayList pp)
	{
		Collections.sort(pp,
		new Comparator<Patient>(){
		public int compare(Patient p1 , Patient p2){
			return p1.getlevel()-p2.getlevel();
		}	
		}	
				);
	}
	public static int ambulance()
	{
		Random ran=new Random();
		int tempambulance = ran.nextInt(40);
		
		return tempambulance;
	}
	public static ArrayList gethospitalEOB()
	{
		return hospital;
	}
	public static void Hospital_Assignment(int medicalnum)
	{
		int error_assignment=0;   //還沒使用到
		for(int tempnum=0;tempnum<medicalnum;tempnum++)
		{
			if(patient.get(tempnum).level==1)  //最嚴重的病人  只能送大醫院
			{
				int temphosp=0;
					while(temphosp<4)    //從第一家醫院挑到第六家醫學中心
					{
					//if (temphosp>2)error_assignment++;
					if(hospital.get(temphosp).EOB>0)   //有急診觀察床位即選擇此醫院
					{
						medical.add(new Medical(1,patient.get(tempnum).time,time,hospital.get(temphosp).name)); //病患等級,發現時間,送醫時間,選擇醫院
						hospital.get(temphosp).EOB--;    //急診觀察床位置變少
						break;  //跳出此迴圈
					}
					else
						temphosp++;
					}
				
			}
			else if(patient.get(tempnum).level==2)   //次等嚴重的病人      送小醫院
			{
				int temphosp=4;
				while(temphosp<14)    //從第5家醫院挑到第14家醫院
				{
				
				if(hospital.get(temphosp).EOB>0)   //有急診觀察床位即選擇此醫院
				{
					medical.add(new Medical(2,patient.get(tempnum).time,time,hospital.get(temphosp).name)); //病患等級,發現時間,送醫時間,選擇醫院
					hospital.get(temphosp).EOB--;    //急診觀察床位置變少
					break;  //跳出此迴圈
				}
				else
					temphosp++;
				}
			}
			else										//最輕微的病人 只能送小醫院
			{
				int temphosp=4;
				while(temphosp<14)    //從第5家醫院挑到第13家醫院
				{
				
				if(hospital.get(temphosp).EOB>0)   //有急診觀察床位即選擇此醫院
				{
					medical.add(new Medical(3,patient.get(tempnum).time,time,hospital.get(temphosp).name)); //病患等級,發現時間,送醫時間,選擇醫院
					hospital.get(temphosp).EOB--;    //急診觀察床位置變少
					break;  //跳出此迴圈
				}
				else
					temphosp++;
				}
			}
		}
		for(int tempnum=0;tempnum<medicalnum;tempnum++)
		{
			patient.remove(0);   //已經分配醫院的傷患將會從patient list被移除
			
		}
		
	}
}
