package com.xantrix.webapp.dtos;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InfoMsg implements Serializable {

	private static final long serialVersionUID = 374063828278064186L;
	private String code;
	private String message;
}
