package Dyck;

import java.util.*;
import java.io.*;

public class Interval{
	private ArrayList<Level> inter = new ArrayList<Level>();
	private int[] sigma;
	private int[] pi;
	
	public Interval(){
		ArrayList<Level> inter = new ArrayList<Level>();
	}
	
	public Interval(int[] sigma, int[] perm){
		inter = Methods.makeInterval(sigma,perm);
		this.sigma=sigma;
		this.pi=perm;
	}
	
	public void add(Level l){
		inter.add(l);
	}
	
	public int rank(){
		return inter.size();
	}
	
	public Level get(int i){
		return inter.get(i);
	}
	
	public int[] sigma(){
		return sigma;
	}
	public int[] pi(){
		return pi;
	}
	
	public void print(){
		for( int i = 0; i < inter.size(); i++){
			inter.get(i).print();
			System.out.println("");
		}
	}
	public void dotprint(){
		File file = new File("poset.dot");
		file.delete();
		File file1 = new File("poset.ps");
		file1.delete();

		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("poset.dot"));

			ArrayList<int[]> paths = listAllPaths();

     
			out.write("digraph poset {");
			out.newLine();
			out.write("center = true ");
			out.newLine();
			out.write("node [fontname=Courier]");
			out.newLine();
			out.write("edge [arrowhead=none,color=blue,style=bold]");
			out.newLine();

			for(int n = 0; n < paths.size()-1; n++){
				for(int k = n+1; k < paths.size(); k++){
					if(Methods.Contains(paths.get(n), paths.get(k)) && paths.get(n).length == paths.get(k).length-1){
					out.write("\"");
					out.write(Methods.pathToString(paths.get(k)));
					out.write("\" -> \"");
					out.write(Methods.pathToString(paths.get(n)));
					out.write("\";");
					out.newLine();
					}
				}
			}

			out.write("}");
			out.close();

			}
			catch (IOException e){
				System.out.println("Exception ");
		}
	}
	
	//returns a list of all the paths in the interval
	public ArrayList<int[]> listAllPaths(){
		ArrayList<int[]> list = new ArrayList<int[]>();
		for(int i = inter.size()-1; i >= 0; i--){
			for(int j = 0; j < inter.get(i).length(); j++){
				list.add(inter.get(i).get(j));
			}
		}
		return list;
	}
	
	public int Mobius(){

		if(inter.size() == 0) return 0;
         
		Level paths = levelAllPaths();
		int numpaths = paths.length();

		//paths.order();
		//BubbleSort(perms);

		boolean[][] contains = new boolean[numpaths][numpaths];

		for(int n = 1; n < numpaths; n++) contains[0][n] = true;// minimal element contained in all

		for(int n = 0; n < numpaths-1; n++) contains[n][numpaths-1] = true;// maximal element contains all

		for(int n = 1; n < numpaths-1; n++){
			for(int k = n+1; k < numpaths-1; k++){
				if(Methods.Contains(paths.get(n), paths.get(k))){
					contains[n][k] = true;
				}
			}
		}

		for(int n = 1; n < numpaths; n++){
			for(int k = n+1; k < numpaths-1; k++){
				if(Methods.Contains(paths.get(n), paths.get(k))){
					contains[n][k] = true;
				}
			}
		}

		int[] mobius = new int[numpaths];
		mobius[0] = 1;

		for(int n = 1; n < numpaths; n++){
			int sum = 0;
			for(int k = 0; k < n; k++){
				if(contains[k][n]){
					sum += mobius[k];
				}
			}
			mobius[n] = -sum;
		}	

		return mobius[numpaths-1]; 
	}
	
	private Level levelAllPaths(){
		Level l = new Level();
		for(int i = inter.size()-1; i >= 0; i--){
			for(int j = 0; j < inter.get(i).length(); j++){
				l.add(inter.get(i));
			}
		}
		return l;
	}
}