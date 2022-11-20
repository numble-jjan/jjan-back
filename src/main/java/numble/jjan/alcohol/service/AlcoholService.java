package numble.jjan.alcohol.service;

import java.util.List;

import numble.jjan.alcohol.dto.AlcoholDetailResponseDto;
import numble.jjan.alcohol.dto.AlcoholResponseDto;
import numble.jjan.alcohol.dto.AlcoholTypeReponseDto;

public interface AlcoholService {
	
	String saveAlcoholWithNaver(String search);
	
	List<AlcoholResponseDto> getAlcoholList(String type);
	
	AlcoholResponseDto getRandomAlcohol();
	
	AlcoholResponseDto getTodayAlcohol();
	
	AlcoholDetailResponseDto getAlcoholDetail(Long id);
	
	List<AlcoholTypeReponseDto> getAlcoholType();
}
