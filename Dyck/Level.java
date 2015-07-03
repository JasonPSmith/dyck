package Dyck;

import java.util.*;
import java.io.*;

//the class of levels that the interval is made up off
public class Level{
	private ArrayList<int[]> paths = new ArrayList<int[]>();
	
	//create empty level
	public Level(){
		ArrayList<int[]> paths = new ArrayList<int[]>();
	}
	
	//create new copy of inputted level
	public Level(Level copy){
		for(int i = 0; i < copy.length(); i++){
			paths.add(copy.get(i).clone());
		}
	}
	
	public int length(){
		return paths.size();
	}
	
	public void remove(int i){
		paths.remove(i);
	}
	
	public int[] get(int index){
		return paths.get(index);
	}
	
	public void add(int[] newpath){
		paths.add(newpath.clone());
	}
	
	public void add(Level copy){
		for(int i = 0; i < copy.length(); i++){
			paths.add(copy.get(i).clone());
		}
	}
	
	public void clear(){
		paths.clear();
	}

	//public void order(){
	//	Collections.sort(paths);
	//}
	public boolean contains(int[] tocheck){
		for(int i = 0; i < paths.size(); i++){
			if(Arrays.equals(paths.get(i),tocheck)) return true;
		}
		return false;
	}
	
	public void print(){
		for(int i = 0; i < paths.size(); i++){
			Methods.printPathln(paths.get(i));
		}
	}
}