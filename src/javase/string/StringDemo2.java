package javase.string;

public class StringDemo2 {
    public static void main(String[] args) {
        long begin=System.currentTimeMillis();
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<100000000;i++){
            sb.append("a");

        }
        String str=sb.toString();
        long end =System.currentTimeMillis();
        System.out.println(end-begin);
    }
}
