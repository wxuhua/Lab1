package lab1;
import java.io.*;
import java.util.*;


public class showgraph {
	
	class MatrixDG {

	    String[] mVexs;       // 
	    int[][] mMatrix;    // 
	    MatrixDG(int v)	
	    {
	    	mMatrix=new int[v+1][v+1];
	    	for (int i=0;i<=v;i++)
	    	{
	    		for(int j=0;j<=v;j++)
	    		{
	    			mMatrix[i][j]=0;
	    		}
	    	}
	    }
	    int vlen=0;

	}				
	public static void main(String[] args) {
		String filename="e:/temp/words.txt";
		showgraph ws = new showgraph();
		MatrixDG Graph = ws.createDirectedGraph(filename);
		
		ws.testfunction(Graph);
		
	}
	public void testfunction(MatrixDG graph)
	{
		showDirectedGraph(graph);
		savegraph(graph);
		
		String bridge;
		bridge=queryBridgeWords(graph, "a", "to");
		displaybridge(graph,bridge,  "a", "to");		
		
		bridge=queryBridgeWords(graph, "young", "and");
		displaybridge(graph,bridge,  "young", "and");
		
		bridge=queryBridgeWords(graph, "to","strange");
		displaybridge(graph,bridge,  "to", "strange");
	
		bridge=queryBridgeWords(graph, "there", "exciting");
		displaybridge(graph,bridge,  "there", "exciting");
		
		bridge=queryBridgeWords(graph, "exciting", "synergies");
		displaybridge(graph,bridge, "exciting", "synergies");
		
		String input="there is plan to a new mall your neighbour";		
		String out=generateNewText(graph, input);
			System.out.println(out);
		input="your the proposed is to home where you have lived your life";
		out=generateNewText(graph, input);
			System.out.println(out);
		input="they lived over years";
		out=generateNewText(graph, input);
			System.out.println(out);
		
			
			
		String calcShortestPath=calcShortestPath(graph, "to", "and");
			System.out.println(calcShortestPath);
		
			
		calcShortestPath=calcShortestPath(graph, "there", "your");
			System.out.println(calcShortestPath);
	
			
		calcShortestPath=calcShortestPath(graph, "build", "site");
			System.out.println(calcShortestPath);
		String rwalk=randomWalk(graph);
			System.out.println(rwalk);
		
			
	}
	public void savegraph(MatrixDG G)
	{
		GraphViz gViz=new GraphViz("C:\\Users\\Xuwang","E:\\Java\\graphviz\\bin\\dot.exe");//目标目录（存图片），dot目录
		gViz.start_graph();
		for(int i=0;i<G.vlen;i++) {			
			for(int j=0;j<G.vlen;j++) {
				if(G.mMatrix[i][j]>0&&G.mMatrix[i][j]!=999)
				{
					gViz.addln(G.mVexs[i]+"->"+G.mVexs[j]+";");				
				}
			}
		}		
        gViz.end_graph();
        try {
            gViz.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
	
	}
	private static int getposition(String s, String[] sw)		//get the position of s in array sw
	{
		for (int i=0;i<sw.length;i++){
			if(s.equals(sw[i])) return i;
		}
		return -1;
	}
	public  MatrixDG createDirectedGraph(String filename)	//createDirectedGraph	createDirectedGraph
	{
		//System.out.println("\n"+"1.Create graph "+"\n");		
		String s;
		String s2="";
		try			//reading file
		{
			FileReader inFile = new FileReader(filename);
			BufferedReader buff = new BufferedReader(inFile);
			boolean endOfFile=false;
			while (!endOfFile)
			{
				s=buff.readLine();	//reading line by line to s
				if (s==null)
				{
					endOfFile=true;
				}
				else
				{
					s2=s2+s+" ";
				}
			}
			buff.close(); //	System.out.print("line 65 "+s2);
		}
		catch (IOException e){
			System.out.print("An Error Occurred : "+e.toString());
		}
		//reading done; converting begins (s2 to s3) 
		String s3="";
		int f=0;
		s2=s2.toLowerCase();// to lower case
		for(int i = 0; i<s2.length()-1;i++)
		{
			char c=s2.charAt(i);
			int b = (int)c;
			if(97<=b && b<=122)
			{
				s3=s3+c;
				f=0;
			}
			else if (b==32)//space
			{
				if (f==0)
					{
						s3=s3+' ';
						f=1;
					}
			}
			else if (b==33 || b==34 || b==39 || b==40 || b==41 || b==44 || b==45 || b==46 || b==58 || b==59 || b==63 )
			{
				if (f==0)
					{
						s3=s3+' ';
						f=1;
					}
			}
		}
		//converting to one space
		//String s4; //s4 has only one space between each words
		
		// extracting words
		String[] words = new String[100]; //all different
		String[] words1 = new String[100];	//all of the words; some are the same
		for(int i=0;i<100;i++){ //initializing 
			words1[i]="";
			words[i]="";
		}
		int i=0;
		int j=0;
		while(i<s3.length())
		{
			char c=s3.charAt(i);
			if (c!=' ')
			{
				words1[j]=words1[j]+c;
				i++;
			}
			else{
				i++;
				j++;
			}
		}

		i=0;
		j=0;
		while(i<s3.length())
		{
			char c=s3.charAt(i);
			finder:
			if (c!=' ')
			{
				words[j]=words[j]+c;
				i++;
			}
			else
			{
				if(j>0){
					
					for(int k=0;k<j;k++){
						if(words[k].equals(words[j])){
							i++;
							words[j]=""; 
							break finder;
						}
					}
					
				}
				i++;
				j++;
			
			}
		}
		for(int k=0;k<words.length;k++){
			if(words[k].equals(words[words.length-1])){
				i++;
				words[j]=""; 
				//finder;
			}
		}
		
		
		for (i=0;i<20;i++){
			if (words[i]!="")
				{
					System.out.print(words[i]+"\n");
				}
		}

	//DAG
		int cw=0,cw2=0;  //cw is the number of differentwords	;	cw2 is number of all words
		for (int t=0;t<words.length;t++)
		{
			if (words[t]!="")
			{
				cw++;
			}
			else
				break;
		}
		for (int t=0;t<words.length;t++)
		{
			if (words1[t]!="")
			{
				cw2++;
			}
			else
				break;
		}
		int vlen = cw;
		int elen=0;
		for(i=0;i<s3.length();i++)
		{
			char c1=s3.charAt(i);
			if(c1==' ')
			{
				elen+=1;
			}
			
		}
		String[] vexs= new String[100];
		for(i=0;i<words.length;i++){
		vexs[i]=words[i];
		}
		
		MatrixDG aMATRIX;		//creating matrix 
		aMATRIX=new MatrixDG(vlen);
		aMATRIX.vlen=cw;
		aMATRIX.mVexs= new String[vlen];
        for (int m = 0; m < aMATRIX.mVexs.length; m++)		// initializing vertexes
        	{
        		aMATRIX.mVexs[m] = words[m];
        		//System.out.print("!:  "+aMATRIX.mVexs[m]+"\n");
        	}
        aMATRIX.mMatrix = new int[vlen][vlen];
        int p1,p2;
       
        for (i = 0; i < cw2; i++) {
            p1=getposition(words1[i],words);
            p2=getposition(words1[i+1],words);
           // System.out.print(p1+"  "+p2+"\n");
            if (words1[i]!="" && words1[i+1]!="")
            {
            		aMATRIX.mMatrix[p1][p2]++;
            }
        }
        for (int x=0;x<aMATRIX.vlen;x++)
        {
        	for (int xx=0;xx<aMATRIX.vlen;xx++)
            {
            	if (aMATRIX.mMatrix[x][xx]==0)
            	{
            		aMATRIX.mMatrix[x][xx]=999;
            	}
            }
        }             
        return aMATRIX;
	}

	public void showDirectedGraph(MatrixDG G)//输出图邻接表
	{
		//System.out.println("\n"+"2.output adjlist"+"\n");
		MatrixDG localG=new MatrixDG(G.vlen);
		localG = G;

		for(int i=0;i<localG.vlen;i++)
		{
			System.out.print(G.mVexs[i]+"->");
			for(int j=0;j<localG.vlen;j++)
			{
				if(localG.mMatrix[i][j]>0 && localG.mMatrix[i][j]!=999)
				{
					System.out.print(G.mVexs[j]+"->");
					//localG.mMatrix[i][j]--;
				}
				
			}
			System.out.print("null"+"\n");
		}
		
		
	}
	private  String queryBridgeWords(MatrixDG G, String word1, String word2)//鏌ヨ妗ユ帴璇 
    {
		
		String s = "";
        int i, i1;
        int j, k, f;
         for(int m=0;m<G.mVexs.length;m++){
        	for(int n=0;n<G.mVexs.length;n++){
        		if(G.mMatrix[m][n]!=999)
        		{
        			if (G.mMatrix[m][n]>1)
        			{
        				G.mMatrix[m][n]=1;
        			}
        		}       			
        	}
        }
        String[] strlist = new String[G.mVexs.length];
        for (i = 0; i < G.mVexs.length; i++) {
            strlist[i] ="";
        }
        int[] list = new int[G.mVexs.length];
        for (i = 0; i < G.mVexs.length; i++) {
            if (G.mVexs[i].equals(word1)) {
                for (i1 = 0; i1 < G.mVexs.length; i1++) {
                    if (G.mMatrix[i][i1] == 1) {
                        list[i1] = 1;
                    } else list[i1] = 0;
                }

            }
            //else return "";//word1 is'nt in Graph

        }
        for (j = 0; j < list.length; j++) {
            if (list[j] == 1) {
                for (k = 0; k < G.mVexs.length; k++) {
                    if (G.mMatrix[j][k] == 1) {
                    	if(G.mVexs[k].equals(word2)) {
                    		strlist[j] = G.mVexs[j];	                        
                    	}
                  //  	else return "";//word1 is'nt in Graph
                    }
                }
            }
        }
        for (f = 0; f < G.mVexs.length; f++) {
           s +=strlist[f] +" ";
        }
      
       return s.trim();
    }	
	
	public void displaybridge(MatrixDG graph,String bridges,String s1,String s2)//display bridge words
	{	
		bridges=queryBridgeWords(graph,s1,s2);
		//System.out.println(bridges.trim());
		String []str=(bridges.trim()).split("\\s+");//去掉首尾两端空格，按照串中空格分割
		//0个，1个，多个bridge words 判断
		if (str.length==0||bridges.trim().length()==0) {
			int flag1=0,flag2=0;
			for(int i=0;i<graph.vlen;i++) {		
				if(graph.mVexs[i].equals(s1)) {
					flag1=1;
				}
				if(graph.mVexs[i].equals(s2)) {
					flag2=1;//
				}
			}
			// s1 s2:00，01，10，11分布情况下的输出
			if(flag1==0&&flag2==0) {				
				System.out.println("No \""+s1+"\" and \""+s2+"\" in the graph!");						
			}
			else if(flag1==0&&flag2==1)	System.out.println("No \""+s1+"\" in the graph!");		
			else if(flag2==0&&flag1==1)	System.out.println("No \""+s2+"\" in the graph!");
			else	System.out.println("No bridge words from \""+s1+"\" to \""+s2+" \"!");
				
		}
		else if(str.length==1)
		{
			System.out.println("The bridge word from \""+s1+"\" to \""+s2+"\" is: "+str[0]);
		}
		else 
		{
			System.out.println("The bridg ewords from \""+s1+"\" to \""+s2+"\" are: ");
			for (int i=0;i<str.length-1;i++)		
			{
				System.out.println(str[i]+" ");
			}
			System.out.println("and "+str[str.length-1]);
		}
		
	}
	
	public boolean checkbridge(String bridges)// check whether exist bridge
	{
		String []str=bridges.split(" ");
		if (str.length==0)
		{
			return false;
		}
		return true;	
	}
	
	public String generateNewText(MatrixDG gg,String inputText)//补全文本
	{
		//System.out.println("\n"+"4.output  generateNewText"+"\n");
		
		String oup="";
		inputText=inputText.toLowerCase();
		String [] str=inputText.split(" ");
	//	System.out.println(str.length+"line 469");
		for(int index=0;index<str.length-1;index++)
		{
			String bridge1=str[index];
			String bridge2=str[index+1];
			String brg=queryBridgeWords(gg,bridge1, bridge2);				
			if (str.length==1) {
				oup=inputText;
			}
			//多个bridge words 随机选取一个
			else if(index<str.length-2){
				if(checkbridge(brg)){
					String [] brgs=brg.split(" ");
					int numspace=brgs.length;
					int n2=(int)(Math.random()*numspace);
					oup +=" "+bridge1+" "+brgs[n2];										
				}
			}
			else 
				{
					oup=oup+bridge2;
				}			
		}		
		return oup.trim();
	}

	private  String randomWalk(MatrixDG g)
	{
		//System.out.println("\n6.Random walk\n");
		String s="";
		Random random = new Random();
		int r = random.nextInt(g.vlen-1); //the starting vertex
		s=s+g.mVexs[r];
		System.out.print("random walk starts. input 's' to stop:\n"+g.mVexs[r]);
		int next=r,cch=0,ch[];
		ch=new int[g.vlen];
		int rpt[]=new int[g.vlen];
		for (int i=0;i<g.vlen;i++)
		{
			ch[i]=0;
			rpt[i]=-1;
		}
		Scanner sc= new Scanner(System.in);
		int cc=1;
		rpt[0]=r;
		while (next!=-1)
		{
			
			cch=0;
			//System.out.print(next);
			String st = sc.nextLine();
			//System.out.print("1"+st+"1");
			if (st.equals("s"))
			{
				System.out.print("STOP");
				return s;
			}
			for (int i=0;i<g.vlen;i++ )
			{
				if (g.mMatrix[next][i]!=999)
				{
					ch[cch]=i;
					cch++;
				}
			}
			if (cch==0)
			{
				System.out.print("over. no where to go\n");
				return s;
			}
			int c = random.nextInt(cch);
			for (int x=0;x<g.vlen;x++)
			{
				if (rpt[x]==ch[c])
				{
					System.out.print("edge repeated, quit\n");
					s=s+" -> "+g.mVexs[ch[c]];
					return s;
				}
			}
			next=ch[c];
			rpt[cc]=next;
			cc++;
			s=s+" -> "+g.mVexs[next];
			
			System.out.print(" -> "+g.mVexs[next]);
		}
		sc.close();
		return s;
	}
	private  String calcShortestPath(MatrixDG g, String word1, String word2)
	{	
		// in the algorithm, the number 999 means "false"
		for (int i=0;i<g.vlen;i++)
		{
			for (int j=0;j<g.vlen;j++)
			{
				if (g.mMatrix[i][j]==0)
				{
					g.mMatrix[i][j]=999;
				}
			}
		}
		String s="";
		int w1,w2;
		w1=getposition(word1,g.mVexs);
		w2=getposition(word2,g.mVexs);
		//System.out.print(w1+" "+w2);
		int d[]=new int[g.vlen];
		int p[]=new int[g.vlen];
		int f[]=new int[g.vlen];
		
		for (int i=0;i<g.vlen;i++)
		{
			if (g.mMatrix[w1][i]!=999)
				{
					d[i]=g.mMatrix[w1][i];
					p[i]=w1;
				}
			else
				{
					d[i]=999; //infinite long, not reached yet
					p[i]=999; // parent node
				}
			
			f[i]=0;
		}
		d[w1]=0;
		f[w1]=1;
		int min=999;
		int m=999;
		for (int i=0;i<g.vlen-1;i++)	//dijkstra
		{
			min=999;
			for (int j=0;j<g.vlen;j++)
			{
				if (f[j]==0)
				{
					if (d[j]<min)
					{
						m=j;
						min=d[j];
					}
				}
			}
			//System.out.print(min+"\n");
			if (min<999)
			{
				f[m]=1;
			}
			else if (m==999)
			{
				System.out.print("not reachable\n");
				return "NULL";
			}
			for (int j=0;j<g.vlen;j++)
			{
				if (f[j]==0 && g.mMatrix[m][j]+d[m]<d[j])
				{
					d[j]=g.mMatrix[m][j]+d[m];
					p[j]=m;
				}
			}
		}//end dijkstra
		int i=w2,c=0;
		String path[]=new String[100];
		for (int j=0;j<100;j++)
			path[j]="";
		while (i!=w1)
		{
			path[c]=g.mVexs[i];
			g.mVexs[i]=g.mVexs[i]+" *";
			c++;
			i=p[i];
		}
		g.mVexs[w2]=g.mVexs[w2]+"*";
		path[c+1]=g.mVexs[w1];
		g.mVexs[w1]=g.mVexs[w1]+" *";
		for (int j=99;j>=0;j--)
		{
			if (path[j]!="" && j!=0)
			{
				s=s+path[j]+" -> ";
				//System.out.print(path[j]+" -> ");
			}
			if (j==0)
			{
				s=s+path[j];
				//System.out.print(path[j]);
			}
		}
		
		return s;
	}

}