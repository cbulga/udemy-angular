package com.xantrix.webapp.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class NotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	private String messaggio = "Elemento Ricercato Non Trovato!";

	public NotFoundException() {
		super();
	}

	public NotFoundException(String Messaggio) {
		super(Messaggio);
		this.messaggio = Messaggio;
	}
}
