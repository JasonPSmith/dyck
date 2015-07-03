import java.util.*;
import Dyck.*;


public class dyckInterval{
	public static void main(String[] args){

		int[] param = new int[args.length];

		String A = args[0];
		String B = args[1];

		int[] sigma = Methods.StringToIntArray(A);
		int[] pi = Methods.StringToIntArray(B);
		
		if(!Methods.IsDyckPath01(sigma) || !Methods.IsDyckPath01(pi)) System.out.println("Invalid Dyck Path");
		else{
			Methods.printPath(sigma);
			System.out.print(" ");
			Methods.printPathln(pi);
			System.out.println("");
			System.out.println("*********************");
			System.out.println("");
		
			//builds and prints interval
			Interval I = new Interval(sigma,pi);
			I.print();
			System.out.println();
			System.out.println("Mobius function = " + I.Mobius());
		
			System.out.println();
		
			//The four trues indicate what to print:
			//print max chains only, print MSI's, print labellings, print embeddings
			Methods.recur(sigma,pi,true,true,true,true);
		}
	}
}
