package javase.string;

public class StringDemo3 {
    public static void main(String[] args) {
        String str="hlanldfhdifaho";
        char[]cs=str.toCharArray();
        for(char d:cs){
            System.out.println(d);
        }
        System.out.println(str.toCharArray().length);
        System.out.println(str.length());
    }
}
