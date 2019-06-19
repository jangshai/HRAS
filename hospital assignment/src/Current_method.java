import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class Current_method {
	static int nownum; //��ثe����o�{���˱w�C
	static int time;   //�ɶ�
	static int ambulancenum; //����W���@�����H��
	static int surplusambulance = 0 ;
	static int allambulancenum ;  //�Ҧ��W���@�����H��
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
		 //System.out.println("�{��patientnummmmmmmmmmmm:"+patientnum);
		 int a=0,b=0,c=0;
		 for(int temp=0;temp<createambulance.size();temp++)
		 {
			 //System.out.println("���@��current "+temp+": "+createambulance.get(temp));
		 }
		 while(allambulancenum<patientnum)
		 {
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
			}
			nowpatientnum=criticalpatientnum+seriouspatientnum+normalpatientnum;
			sorting(patient);	 //��z�w�g�b�{�����������g
			
			//-----------�H���o�ɶ����˱w �i��231223323112113221�o��
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
				
			Hospital_Assignment(ambulancenum);   //�{���k
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
		int error_assignment=0;   //�٨S�ϥΨ�
		for(int tempnum=0;tempnum<medicalnum;tempnum++)
		{
			if(patient.get(tempnum).level==1)  //���Y�����f�H  �u��e�j��|
			{
				int temphosp=0;
					while(temphosp<4)    //�q�Ĥ@�a��|�D��Ĥ��a��Ǥ���
					{
					//if (temphosp>2)error_assignment++;
					if(hospital.get(temphosp).EOB>0)   //����E�[��ɦ�Y��ܦ���|
					{
						medical.add(new Medical(1,patient.get(tempnum).time,time,hospital.get(temphosp).name)); //�f�w����,�o�{�ɶ�,�e��ɶ�,�����|
						hospital.get(temphosp).EOB--;    //��E�[��ɦ�m�ܤ�
						break;  //���X���j��
					}
					else
						temphosp++;
					}
				
			}
			else if(patient.get(tempnum).level==2)   //�����Y�����f�H      �e�p��|
			{
				int temphosp=4;
				while(temphosp<14)    //�q��5�a��|�D���14�a��|
				{
				
				if(hospital.get(temphosp).EOB>0)   //����E�[��ɦ�Y��ܦ���|
				{
					medical.add(new Medical(2,patient.get(tempnum).time,time,hospital.get(temphosp).name)); //�f�w����,�o�{�ɶ�,�e��ɶ�,�����|
					hospital.get(temphosp).EOB--;    //��E�[��ɦ�m�ܤ�
					break;  //���X���j��
				}
				else
					temphosp++;
				}
			}
			else										//�̻��L���f�H �u��e�p��|
			{
				int temphosp=4;
				while(temphosp<14)    //�q��5�a��|�D���13�a��|
				{
				
				if(hospital.get(temphosp).EOB>0)   //����E�[��ɦ�Y��ܦ���|
				{
					medical.add(new Medical(3,patient.get(tempnum).time,time,hospital.get(temphosp).name)); //�f�w����,�o�{�ɶ�,�e��ɶ�,�����|
					hospital.get(temphosp).EOB--;    //��E�[��ɦ�m�ܤ�
					break;  //���X���j��
				}
				else
					temphosp++;
				}
			}
		}
		for(int tempnum=0;tempnum<medicalnum;tempnum++)
		{
			patient.remove(0);   //�w�g���t��|���˱w�N�|�qpatient list�Q����
			
		}
		
	}
}
