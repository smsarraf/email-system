/*
 * Author : Siddharth Saraf
 * Project : SeekAsia
 */
package com.hackerrank.emailsystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @author smsarraf
 *
 */
@Service
public class EmailService {

	@Autowired
	JavaMailSender javaMailSender;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * Send Email.
	 *
	 * @param from    the from
	 * @param to      the to
	 * @param subject the subject
	 * @param body    the body
	 * @return true, if successful
	 */
	public boolean sendMail(String from, String to, String subject, String body) {
		try {
			SimpleMailMessage mail = new SimpleMailMessage();

			mail.setFrom(from);
			mail.setTo(to);
			mail.setSubject(subject);
			mail.setText(body);
			javaMailSender.send(mail);

			logger.info("Email successfully sent with [To:" + to + ",subject:" + subject + ", body:" + body + "]");
			return true;
		} catch (Exception e) {
			logger.error("Error Occurred While Sending Email on [To:" + to + ",subject:" + subject + ", body:" + body
					+ "], Error Message :: " + e.getMessage(), e);
		}
		return false;

	}
}
