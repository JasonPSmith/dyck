package Dyck;
import java.util.*;
import java.io.*;

public class Chain implements Comparable<Chain>{
	public int length;
	public int[][] hashFun;
	private int[] embed;
	public boolean checked;
	private ArrayList<ArrayList<int[]>> MSI = new ArrayList<ArrayList<int[]>>();
	private ArrayList<int[]> thechain = new ArrayList<int[]>();
	
	//create empty chain
	public Chain(){
		ArrayList<int[]> thechain = new ArrayList<int[]>();
		ArrayList<ArrayList<int[]>> MSI = new ArrayList<ArrayList<int[]>>();
		length = 1;
		int[] embed;
	}
	
	//create copy of chain and add an extra perm
	public Chain(Chain copy,int[] toadd){
		for(int i = 0; i < copy.length-1; i++){
			thechain.add(copy.get(i));
		}
		thechain.add(toadd);
		length = copy.length+1;
		ArrayList<ArrayList<int[]>> MSI = new ArrayList<ArrayList<int[]>>();
		int[] embed;
	}
	
	public Chain(Chain copy){
		for(int i = 0; i < copy.length-1; i++){
			thechain.add(copy.get(i));
		}
		length = copy.length;
		ArrayList<ArrayList<int[]>> MSI = new ArrayList<ArrayList<int[]>>();
		int[] embed;
	}
	
	public ArrayList<int[]> getChain(){
		return thechain;
	}

	public void addMSI(ArrayList<ArrayList<int[]>> in){
		MSI=in;
	}

	public ArrayList<ArrayList<int[]>> getMSI(){
		return MSI;
	}
	
	public void setEmbed(int[] in){
		embed = new int[in.length];
		System.arraycopy(in,0,embed,0,in.length);
		checked = false;
	}
	public int[] embed(){
		return embed;
	}
	
	public void makeEmbed(int[] pi){
		int[] e = new int[pi.length];
		for(int i = 0; i < hashFun.length; i++){
			e[hashFun[i][0]-1] = -1;
			e[hashFun[i][1]-1] = -1;
		}
		for(int i = 0; i < pi.length; i++){
			if(e[i] != -1) e[i] = pi[i];
		}
		
		this.embed = e;
	}
	
	public void check(){
		checked = true;
	}
	
	public void printEmbed(){
		String s = "";
		for(int i = 0; i < this.embed.length; i++){
			if(embed[i] == -1) s += "*";
			else s += embed[i];
		}
		System.out.print(s);
	}
	
	
	public void add(int[] newElem){
		thechain.add(newElem);
		length++;
	}

	public void printMSI(){
		for(int i = 0; i < MSI.size(); i++){
			for(int j = 0; j < MSI.get(i).size(); j++){
				Methods.printPath(MSI.get(i).get(j));
				if(j!=MSI.get(i).size()-1) System.out.print(" > ");
			}
			if(i!=MSI.size()-1) System.out.print(" : ");
		}
		System.out.print("");
	}
	
	public void print(boolean incHash){
		if(length == 1) System.out.println("NULL chain");
		else{
			for(int i = 0; i < length-1; i++){
				Methods.printPath(thechain.get(i));
				if(i!=length-2) System.out.print(" > ");
			}
			if(incHash) System.out.print(" : "+Arrays.deepToString(hashFun));
		}
	}	
	
	public int[] get(int index){
		return thechain.get(index);
	}
	
	public int[] last(){
		if(length-1 == 0) return new int[]{};
		return thechain.get(length-2);
	}
	
	public void hashFun(int[][] in){
		hashFun = in;
	}
	
	public boolean contains(int[] path){
		for(int i = 0; i < length-1; i++){
			if(Arrays.equals(thechain.get(i),path)) return true;
		}
		return false;
	}

	public int arrayToInt(int[] path){
		String theint = "";
		for(int i = 0; i < path.length; i++){
			theint += path[i];
		}
		return Integer.parseInt(theint);
	}
	
	public boolean equals(Chain p2){
		if(length != p2.length) return false;
		for(int i = 0; i < length-1; i++){
			if(!Arrays.equals(thechain.get(i),p2.get(i))) return false;
		}
		return true;
	}

	public int compareTo(Chain p2){
		if(length>p2.length) return 1;
		if(length<p2.length) return -1;
		int temp = 0;
		for(int i = 0; i < this.hashFun.length; i++){
			temp = comparePair(this.hashFun[i],p2.hashFun[i]);
			if(temp !=0) return temp;
		}
		return 0;
	}
	
	public int comparePair(int[] p1, int[] p2){
		int a =  p1[0]-p2[0];
		if(a!=0) return a;
		return p1[1]-p2[1];
	}
}