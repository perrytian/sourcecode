package util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author jianghao11
 *
 */
public class VerifyMessageFormat {
	
	public static boolean verifyEmail(String emailAddress) throws Exception{
		Pattern p = Pattern.compile("\\w+@(\\w+\\.)+[a-z]{2,3}"); 
		Matcher m = p.matcher(emailAddress);
		if(m.matches()){
			return true;
		}
		else{
			throw new Exception("The format of email address:"+emailAddress+" is not correct!");
		}
	}
	
	public static boolean verifyEmail(List<String> destin) throws Exception{
		boolean result = true;
		for(String dest:destin){
			if(!result) return false;
			result = verifyEmail(dest);
		}
		return result;
	}
	
	public static boolean verifySMS(String phoneNumber) throws Exception{
		if(phoneNumber.equals("")||phoneNumber == null){
			return false;
		}
		Pattern p = Pattern.compile("^((\\+{0,1}86){0,1})1[0-9]{10}");
		Matcher m = p.matcher(phoneNumber);
		if(m.matches()){
			return true;
		}
		else{
			throw new Exception("The format of SMS address:"+phoneNumber+" is not correct!");
		}
	}
	
	public static boolean verifySMS(List<String> destPhoneNumbers) throws Exception{
		boolean result = true;
		for(String dest:destPhoneNumbers){
			System.out.println(dest);
			if(!result) return false;
			result = verifySMS(dest);
		}
		return result;
	}
	
	public static boolean verifyMMS(String phoneNumber) throws Exception{
		return true;
	}
	
	public static boolean verifyMMS(List<String> phoneNumbers) throws Exception{
		return true;
	}
}
