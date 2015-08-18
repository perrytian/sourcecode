package date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat sdfNoSign = new SimpleDateFormat("yyyyMMddHHmmss");

	/**
	 * yyyyMMddHHmmss
	 * 
	 * @return
	 */
	public static String dateCurrentFormate() {
		String time = sdfNoSign.format(new Date());
		return time;
	}

	/**
	 * "yyyy-MM-dd-HH:mm:ss"
	 * 
	 * @param dateFormat
	 * @return
	 */
	public static String dateCurrentFormate(final String dateFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		String time = sdf.format(new Date());
		return time;
	}

	/**
	 * yyyyMMdd
	 * 
	 * @return
	 */
	public static String currentDateYYYYMMDD() {
		return dateYYYYMMDD(new Date());
	}

	public static String dateYYYYMMDD(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(date);
	}

	/**
	 * 当前日期减入参得到的时间
	 * 
	 * @param dec
	 * @return
	 */
	public static String dateCurrentDecreaseDay(int dec) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - dec);
		return dateYYYYMMDD(calendar.getTime());
	}
	
	public static Long dateCurrentDayInMillis(int dec) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - dec);
		return calendar.getTimeInMillis();
	}
	/**
	 * 当前日期减入参得到的时间
	 * 
	 * @param dec
	 * @return
	 */
	public static String dateCurrentDecreaseMonth(int dec) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - dec);
		return dateYYYYMMDD(calendar.getTime());
	}
	
	public static Long dateCurrentMonthInMillis(int dec) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - dec);
		return calendar.getTimeInMillis();
	}
	
	/*
	 * 把yyyyMMddhhmmss的串转成yyyy-MM-dd hh:mm:ss
	 * */
	public static String formatDate(String src){
		if(null!=src){
			Date date=new Date();
			DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			try {
				//format.parse(src);
				return new SimpleDateFormat("HH:mm:ss").format(format.parse(src)).toString();
			} catch (ParseException e) {
				return null;
			}
		}
		return null;
	}
	
	/*
	 * 时间差
	 * */
	public static String strDateMinus(String beginTime,String endTime){	
		if(beginTime==null||endTime==null)
			return null;
		try {
			return dateMinus(sdfNoSign.parse(beginTime), sdfNoSign.parse(endTime));
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static String dateMinus(Date beginTime,Date endTime){
		long temp=(endTime.getTime()-beginTime.getTime())/1000;
		return temp/3600+":"+(temp%3600)/60+":"+temp%60;
	}
	
	public static String calculateDate(int intervalTime,int offset){
		Calendar calendar = Calendar.getInstance();
		Date currentDate = new Date();
		StringBuilder sb = new StringBuilder();
		long currentMillTime = currentDate.getTime();	//当前时间的毫秒数
		if(intervalTime==0){
			intervalTime=5;
		}
		if(offset==0){
			offset = 2;		//偏移量设置为2
		}
		for(int i=0;i<intervalTime+1;i++){
			long newTime = currentMillTime-i*1000;
			calendar.setTimeInMillis(newTime);
			String nowDateStr = sdf.format(calendar.getTime());
			sb.append("'");
			sb.append(nowDateStr);
			sb.append("'");
			sb.append(",");
		}
		String finalDate = sb.toString().substring(0,sb.toString().lastIndexOf(","));
		if(finalDate!=null && !"".equals(finalDate)){
			return finalDate;
		}
		return null;
	}
	
	/**
	 * yyyy-MM-dd HH:mm:ss
	 * @param date
	 * @return
	 */
	public static String DatetoString(Date date){
		if(date!=null){
			return sdf.format(date);
		}
		return null;
	}
	
	/**
	 * yyyyMMddHHmmss
	 * @param date
	 * @return
	 */
	public static String dateStr(Date date){
		if(date!=null){
			return sdfNoSign.format(date);
		}
		return null;
	}
	
	/**
	 * 计算两个时间字符串差,结果值以秒为单位
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static long getDiffTime(String startTime, String endTime){
		long result = 0l;
//		if(StringUtil.isNull(startTime) || StringUtil.isNull(endTime)){
//			return result;
//		}
		try {
			result = (sdfNoSign.parse(endTime).getTime() - sdfNoSign.parse(startTime).getTime())/1000;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public static Date getFormatDate(){
		return StringtoDate(DatetoString(new Date(System.currentTimeMillis())));
	}
	
	public static Date StringtoDate(String date){
		if(date!=null && !"".equals(date)){
			try {
				return sdf.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return new Date();
	}
	
	/**
	 * 根据系统当前时间获取账期，以便获得应插入的月表
	 * @return
	 */
	public static String cycleDateString(){
		SimpleDateFormat df = new SimpleDateFormat("yyyyMM");
		String date = df.format(new Date());
		return date;
	}
//	public static void main(String[] args) {
////		System.out.println(DateUtils.dateCurrentDecreaseMonth(3));
//	}
}
