// ALEX GERBESSIOTIS    cs610 9731 prp

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;

public class henc9731{
	public static void main(String[] args) {
		EnCode9731 ec = new EnCode9731();
		ec.henc9731(args[0]);
	}
}

class EnCode9731{
	public int[] readFile9731(String file) {
		System.out.println("Encoding....");
	  int[] characters = new int[256];
	  try {
			File f = new File(file);
			InputStream is = new FileInputStream(f);
	    int bytenumber = is.available();
	    if(bytenumber == 0) {
				String filename = file + ".huf";
	    	FileWriter fw = new FileWriter(filename);
				f.delete();
	    	fw.close();
				System.out.println("Encoding completed");
	    	return null;
	   	}
	    int c = is.read();
	    while(c!=-1) {
				characters[c] ++;
	    	c = is.read();
	    }
	    is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return characters;
	}

	public Vector<Node9731> getA9731(int[] characters ){
		Vector<Node9731> A = new Vector<Node9731>();
		for(int i=0; i<256; i++){
			if(characters[i] != 0){
				Node9731 temp = new Node9731(i, characters[i]);
				A.add(temp);
			}
		}
		return A;
	}

	public int getMin9731(Vector<Node9731> A, int a, int b){
		if(A.get(a).weight<A.get(b).weight){
			return a;
		}
		else{
			return b;
		}
	}

	public int getMinest9731(Vector<Node9731> A, int a, int b, int c){
		if(A.get(a).weight<A.get(b).weight){
			if(A.get(a).weight<A.get(c).weight)
			{
				return a;
			}
			else{
				return c;
			}
		}
		else{
			if(A.get(b).weight<A.get(c).weight)
			{
				return b;
			}
			else{
				return  c;
			}
		}
	}

	public void minDownHeap9731(Vector<Node9731> A, int i,int heapsize){
		int minest;
		if(i>=heapsize){
			return;
	  }
	  if((2*i+1)>heapsize-1 && (2*i+2)>heapsize-1){
			minest = i;
	  }
	  else if((2*i+2)>heapsize-1){
			minest = getMin9731(A,i,(2*i+1));
	  }
	  else {
	  	minest = getMinest9731(A,i,(2*i+1),(2*i+2));
	  }
	  if(minest != i ){
	  	Node9731 temp = new Node9731();
	  	temp = A.get(i);
	  	A.add(i, A.get(minest));
	  	A.remove(i+1);
	  	A.add(minest, temp);
	   	A.remove(minest+1);
	   	minDownHeap9731(A,minest,heapsize);
		}
	}

	public void buildMinHeap9731(Vector<Node9731> A){
	    int heapsize = A.size();
	    for(int i = ((heapsize -1)/2);i>=0 ; i--){
	    	minDownHeap9731(A, i, heapsize);
	    }
	}

	public Node9731 extractMinHeap9731(Vector<Node9731> A){
	    int heapsize = A.size();
	    if(heapsize>=1){
	    	Node9731 min = A.get(0);
	    	A.add(1, A.get(heapsize - 1));
	    	A.remove(heapsize);
	    	A.remove(0);
	    	heapsize--;
	    	minDownHeap9731(A, 0, heapsize);
	    	return min;
	    }
	    return null;
	}

	public void insertMinHeap9731(Vector<Node9731> A, Node9731 key){
		A.add(key);
		int heapsize = A.size();
		int i = heapsize -1;
		while((i>0) && (A.get((i-1)/2).weight>key.weight)){
			A.add(i, A.get((i-1)/2));
			A.remove(i+1);
			i = (i-1)/2;
		}
		A.add(i, key);
		A.remove(i+1);
	}

	public Node9731 makeHuffmanTree9731(Vector<Node9731> A){
		Vector<Node9731> Q = new Vector<Node9731>();
		for(int i = 0; i<A.size(); i++) {
			Q.add(A.get(i));
		}
		buildMinHeap9731(Q);
		for(int i=1;i<(A.size());i++){
			Node9731 a = extractMinHeap9731(Q);
			Node9731 b = extractMinHeap9731(Q);
	    Node9731 c = new Node9731(-1,a.weight + b.weight);
	    c.lchild = a;
	    c.rchild = b;
	    insertMinHeap9731(Q, c);
		}
		return extractMinHeap9731(Q);
	}

	public void makeHufCode9731(Node9731 root, String[] hufcode, String str) {
		if(root.rchild == null && root.lchild == null) {
			hufcode[root.data] = str;
			return;
		}
		str = str+"0";
		makeHufCode9731(root.lchild, hufcode, str);
		str = str.substring(0, str.length() - 1);
		str = str+"1";
		makeHufCode9731(root.rchild, hufcode, str);
		str = str.substring(0, str.length() - 1);
	}

	public void writeToFile9731(String[] hufcode,String file, Vector<Node9731> A){
		String result ="";
		String sub ="";
		int count = 0;
	  int a,b;
	  char real;
		int i;
		int bitcount = 0;
		int restbit = 0;
		int countzero = 0;
		try {
			File f = new File(file);
			String hufname = file + ".huf";
			InputStream is = new FileInputStream(f);
			FileWriter writer = new FileWriter(hufname);
			BufferedWriter bu = new BufferedWriter(writer);
			int number = is.available();
			bu.write(A.size());
	    for(i = 0;i<A.size();i++) {
				bu.write(A.get(i).data);
	    	bu.write(A.get(i).weight);
	    }
	    bu.flush();
			int c = is.read();
	    while(c!=-1) {
	 			a = 0;
	    	b = 1;
	   		bitcount = bitcount + hufcode[c].length();
	   		result = result + hufcode[c];
	   		if(bitcount>=8) {
	   			restbit = bitcount-8;
	   			sub = result.substring(0,8);
	   			for(i=7; i>=0; i--) {
	   	    		a += (sub.charAt(i)-'0')*b;
	   	    		b *= 2;
	   	   	}
	   	  	real = (char)a;//转化为新字符
	   	   	bu.write(real);
    	   	count++;
				  result = result.substring(8);
			   	bitcount = restbit;
	   	    }
	   		c = is.read();
	    }
	    countzero = 8 - bitcount;
	    if(countzero<8) {
	    	for(i =0 ; i<countzero;i++) {
		    	result = result+"0";
		   	}
		   	a = 0;
		   	b = 1;
		   	for(i=7; i>=0; i--) {
		    	a += (result.charAt(i)-'0')*b;
		    	b *= 2;
		   	}
		   	real = (char)a;
		   	bu.write(real);
		   	count++;
	   	}
	   	bu.flush();
			f.delete();
		  is.close();
		  writer.close();
		  bu.close();
			System.out.println("Encoding completed");
  	} catch (IOException e) {
 			e.printStackTrace();
		}
	}

	public void henc9731(String arg){
		String str = "";
		String file = arg;
		int [] characters = readFile9731(file);
		if(characters == null)
			return;
		Vector<Node9731> A = getA9731(characters);
		Node9731 root = makeHuffmanTree9731(A);
	  String[] hufcode = new String[256];
	  makeHufCode9731(root,hufcode,str);
	  writeToFile9731(hufcode,file, A);
	}
}

class Node9731 {
	public int data;
	public int weight;
	public Node9731 lchild;
	public Node9731 rchild;

	public Node9731(){
		this.data = 0;
		this.weight = 0;
		lchild = rchild = null;
	}

	public Node9731(int d, int w) {
		this.data = d;
		this.weight = w;
		lchild = rchild = null;
	}
}
