package numble.jjan.alcohol.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import numble.jjan.alcohol.dto.AlcoholDetailResponseDto;
import numble.jjan.alcohol.dto.AlcoholResponseDto;
import numble.jjan.alcohol.dto.AlcoholTypeReponseDto;

@Mapper
public interface AlcoholMapper {
	// 술 리스트 조회
	List<AlcoholResponseDto> findAlcoholList(String type);
	// 술 상세 조회
	AlcoholDetailResponseDto findAlcoholDetail(Long id);
	// 랜덤 술 조회
	AlcoholResponseDto findRandomAlcohol();
	// 오늘의 술 조회
	AlcoholResponseDto findTodayAlcohol();
	// 술 종류 조회
	List<AlcoholTypeReponseDto> findAlcoholType();
}
