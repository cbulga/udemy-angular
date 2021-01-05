package com.xantrix.webapp.exception;

import lombok.Getter;

public class NotFoundException extends Exception {

	private static final long serialVersionUID = -8729169303699924451L;
	private static final String DEFAULT_MESSAGE = "Elemento Ricercato Non Trovato!";
	@Getter
	private final String messaggio;

	public NotFoundException() {
		this(DEFAULT_MESSAGE);
	}

	public NotFoundException(String messaggio) {
		super(messaggio);
		this.messaggio = messaggio;
	}
}
