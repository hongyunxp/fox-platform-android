package com.foxchan.foxmail.entity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import android.util.Log;

import com.foxchan.foxmail.exception.ErrorEmailAddressTypeException;
import com.wecan.veda.utils.StringUtil;

/**
 * 邮件对象
 * @author gulangxiangjie@gmail.com
 * @create 2013-4-5
 */
public class FoxMail {
	
	/** 邮件的接收人的参数 */
	public static final String TO = "to";
	/** 邮件的抄送人的参数 */
	public static final String CC = "cc";
	/** 邮件的密送人的参数 */
	public static final String BCC = "bcc";
	
	/** 主键 */
	private Integer id;
	/** 主题 */
	private String subject;
	/** 邮件的发送/接收日期 */
	private Date createDate;
	/** 是否需要回复的标志 */
	private Boolean replySign;
	/** 是否已经被阅读的标志 */
	private Boolean isNew;
	/** 是否含有附件的标志 */
	private Boolean isContainAttache;
	/** 邮件的发送人的地址 */
	private String from;
	/** 邮件接收人的地址 */
	private String to;
	/** 邮件的抄送人的地址 */
	private String cc;
	/** 邮件的密送人的地址 */
	private String bcc;
	/** 邮件的ID号 */
	private String messageId;
	/** 邮件的正文内容 */
	private String bodyText;
	/** 邮件的附件的保存地址 */
	private String attachPath;
	
	public FoxMail(){}
	
