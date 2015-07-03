package Dyck;

import java.util.*;
import java.io.*;

public class Methods{
	
	
	/**************************************/
	//print dyck path
	public static void printPath(int[] d){
		for(int i = 0; i < d.length; i++){
			System.out.print(d[i]);
		}
	}
	
	public static void printPathln(int[] d){
		for(int i = 0; i < d.length; i++){
			System.out.print(d[i]);
		}
		System.out.println();
	}
	
	public static String pathToString(int[] d){
		String s = "";
		for(int i = 0; i < d.length; i++){
			s += d[i];
		}
		return s;
	}
	/*****************************************/
	// construct the dyck poset
	
	//produces the interval [sigma,pi] as a list of Level objects
	public static ArrayList<Level>  makeInterval(int[] sigma, int[] pi){

		//create empty interval
		ArrayList<Level> interval = new ArrayList<Level>();

		//create and add top level containing pi
		Level toplevel = new Level();
		toplevel.add(pi);
		interval.add(toplevel);
		
		//add middle levels
		for(int i = 0; i < (int)((pi.length - sigma.length)/2) - 1; i++){
			interval.add(new Level(makelevel(interval.get(i),sigma)));
		}
		
		//create and add bottom level containing sigma
		Level bottomlevel = new Level();
		bottomlevel.add(sigma);
		interval.add(bottomlevel);
		
		return interval;
	}
	
	//makes a level of the poset
	public static Level makelevel(Level above, int[] sigma){

		Level newLevel = new Level();
		newLevel.clear();

		//loop through the above level remove each letter from each perm check it 
		//contains sigma and is not already present and then add it to the level
		for(int k = 0; k < above.length(); k++){
			for(int i = 0; i < above.get(k).length; i++){
				for(int j = i+1; j < above.get(k).length; j++){
					int[] redPath = CutOut(above.get(k),i,j);
					if(IsDyckPath01(redPath) && Contains(sigma,redPath) && !newLevel.contains(redPath)) newLevel.add(redPath);
				}
			}
		}
		return newLevel;
	}
	
	//remove letter and reduce all letters with higher value
	public static int[] CutOut(int[] perm, int first, int second){
 
		if(perm == null || perm.length <= 1)
		return null;
     
		int dim = perm.length;
		int[] output = new int[perm.length-2];

		for(int k = 0; k < first; k++)
		output[k] = perm[k];

		for(int k = first+1; k < second; k++)
		output[k-1] = perm[k];

		for(int k = second+1; k < dim; k++)
		output[k-2] = perm[k];

		return output;
	}
	
	public static boolean IsDyckPath01(int[] path){

		if(path == null)
			return true;

		int height = 0;
		for(int k = 0; k < path.length; k++){
		if(path[k] == 0)
			height++;
		if(path[k] == 1)
			height--;
		if(height < 0)
			return false;
		}

		return height == 0;
	}
	
	
	public static boolean Contains(int[] A, int[] B){
 
		if(A.length > B.length) return false;

		int k = -1;
		for(int n = 0; k < B.length && n < A.length; n++){
			while(++k < B.length && B[k] != A[n]);
		}
		return k < B.length;
	}
	
	public static int[] StringToIntArray(String str){
                
		if(str.length() == 0)
		return null;
  
		int[] output = new int[str.length()];
 
		for(int k = 0; k < str.length(); k++)
		output[k] = Character.getNumericValue(str.charAt(k));

		return output;
	}
	
/***************************************************/
//Methods for computing chains and MSI's

	//main function that will print all chains
	public static int recur(int[] sigma,int[] pi, boolean printMaxOnly, boolean printMSI,boolean printLabel,boolean printEmbeds){
		Interval I = new Interval(sigma,pi);
		ArrayList<ArrayList<Chain>> allChains = makeChains(I);
		
		if(pi.length-sigma.length == 2) return 0;

		return printChains(allChains,pi,sigma,printMaxOnly,printLabel,printMSI,printEmbeds);
	}
	
	
	//builds all the chains in the interval sorted into length
	public static ArrayList<ArrayList<Chain>> makeChains(Interval I){
	
		//create a list of list of chains to store the chains
		ArrayList<ArrayList<Chain>> allChains = new ArrayList<ArrayList<Chain>>(I.rank()-1);

		//add an empty list of chains for each level of the interval
		for(int i = 0; i < I.rank()-1; i++) allChains.add(new ArrayList<Chain>());

		//add the empty chain at the start of the list
		Chain emptychain = new Chain();
		allChains.get(0).add(emptychain);

		//make the chains
		makeChainsRecur(I,emptychain,1,allChains,true);
		
		return allChains;
	}
	
