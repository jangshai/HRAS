import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class HRAS {
	static int nownum; //��ثe����o�{���˱w�C
	static int time;   //�ɶ�
	static int ambulancenum; //����W���@�����H��
	static int surplusambulance = 0 ;
	static int allambulancenum ;  //�Ҧ��W���@�����H��
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
		allambulancenum=0;   // �k0
		nownum=0;			 // �k0
		allcriticalnum=0;
		saved_criticalnum=0;
		unsaved_criticalnum=0;
		HRASpatient=0;
		HRASambulance=0;
		time=0;	
		surplusambulance =0;  //�h�����@���k0
		 createpatient=patientlist;
		 createambulance=ambulancelist;
		 for(int temp=0;temp<createpatient.size();temp++)
		 {
			 //System.aout.println("�˱wHRAS "+temp+": "+createpatient.get(temp));
		 }
		 for(int temp=0;temp<createambulance.size();temp++)
		 {
			 //System.out.println("���@��HRAS "+temp+": "+createambulance.get(temp));
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
				criticalpatientnum = createpatient.get(0);   //�e�T��  �Ĥ@�ӭ��g �ĤG�Ӥ��g �ĤT�ӻ��g;
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
			
			ambulancenum = tempambulance+surplusambulance;   //�[�W�@���Ѿl�����@��
			surplusambulance=0;
			if(ambulancenum>=patient.size())    //���@����H�h  �h���d���U�@�i���f�H
			{
				surplusambulance = ambulancenum-patient.size();
				ambulancenum=patient.size();
			}
			a+=criticalpatientnum;
			b+=seriouspatientnum;
			c+=normalpatientnum;
				
			HRAS(ambulancenum);   //�{���k
			//System.out.println(a+","+b+","+c);
			allambulancenum=allambulancenum+ambulancenum;	
			//System.out.println("HRAS��"+(d-1)+"��: "+ambulancenum+" allambulancenum"+allambulancenum);
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
		
		for(int tempnum=0;tempnum<medicalnum;tempnum++)  //��medicalnum�����@��    ������  1:�n�d�����������g ,2:10�ӥH�W�������g�~�|�e�����g
			{
				if(patient.get(tempnum).level==1)  //���Y�����f�H  �u��e�j��| ���F����o�{������e������| �ҥH�q�����
				{
					int temphosp=3,judge=0;
						while(temphosp>=0)    //�q��4�a��|�D���1�a��|
						{
						if(hospital.get(temphosp).EOB>0&&time+hospital.get(temphosp).getdistance()<c_deadline)   //����E�[��ɦ�Y��ܦ���|
						{						
							medical.add(new Medical(1,patient.get(tempnum).time,time,hospital.get(temphosp).name)); //�f�w����,�o�{�ɶ�,�e��ɶ�,�����|
							hospital.get(temphosp).EOB--;    //��E�[��ɦ�m�ܤ�
							success_assignment++;
							saved_criticalnum++;
							judge=1;
							//System.out.println("1:"+success_assignment);
							break;  //���X���j��
						}
						/*else if(hospital.get(temphosp).EOB>0&&temphosp==0)     //�w�g��̪���|����ܤF
						{
							medical.add(new Medical(1,patient.get(tempnum).time,time,hospital.get(temphosp).name)); //�f�w����,�o�{�ɶ�,�e��ɶ�,�����|
							hospital.get(temphosp).EOB--;    //��E�[��ɦ�m�ܤ�
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
									medical.add(new Medical(1,patient.get(tempnum).time,time,hospital.get(temphosp).name)); //�f�w����,�o�{�ɶ�,�e��ɶ�,�����|
									hospital.get(temphosp).EOB--;    //��E�[��ɦ�m�ܤ�
									success_assignment++;
									saved_criticalnum++;
									break; 
								}	
								temphosp++;
							}
						}
				}
				else if(patient.get(tempnum).level==2)   //�����Y�����f�H      �e�p��|
				{
					unsaved_criticalnum=allcriticalnum-saved_criticalnum;
					HRASpatient=(int)Math.ceil(1.5*unsaved_criticalnum*time/c_deadline);	//�˱w�ƶq�Y��*���Q�Ϫ����g�˱w*�ثe�ɶ�/�����ɶ�
					HRASambulance=(int)Math.ceil(0.8*unsaved_criticalnum*time/c_deadline);
					//System.out.println("unsaved:"+unsaved_criticalnum);
					//System.out.println("time:"+time);
					//System.out.println("HRASpatient:"+HRASpatient);
					//System.out.println("HRASambulance:"+HRASambulance);
					//medicalnum-tempnum  = �{�b�ѤU���x��
					// patientHRAS.size()-tempnum ==�ѤU���H��
					//�ѤU���H��>10   �e���10�H
					//�ѤU�����l>5�x �B�H��>10�H  �d���5�x
					if(patient.size()-tempnum>HRASpatient) //�˪̤H�Ƹ��h �~��e  
					{
						int temphosp=4;  
						while(temphosp<14)    //�q��5�a��|�D���13�a��|
						{
							if(hospital.get(temphosp).EOB>0)   //����E�[��ɦ�Y��ܦ���|
							{
								medical.add(new Medical(2,patient.get(tempnum).time,time,hospital.get(temphosp).name)); //�f�w����,�o�{�ɶ�,�e��ɶ�,�����|
								hospital.get(temphosp).EOB--;    //��E�[��ɦ�m�ܤ�
								success_assignment++;
								//System.out.println("2:"+success_assignment);
								break;  //���X���j��
							}
							else
								temphosp++;
						}
					}
					else if(medicalnum-tempnum>HRASambulance||judgecreate==1)       //���@���d���x�A�Y�O���D���|�A�X�˱w�A���ίd
					{
						int temphosp=4;
						while(temphosp<14)    //�q��5�a��|�D���13�a��|
						{
					
							if(hospital.get(temphosp).EOB>0)   //����E�[��ɦ�Y��ܦ���|
							{
								medical.add(new Medical(2,patient.get(tempnum).time,time,hospital.get(temphosp).name)); //�f�w����,�o�{�ɶ�,�e��ɶ�,�����|
								hospital.get(temphosp).EOB--;    //��E�[��ɦ�m�ܤ�
								success_assignment++;
								//System.out.println("2.5:"+success_assignment);
								break;  //���X���j��
							}
							else
								temphosp++;
						}
					}
					
				}
				else if(patient.get(tempnum).level==3)								//�̻��L���f�H �u��e�p��|
				{
					unsaved_criticalnum=allcriticalnum-saved_criticalnum;
					HRASpatient=(int)Math.ceil(1.5*unsaved_criticalnum*time/c_deadline);	//�˱w�ƶq�Y��*���Q�Ϫ����g�˱w*�ثe�ɶ�/�����ɶ�
					HRASambulance=(int)Math.ceil(0.8*unsaved_criticalnum*time/c_deadline);
					//System.out.println("unsaved:"+unsaved_criticalnum);
					//System.out.println("time:"+time);
					//System.out.println("HRASpatient:"+HRASpatient);
					//System.out.println("HRASambulance:"+HRASambulance);
					if(patient.size()-tempnum>HRASpatient) //�˪̤H�Ƹ��h �~��e  
					{
						int temphosp=4;
						while(temphosp<14)    //�q��5�a��|�D���13�a��|
						{
					
							if(hospital.get(temphosp).EOB>0)   //����E�[��ɦ�Y��ܦ���|
							{
								medical.add(new Medical(3,patient.get(tempnum).time,time,hospital.get(temphosp).name)); //�f�w����,�o�{�ɶ�,�e��ɶ�,�����|
								hospital.get(temphosp).EOB--;    //��E�[��ɦ�m�ܤ�
								success_assignment++;
								//System.out.println("3:"+success_assignment);
								break;  //���X���j��
							}
							else
								temphosp++;
						}
					}
					else if(medicalnum-tempnum>HRASambulance||judgecreate==1)      //���@���d���x�A�Y�O���D���|�A�X�˱w�A���ίd
					{
						int temphosp=4;
						while(temphosp<14)    //�q��5�a��|�D���13�a��|
						{
					
							if(hospital.get(temphosp).EOB>0)   //����E�[��ɦ�Y��ܦ���|
							{
								medical.add(new Medical(3,patient.get(tempnum).time,time,hospital.get(temphosp).name)); //�f�w����,�o�{�ɶ�,�e��ɶ�,�����|
								hospital.get(temphosp).EOB--;    //��E�[��ɦ�m�ܤ�
								success_assignment++;
								//System.out.println("3.5:"+success_assignment);
								break;  //���X���j��
							}
							else
								temphosp++;
						}
					}
				}
			}
		for(int tempnum=0;tempnum<success_assignment;tempnum++)
		{
			patient.remove(0);   //�w�g���t��|���˱w�N�|�qpatient list�Q����
			
		}
		surplusambulance=  surplusambulance+medicalnum-success_assignment;  //�h�����@��=����-�w�g���t��
		//System.out.println("���ɭn���t�����@��"+medicalnum);
		//System.out.println("���ɤw���t�����@��"+success_assignment);
		//System.out.println("���ɦh�l�����@��"+(medicalnum-success_assignment));
		ambulancenum=ambulancenum-(medicalnum-success_assignment);
		
		
	}

}
