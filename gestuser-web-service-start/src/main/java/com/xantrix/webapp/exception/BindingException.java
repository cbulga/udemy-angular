package com.xantrix.webapp.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BindingException extends Exception {

	private static final long serialVersionUID = -1646083143194195402L;

	private String messaggio;

	public BindingException() {
		super();
	}

	public BindingException(String messaggio) {
		super(messaggio);
		this.messaggio = messaggio;
	}
}
