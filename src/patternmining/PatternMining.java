/*
 @RupeshKumarSingh
 */
package patternmining;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

 
import weka.core.*;
import weka.core.converters.ConverterUtils.DataSource;
 import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Discretize;
 
 public class PatternMining {
   public static void main(String[] args) throws Exception {
     Discretize    filter;
     Instances     outputTrain;
     BufferedWriter ClosedPattern= null;
    
     String filename = "discretized.txt";
     BufferedWriter  writer;
     String dsfileName;//the data set file name
     Instances ini_instances = null; //the initial instances
     Instances ini_instances1 = null;//the instances for .txt
     
     //taking Input
     dsfileName=args[0];
     int MS = Integer.parseInt(args[1]);
     double MG = Double.parseDouble(args[2]);

     //Output Of Data File
     ClosedPattern = new BufferedWriter(new FileWriter("ClosedAndMG.csv"));//Pattern, Support, Minima Generator
      
     // DataFile as Input
    /* BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
     System.out.println("Enter the input file name:");
     dsfileName = br.readLine();	*/
     //creating .csv file from .txt
     
     //creating .csv file from .txt
     FileWriter writerc = null;
     File filec = new File(dsfileName);
     Scanner scanFile = new Scanner(filec);
     File Csvfile = new File(dsfileName+".csv");
     Csvfile.createNewFile();
     writerc = new FileWriter(Csvfile);
     
     //copying all the data from .txt to .csv
     while (scanFile.hasNext()) {
         String csv = scanFile.nextLine();
         writerc.append(csv);
         writerc.append("\n");
         writerc.flush();
     }
     
     DataSource frDatac;  
     frDatac = new DataSource( dsfileName+".csv");
     ini_instances1 = frDatac.getDataSet();
     
     //Adding header in .csv
     String header = "CLASS";
     for( int i=1;i<=ini_instances1.numAttributes()-1; i++){
        header = header+", ATTR"+i; 
     }
     
     RandomAccessFile filet = new RandomAccessFile(dsfileName+".csv", "rws");
     byte[] text = new byte[(int) filet.length()];
     filet.readFully(text);
     filet.seek(0);
     filet.writeBytes(header);
     filet.writeBytes("\n");
     filet.write(text);
     filet.close();
     
     DataSource frData = new DataSource( dsfileName+".csv" );  
     ini_instances = frData.getDataSet();           
     ini_instances.setClassIndex(0);
     String[] discoptions = new String [2];
     discoptions[0] = "-R";
     discoptions[1] = "first";
     filter = new Discretize();
	 int bin = 5;	
     filter.setBins(bin);
     filter.setInputFormat(ini_instances);
     // apply filter
     outputTrain = Filter.useFilter(ini_instances, filter);
 
     // save output
     writer = new BufferedWriter(new FileWriter(filename));
     writer.write(outputTrain.toString());
     writer.newLine();
     writer.flush();
     writer.close();
     File tempFile = new File("discretized.txt");
	 Scanner file = new Scanner(tempFile);
	 ArrayList<String> DataSet1=new ArrayList<String>();
	 while(file.hasNext()){
			String line = file.nextLine();
			DataSet1.add(line);
	 }
	 int numAttr=ini_instances.numAttributes()-1;
	 
	 //Calculation Of Minimal Generator and Closed Pattern
	ClosedPatternMinimalGen(bin,DataSet1,numAttr,dsfileName);
	
	/*System.out.println("Enter the Min Support Value: ");
	reader = new Scanner(System.in);
	int MS = reader.nextInt();	
	
	System.out.println("Enter the Min Growth Rate Value: ");
	reader = new Scanner(System.in);
	Double MG = reader.nextDouble();*/
	//FP Growth Implementation
        new FPGrowth(new File("MappingData.csv"), MS);
    
    System.out.println("FP Pattern Completed");
    File tempFile2 = new File("FPPattern.csv");
	Scanner file2 = new Scanner(tempFile2);
	ArrayList<String> DataSet3=new ArrayList<String>();
	while(file2.hasNext()){
		String line = file2.nextLine();
		DataSet3.add(line);
	}

	if(DataSet3.size()==0){
		System.out.println("Error: Minimum Support Value exceeds the Number of maximum pattern inside Datasets");
		System.exit(1);
	}
	String[] stringVal2=null;
	for(int i=0;i<DataSet3.size();i++){
			String[] Pairf=null;
			stringVal2=DataSet3.get(i).split(",");
			Pairf=stringVal2[0].split(" ");
			String[] stringVal3=null;
			int count=0;
			int count2=0;
			for(int j=0;j<DataSet3.size();j++){
				stringVal3=DataSet3.get(j).split(",");
				String[] Pairf1=null;
				Pairf1=stringVal3[0].split(" ");
				if(i!=j){
	
					if (Arrays.asList(Pairf1).containsAll(Arrays.asList(Pairf)))
					{
						if(stringVal2[1].equals(stringVal3[1])){
							count2++;
						}else{						
						}
					
					}	
				}

			}
			if(count2==0){
				ClosedPattern.write("{");
				for(int j=0;j<Pairf.length;j++){
					ClosedPattern.write(Pairf[j]);
					if(j!=(Pairf.length-1)){
						ClosedPattern.write(",");
					}
				}
				ClosedPattern.write("}");
				ClosedPattern.write(" "+stringVal2[1]);
				ClosedPattern.write(" ");
				ArrayList<String> SubSet=new ArrayList<String>();
				for(int j=0;j<DataSet3.size();j++){
					String[] stringVal5=DataSet3.get(j).split(",");
					String[] Pairf1=null;
					Pairf1=stringVal5[0].split(" ");
					if(stringVal2[1].equals(stringVal5[1])){
						if (Arrays.asList(Pairf).containsAll(Arrays.asList(Pairf1)))
						{
							if(stringVal2[0].equals(stringVal5[0])){
								
							}else{
								SubSet.add(stringVal5[0]);
							}
						}
					}
				
				}
				if(SubSet.size()==0){
					ClosedPattern.write("{");
					for(int j=0;j<Pairf.length;j++){
						ClosedPattern.write(Pairf[j]);
						if(j!=(Pairf.length-1)){
							ClosedPattern.write(",");
						}
					}
					ClosedPattern.write("}");
				}
				for(int j=0;j<SubSet.size();j++){
					String[] Pairf5=SubSet.get(j).split(" ");
					int count1=0;
					for(int x=0;x<SubSet.size();x++){
						String[] Pairf4=SubSet.get(x).split(" ");
						
						if (Arrays.asList(Pairf5).containsAll(Arrays.asList(Pairf4)))
						{
							count1++;						
						}
					}
					if(count1==1){
						ClosedPattern.write("{");
						for(int l=0;l<Pairf5.length;l++){
							ClosedPattern.write(Pairf5[l]);
							if(l!=(Pairf5.length-1)){
								ClosedPattern.write(",");
							}
						}
						ClosedPattern.write("}");
					}
				}
				ClosedPattern.write("\n");
	
			}
	}
	ClosedPattern.close();
	System.out.println("Task:1 (Closed Pattern N Minimal Generator)");
        BitwiseMds();
    File tempFileFp = new File("MappingData.csv");
	Scanner filefp = new Scanner(tempFileFp);
	ArrayList<String> DataSetFp=new ArrayList<String>();
	while(filefp.hasNext()){
		String line = filefp.nextLine();
		DataSetFp.add(line);
	}

	int totalp=0;
	int totaln=0;
	 for (int j=0;j<ini_instances.numInstances();j++)
     {     	
      		if(ini_instances.instance(j).value(0)==1){
      			totalp++;
      		}else{
      			totaln++;
      		}           	
     }
	 ArrayList<Double> GrowthRate=new ArrayList<Double>();
	 ArrayList<String> GrowthRateIndex=new ArrayList<String>();
	int c=1;
	int epid=0;
	for(int j=0;j<DataSet3.size();j++){
		String[] Pairep=null;
		String[] stringout=DataSet3.get(j).split(",");
		Pairep=stringout[0].split(" ");
		int countp=0;
		int countn=0;
		Double[] Support=new Double[2];
		for(int i=1;i<DataSetFp.size();i++){
			String[] stringEp= DataSetFp.get(i).split(" ");
			if (Arrays.asList(stringEp).containsAll(Arrays.asList(Pairep)))
			{
				if(ini_instances.instance(i-1).value(0)==1){
					countp++;
				}else{
					countn++;
				}
			}
		}

		Support[0]=((double) countp/(double) totalp);
		Support[1]=(double) ((double) countn/(double) totaln);
		
		Double term1=(Support[0]/Support[1])+0.000001*c;
		 if (Double.isNaN(term1)){
      		 term1=0.0;
      	 }
		 Double term2=(Support[1]/Support[0])+0.000001*c;
		 if (Double.isNaN(term2)){
      		 term2=0.0;
      	 }
		 c++;
		if(term1>term2){
			 if (Double.isInfinite(term1)){
	      	 }else{
	     		GrowthRate.add(term1);
	     		GrowthRateIndex.add(stringout[0]+","+round(Support[0],4)+"  "+round(Support[1],4));
	      		epid++;
	      	 }
		
		}else{
			 if (Double.isInfinite(term2)){
	      	 }else{
	      		GrowthRate.add(term2);
	      		GrowthRateIndex.add(stringout[0]+", "+round(Support[0],4)+"  "+round(Support[1],4));
	      		 epid++;
	      	 }
			
		}	
	}	
	Collections.sort(GrowthRate);
	System.out.println("Task:2 (BitSet Mds Completed)");
	//Call to EmergingPattern Function
	if(round(GrowthRate.get(GrowthRate.size()-1),4)<MG){
		System.out.println("Error:  Minimum Growth Rate Should Be Lower Than Maximum GrowthRate Calculated");
		System.exit(1);
	}
    double Avg=EmergingPattern(GrowthRate, GrowthRateIndex, MG);
    System.out.println("Task:3 (Emerging Pattern Completed)"); 		
    
	//Call to JaccardSimilarity Function
	double jaccardmin=JaccardSimilarity();
	double sumJac=Avg*(1-jaccardmin);
	String appjacc=sumJac+"";
	 RandomAccessFile f = new RandomAccessFile("PSkEPs.csv", "rws");
     byte[] text1 = new byte[(int) f.length()];
     f.readFully(text1);
     f.seek(0);
     f.writeBytes(appjacc);
     f.write(text1);
     f.close();
	System.out.println("Task:4 (Jaccard Similarity Completed)");
    System.out.println("Done");
   }
   
   
	//Closed Pattern and Minimal Generator Calculation
   //Writing to DiscretizedD.csv, ClosedAndMG.csv and Mappingdata.csv
   public static void ClosedPatternMinimalGen(int bin,ArrayList<String>DataSet1, int numAttr,String dsfileName) throws IOException{
	   
	     BufferedWriter DiscretizationMap = null;
	     BufferedWriter DiscretizedD = null;
	     BufferedWriter FPGrowth= null;
	     DiscretizationMap = new BufferedWriter(new FileWriter("DiscretizationMap.csv"));//attribute withvaluepair and ID            
	     DiscretizedD = new BufferedWriter(new FileWriter("DiscretizedD.csv"));//discretized Data
	     FPGrowth = new BufferedWriter(new FileWriter("MappingData.csv")); // Mapping of discretized data
		 File tempFile1 = new File(dsfileName);
		 Scanner file1 = new Scanner(tempFile1);
		 ArrayList<String> DataSet2=new ArrayList<String>();
		 while(file1.hasNext()){
			String line = file1.nextLine();
			DataSet2.add(line);
		}
		String[] stringVal1=null;
		int ID=1;
		String[] attribute = null;
	   DiscretizationMap.write("Attribute"+" "+"Interval/Value-Pair"+" "+" ID"+"\n");
		attribute=DataSet2.get(1).split(",");
		for(int j=1;j<attribute.length;j++){
			DiscretizedD.write("Attribute"+j+" ");
		}
		DiscretizedD.write("\n");
		int index1=0;
		Integer[] IDp=null;
		int ID1=0;
		int totalattr=numAttr;
		String[] valueP=new String[bin*totalattr];
		for(int i=0;i<DataSet1.size();i++){
			if(i>=3){
				stringVal1=DataSet1.get(i).split("'");
				if(stringVal1[0].equals("@attribute ")){				
					for(int j=4;j<stringVal1.length;j=j+4){
						int l = stringVal1[j].length();
						DiscretizationMap.write(stringVal1[1]+" ");
						String Valpair=removeCharAt(stringVal1[j], l-1);
						valueP[ID1]=Valpair;
						DiscretizationMap.write(Valpair+" "+ID+"\n");
						ID++;
						ID1++;
					}
				} else if(DataSet1.get(i).equals("@data")){
					index1=i;
				}else if(i>index1){
					int kl=0;
					 for(int j=2;j<stringVal1.length;j=j+4){
						int l = stringVal1[j].length();
						String Valpair=removeCharAt(stringVal1[j], l-1);
						DiscretizedD.write(Valpair+" ");
						for(int x=kl;x<bin+kl;x++){
							if(valueP[x].equals(Valpair)){
								FPGrowth.write((x+1)+" ");
							}
						}
						kl=kl+bin;	
					}
					DiscretizedD.write("\n");
					FPGrowth.write("\n");
				}
				
			}
		 }
		DiscretizationMap.close();
		DiscretizedD.close();
		FPGrowth.close();
   }
   
   //Bitwise of Mds Pattern
   public static void BitwiseMds() throws IOException{
	   
	   BufferedWriter BitSet= null;
	   BitSet = new BufferedWriter(new FileWriter("BitSet.csv"));//Matching datasets of patterns 
	   File tempFile2 = new File("FPPattern.csv");
		Scanner file2 = new Scanner(tempFile2);
		ArrayList<String> DataSet3=new ArrayList<String>();
		while(file2.hasNext()){
			String line = file2.nextLine();
			DataSet3.add(line);
		}
	   File tempFileFp = new File("MappingData.csv");
		Scanner filefp = new Scanner(tempFileFp);
		ArrayList<String> DataSetFp=new ArrayList<String>();
		while(filefp.hasNext()){
			String line = filefp.nextLine();
			DataSetFp.add(line);
		}
		String[] stringFp=null;
		String[] PatternId=new String[DataSet3.size()];
		for(int j=0;j<DataSet3.size();j++){
			BitSet.write("P"+j+" ");
			String[] Pairfp1=null;
			String[] stringout1=DataSet3.get(j).split(",");
			PatternId[j]=stringout1[0];
		}
	   BitSet.write("P"+(DataSet3.size())+" ");
		BitSet.write(" \n");
		
		for(int i=1;i<DataSetFp.size();i++){
			stringFp= DataSetFp.get(i).split(" ");
			String[] stringout=null;
			for(int j=0;j<DataSet3.size();j++){
				String[] Pairfp=null;
				stringout=DataSet3.get(j).split(",");
				Pairfp=stringout[0].split(" ");
				if (Arrays.asList(stringFp).containsAll(Arrays.asList(Pairfp)))
				{
							BitSet.write(1+"  ");			
				}else{
					BitSet.write(0+"  ");
				}
			}
			BitSet.write(" \n");
		}
		BitSet.close();
   }
   
   
   //Closed Emerging Pattern Calculation
   public static double EmergingPattern(ArrayList<Double> GrowthRate, ArrayList<String> GrowthRateIndex, Double MG) throws IOException{
	   
	    ArrayList<Double> GrowthArray=new ArrayList<Double>();
	    BufferedWriter EmerPattern= null;
	    EmerPattern = new BufferedWriter(new FileWriter("PSkEPs.csv"));//Pattern with growthrate and support
	    Double[] GrowthRate1=new Double[GrowthRate.size()];
	    EmerPattern.write("\n");
		String[] GrowthRateIndex1=new String[GrowthRate.size()];
		Map<Double, String> MapIndex=new HashMap<Double, String>();
		GrowthRate1=GrowthRate.toArray(GrowthRate1);
		GrowthRateIndex1=GrowthRateIndex.toArray(GrowthRateIndex1);
	    for(int ind=0; ind < GrowthRate1.length; ind++){
	   	 MapIndex.put(Double.valueOf(GrowthRate1[ind]), String.valueOf(GrowthRateIndex1[ind]));
	    }
	    Arrays.sort(GrowthRate1); 
	    for(int ind=0; ind < GrowthRate1.length; ind++){
	    	GrowthRateIndex1[ind] = MapIndex.get(GrowthRate1[ind]).toString();
	    }
	    int j=0;
	    for(int ind=GrowthRate1.length-1; ind > 0; ind--){
	    	int retval = Double.compare(round(GrowthRate1[ind],4), MG);
	    	String[] stringout=GrowthRateIndex1[ind].split(",");
	    	if(retval>0){
	    		EmerPattern.write("{"+stringout[0]+"}  "+round(GrowthRate1[ind],4)+"  "+stringout[1]+"\n");
	    		GrowthArray.add(round(GrowthRate1[ind],4));
	    		if(j==0){
	    			System.out.println("Highest GrowthRate "+round(GrowthRate1[ind],4)+" Emerging Pattern "+stringout[0]);
	    		}
	    	    	j++;    	 	
	    	}
	    }
	
		EmerPattern.close();
		Double Sum=0.0;
		for(int i=0;i<GrowthArray.size();i++){
		    Sum=Sum+GrowthArray.get(i);
		}
		double Avg=Sum/GrowthArray.size();
		return Avg;
   }
   
   
   //Jaccard Similarity Calculation
   public static double JaccardSimilarity() throws IOException{
	   
	   ArrayList<Double> jaccardArray=new ArrayList<Double>();
	   BufferedWriter Jaccard= null;
	   Jaccard = new BufferedWriter(new FileWriter("PSkEPJaccard.csv"));//two EPs with Jaccard Similarity	     
	   File tempFileFp = new File("MappingData.csv");
		Scanner filefp = new Scanner(tempFileFp);
		ArrayList<String> DataSetFp=new ArrayList<String>();
		while(filefp.hasNext()){
			String line = filefp.nextLine();
			DataSetFp.add(line);
		}
	    File tempFileEp = new File("PSkEPs.csv");
	 	Scanner fileEp = new Scanner(tempFileEp);
	 	ArrayList<String> DataSetEp=new ArrayList<String>();
	 	
	 	
	 	while(fileEp.hasNext()){
	 		String line = fileEp.nextLine();
	 		DataSetEp.add(line);
	 	}
	 	String[] array1=new String[DataSetEp.size()];
	 	for(int n=1;n<DataSetEp.size();n++){
	 		 String[] ValEp1=DataSetEp.get(n).split("}");
	    	 String Valpair1=removeCharAt(ValEp1[0], 0);
	    	 array1[n]=Valpair1;
	 	}
	    int[] row1=new int[DataSetFp.size()];
	 	int[] row2=new int[DataSetFp.size()];	 
	 	for(int n=1;n<DataSetEp.size();n++){
	 		String[] ValEp1=array1[n].split(" ");
	 		int on=0,ze=0;
	 		for(int j=1;j<DataSetFp.size();j++){
	 			String[] ValEp2=DataSetFp.get(j).split(" ");
	 			if (Arrays.asList(ValEp2).containsAll(Arrays.asList(ValEp1)))
				{
	 				row1[j-1]=1;on++;
				}else{
					row1[j-1]=0;ze++;
				}
	 	 	}
	 		for(int i=1;i<DataSetEp.size();i++){
	 			String[] ValEp3=array1[i].split(" ");
	 			int on1=0,ze1=0;
	 			for(int j=1;j<DataSetFp.size();j++){
	 				String[] ValEp4=DataSetFp.get(j).split(" ");
	 	 			if (Arrays.asList(ValEp4).containsAll(Arrays.asList(ValEp3)))
	 				{
	 	 				row2[j-1]=1;on1++;
	 				}else{
	 					row2[j-1]=0;ze1++;
	 				}
	 	 	 	}
	 	 	 	Jaccard.write("{"+array1[n]+"}  ,  {"+array1[i]+"}");
	 			double one1=0.0,one0=0.0,zero0=0.0,zero1=0.0;
	 			for(int l=0;l<row1.length;l++){
	 				if((row1[l]==1) && (row2[l]==1)){
	 					one1=one1+1.0;
	 				}else if((row1[l]==1) && (row2[l]==0)){
	 					one0=one0+1.0;
	 				}else if((row1[l]==0) && (row2[l]==1)){
	 					zero1=zero1+1.0;
	 				}else{
	 					zero0=zero0+1.0;
	 				}
	 			}
	 			double jaccard=0.0;
	 			jaccard=(double)one1/(double)(one0+one1+zero1);
	 			if (Double.isInfinite(jaccard) || Double.isNaN(jaccard)){
	 				if(array1[i].equals(array1[n])){
	 					jaccard=1.0;
	 				}else{
	 					jaccard=0.0;
	 				}
		      	 }
	 			Jaccard.write("  , "+round(jaccard,4));
	 			jaccardArray.add(round(jaccard,4));
	 			Jaccard.write("\n");
	 		}
	 		
	 	}
	 	Collections.sort(jaccardArray);
	 	double jaccardmin=jaccardArray.get(0);
	 	Jaccard.close();
	 	return jaccardmin;
   }
   
   
   //Rounding of double value
	public static double round(double value, int places) {
	    
		if (places < 0) throw new IllegalArgumentException();
	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	
	//Character removal at an index in a String
   public static String removeCharAt(String s, int pos) {
	     
	   return s.substring(0, pos) + s.substring(pos + 1);
	   }
 }
