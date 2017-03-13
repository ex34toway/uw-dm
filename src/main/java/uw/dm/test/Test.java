package uw.dm.test;

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

	public static void main(String[] args) {
		System.out.println(sqlValidate("and d.sale_price<=1/**/OR/**/NOT/**/4015=8705 )")); 
	}

}
