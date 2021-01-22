package com.xantrix.webapp.entity;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.xantrix.webapp.entity.Listini.Obsoleto;

import io.micrometer.core.instrument.util.StringUtils;

@Converter(autoApply = true)
public class ObsoletoConverter implements AttributeConverter<Obsoleto, String> {

	@Override
	public String convertToDatabaseColumn(Obsoleto attribute) {
		return attribute != null ? attribute.getValue() : null;
	}

	@Override
	public Obsoleto convertToEntityAttribute(String dbData) {
		return StringUtils.isNotEmpty(dbData) ? Obsoleto.fromValue(dbData) : null;
	}
}
