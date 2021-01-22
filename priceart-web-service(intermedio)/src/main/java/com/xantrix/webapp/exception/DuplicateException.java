package com.xantrix.webapp.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DuplicateException extends Exception {
	private static final long serialVersionUID = 1L;

	private String messaggio;

	public DuplicateException() {
		super();
	}

	public DuplicateException(String Messaggio) {
		super(Messaggio);
		this.messaggio = Messaggio;
	}
}
