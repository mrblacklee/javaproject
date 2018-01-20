package javase.string;

public class StringBuilderDemo {
    public static void main(String[] args){
        String str="abcdefg";
        str=new StringBuilder(str).reverse().toString();
        System.out.println(str);

    }
}
