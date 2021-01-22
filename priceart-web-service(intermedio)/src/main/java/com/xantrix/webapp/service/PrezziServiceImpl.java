package com.xantrix.webapp.service;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xantrix.webapp.entity.DettListini;
import com.xantrix.webapp.entity.Listini.Obsoleto;
import com.xantrix.webapp.entity.dto.DettListiniDto;
import com.xantrix.webapp.repository.PrezziRepository;

@Service
@Transactional(readOnly = true)
public class PrezziServiceImpl implements PrezziService {

	protected static final Function<DettListini, DettListiniDto> dettListini2DettListiniDto = dettListini -> {
		DettListiniDto dettListiniDto = null;
		if (dettListini != null) {
			dettListiniDto = DettListiniDto.builder()
					.id(dettListini.getId())
					.codArt(dettListini.getCodArt())
					.prezzo(dettListini.getPrezzo())
					//					.listinoDto(listinoDto)
					.build();
		}
		return dettListiniDto;
	};
	protected static final Function<DettListiniDto, DettListini> dettListiniDto2DettListini = dettListiniDto -> {
		DettListini dettListini = null;
		if (dettListiniDto != null) {
			dettListini = DettListini.builder()
					.id(dettListiniDto.getId())
					.codArt(dettListiniDto.getCodArt())
					.prezzo(dettListiniDto.getPrezzo())
					//					.listinoDto(listinoDto)
					.build();
		}
		return dettListini;
	};

	@Autowired
	private PrezziRepository prezziRepository;

	@Override
	public Double findPrezzoArticoloByListinoIdAndCodArt(String listinoId, String codArt) {
		return prezziRepository.findPrezzoArticoloByListinoIdAndCodArt(listinoId, codArt, Obsoleto.NO);
	}

	@Override
	public DettListiniDto findByCodArtAndListinoId(String codArt, String listinoId) {
		return dettListini2DettListiniDto.apply(prezziRepository.findByCodArtAndListinoId(codArt, listinoId));
	}

	@Override
	@Transactional
	public void delDettListini(DettListiniDto dettListiniDto) {
		prezziRepository.delete(dettListiniDto2DettListini.apply(dettListiniDto));
	}
}
