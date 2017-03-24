package uw.dm.test;

import uw.dm.DAOFactory;

public class Test {

	public static boolean sqlValidate(String str) {
        str = str.toLowerCase();//统一转为小写
        if(str.indexOf("script")!=-1){
        	return true;
        }
        String badStr = "exec|execute|insert|select|delete|update|drop|mid|master|truncate|xmltype|upper|" +
                "char|chr(|declare|--|net user|xp_|exec|execute|insert|case when| union |substr|ascii|length" +
                "|group_concat|column_name|dbms_pipe| and |receive_message|" +
                "information_schema.columns|table_schema|script| or |";//过滤掉的sql关键字，可以手动添加
        String[] badStrs = badStr.split("\\|");
        for (int i = 0; i < badStrs.length; i++) {
            if (badStrs[i].length()>0&&str.indexOf(badStrs[i])!=-1) {
                return true;
            }
        }
        return false;
    }
	private static byte b = -1;
	private static short s = -1;
	private static int i = -1;
	private static long l = -1;
	private static double d = -1;
	private static float f = -1;

	public static void main(String[] args) {
		System.out.println(String.valueOf(b));
		System.out.println(String.valueOf(s));
		System.out.println(String.valueOf(i));
		System.out.println(String.valueOf(l));
		System.out.println(String.valueOf(d));
		System.out.println(String.valueOf(f));
		System.out.println(DAOFactory.getInstance().getSequenceId("test"));
//		System.out.println(sqlValidate("and d.sale_price<=1/**/OR/**/NOT/**/4015=8705 )")); 
	}

}
