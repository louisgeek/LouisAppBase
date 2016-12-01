package com.louisgeek.louisappbase.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {
	/** 
     * 手机号码格式匹配 
     *  
     * @param mobiles 
     * @return 
     */
    @Deprecated
    public static boolean isMobileNO(String mobiles) {  
        Pattern p = Pattern  
                .compile("^((13[0-9])|(15[^4,\\d])|(18[0,1,3,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);  
        System.out.println(m.matches() + "-telnum-");  
        return m.matches();  
    }  
  
    /** 
     * 是否含有指定字符 
     *  
     * @param expression 
     * @param text 
     * @return 
     */  
    private static boolean matchingText(String expression, String text) {  
        Pattern p = Pattern.compile(expression);  
        Matcher m = p.matcher(text);  
        boolean b = m.matches();  
        return b;  
    }  
  
    /** 
     * 邮政编码 
     *
     * @param zipcode 
     * @return 
     */  
    public static boolean isZipcode(String zipcode) {  
        Pattern p = Pattern.compile("[0-9]\\d{5}");  
        Matcher m = p.matcher(zipcode);  
        System.out.println(m.matches() + "-zipcode-");  
        return m.matches();  
    }  
  
    /** 
     * 邮件格式 
     *  
     * @param email 
     * @return 
     */  
    public static boolean isValidEmail(String email) {  
        Pattern p = Pattern  
                .compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");  
        Matcher m = p.matcher(email);  
        System.out.println(m.matches() + "-email-");  
        return m.matches();  
    }  
  
    /** 
     * 固话号码格式 
     *  (\(\d{3,4}\)|\d{3,4}-|\s)?\d{7,14}
     * @param telfix 
     * @return 
     */  
    public static boolean isTelfix(String telfix) {  
        Pattern p = Pattern.compile("d{3}-d{8}|d{4}-d{7}");  
        Matcher m = p.matcher(telfix);  
        System.out.println(m.matches() + "-telfix-");  
        return m.matches();  
    }  
  
    /** 
     * 用户名匹配 
     *  
     * @param name 
     * @return 
     */  
    public static boolean isCorrectUserName(String name) {  
        Pattern p = Pattern.compile("([A-Za-z0-9]){2,10}");  
        Matcher m = p.matcher(name);  
        System.out.println(m.matches() + "-name-");  
        return m.matches();  
    }  
  
    /** 
     * 密码匹配，以字母开头，长度 在6-18之间，只能包含字符、数字和下划线。 
     *  
     * @param pwd 
     * @return 
     *  
     */  
    public static boolean isCorrectUserPwd(String pwd) {  
        Pattern p = Pattern.compile("\\w{6,18}");  
        Matcher m = p.matcher(pwd);  
        System.out.println(m.matches() + "-pwd-");  
        return m.matches();  
    }
    /**
     * 验证Email
     * @param email email地址，格式：zhangsan@sina.com，zhangsan@xxx.com.cn，xxx代表邮件服务商
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkEmail(String email) {
        String regex = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?";
        return Pattern.matches(regex, email);
    }

    /**
     *


     * 验证身份证号码
     * @param idCard 居民身份证号码18位，第一位不能为0，最后一位可能是数字或字母，中间16位为数字 \d同[0-9]
     * @return 验证成功返回true，验证失败返回false
     */
    @Deprecated
    public static boolean checkIdCard(String idCard) {
        String regex = "[1-9]\\d{16}[a-zA-Z0-9]{1}";
        return Pattern.matches(regex,idCard);
    }

    /**
     *  //身份证正则表达式(15位)
     isIDCard1=/^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/;
     //身份证正则表达式(18位)
     isIDCard2=/^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{4}$/;
     身份证正则合并：
     (^\d{15}$)|(^\d{17}([0-9]|X)$)
     * @param idCard
     * @return
     */
    public static boolean checkIdCardNew(String idCard) {
        String regex = "(^\\d{15}$)|(^\\d{17}([0-9]|X)$)";
        return Pattern.matches(regex,idCard);
    }

    public static boolean checkIdCardNewSimple(String idCard) {
        String regex = "\\d{18}|\\d{15}";
        return Pattern.matches(regex,idCard);
    }
    public static boolean checkCount(String text,int count) {
        String regex = "^\\d{"+count+"}$";
        return Pattern.matches(regex,text);
    }
    public static boolean checkCount(String text,int minCount,int maxCount) {
        String regex = "^\\d{"+minCount+","+maxCount+"}$";
        return Pattern.matches(regex,text);
    }
    /**
     * 验证手机号码（支持国际格式，+86135xxxx...（中国内地），+00852137xxxx...（中国香港））
     * @param mobile 移动、联通、电信运营商的号码段
     *<p>移动的号段：134(0-8)、135、136、137、138、139、147（预计用于TD上网卡）
     *、150、151、152、157（TD专用）、158、159、187（未启用）、188（TD专用）</p>
     *<p>联通的号段：130、131、132、155、156（世界风专用）、185（未启用）、186（3g）</p>
     *<p>电信的号段：133、153、180（未启用）、189</p>
     * @return 验证成功返回true，验证失败返回false
     */
    //
    @Deprecated
    public static boolean checkMobile(String mobile) {
        String regex = "(\\+\\d+)?1[3458]\\d{9}$";
        return Pattern.matches(regex,mobile);
    }

    /**
     *
     * 手机号码:
     * 13[0-9], 14[5,7], 15[0, 1, 2, 3, 5, 6, 7, 8, 9], 17[6, 7, 8], 18[0-9], 170[0-9]
     *
     * 移动号段: 134,135,136,137,138,139,150,151,152,157,158,159,182,183,184,187,188,147,178,1705
     * 联通号段: 130,131,132,155,156,185,186,145,176,1709
     * 电信号段: 133,153,180,181,189,177,1700
     *
     *   NSString *MOBILE = @"^1(3[0-9]|4[57]|5[0-35-9]|8[0-9]|70)\\d{8}$";
     *
     ^1((3[0-9]|4[57]|5[0-35-9]|7[0678]|8[0-9])\d{8}$)
     */
    public static boolean checkMobileNew(String mobile) {
        String regex = "^1(3[0-9]|4[57]|5[0-35-9]|8[0-9]|70)\\d{8}$";
        return Pattern.matches(regex,mobile);
    }
    /**
     *
     * 中国移动：China Mobile
     * 134,135,136,137,138,139,150,151,152,157,158,159,182,183,184,187,188,147,178,1705
     *
     *     NSString *CM = @"(^1(3[4-9]|4[7]|5[0-27-9]|7[8]|8[2-478])\\d{8}$)|(^1705\\d{7}$)";
     */
    public static boolean checkMobileNew_ChinaMobile(String mobile) {
        String regex = "(^1(3[4-9]|4[7]|5[0-27-9]|7[8]|8[2-478])\\d{8}$)|(^1705\\d{7}$)";
        return Pattern.matches(regex,mobile);
    }
    /**
     * 中国联通：China Unicom
     * 130,131,132,155,156,185,186,145,176,1709
     *
     *  NSString *CU = @"(^1(3[0-2]|4[5]|5[56]|7[6]|8[56])\\d{8}$)|(^1709\\d{7}$)";
     */
    public static boolean checkMobileNew_ChinaUnicom(String mobile) {
        String regex = "(^1(3[0-2]|4[5]|5[56]|7[6]|8[56])\\d{8}$)|(^1709\\d{7}$)";
        return Pattern.matches(regex,mobile);
    }
    /**
     * 中国电信：China Telecom
     * 133,153,180,181,189,177,1700
     *
     *   NSString *CT = @"(^1(33|53|77|8[019])\\d{8}$)|(^1700\\d{7}$)";
     */
    public static boolean checkMobileNew_ChinaTelecom(String mobile) {
        String regex = "(^1(33|53|77|8[019])\\d{8}$)|(^1700\\d{7}$)";
        return Pattern.matches(regex,mobile);
    }
    /**
     *^1[3|4|5|7|8][0-9]\\d{8}]$
     * /^1[34578]\d{9}$/
     * @param mobile
     * @return
     */
    public static boolean checkMobileNewSimple(String mobile) {
        String regex = "^1[3|4|5|7|8]\\d{9}$";
        return Pattern.matches(regex,mobile);
    }
    /**
     * 验证固定电话号码
     * @param phone 电话号码，格式：国家（地区）电话代码 + 区号（城市代码） + 电话号码，如：+8602085588447
     * <p><b>国家（地区） 代码 ：</b>标识电话号码的国家（地区）的标准国家（地区）代码。它包含从 0 到 9 的一位或多位数字，
     *  数字之后是空格分隔的国家（地区）代码。</p>
     * <p><b>区号（城市代码）：</b>这可能包含一个或多个从 0 到 9 的数字，地区或城市代码放在圆括号——
     * 对不使用地区或城市代码的国家（地区），则省略该组件。</p>
     * <p><b>电话号码：</b>这包含从 0 到 9 的一个或多个数字 </p>
     * @return 验证成功返回true，验证失败返回false
     *
     *
     * (\(\d{3,4}\)|\d{3,4}-|\s)?\d{7,14}
     */
    public static boolean checkPhone(String phone) {
        String regex = "(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$";
        return Pattern.matches(regex, phone);
    }

    /**
     * 验证整数（正整数和负整数）
     * @param digit 一位或多位0-9之间的整数
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkDigit(String digit) {
        String regex = "\\-?[1-9]\\d+";
        return Pattern.matches(regex,digit);
    }

    /**
     * 验证整数和浮点数（正负整数和正负浮点数）
     * @param decimals 一位或多位0-9之间的浮点数，如：1.23，233.30
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkDecimals(String decimals) {
        String regex = "\\-?[1-9]\\d+(\\.\\d+)?";
        return Pattern.matches(regex,decimals);
    }

    /**
     * 验证空白字符
     * @param blankSpace 空白字符，包括：空格、\t、\n、\r、\f、\x0B
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkBlankSpace(String blankSpace) {
        String regex = "\\s+";
        return Pattern.matches(regex,blankSpace);
    }

    /**
     * 验证中文
     * @param chinese 中文字符
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkChinese(String chinese,int countMin,int countMax) {
        String regex = "^[\u4E00-\u9FA5]{"+countMin+","+countMax+"}+$";
        return Pattern.matches(regex,chinese);
    }
    /**
     * 验证中文
     * @param chinese 中文字符
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkChinese(String chinese) {
        String regex = "^[\u4E00-\u9FA5]+$";
        return Pattern.matches(regex,chinese);
    }
    /**
     * 验证双字节字符 包括中文
     * @param text 双字节字符
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkDoubleByteCharacter(String text) {
        String regex = "^[^\\x00-\\xff]+$";
        return Pattern.matches(regex,text);
    }


    /**
     * 验证日期（年月日）
     * @param birthday 日期，格式：1992-09-03，或1992.09.03
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkBirthday(String birthday) {
        String regex = "[1-9]{4}([-./])\\d{1,2}\\1\\d{1,2}";
        return Pattern.matches(regex,birthday);
    }

    /**
     * 验证URL地址
     */
    public static boolean checkURL(String url) {
        String regex = "((http|ftp|https)://)(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\\&%_\\./-~-]*)?";
        return Pattern.matches(regex, url);
    }

    /**
     * <pre>
     * 获取网址 URL 的一级域名
     * http://detail.tmall.com/item.htm?spm=a230r.1.10.44.1xpDSH&id=15453106243&_u=f4ve1uq1092 ->> tmall.com
     * </pre>
     *
     * @param url
     * @return
     */
    public static String getDomain(String url) {
        Pattern p = Pattern.compile("(?<=http://|\\.)[^.]*?\\.(com|cn|net|org|biz|info|cc|tv)", Pattern.CASE_INSENSITIVE);
        // 获取完整的域名
        // Pattern p=Pattern.compile("[^//]*?\\.(com|cn|net|org|biz|info|cc|tv)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(url);
        matcher.find();
        return matcher.group();
    }
    /**
     *
     * 匹配中国邮政编码
     * @param postcode 邮政编码
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkPostcode(String postcode) {
        String regex = "[1-9]{1}(\\d+){5}";
        return Pattern.matches(regex, postcode);
    }

    /**
     * 匹配IP地址(简单匹配，格式，如：192.168.1.1，127.0.0.1，没有匹配IP段的大小)
     * @param ipAddress IPv4标准地址
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkIpAddress(String ipAddress) {
        String regex = "[1-9](\\d{1,2})?\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))";
        return Pattern.matches(regex, ipAddress);
    }
}
