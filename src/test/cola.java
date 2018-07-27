package test;
import java.util.Scanner;


public class cola {

	 /*
	  * 
	  * 描述：每 3 个可乐盖可兑换 1 瓶子可乐，求买 n 瓶可乐最终可获得的可乐瓶子数。
	  * 
	  *  
	  * */

   public static int times = 1;
   
   public static void main(String[] args){
	 
       Scanner input = new Scanner(System.in);
       System.out.print("输入购买的可乐数：");
       
       times = 1;
       
       int j = input.nextInt();
       int i = func(j);
       
       System.out.println("--------------------------");
       System.out.println("总共可获得 " + i + " 瓶\n\n");
   }
   
   public static int func(int i){
       if(i < 3){
           System.out.println("最终剩下 " + i + " 个瓶盖，不足以兑换");
           return i;
       }
       else{
           System.out.println("第 " + times++ + " 次兑换，" + "本次兑换总共有 " + i + " 个瓶盖，用 " + (i - i%3) + " 个瓶盖换了 " + i/3 + " 瓶可乐，剩余 " + i%3 + " 个瓶盖可用于下次兑换");
           return ((i - i%3) + func(i/3 + i%3));
       }
   }
}