	//recursively builds the chains top to bottom
	public static void makeChainsRecur(Interval I,Chain parent, int onlevel,ArrayList<ArrayList<Chain>> allChains,boolean first){
		//recur till reach the top of the interval
		if(onlevel < I.rank()-1){
			for(int i = 0; i <I.get(onlevel).length(); i++){
				//checks to see if each perm of level i is contained in parent if so create a new chain of the two combined
				if(first || Contains(I.get(onlevel).get(i),parent.last())){
					Chain newchain = new Chain(parent,I.get(onlevel).get(i));
					allChains.get(newchain.length-1).add(newchain);
					//recur this function with newly created chain
					makeChainsRecur(I,newchain,onlevel+1,allChains,false);
				}
			}
			//recur again with same parent chain but to the next level in the interval
			makeChainsRecur(I,parent,onlevel+1,allChains,first);
		}
	}
	

	//prints all the chains
	public static int printChains(ArrayList<ArrayList<Chain>> allChains,int[] pi,int[] sigma,boolean printMaxOnly,boolean printLabel, boolean printMSI,boolean printEmbeds){

		int c = 0;
		int start = 0;
		if(printMaxOnly) start = allChains.size()-1;
		for(int i = start; i < allChains.size(); i++){

			System.out.println("Chains of rank " + (i+1));

			//compute the hash Function used for ordering
			for(int j = 0; j < allChains.get(i).size(); j++){
				allChains.get(i).get(j).hashFun(computeHash(allChains.get(i).get(j),pi,sigma));
			}
			

			//order the chains
			if(i == allChains.size()-1){
				Collections.sort(allChains.get(i));
			}

			//Computes all MSI's and returns the number greater in size than 1
			c = MSI(allChains.get(allChains.size()-1));
			
			//print chains
			for(int k = 0; k < allChains.get(i).size(); k++){
				allChains.get(i).get(k).print(printLabel);
				
				if(printEmbeds){
					System.out.print(" : ");
					allChains.get(i).get(k).makeEmbed(pi);
					allChains.get(i).get(k).printEmbed();
				}
				
				if(printMSI){
					System.out.print(" : ");
					allChains.get(i).get(k).printMSI();
				}
				
				System.out.println();
			}
		}

		System.out.println("Number of MSI of size greater than 1 is: " + c);
		return c;
	}
	
	
		
	//returns the complement of positions in the array A i.e if A={0,2,3} and n=5 then returns {1,4}
	public static int[] compOcc(int[] A, int n){
		int[] out = new int[n-A.length];
		int f = 0;
		for(int i = 0; i < n; i++){
			if(!con(i,A)){
				out[f] = i;
				f++;
			}
		}
		return out;
	}
	
	//does array contain a
	public static boolean con(int a, int[] array){
		for(int i = 0; i < array.length; i++){
			if(array[i] == a) return true;
		}
		return false;
	}
	
	//returns the locations in pi of letters from of the rightmost occurence of sigma in pi
	public static int[] rightOcc(int[] C, int[] D){

		if(C == null || D == null)
			return null;

		int[] output = D.clone();

		int curr = D.length;
		int place = C.length-1;

		while(curr > 0 && place >= 0){
			while(output[--curr] != C[place]){
				if(output[curr] >= 0)
					output[curr] = -1;
			}
			output[curr] = C[place];
			if(place >= 0)
				place--;
		}

		for(int k = 0; k < curr; k++){
			output[k] = -1;
		}

		//System.out.print("-");
		
		int[] out = new int[C.length];
		int c = 0;
		for(int i = 0; i < D.length; i++){
			if(output[i] > -1){
				out[c] = i;
				c++;
			}
		}

		return out;
                
	}
	
