package com.xantrix.webapp.service;

import com.xantrix.webapp.entity.dto.ListiniDto;

public interface ListiniService {

	ListiniDto findById(String id);

	void delListini(ListiniDto listiniDto);

	void insListini(ListiniDto listiniDto);
}
