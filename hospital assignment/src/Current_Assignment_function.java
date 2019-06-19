import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.Collections;
import jxl.Workbook;
import java.io.IOException;
import java.io.File;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
public class Current_Assignment_function {
	static int nownum; //到目前為止發現的傷患。
	static int time;   //時間
	static int ambulancenum; //此刻上救護車的人數
	static int surplusambulance = 0 ;
	static int allambulancenum ;  //所有上救護車的人數
	static int HRASpatient = 10 ;
	static int HRASambulance = 5 ;
	static int c_deadline= 20 ;
	static int judgecreate =0; //傷患有無再產生   0有1無
	static ArrayList<Patient> patient = new ArrayList<>(); 
	static ArrayList<Patient> patientHRAS = new ArrayList<>();
	static ArrayList<Hospital> hospital = new ArrayList<>();
	static ArrayList<Hospital> hospitalHRAS = new ArrayList<>();
	static ArrayList<Medical> medical = new ArrayList<>();
	static ArrayList<Medical> medicalHRAS = new ArrayList<>();
	static ArrayList<Integer> createpatient = new ArrayList();     //假如3,5,6,7,8,9  表示第一時間產生重症3,中症5,輕症6 第二時間7,8,9
	static ArrayList<Integer> createpatientHRAS;
	static ArrayList<Integer> createambulance = new ArrayList();   //假如3,5,6 表示第一時間3台車,第二時間5台車
	static ArrayList<Integer> createambulanceHRAS;
	
	
	public static int patientnum = 200; //大概值
	public static void main(String[] args) throws IOException, WriteException
	{
		WritableWorkbook workbook = Workbook.createWorkbook(new File(   //寫入excel檔
                "test.xls"));
		WritableSheet sheet = workbook.createSheet("My Sheet", 0);
        
        WritableFont myFont = new WritableFont(WritableFont.createFont("標楷體"), 12);        
        myFont.setColour(Colour.BLACK);            
        WritableCellFormat cellFormat = new WritableCellFormat();
        
        cellFormat.setFont(myFont); // 指定字型
        
        cellFormat.setAlignment(Alignment.CENTRE); // 對齊方式
        Label label0 = new Label(0, 0, "現行全部等待時間", cellFormat);
        sheet.addCell(label0);
        Label label1 = new Label(1, 0, "現行重症", cellFormat);
        sheet.addCell(label1);
        Label label2 = new Label(2, 0, "現行中症", cellFormat);
        sheet.addCell(label2);
        Label label3 = new Label(3, 0, "現行輕症", cellFormat);
        sheet.addCell(label3);
        Label label4 = new Label(4, 0, "HRAS全部等待時間", cellFormat);
        sheet.addCell(label4);
        Label label5 = new Label(5, 0, "HRAS重症", cellFormat);
        sheet.addCell(label5);
        Label label6 = new Label(6, 0, "HRAS中症", cellFormat);
        sheet.addCell(label6);
        Label label7 = new Label(7, 0, "HRAS輕症", cellFormat);
        sheet.addCell(label7);
        Label label8 = new Label(8, 0, "重症人數", cellFormat);
        sheet.addCell(label8);
        Label label9 = new Label(9, 0, "中症人數", cellFormat);
        sheet.addCell(label9);
        Label label10 = new Label(10, 0, "輕症人數", cellFormat);
        sheet.addCell(label10);
        Label label11 = new Label(11, 0, "現行重症超出deadline人數", cellFormat);
        sheet.addCell(label11);
        Label label12 = new Label(12, 0, "HRAS超出", cellFormat);
        sheet.addCell(label12);
       
	for(int times=0;times<1000;times++) //運作的次數
	{
		nownum=0;time=0;ambulancenum=0;surplusambulance = 0 ;allambulancenum=0;judgecreate =0; //全部初始化
		/*
		File newtext = new File("patient.txt");
		FileWriter datafile = new FileWriter("patient.txt");    //輸出
		BufferedWriter input = new BufferedWriter(datafile);
		*/
		 createpatient=Patient_find.patient_main_function(patientnum);
		 for(int temp=0;temp<createpatient.size();temp++)
		 {
			 //System.out.println("每次的傷患 "+temp+": "+createpatient.get(temp));
		 }
		 createambulance=Patient_find.ambulance_create_function(patientnum);
		 for(int temp=0;temp<createambulance.size();temp++)
		 {
			 //System.out.println("每次的救護車 "+temp+": "+createambulance.get(temp));
		 }
		 createpatientHRAS=new ArrayList (createpatient);
		 createambulanceHRAS=new ArrayList (createambulance);
		 medical=Current_method.current_method(createpatient,createambulance);
		 //System.out.println("送到醫院的傷患總數:"+medical.size());
		 hospital=Current_method.gethospitalEOB();
		/////////output 現行方法的結果 包含傷患等級,發現時間(不是發生時間),送醫時間,選擇醫院
		float c_time=0,s_time=0,n_time=0,all_time=0;
		float c_num=0,s_num=0,n_num=0;
		float c_dis=0,s_dis=0,n_dis=0;
		float  ave_c_time,ave_s_time,ave_n_time,ave_time,ave_c_dis,ave_s_dis,ave_n_dis,ave_dis;
		
		int c_deadpeople=0;
		/*
		for(int tempnum=0;tempnum<10;tempnum++)
		{
			input.write("醫院"+tempnum+"的觀察床位數:"+hospital.get(tempnum).EOB);
			input.write("\r\n");
		}
		*/
		for(int tempnum=0;tempnum<medical.size();tempnum++)   //全部傷患量
		{
			//input.write("傷患等級:"+medical.get(tempnum).level+",發現時間: "+medical.get(tempnum).findtime+",送醫時間: "+medical.get(tempnum).treattime+",選擇醫院: "+medical.get(tempnum).hospitalname);
			//input.write("\r\n");
			//input.write("醫院"+medical.get(tempnum).hospitalname+"的觀察床位數:"+hospital.get(medical.get(tempnum).hospitalname).EOB);
			//input.write("\r\n");
			if(medical.get(tempnum).level==1)
			{
				c_num++;
				c_time=c_time+medical.get(tempnum).treattime+hospital.get(medical.get(tempnum).hospitalname).distance; //等待時間=上車時間+醫院距離
				c_dis=c_dis+hospital.get(medical.get(tempnum).hospitalname).distance;
				if(medical.get(tempnum).treattime+hospital.get(medical.get(tempnum).hospitalname).distance>c_deadline) //上車時間+醫院距離比deadline還久
				{
					c_deadpeople++;
				}
				
			}
			else if(medical.get(tempnum).level==2)
			{
				s_num++;
				s_time=s_time+medical.get(tempnum).treattime+hospital.get(medical.get(tempnum).hospitalname).distance;
				s_dis=s_dis+hospital.get(medical.get(tempnum).hospitalname).distance;
			}
			else
			{
				n_num++;
				n_time=n_time+medical.get(tempnum).treattime+hospital.get(medical.get(tempnum).hospitalname).distance;
				n_dis=n_dis+hospital.get(medical.get(tempnum).hospitalname).distance;
			}
		}
		//input.close();
		ave_c_time=c_time/c_num;
		ave_s_time=s_time/s_num;
		ave_n_time=n_time/n_num;
		ave_c_dis=c_dis/c_num;
		ave_s_dis=s_dis/s_num;
		ave_n_dis=n_dis/n_num;
		ave_time=(c_time+s_time+n_time)/(c_num+s_num+n_num);
		all_time = c_time+s_time+n_time;
		ave_dis=(c_dis+s_dis+n_dis)/(c_num+s_num+n_num);
		/*
		System.out.println("現行方式:");
		System.out.println("全部傷患數量:  重症傷患數量: 中症傷患數量 : 輕症傷患數量: ");
		System.out.println(medical.size()+" "+c_num+" "+s_num+" "+n_num);
		System.out.println("總等待時間: 重症等待時間 : 中症等待時間:  輕症等待時間: ");
		System.out.println(all_time+" "+c_time+" "+s_time+" "+n_time);
		//System.out.println("醫院現場總平均距離: 重症平均距離: 中症平均距離: 輕症平均距離 :");
		//System.out.println(ave_dis+" "+ave_c_dis+" "+ave_s_dis+" "+ave_n_dis);
		System.out.println("重症超出deadline人數"+c_deadpeople);
		*/
        
         jxl.write.Number writeNumber0 = new jxl.write.Number(0, times+1, all_time); // 在A2寫入
         sheet.addCell(writeNumber0);
         jxl.write.Number writeNumber1 = new jxl.write.Number(1, times+1, c_time); // 在A2寫入
         sheet.addCell(writeNumber1);
         jxl.write.Number writeNumber2 = new jxl.write.Number(2, times+1, s_time); // 在A2寫入
         sheet.addCell(writeNumber2);
         jxl.write.Number writeNumber3 = new jxl.write.Number(3, times+1, n_time); // 在A2寫入
         sheet.addCell(writeNumber3);
         jxl.write.Number writeNumber8 = new jxl.write.Number(8, times+1, c_num); // 在A2寫入
         sheet.addCell(writeNumber8);
         jxl.write.Number writeNumber9 = new jxl.write.Number(9, times+1, s_num); // 在A2寫入
         sheet.addCell(writeNumber9);
         jxl.write.Number writeNumber10 = new jxl.write.Number(10, times+1, n_num); // 在A2寫入
         sheet.addCell(writeNumber10);
         jxl.write.Number writeNumber11 = new jxl.write.Number(11, times+1, c_deadpeople); // 在A2寫入
         sheet.addCell(writeNumber11);
        
        medicalHRAS=HRAS.HRAS_method(createpatientHRAS,createambulanceHRAS);
 	    hospitalHRAS=HRAS.gethospitalEOB();
		/*
 	    File newtext2 = new File("patientHRES.txt");
		FileWriter datafile2 = new FileWriter("patientHRES.txt");    //輸出
		BufferedWriter input2 = new BufferedWriter(datafile2);
		
		for(int temp=0;temp<patientHRAS.size();temp++)
		{
			input2.write("病人level為"+patientHRAS.get(temp).level+"時間為"+patientHRAS.get(temp).time+"\r\n");
		}
		for(int temp=0;temp<hospitalHRAS.size();temp++)
		{
			input2.write("醫院"+temp+"的觀察床位數:"+hospitalHRAS.get(temp).EOB);
			input2.write("\r\n");
		}
		*/
		/////////////////output HRAS方法的結果  包含傷患等級,發現時間,送醫時間,選擇醫院
		c_time=0;s_time=0;n_time=0;all_time=0;
		 c_num=0;s_num=0;n_num=0;
		 c_dis=0;s_dis=0;n_dis=0;
		 c_deadpeople=0;
		for(int tempnum=0;tempnum<medicalHRAS.size();tempnum++)   //全部傷患量
		{
			//input2.write("傷患等級:"+medicalHRAS.get(tempnum).level+",發現時間: "+medicalHRAS.get(tempnum).findtime+",送醫時間: "+medicalHRAS.get(tempnum).treattime+",選擇醫院: "+medicalHRAS.get(tempnum).hospitalname);
			//input2.write("\r\n");
			//input.write("醫院"+medical.get(tempnum).hospitalname+"的觀察床位數:"+hospital.get(medical.get(tempnum).hospitalname).EOB);
			//input.write("\r\n");
			if(medicalHRAS.get(tempnum).level==1)
			{
				c_num++;
				c_time=c_time+medicalHRAS.get(tempnum).treattime+hospitalHRAS.get(medicalHRAS.get(tempnum).hospitalname).distance;
				c_dis=c_dis+hospitalHRAS.get(medicalHRAS.get(tempnum).hospitalname).distance;
				if(medicalHRAS.get(tempnum).treattime+hospitalHRAS.get(medicalHRAS.get(tempnum).hospitalname).distance>c_deadline)
				{
					c_deadpeople++;
				}
			}
			else if(medicalHRAS.get(tempnum).level==2)
			{
				s_num++;
				s_time=s_time+medicalHRAS.get(tempnum).treattime+hospitalHRAS.get(medicalHRAS.get(tempnum).hospitalname).distance;
				s_dis=s_dis+hospitalHRAS.get(medicalHRAS.get(tempnum).hospitalname).distance;
			}
			else
			{
				n_num++;
				n_time=n_time+medicalHRAS.get(tempnum).treattime+hospitalHRAS.get(medicalHRAS.get(tempnum).hospitalname).distance;
				n_dis=n_dis+hospitalHRAS.get(medicalHRAS.get(tempnum).hospitalname).distance;
			}
		}
		ave_c_time=c_time/c_num;
		ave_s_time=s_time/s_num;
		ave_n_time=n_time/n_num;
		ave_c_dis=c_dis/c_num;
		ave_s_dis=s_dis/s_num;
		ave_n_dis=n_dis/n_num;
		ave_time=(c_time+s_time+n_time)/(c_num+s_num+n_num);
		all_time=c_time+s_time+n_time;
		ave_dis=(c_dis+s_dis+n_dis)/(c_num+s_num+n_num);
		/*
		System.out.println("\r\nHRAS方式:");
		System.out.println(medicalHRAS.size()+" "+c_num+" "+s_num+" "+n_num);
		System.out.println(all_time+" "+c_time+"  "+s_time+"  "+n_time);
		System.out.println(ave_dis+" "+ave_c_dis+" "+ave_s_dis+"  "+ave_n_dis);
		System.out.println("重症超出deadline人數"+c_deadpeople);
		*/
		//input2.close();
		
		jxl.write.Number writeNumber4 = new jxl.write.Number(4, times+1, all_time); // 在A2寫入
        sheet.addCell(writeNumber4);
        jxl.write.Number writeNumber5 = new jxl.write.Number(5, times+1, c_time); // 在A2寫入
        sheet.addCell(writeNumber5);
        jxl.write.Number writeNumber6 = new jxl.write.Number(6, times+1, s_time); // 在A2寫入
        sheet.addCell(writeNumber6);
        jxl.write.Number writeNumber7 = new jxl.write.Number(7, times+1, n_time); // 在A2寫入
        sheet.addCell(writeNumber7);
        jxl.write.Number writeNumber12 = new jxl.write.Number(12, times+1, c_deadpeople); // 在A2寫入
        sheet.addCell(writeNumber12);
		
		
		hospital.clear();
		hospitalHRAS.clear();
		medical.clear();
		medicalHRAS.clear();
		createpatient.clear();
		createpatientHRAS.clear();
		createambulance.clear();
		createambulanceHRAS.clear();
		
	}
	workbook.write(); 
    workbook.close();
	}
}

