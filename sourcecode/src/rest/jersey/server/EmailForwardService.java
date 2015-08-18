package rest.jersey.server;

import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import org.apache.log4j.Logger;
import com.cusi.hods.dao.CbssRestDao;
import com.cusi.hods.dao.impl.CbssRestDaoImpl;
import com.cusi.hods.entity.CbssResponseEntity;
import com.cusi.hods.entity.EmailVO;
import com.cusi.hods.utils.JSONUtils;
import com.google.gson.Gson;

@Path("/email")
public class EmailForwardService {
	
	private static Logger logger = Logger.getLogger(EmailForwardService.class);
	private static Properties props;
	private static final String FROM = "jianghao11@chinaunicom.cn";
	private static Gson gson = null;
	
	static{
		gson = new Gson();
		props = new Properties();
//		props.put("mail.smtp.host", "hq.smtp.chinaunicom.cn");
		props.put("mail.smtp.host", "132.35.108.22");
	}
	
	/**
	 * 发送邮件(文本)  多个
	 * @param subject
	 * @param mess
	 * @param toAddressList
	 */
	public void sendMessage(String subject,String mess,List<String> toAddressList) throws MessagingException{
		Session session = Session.getInstance(props,null);
		MimeMessage msg = new MimeMessage(session);
		try {
			msg.setFrom(FROM);
			for(String toAddress:toAddressList){
				msg.addRecipients(Message.RecipientType.TO, toAddress);
			}
			msg.setSubject(subject);
			msg.setSentDate(new Date());
			msg.setText(mess+"\n");
			Transport.send(msg,"jianghao11@chinaunicom.cn","Hr@62343");
			logger.info("发送前转邮件成功：标题["+subject+"],内容["+mess+"]");
		} catch (MessagingException e) {
			logger.error("发送前转邮件失败：标题["+subject+"],内容["+mess+"]",e);
			throw e;
		}
	}
	
	/**
	 * 发送邮件(文本)  一个
	 * @param subject
	 * @param mess
	 * @param toAddress
	 */
	public void sendMessage(String subject,String mess,String toAddress) throws MessagingException{
		Session session = Session.getInstance(props,null);
		MimeMessage msg = new MimeMessage(session);
		try {
			msg.setFrom(FROM);
			msg.setRecipients(Message.RecipientType.TO, toAddress);
			msg.setSubject(subject);
			msg.setSentDate(new Date());
			msg.setText(mess+"\n");
			Transport.send(msg,"jianghao11@chinaunicom.cn","Hr@62343");
			logger.info("发送前转邮件成功：标题["+subject+"],内容["+mess+"]");
		} catch (MessagingException e) {
			logger.error("发送前转邮件失败：标题["+subject+"],内容["+mess+"]",e);
			throw e;
		}
	}
	
	/**
	 * 发送邮件(html)  多个
	 * @param subject
	 * @param mess
	 * @param toAddressList
	 */
	public void sendHtmlMessage(String subject,String mess,List<String> toAddressList) throws MessagingException{
		Session session = Session.getInstance(props,null);
		MimeMessage msg = new MimeMessage(session);
		try {
			msg.setFrom(FROM);
			for(String toAddress:toAddressList){
				msg.addRecipients(Message.RecipientType.TO, toAddress);
			}
			msg.setSubject(subject);
			msg.setSentDate(new Date());
			msg.setContent(mess, "text/html;charset=utf-8");
			Transport.send(msg,"jianghao11@chinaunicom.cn","Hr@62343");
			logger.info("发送前转邮件成功：标题["+subject+"],内容["+mess+"]");
		} catch (MessagingException e) {
			logger.error("发送前转邮件失败：标题["+subject+"],内容["+mess+"]",e);
			throw e;
		}
	}
	
	/**
	 * 处理发送邮件业务
	 * @param pro
	 */
	@PUT
	@Path("sendemail")
	public String emailProcessingBusiness(String json){
		CbssResponseEntity response=new CbssResponseEntity();
		CbssRestDao cbssRestDao=new CbssRestDaoImpl();
		logger.info("接收到的Json字符串:" + json);
		if(null==json || "".equals(json.trim())){
			response.setRet("1");
			response.setResultMsg("json字符串为空");
			logger.info("json字符串为空");
			return gson.toJson(response).toString();
		}
		
		String result = "";
		try {
			EmailVO vo=(EmailVO)JSONUtils.jsonStrToObject(json,EmailVO.class);
			List<String> userList=cbssRestDao.findEmailByEventID(Integer.parseInt(vo.getEventId()));
			if(userList.size()>0){
				//文件
				//sendMessage(vo.getSubject(), vo.getMess(), userList);  
				//html
				sendHtmlMessage(vo.getSubject(), vo.getMess(), userList);   
			}else{
				logger.info("邮箱地址为空...");
			}
			response.setRet("0");
			response.setResultMsg("邮件发送成功");
			result = gson.toJson(response).toString();
		} catch (Exception e) {
			response.setRet("1");
			response.setResultMsg("邮件发送失败"+e.getMessage());
			logger.error("邮件发送失败",e);
			return gson.toJson(response).toString();
		}
		return result;
	}
	
}
