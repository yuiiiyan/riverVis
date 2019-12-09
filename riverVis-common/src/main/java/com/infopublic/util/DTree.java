package com.infopublic.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public class DTree {
	public TreeNode root;
	private boolean[] visable;//可见性数组，在比较的过程中，有些属性使用完毕
	private static final int NO_FOUND=-1;//未找到节点
	private Object[] trainingArray;
	private int nodeIndex; //需要判断的属性在字符串中的位置,本实例中在第5个，节点索引
	public String result1;
	public String result2;
	public double result3;  //将result1、result2、result3传至DataAnalysisController中
	//public static void main(String[] args){
		/*try{
            //调用Class.forName()方法加载驱动程序
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("成功加载MySQL驱动！");
        }catch(ClassNotFoundException e1){
            System.out.println("找不到MySQL驱动!");
            e1.printStackTrace();
        }
        
        String url="jdbc:mysql://10.9.9.30:3306/mysql";    //JDBC的URL    
        //调用DriverManager对象的getConnection()方法，获得一个Connection对象
        Connection conn;
        try {
            conn = DriverManager.getConnection(url,"root","hn123456Aa");
            //创建一个Statement对象
            Statement stmt = conn.createStatement(); //创建Statement对象
            System.out.println("成功连接到数据库！");
            String sql = "select * from data_analysis where time=1";    //要执行的SQL
            ResultSet rs = stmt.executeQuery(sql);//创建数据对象
                System.out.println("编号"+"\t"+"姓名"+"\t"+"年龄");
                while (rs.next()){
                    System.out.print(rs.getString(1) + "\t");
                    System.out.print(rs.getString(2) + "\t");
                    System.out.print(rs.getString(3) + "\t");
                    System.out.println();
                }

            stmt.close();
            conn.close();
        } catch (SQLException e){
            e.printStackTrace();
        }*/
 
/*		Object[] array=new Object[]{ //实例化一个数据集array
			new String[]{"开慧镇","2","28","1","10","预警五级"}, //2代表数据类型为雨量,1代表数据类型为水位
			new String[]{"开慧镇","2","23","1","8","预警四级"},
			new String[]{"开慧镇","2","20","1","5","安全"},
			new String[]{"开慧镇","2","17","1","8","安全"},
			new String[]{"开慧镇","2","14","1","2","安全"	},
			new String[]{"开慧镇","2","15","1","8","预警一级"},
			//new String[]{"开慧镇","2","14","1","6","预警一级"},
			};
	    DTree tree=new DTree();//初始化一个实例树Tree
		tree.create(array,5);//样本以及样本中需要判断的属性所处的位置
		System.out.println("========END PRINT TREE========");
		//String[] printData=new String[]{"女","中年","未婚","大学","低"};
		String[] printData=new String[]{"开慧镇","2","23","1","7"};
		System.out.println("=========DECISION RESULT========");
		tree.compare(printData,tree.root);*/
	//}
	
	
	public void compare(String[] printData,TreeNode node){
			int index=getNodeIndex(node.nodeName);
			if(index==NO_FOUND){
			result1=printData[0];
			result2=node.nodeName;
			result3=(node.percent*100);
				//System.out.println(printData[0]+":"+node.nodeName+":"+(node.percent*100)+"%");
				
				//System.out.println((node.percent*100)+"%");
			}
			TreeNode[] childs=node.childNodes;
			for(int i=0;i<childs.length;i++){
				if(childs[i]!=null){
					if(childs[i].parentArrtibute.equals(printData[index])){
						compare(printData, childs[i]);
					}
					
				}
			}
		}
	
    public void create(Object[] array,int index){
					 this.trainingArray=array;//将数据集付值给训练集
					 init(array,index);
					 createDTree(array);
					 printDTree(root);
				 }
    public Object[] getMaxGain(Object[] array){//第一个是最大熵值,第二个是熵值所在的列//这个地方每个训练集中的visable是不一样的
					 Object[] result=new Object[2];
					 double gain=0;
					 int index=-1;
					 for(int i=0;i<visable.length;i++){
						 if(!visable[i]){//在初始化中visable[5]被定义为true,其他的都被定义为false//这个地方就是考虑那些列参加熵值计算
							 double value=gain(array,i);//value为训练集中列i的属性的熵值
							 if(gain<value){
								 gain=value;
								 index=i;
							 }
						 }
					 }
					 result[0]=gain;//熵值最大的一个
					 result[1]=index;//商值最大的一个所在的列
					 if(index!=-1){
						 visable[index]=true;//将这一列定义为true,这就和第5列一样,在以后的熵值计算中不考虑//由此可见每计算出一个最大熵值这个地方就改变
					 }
					 return result;
								 
				
				 }
    public void createDTree(Object[] array){
					 Object[] maxgain=getMaxGain(array);//visable一定的条件下,样本集array中熵值最大的值数,以及所在的列
					 if(root==null){
						 root=new TreeNode();//新建一个节点，这个地方树是一个类,TreeNode是树中的一个类
						 root.parent=null;
						 root.parentArrtibute=null;
						 root.arrtibutes=getArrtibutes(((Integer)maxgain[1]).intValue());//参数为array中熵值最大的一个所在的列,arrtibutes中保存了该列属性值的不同取值,以数组的方式保存
						 root.nodeName=getNodeName(((Integer)maxgain[1]).intValue());//该列的属性名称
						 root.childNodes=new TreeNode[root.arrtibutes.length];//实例化该节点的孩子节点数组
						 insertTree(array,root);//在这里root是一个实例化的对象,他的孩子节点也已经实例化//每计算出一个熵值,array中的visble就改变
					 }//可见这里的array和上面的已经不同了
				 }
    public void insertTree(Object[] array,TreeNode parentNode){
					 String[] arrtibutes=parentNode.arrtibutes;
					 for(int i=0;i<arrtibutes.length;i++){
						 //System.out.println(parentNode.nodeName);
						// String c1=parentNode.nodeName;
						// int b=getNodeIndex(parentNode.nodeName);
						// String a1=arrtibutes[i];
						// int b1=getNodeIndex(parentNode.nodeName);
						 Object[] pickArray=pickUpAndCreateArray(array,arrtibutes[i],//父节点nodeName列中第i个值
						getNodeIndex(parentNode.nodeName));//第三个参数为父节点nodeName（属性值名称）所在array中的列
						
						 
						 //这个地方是把和该列属性的属性值相同的行（样本集中）提出来,组成一个新的训练集pickArray
						 Object[] info=getMaxGain(pickArray);
						 double gain=((Double)info[0]).doubleValue();//gain为熵值的大小
						// Double a=gain;
						 if(gain!=0){
							 ///////////////////////////////////////
							 //////////////////////////////////////////2
				 int index=((Integer)info[1]).intValue();
				 TreeNode currentNode=new TreeNode();
				 currentNode.parent=parentNode;
				 currentNode.parentArrtibute=arrtibutes[i];//这个是父节点分过来的属性的一个取值
				 currentNode.arrtibutes=getArrtibutes(index);
				 currentNode.nodeName=getNodeName(index);
				 currentNode.childNodes=new TreeNode[currentNode.arrtibutes.length];
				 parentNode.childNodes[i]=currentNode;//将目前这个节点付值给父节点的子节点数组的第i个
				 insertTree(pickArray, currentNode);
						 }
						 else{
							// int b=1;
							 TreeNode leafNode=new TreeNode();
							 leafNode.parent=parentNode;
							 leafNode.parentArrtibute=arrtibutes[i];
							 leafNode.arrtibutes=new String[0];
							 leafNode.nodeName=getLeafNodeName(pickArray);
							 leafNode.childNodes=new TreeNode[0];
							 parentNode.childNodes[i]=leafNode;
							 double percent=0;
							 String[] arrs=getArrtibutes(this.nodeIndex);//在这里表示第5列的属性的集合
							 for(int j=0;j<arrs.length;j++){//这个地方有问题
								 if(leafNode.nodeName.equals(arrs[j])){//nodeName=第5列中的一个数值,说明到了叶子节点
									 Object[]subo=pickUpAndCreateArray(pickArray,arrs[j],this.nodeIndex);//第一个参数是该训练集,arrs[j]代表第第j个值
									 Object[] o=pickUpAndCreateArray(this.trainingArray,
											 arrs[j],this.nodeIndex);
									 double subCount=subo.length;
									 percent=subCount/o.length;
								 }
							 }
							 leafNode.percent=percent;
								 }
							 }
				 }
    public void printDTree(TreeNode node){
						// System.out.println(node.nodeName);
						 TreeNode[] childs=node.childNodes;
						 for(int i=0;i<childs.length;i++){
							 if(childs[i]!=null){
								// System.out.print(childs[i].parentArrtibute);
								 printDTree(childs[i]);
							 }
						 }
					 }
    public void init(Object[] dataArray,int index){
						 this.nodeIndex=index;//给节点索引付值
						 visable=new boolean[((String[])dataArray[0]).length];//给可见性数组付值
						 for(int i=0;i<visable.length;i++){
							 if(i==index){
								 visable[i]=true;
								 
							 }else{
								 visable[i]=false;
							 }
								 
							 }
						 }
    public Object[] pickUpAndCreateArray(Object[] array,String arrtibute,//第一个参数是该训练集,arrs[j]代表第第j个值,代表5
							 int index){
						 List<String[]> list=new ArrayList<String[]>();
						// int a=array.length;
						// String b=arrtibute;
						// int c=index;
						 for(int i=0;i<array.length;i++){
							// System.out.println(index);
					
						
						
						//	try {
							
							 String[] strs=(String[])array[i];
							
							 if(strs[index].equals(arrtibute)){
								 list.add(strs);
							 }
							//}catch (Exception e)  {
								//System.out.println("error!");
							//}
						 }
							
					 return list.toArray();
					 }//gain为列index的属性的熵
	public double gain(Object[] array,int index){//array为训练集,index为visable组中的一次循环值//也是列的一次循环 其中第5列中visable[5]=true,其他都为false
						 String[] playBalls=getArrtibutes(this.nodeIndex);//该树种 nodeIndex为5, （猜测）playBalls中存放的是第5列的不同值的一个数组//
						 int[] counts=new int[playBalls.length];//这个就相当于不同值的个数
						 for(int i=0;i<counts.length;i++){
							 counts[i]=0;//都付值为0
						 }
						 for(int i=0;i<array.length;i++){//训练集的行数  应该和上面的是一样的,两个for嵌套  当strs代表第一行是做考虑对象进行考虑
							 String[] strs= (String[]) array[i];//strs代表的是训练集中的第i行
							 for(int j=0;j<playBalls.length;j++){//length代表的是行数
								 if(strs[this.nodeIndex].equals(playBalls[j])){//第i行第5列的值等于
									 counts[j]++;//j是第五列不同值的个数  counts[j]是数据集中第j个值的个数
								 }
							 }
						 }
							 double entropyS=0;
							 for( int i=0;i<counts.length;i++){//counts.length代表的是第5列不同值的个数
								 entropyS+= DTreeUtil.sigma(counts[i],array.length);//entropyS的值为第5列的商值
								 
							 }
							 String[] arrtibutes=getArrtibutes(index);//index为一列列的进行循环
							 double sv_total=0;
							 for( int i=0;i<arrtibutes.length;i++){
								 sv_total+=entropySv(array,index,arrtibutes[i],array.length);//第一个为训练集,第二个为计算的列,第三个位该列中的一中属性值,第5个为行数
							 }//sv_total为该属性的熵
							 return entropyS-sv_total;
							 
					 }
	public double entropySv(Object[] array,int index,String arrtibute,//第一个为训练集,第二个为计算的列,第三个为该列中的一中属性值,第5个为行数
								 int allTotal){//这个为该属性值的熵
							 String[] playBalls=getArrtibutes(this.nodeIndex);
							 int[] counts=new int[playBalls.length];
							 for(int i=0;i<counts.length;i++){
								 counts[i]=0;
							 }
							 for(int i=0;i<array.length;i++){
								 String[] strs=(String[]) array[i];//for循环每一行给strs
								 if(strs[index].equals(arrtibute)){//固定列中的一个固定属性
									 for(int k=0;k<playBalls.length;k++){
										 if(strs[this.nodeIndex].equals(playBalls[k])){
											 counts[k]++;//表示第index列,属性arrtibute,第5列中的值和playBalls[k]相同的个数
										 }
									 }
								 }
							 }
								 
									 
									 
									 ///////////////////////////////////////
									 /////////////////////////////////////////4
								 int total=0;
									 double entropySv=0;
									 for(int i=0;i<counts.length;i++){
										 total+=counts[i];//表示第index列，属性值为arrtibute,的行数
									 }
									 for(int i=0;i<counts.length;i++){//第5列不同属性值的个数
										 entropySv+= DTreeUtil.sigma(counts[i],total);//enteropysV为第index列,属性值为arrtibute的行的熵
										 }
									 return DTreeUtil.getPi(total,allTotal)*entropySv;
									
								
							 
						 }
	public String[] getArrtibutes(int index){//index在这个实例中应用付给的值是this.nodeIndex值是5//猜测这里的值为index列不同属性值的一个保存数组
							TreeSet<String> set=new TreeSet<String>(new SequenceComparator());//这个地方不懂
									for(int i=0;i<trainingArray.length;i++){//这个长度为训练集的行数
										String[] strs=(String[]) trainingArray[i];//将训练集中的每一行都付值给strs
										set.add(strs[index]);//strs[5]代表的是购买  没有购买 将他们都加到set这个数组中了
									}
									String[] result=new String[set.size()];//result为set的大小,在该程序中为行数了
									return set.toArray(result);//这个地方是第5列值的一个数组,这个数组经过了某种排列//不同情况下index付值不同
								}
	public String getNodeName(int index){//index为某一列,getNodeName为该列的属性名称
									String[] strs=new String[]{"镇","2","雨量大小","1","水位高低","是否预警"};
									for(int i=0;i<strs.length;i++){
										if(i==index){
											return strs[i];
										}
									}
									return null;
								
						}
	public String getLeafNodeName(Object[] array){
									if(array!=null&&array.length>0){
										String[] strs=(String[]) array[0];
										return strs[nodeIndex];
									}
									return null;
								}
    public int getNodeIndex(String name){
									String[] strs=new String[]{"镇","2","雨量大小","1","水位高低","是否预警"};
					                for(int i=0;i<strs.length;i++){
					                	if(name.equals(strs[i])){
					                		return i;
					                	}
					                }
					                return NO_FOUND;
					}
    public static class DTreeUtil{
    	public static double sigma(int x,int total){//循环x=counts[i]为每行下的,total代表行数  i为第5列中不同值的数量,counts[i]某一个确定值在第5列中所拥有的数量
    		if(x==0){
    			return 0;
    		}
    		double x_pi=getPi(x,total);
    		return -(x_pi*logYBase2(x_pi));
    	}
    	public static double logYBase2(double y){
    		return Math.log(y)/Math.log(2);
    	}
    //////////////////////////////
    	//////////////////////6
    	public static double getPi(int x,int total){
    		return x*Double.parseDouble("1.0")/total;
    	}
    }
    public class TreeNode{ //定义一个树的节点类
    	TreeNode parent; //属性值包括,父节点parent
    	String parentArrtibute;//父节点属性值
    	String nodeName;//在该特定训练集中,熵值最大的列的属性名称
    	String[] arrtibutes;//该节点所在位置训练集中熵值最高的列中属性的取值数组,这个地方训练集和其他节点不同的地方为visable中有些不参与的列被设置为了true,在以后的熵值计算中不参与
    	TreeNode[] childNodes;//孩子节点组
    	double percent;//不清楚
    	
    }
    public class SequenceComparator implements Comparator<Object>{
    	public int compare(Object o1,Object o2) throws ClassCastException{
    		String str1=(String) o1;
    		String str2=(String) o2;
    		return str1.compareTo(str2);//按照字典顺序比较两个字符串,在参数之前则负,之后则正
    	}
    }
}
						 


		
