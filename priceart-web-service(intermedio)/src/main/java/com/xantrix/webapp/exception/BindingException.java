package com.xantrix.webapp.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BindingException extends Exception {
	private static final long serialVersionUID = 1L;

	private String messaggio;

	public BindingException() {
		super();
	}

	public BindingException(String Messaggio) {
		super(Messaggio);
		this.messaggio = Messaggio;
	}
}
