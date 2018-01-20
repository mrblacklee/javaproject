package javase.string;

public class StringDemo1 {
    public static void main(String[] args) {
        String s1="ab";
        String s2=new String("ab");
        String s3="a"+"b";
        String s4="a";
        s4+="b";
        System.out.println(s1==s2);
        System.out.println(s1==s3);
        System.out.println(s1==s4);
        System.out.println(s2==s4);
    }
}