	//compute MSI's
	public static int MSI(ArrayList<Chain> chains){
		int a = 0;
		int b = 0;
		int count = 0;
		boolean con = true;

		for(int i = 0; i < chains.size(); i++){

			ArrayList<ArrayList<int[]>> allMSI = new ArrayList<ArrayList<int[]>>();
			ArrayList<int[]> thisChain = chains.get(i).getChain();

			//loop through all the subchains
			for(int j = 0; j < thisChain.size(); j++){
				for(int k = j+1; k <= thisChain.size(); k++){
					ArrayList<int[]> subChain = new ArrayList<int[]>();
					ArrayList<int[]> subChainComp = new ArrayList<int[]>();

					//create subchain and its complement
					for(int t = j; t < k; t++) subChain.add(thisChain.get(t));
					for(int t = 0; t < j; t++) subChainComp.add(thisChain.get(t));
					for(int t = k; t < thisChain.size(); t++) subChainComp.add(thisChain.get(t));

					//check if the preceeding chains contain the complement, that if the subchain is an skipped interval
					for(int t = 0; t < i; t++){
						if(contain(subChainComp,chains.get(t).getChain())) allMSI.add(subChain);
					}
				}
			}

			//remove any skipped intervals that are not minimal
			for(int j = 0; j < allMSI.size(); j++){
				for(int k = j+1; k < allMSI.size(); k++){
					a = allMSI.get(j).size();
					b = allMSI.get(k).size();
					if(a<b) con = contain(allMSI.get(j),allMSI.get(k));
					else con = contain(allMSI.get(k),allMSI.get(j));
					if(con){
						
						if(a<b){allMSI.remove(k); k--;}
						else{allMSI.remove(j); j--;break;}
					}
				}
			}

			//check to see if any MSI's are of size greater than two
			for(int j = 0; j < allMSI.size(); j++){ if(allMSI.get(j).size()>1) count++;}

			chains.get(i).addMSI(allMSI);	
		}

		return count;
	}


	//check to see if a subchain is contained within another chain
	public static boolean contain(ArrayList<int[]> subchain, ArrayList<int[]> thechain){
		boolean store = true;

		for(int i = 0; i < subchain.size(); i++){
			for(int j = 0; j < thechain.size(); j++){
				//loop through chain and if perm doesn't occur at any point set store as false
				if(Arrays.equals(thechain.get(j),subchain.get(i))) break;
				if(j == thechain.size()-1) store = false;
			}
			if(!store) return store;
		}

		return store;
	}
	
	
	public static int[][] computeHash(Chain a,int[] pi,int[] sigma){
	
		if(a.length < (int)((pi.length - sigma.length)/2)) return new int[0][0];
	
		int[][] loc = new int[a.length][2];
	
		int[] C0 =  compOcc(rightOcc(a.get(0),pi),pi.length);
		for(int j = 0; j < C0.length; j++){
			loc[0][j] = C0[j]+1;
		}
	
		for(int i = 0; i < a.length-2; i++){
			int[] C1 =  compOcc(rightOcc(a.get(i+1),a.get(i)),a.get(i).length);
			for(int j = 0; j < C1.length; j++){
				loc[i+1][j] = C1[j]+1;
			}
		}
	
		int[] C2 =  compOcc(rightOcc(sigma,a.get(a.length-2)),a.get(a.length-2).length);
		for(int j = 0; j < C2.length; j++){
			loc[a.length-1][j] = C2[j]+1;
		}
	
		ArrayList<Integer> ints = new ArrayList<Integer>();
		for(int i = 0; i < pi.length; i++) ints.add(i+1);
	
		//normalise the positions so that position of pi not of previous path of chain
		int temp0,temp1;
		for(int i = 0; i < loc.length; i++){
			temp0 = loc[i][0]-1;
			temp1 = loc[i][1]-1;
			loc[i][0] = ints.get(temp0);
			loc[i][1] = ints.get(temp1);
			ints.remove(temp0);
			ints.remove(temp1-1);
		}
	
		return loc;
	}
}
	