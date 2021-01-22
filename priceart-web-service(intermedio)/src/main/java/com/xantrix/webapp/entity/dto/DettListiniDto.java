package com.xantrix.webapp.entity.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DettListiniDto implements Serializable {

	private static final long serialVersionUID = -7390917990276130062L;
	private Integer id;
	private String codArt;
	private Double prezzo;
//	private ListiniDto listinoDto;
}
