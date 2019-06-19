import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class HRAS {
	static int nownum; //到目前為止發現的傷患。
	static int time;   //時間
	static int ambulancenum; //此刻上救護車的人數
	static int surplusambulance = 0 ;
	static int allambulancenum ;  //所有上救護車的人數
	static int patientnum;
	static int allcriticalnum;
	static int saved_criticalnum;
	static int unsaved_criticalnum;
	static int judgecreate=0;
	static int HRASpatient = 0 ;
	static int HRASambulance = 0 ;
	static int c_deadline=20;
	static ArrayList<Patient> patient = new ArrayList<>(); 
	static ArrayList<Hospital> hospital = new ArrayList<>();	
    static ArrayList<Medical> medical = new ArrayList<>();
	static ArrayList<Integer> createpatient;
	static ArrayList<Integer> createambulance;
	static ArrayList HRAS_method(ArrayList patientlist,ArrayList ambulancelist)
	{ 
		ambulancenum=0;judgecreate=0;
		allambulancenum=0;   // 歸0
		nownum=0;			 // 歸0
		allcriticalnum=0;
		saved_criticalnum=0;
		unsaved_criticalnum=0;
		HRASpatient=0;
		HRASambulance=0;
		time=0;	
		surplusambulance =0;  //多的救護車歸0
		 createpatient=patientlist;
		 createambulance=ambulancelist;
		 for(int temp=0;temp<createpatient.size();temp++)
		 {
			 //System.aout.println("傷患HRAS "+temp+": "+createpatient.get(temp));
		 }
		 for(int temp=0;temp<createambulance.size();temp++)
		 {
			 //System.out.println("救護車HRAS "+temp+": "+createambulance.get(temp));
		 }
		 hospital=Patient_find.getHRAShospital();
		 patientnum=Patient_find.get_patientnum();
		 allcriticalnum=Patient_find.get_allcritical();
		 //System.out.println(patientnum);
		 int a=0,b=0,c=0,d=0;
		 int ab=createambulance.size();
		 while(d<ab)
		 {
			 d++;
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
			}else {judgecreate = 1 ;}
			nowpatientnum=criticalpatientnum+seriouspatientnum+normalpatientnum;
			for(int tempnum=0;tempnum<criticalpatientnum;tempnum++)
			patient.add(new Patient(1,time));
			for(int tempnum=0;tempnum<seriouspatientnum;tempnum++)
			patient.add(new Patient(2,time));
			for(int tempnum=0;tempnum<normalpatientnum;tempnum++)
			patient.add(new Patient(3,time));
			sorting(patient);	
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
				
			HRAS(ambulancenum);   //現行方法
			//System.out.println(a+","+b+","+c);
			allambulancenum=allambulancenum+ambulancenum;	
			//System.out.println("HRAS第"+(d-1)+"次: "+ambulancenum+" allambulancenum"+allambulancenum);
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
	public static ArrayList gethospitalEOB()
	{
		return hospital;
	}
	public static void HRAS(int medicalnum)
	{
		int success_assignment=0;   
		
		for(int tempnum=0;tempnum<medicalnum;tempnum++)  //有medicalnum輛救護車    條件兩個  1:要留五輛車給重症 ,2:10個以上的輕中症才會送輕中症
			{
				if(patient.get(tempnum).level==1)  //最嚴重的病人  只能送大醫院 為了讓後發現的能夠前往近的醫院 所以從遠到近
				{
					int temphosp=3,judge=0;
						while(temphosp>=0)    //從第4家醫院挑到第1家醫院
						{
						if(hospital.get(temphosp).EOB>0&&time+hospital.get(temphosp).getdistance()<c_deadline)   //有急診觀察床位即選擇此醫院
						{						
							medical.add(new Medical(1,patient.get(tempnum).time,time,hospital.get(temphosp).name)); //病患等級,發現時間,送醫時間,選擇醫院
							hospital.get(temphosp).EOB--;    //急診觀察床位置變少
							success_assignment++;
							saved_criticalnum++;
							judge=1;
							//System.out.println("1:"+success_assignment);
							break;  //跳出此迴圈
						}
						/*else if(hospital.get(temphosp).EOB>0&&temphosp==0)     //已經到最近醫院的選擇了
						{
							medical.add(new Medical(1,patient.get(tempnum).time,time,hospital.get(temphosp).name)); //病患等級,發現時間,送醫時間,選擇醫院
							hospital.get(temphosp).EOB--;    //急診觀察床位置變少
							success_assignment++;
							judge=1;
							break;
						}*/
						temphosp--;
						}
						if(judge==0&&temphosp==-1)
						{
							temphosp++;
							while(temphosp<4)
							{
								if(hospital.get(temphosp).EOB>0)
								{
									medical.add(new Medical(1,patient.get(tempnum).time,time,hospital.get(temphosp).name)); //病患等級,發現時間,送醫時間,選擇醫院
									hospital.get(temphosp).EOB--;    //急診觀察床位置變少
									success_assignment++;
									saved_criticalnum++;
									break; 
								}	
								temphosp++;
							}
						}
				}
				else if(patient.get(tempnum).level==2)   //次等嚴重的病人      送小醫院
				{
					unsaved_criticalnum=allcriticalnum-saved_criticalnum;
					HRASpatient=(int)Math.ceil(1.5*unsaved_criticalnum*time/c_deadline);	//傷患數量係數*未被救的重症傷患*目前時間/期限時間
					HRASambulance=(int)Math.ceil(0.8*unsaved_criticalnum*time/c_deadline);
					//System.out.println("unsaved:"+unsaved_criticalnum);
					//System.out.println("time:"+time);
					//System.out.println("HRASpatient:"+HRASpatient);
					//System.out.println("HRASambulance:"+HRASambulance);
					//medicalnum-tempnum  = 現在剩下的台數
					// patientHRAS.size()-tempnum ==剩下的人數
					//剩下的人數>10   送到剩10人
					//剩下的車子>5台 且人數>10人  留到剩5台
					if(patient.size()-tempnum>HRASpatient) //傷者人數較多 繼續送  
					{
						int temphosp=4;  
						while(temphosp<14)    //從第5家醫院挑到第13家醫院
						{
							if(hospital.get(temphosp).EOB>0)   //有急診觀察床位即選擇此醫院
							{
								medical.add(new Medical(2,patient.get(tempnum).time,time,hospital.get(temphosp).name)); //病患等級,發現時間,送醫時間,選擇醫院
								hospital.get(temphosp).EOB--;    //急診觀察床位置變少
								success_assignment++;
								//System.out.println("2:"+success_assignment);
								break;  //跳出此迴圈
							}
							else
								temphosp++;
						}
					}
					else if(medicalnum-tempnum>HRASambulance||judgecreate==1)       //救護車留五台，若是知道不會再出傷患，不用留
					{
						int temphosp=4;
						while(temphosp<14)    //從第5家醫院挑到第13家醫院
						{
					
							if(hospital.get(temphosp).EOB>0)   //有急診觀察床位即選擇此醫院
							{
								medical.add(new Medical(2,patient.get(tempnum).time,time,hospital.get(temphosp).name)); //病患等級,發現時間,送醫時間,選擇醫院
								hospital.get(temphosp).EOB--;    //急診觀察床位置變少
								success_assignment++;
								//System.out.println("2.5:"+success_assignment);
								break;  //跳出此迴圈
							}
							else
								temphosp++;
						}
					}
					
				}
				else if(patient.get(tempnum).level==3)								//最輕微的病人 只能送小醫院
				{
					unsaved_criticalnum=allcriticalnum-saved_criticalnum;
					HRASpatient=(int)Math.ceil(1.5*unsaved_criticalnum*time/c_deadline);	//傷患數量係數*未被救的重症傷患*目前時間/期限時間
					HRASambulance=(int)Math.ceil(0.8*unsaved_criticalnum*time/c_deadline);
					//System.out.println("unsaved:"+unsaved_criticalnum);
					//System.out.println("time:"+time);
					//System.out.println("HRASpatient:"+HRASpatient);
					//System.out.println("HRASambulance:"+HRASambulance);
					if(patient.size()-tempnum>HRASpatient) //傷者人數較多 繼續送  
					{
						int temphosp=4;
						while(temphosp<14)    //從第5家醫院挑到第13家醫院
						{
					
							if(hospital.get(temphosp).EOB>0)   //有急診觀察床位即選擇此醫院
							{
								medical.add(new Medical(3,patient.get(tempnum).time,time,hospital.get(temphosp).name)); //病患等級,發現時間,送醫時間,選擇醫院
								hospital.get(temphosp).EOB--;    //急診觀察床位置變少
								success_assignment++;
								//System.out.println("3:"+success_assignment);
								break;  //跳出此迴圈
							}
							else
								temphosp++;
						}
					}
					else if(medicalnum-tempnum>HRASambulance||judgecreate==1)      //救護車留五台，若是知道不會再出傷患，不用留
					{
						int temphosp=4;
						while(temphosp<14)    //從第5家醫院挑到第13家醫院
						{
					
							if(hospital.get(temphosp).EOB>0)   //有急診觀察床位即選擇此醫院
							{
								medical.add(new Medical(3,patient.get(tempnum).time,time,hospital.get(temphosp).name)); //病患等級,發現時間,送醫時間,選擇醫院
								hospital.get(temphosp).EOB--;    //急診觀察床位置變少
								success_assignment++;
								//System.out.println("3.5:"+success_assignment);
								break;  //跳出此迴圈
							}
							else
								temphosp++;
						}
					}
				}
			}
		for(int tempnum=0;tempnum<success_assignment;tempnum++)
		{
			patient.remove(0);   //已經分配醫院的傷患將會從patient list被移除
			
		}
		surplusambulance=  surplusambulance+medicalnum-success_assignment;  //多的救護車=全部-已經分配的
		//System.out.println("此時要分配的救護車"+medicalnum);
		//System.out.println("此時已分配的救護車"+success_assignment);
		//System.out.println("此時多餘的救護車"+(medicalnum-success_assignment));
		ambulancenum=ambulancenum-(medicalnum-success_assignment);
		
		
	}

}
