
package org.xtremeware.iudex.vo;

public class MailingConfigVo extends ValueObject{
    
    	private String sender;
	private String smtpServer;

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
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.sender != null ? this.sender.hashCode() : 0);
        hash = 79 * hash + (this.smtpServer != null ? this.smtpServer.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "MailingConfigVo{" + "sender=" + sender + ", smtpServer=" + smtpServer + '}';
    }
	
    public String getSender(){
        return this.sender;
    }
    
    public void setSender(String sender){
        this.sender = sender;
    }
    
    public String getSmtpServer(){
        return this.smtpServer;
    }
    
    public void setSmtpServer(String smtpServer){
        this.smtpServer = smtpServer;
    }
}
