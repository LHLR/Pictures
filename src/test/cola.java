package test;
import java.util.Scanner;


public class cola {

	 /*
	  * 
	  * ������ÿ 3 �����ָǿɶһ� 1 ƿ�ӿ��֣����� n ƿ�������տɻ�õĿ���ƿ������
	  * 
	  *  
	  * */

   public static int times = 1;
   
   public static void main(String[] args){
	 
       Scanner input = new Scanner(System.in);
       System.out.print("���빺��Ŀ�������");
       
       times = 1;
       
       int j = input.nextInt();
       int i = func(j);
       
       System.out.println("--------------------------");
       System.out.println("�ܹ��ɻ�� " + i + " ƿ\n\n");
   }
   
   public static int func(int i){
       if(i < 3){
           System.out.println("����ʣ�� " + i + " ��ƿ�ǣ������Զһ�");
           return i;
       }
       else{
           System.out.println("�� " + times++ + " �ζһ���" + "���ζһ��ܹ��� " + i + " ��ƿ�ǣ��� " + (i - i%3) + " ��ƿ�ǻ��� " + i/3 + " ƿ���֣�ʣ�� " + i%3 + " ��ƿ�ǿ������´ζһ�");
           return ((i - i%3) + func(i/3 + i%3));
       }
   }
}
