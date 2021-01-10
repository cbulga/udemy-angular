package com.xantrix.webapp.model.dtos;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class UtentiDto implements Serializable {

	private static final long serialVersionUID = -1672225487513016417L;

	private String id;

	@Size(min = 5, max = 80, message = "{Size.Utenti.userId.Validation}")
	@NotNull(message = "{NotNull.Articoli.userId.Validation}")
	private String userId;

	@Size(min = 8, max = 80, message = "{Size.Utenti.password.Validation}")
	private String password;

	private String attivo;

	private List<String> ruoli;
}
