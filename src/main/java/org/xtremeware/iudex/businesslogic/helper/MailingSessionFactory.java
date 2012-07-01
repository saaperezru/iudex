package org.xtremeware.iudex.businesslogic.helper;

import javax.mail.Session;

public interface MailingSessionFactory {

    public Session createSession();
}
