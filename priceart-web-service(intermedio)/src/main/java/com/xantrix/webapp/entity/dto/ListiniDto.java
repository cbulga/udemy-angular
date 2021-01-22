package com.xantrix.webapp.entity.dto;

import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ListiniDto implements Serializable {

	private static final long serialVersionUID = -6941105193844852815L;
	@NotNull(message = "{NotNull.Listini.id.Validation}")
	private String id;
	@Size(min = 3, max = 30, message = "{Size.Listini.descrizione.Validation}")
	private String descrizione;
	@NotNull(message = "{NotNull.Listini.obsoleto.Validation}")
	private String obsoleto;
	private Set<DettListiniDto> dettListini;
}
