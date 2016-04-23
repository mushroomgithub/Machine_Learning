package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by mashuai on 16/4/5.
 */
public class Main2 {
    public static  void main(String[] args){
        Scanner sc=new Scanner(System.in);
        int bid[]=new int[7];
        Double [] e= new Double[7];/*期望*/
        int i=0;
        while(sc.hasNext()){
            bid[++i]=sc.nextInt();
            double pi=1.0;
            for(int j=1;j<=i;j++){
                if(j<i){
                    pi*=(1-(bid[i]/100000.0));
                }else{
                    pi*=bid[i]/100000.0;
                }
            }
            e[i]+=bid[i]*pi;
            int[] res=getRes(e, bid, bid.length);
            for(int t:res){
                System.out.print(t+" ");
            }
        }
    }

    private static int[] getRes(Double[] e, int[] bid, int length) {
        int res[]=new int[length];
        double min=0.0;
        for(int i=1;i<=length-1;i++){
            if(min>e[i]){
                min=e[i];
                res=Arrays.copyOf(bid,i);
            }else {
                return res;
            }
        }

        return null;
    }

}
