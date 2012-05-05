
package org.xtremeware.iudex.vo;

public class MailingConfigVo implements ValueObject{
    
    	private String sender;
	private String smtpServer;
	private int smtpServerPort;
	private String smtpUser;
	private String smtpPassword;

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final MailingConfigVo other = (MailingConfigVo) obj;
		if ((this.sender == null) ? (other.sender != null) : !this.sender.equals(other.sender)) {
			return false;
		}
		if ((this.smtpServer == null) ? (other.smtpServer != null) : !this.smtpServer.equals(other.smtpServer)) {
			return false;
		}
		if (this.smtpServerPort != other.smtpServerPort) {
			return false;
		}
		if ((this.smtpUser == null) ? (other.smtpUser != null) : !this.smtpUser.equals(other.smtpUser)) {
			return false;
		}
		if ((this.smtpPassword == null) ? (other.smtpPassword != null) : !this.smtpPassword.equals(other.smtpPassword)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 29 * hash + (this.sender != null ? this.sender.hashCode() : 0);
		hash = 29 * hash + (this.smtpServer != null ? this.smtpServer.hashCode() : 0);
		hash = 29 * hash + this.smtpServerPort;
		hash = 29 * hash + (this.smtpUser != null ? this.smtpUser.hashCode() : 0);
		hash = 29 * hash + (this.smtpPassword != null ? this.smtpPassword.hashCode() : 0);
		return hash;
	}

	@Override
	public String toString() {
		return "MailingConfigVo{" + "sender=" + sender + ", smtpServer=" + smtpServer + ", smtpServerPort=" + smtpServerPort + ", smtpUser=" + smtpUser + ", smtpPassword=" + smtpPassword + '}';
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getSmtpPassword() {
		return smtpPassword;
	}

	public void setSmtpPassword(String smtpPassword) {
		this.smtpPassword = smtpPassword;
	}

	public String getSmtpServer() {
		return smtpServer;
	}

	public void setSmtpServer(String smtpServer) {
		this.smtpServer = smtpServer;
	}

	public int getSmtpServerPort() {
		return smtpServerPort;
	}

	public void setSmtpServerPort(int smtpServerPort) {
		this.smtpServerPort = smtpServerPort;
	}

	public String getSmtpUser() {
		return smtpUser;
	}

	public void setSmtpUser(String smtpUser) {
		this.smtpUser = smtpUser;
	}

}