	/**
	 * 构造一个邮件对象
	 * @param mimeMessage	邮件
	 */
	public FoxMail(Message message){
		try {
			MimeMessage mimeMessage = (MimeMessage)message;
			Part part = (Part)message;
			this.setSubject(MimeUtility.decodeText(mimeMessage.getSubject()));
			this.setCreateDate(mimeMessage.getSentDate());
			this.setReplySign(mimeMessage.getHeader("Disposition-Notification-To") == null ? false : true);
			Flags flags = ((Message)mimeMessage).getFlags();
			Flags.Flag[] flag = flags.getSystemFlags();
			this.setIsNew(false);
			for(int i = 0; i < flag.length; i++){
				if(flag[i] == Flags.Flag.SEEN){
					this.setIsNew(true);
					break;
				}
			}
			this.setIsContainAttache(isContainAttache(part));
			this.setFrom(parseFrom(mimeMessage));
			this.setTo(parseMailAddress(mimeMessage, TO));
			this.setCc(parseMailAddress(mimeMessage, CC));
			this.setBcc(parseMailAddress(mimeMessage, BCC));
			this.setMessageId(mimeMessage.getMessageID());
			StringBuilder bodyText = new StringBuilder();
			this.setBodyText(parseMailContent(part, bodyText));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ErrorEmailAddressTypeException e) {
			e.printStackTrace();
		}
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Boolean getReplySign() {
		return replySign;
	}

	public void setReplySign(Boolean replySign) {
		this.replySign = replySign;
	}

	public Boolean getIsNew() {
		return isNew;
	}

	public void setIsNew(Boolean isNew) {
		this.isNew = isNew;
	}

	public Boolean getIsContainAttache() {
		return isContainAttache;
	}

	public void setIsContainAttache(Boolean isContainAttache) {
		this.isContainAttache = isContainAttache;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getBcc() {
		return bcc;
	}

	public void setBcc(String bcc) {
		this.bcc = bcc;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getBodyText() {
		return bodyText;
	}

	public void setBodyText(String bodyText) {
		this.bodyText = bodyText;
	}

	public String getAttachPath() {
		return attachPath;
	}

	public void setAttachPath(String attachPath) {
		this.attachPath = attachPath;
	}
	
	/**
	 * 判断邮件是否包含附件
	 * @param part
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */
	private boolean isContainAttache(Part part) throws MessagingException,
			IOException {
		boolean attachflag = false;
		// String contentType = part.getContentType();
		if (part.isMimeType("multipart/*")) {
			Multipart mp = (Multipart) part.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				BodyPart mpart = mp.getBodyPart(i);
				String disposition = mpart.getDisposition();
				if ((disposition != null)
						&& ((disposition.equals(Part.ATTACHMENT)) || (disposition
								.equals(Part.INLINE))))
					attachflag = true;
				else if (mpart.isMimeType("multipart/*")) {
					attachflag = isContainAttache((Part) mpart);
				} else {
					String contype = mpart.getContentType();
					if (contype.toLowerCase().indexOf("application") != -1)
						attachflag = true;
					if (contype.toLowerCase().indexOf("name") != -1)
						attachflag = true;
				}
			}
		} else if (part.isMimeType("message/rfc822")) {
			attachflag = isContainAttache((Part) part.getContent());
		}
		return attachflag;
	}
	
	/**
	 * 获得邮件的发件人的地址
	 * @param mimeMessage
	 * @return
	 * @throws MessagingException
	 */
	private String parseFrom(MimeMessage mimeMessage) throws MessagingException{
		InternetAddress[] address = (InternetAddress[])mimeMessage.getFrom();
		String from = address[0].getAddress();
		if(StringUtil.isEmpty(from)){
			from = "";
		}
		String personal = address[0].getPersonal();
		if(StringUtil.isEmpty(personal)){
			personal = "";
		}
		String fromAddr = StringUtil.concat(new Object[]{
				personal, "<", from, ">"
		});
		return fromAddr;
	}
	
	/**
	 * 解析邮件中的发送目的地址
	 * @param mimeMessage
	 * @param type
	 * @return
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 * @throws ErrorEmailAddressTypeException
	 */
	private String parseMailAddress(MimeMessage mimeMessage, String type)
			throws MessagingException, UnsupportedEncodingException,
			ErrorEmailAddressTypeException {
		String mailaddr = "";
		String addtype = type.toUpperCase();
		InternetAddress[] address = null;
		if (addtype.equals("TO") || addtype.equals("CC")
				|| addtype.equals("BCC")) {
			if (addtype.equals("TO")) {
				address = (InternetAddress[]) mimeMessage
						.getRecipients(Message.RecipientType.TO);
			} else if (addtype.equals("CC")) {
				address = (InternetAddress[]) mimeMessage
						.getRecipients(Message.RecipientType.CC);
			} else {
				address = (InternetAddress[]) mimeMessage
						.getRecipients(Message.RecipientType.BCC);
			}
			if (address != null) {
				for (int i = 0; i < address.length; i++) {
					String email = address[i].getAddress();
					if (email == null)
						email = "";
					else {
						email = MimeUtility.decodeText(email);
					}
					String personal = address[i].getPersonal();
					if (personal == null)
						personal = "";
					else {
						personal = MimeUtility.decodeText(personal);
					}
					String compositeto = personal + "<" + email + ">";
					mailaddr += "," + compositeto;
				}
				mailaddr = mailaddr.substring(1);
			}
		} else {
			throw new ErrorEmailAddressTypeException("Error emailaddr type!");
		}
		return mailaddr;
	}
	
	/**
	 * 解析邮件的正文内容
	 * @param part
	 * @return
	 * @throws IOException
	 * @throws MessagingException
	 */
	private String parseMailContent(Part part, StringBuilder bodyText) throws IOException, MessagingException{
		String contenttype = part.getContentType();
        int nameindex = contenttype.indexOf("name");
        boolean conname = false;
        if (nameindex != -1)
            conname = true;
        Log.d("cqm", "CONTENTTYPE: " + contenttype);
        if (part.isMimeType("text/plain") && !conname) {
        	bodyText.append((String) part.getContent());
        } else if (part.isMimeType("text/html") && !conname) {
        	bodyText.append((String) part.getContent());
        } else if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            int counts = multipart.getCount();
            for (int i = 0; i < counts; i++) {
            	parseMailContent(multipart.getBodyPart(i), bodyText);
            }
        } else if (part.isMimeType("message/rfc822")) {
        	parseMailContent((Part) part.getContent(), bodyText);
        } else {
        	bodyText.append((String) part.getContent());
        }
        return bodyText.toString();
	}
	
	@Override
	public String toString(){
		return StringUtil.concat(new Object[]{
				"标题：", getSubject(), "---发送/接收日期：", getCreateDate().toLocaleString(),
				"---是否需要回复：", getReplySign(), "---是否是新邮件：", getIsNew(), "---是否含有附件：",
				getIsContainAttache(), "---发送人：", getFrom(), "---接收人：", getTo(), 
				"---抄送人：", getCc(), "---密送人：", getBcc(), "---邮件的ID号：", getMessageId(),
				"---邮件的正文：", getBodyText()
		});
	}
	
}
