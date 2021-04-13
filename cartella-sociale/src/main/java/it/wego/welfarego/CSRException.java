/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.wego.welfarego;

/**
 *
 * @author giuseppe
 */
public class CSRException extends Exception {

	private static final long serialVersionUID = 1L;
	
	protected String message;

    public CSRException(String message) {
        this.message = message;
    }

    public CSRException(String message, Throwable cause) {
        super(message, cause);
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
