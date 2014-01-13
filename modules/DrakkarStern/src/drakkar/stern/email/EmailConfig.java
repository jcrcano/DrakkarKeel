/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.stern.email;

public class EmailConfig {
    
    String user;
    String password;
    private String hostSmtp;
    private int portSmtp;
    private String email;//senderAddress

    /**
     *
     * @param user
     * @param password
     * @param email 
     * @param hostSmtp
     * @param portSmtp
     */
    public EmailConfig(String user, String password , String email,String hostSmtp, int portSmtp) {
        this.user = user;
        this.password = password;
        this.hostSmtp = hostSmtp;
        this.portSmtp = portSmtp;
        this.email = email;
    }

    /**
     *
     * @return
     */
    public String getHostSmtp() {
        return hostSmtp;
    }

    /**
     * 
     * @param hostSmtp
     */
    public void setHostSmtp(String hostSmtp) {
        this.hostSmtp = hostSmtp;
    }

    /**
     *
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *
     * @return
     */
    public int getPortSmtp() {
        return portSmtp;
    }

    /**
     *
     * @param portSmtp
     */
    public void setPortSmtp(int portSmtp) {
        this.portSmtp = portSmtp;
    }

    /**
     *
     * @return
     */
    public String getUser() {
        return user;
    }

    /**
     *
     * @param user
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    
}
