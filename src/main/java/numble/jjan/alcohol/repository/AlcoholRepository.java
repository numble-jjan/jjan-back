package numble.jjan.alcohol.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import numble.jjan.alcohol.dto.AlcoholResponseDto;
import numble.jjan.alcohol.entity.Alcohol;

public interface AlcoholRepository  extends JpaRepository<Alcohol, String>{
	
	List<Alcohol> findAll();
	
	List<AlcoholResponseDto> findAllByType(String type);
	
	AlcoholResponseDto findById(Long id);
}
