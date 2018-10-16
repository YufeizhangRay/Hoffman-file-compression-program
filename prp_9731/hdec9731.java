// ALEX GERBESSIOTIS    cs610 9731 prp

import java.io.File;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

public class hdec9731 {
	public static void main(String[] args) {
		DeCode9731 ec = new DeCode9731();
		ec.hdec9731(args[0]);
	}
}

class DeCode9731{
	static int charnumber;
	static int sizeofA;
	public Vector<Node9731> readFile9731(String hufname) {
		System.out.println("Decoding....");
    Vector<Node9731>A = new Vector<Node9731>();
	  try {
			File hf = new File(hufname);
	   	InputStream is = new FileInputStream(hf);
	   	FileReader reader = new FileReader(hf);
	   	int number = is.available();
	   	if(number == 0) {
	   		String filename = hufname.substring(0,hufname.length()-4);
	   		OutputStream os = new FileOutputStream(filename);
				hf.delete();
	   		os.close();
				System.out.println("Decoding completed");
	   		return null;
	   		}
	   	sizeofA =reader.read();
	   	for(int i = 0; i<sizeofA;i++) {
	   		Node9731 temp = new Node9731(reader.read(),reader.read());
	   		A.add(temp);
	   	}
	   	reader.close();
	   	is.close();
		} catch (IOException e) {
	   	e.printStackTrace();
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

	public String writeToFile9731(String hufname,Node9731 root) {
		String filename = hufname.substring(0,hufname.length()-4);
		String result = "";
    String bit = "";
    String real = "";
    boolean flag = false;
    int bitcount = 0;
    int j = 0;
    int ascii;
	  try {
			File hf = new File(hufname);
			OutputStream os = new FileOutputStream(filename);
	    DataOutputStream dos = new DataOutputStream(os);
	    FileReader fr= new FileReader(hf);
	    for(int i = 0; i<(sizeofA*2+1);i++) {
	    	fr.read();
	    }
	    while((ascii=fr.read())!=-1) {
	    	bit = "";
	      if(ascii!=0) {
					while(ascii!=0) {
						bit = (ascii%2) + bit;
						ascii /= 2;
	        }
					while(bit.length() < 8) {
						bit = '0' + bit;
					}
					result += bit;
				}else {
					result += "00000000";
				}
				Node9731 p = root;
				int len = result.length();
		    for(int i=0; i<len && j< charnumber; i++) {
		   		if(p.data == -1) {
						if(result.charAt(i) == '0') {
							p = p.lchild;
						} else {
							p = p.rchild;
						}
						if(p.data != -1) {
							real += (char)p.data;
		          dos.writeBytes(real);
		          real = "";
		          j++;
		          flag = true;
		          bitcount = i;
		          p = root;
						}
					}
				}
				if(flag) {
					result = result.substring(bitcount+1);
					bitcount=0;
				}
				flag = false;
			}
			hf.delete();
			fr.close();
			dos.close();
			System.out.println("Decoding completed");
		} catch (IOException e) {
	    e.printStackTrace();
		}
		return result;
	}

	public void hdec9731(String arg){
		String hufname = arg;
		Vector<Node9731> A = readFile9731(hufname);
		if(A == null)
			return;
		Node9731 root = makeHuffmanTree9731(A);
		charnumber = root.weight;
	  writeToFile9731(hufname,root);
	}
}

class Node9731{
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
