package it.wego.welfarego.persistence.exception;


public class BudgetAssentiException extends Exception {

	private static final long serialVersionUID = 1L;

	public BudgetAssentiException(String s) {
        super(s, null);
    }

    public BudgetAssentiException(String s, Throwable t) {
        super(s, t);
    }
}
