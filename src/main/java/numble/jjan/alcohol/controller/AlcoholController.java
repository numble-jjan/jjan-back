package numble.jjan.alcohol.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;
import numble.jjan.alcohol.dto.AlcoholDetailResponseDto;
import numble.jjan.alcohol.dto.AlcoholResponseDto;
import numble.jjan.alcohol.dto.AlcoholTypeReponseDto;
import numble.jjan.alcohol.service.AlcoholService;

@RequiredArgsConstructor
@RestController
public class AlcoholController {
	 @Autowired
	 private final AlcoholService alcoholService;
	 
	 // 술 저장
	 @GetMapping("/alcohol/search/{search}")
	 public String saveAlcoholWithNaver(@PathVariable String search) {
		 // 네이버 지식백과 통해서 저장
		 String returnstr = alcoholService.saveAlcoholWithNaver(search);
		 return returnstr;
	 }
	 
	 
	 // 주종 조회
	 @GetMapping("/api/alcohol-type")
	 public String getAlcoholTypeList() {
		 Gson gson = new Gson();
		 List<AlcoholTypeReponseDto> alcoholTypeResponseDtoList = alcoholService.getAlcoholType();
		 String returnString = gson.toJson(alcoholTypeResponseDtoList);
		 return returnString;
	 }
	 
	 // 전체 술 조회
	 @GetMapping("/api/alcohol") 
	 public String getAlcoholList() {
		 Gson gson = new Gson();
		 List<AlcoholResponseDto> alcoholResponseDtoList = alcoholService.getAlcoholList(null);
		 String returnString = gson.toJson(alcoholResponseDtoList);
		 return returnString;
	 }
	 
	 // 주중으로 술 목록 조회
	 @GetMapping("/api/alcohol-type/{type}") 
	 public String getAlcoholListByType(@PathVariable String type) {
		 Gson gson = new Gson();
		 List<AlcoholResponseDto> alcoholResponseDtoList = alcoholService.getAlcoholList(type);
		 String returnString = gson.toJson(alcoholResponseDtoList);
		 return returnString;
	 }
	 
	 // 술 상세 조회
	 @GetMapping("/api/alcohol/{id}") 
	 public String getAlcoholDetail(@PathVariable Long id) {
		 Gson gson = new Gson();
		 AlcoholDetailResponseDto alcoholResponseDto = alcoholService.getAlcoholDetail(id);
		 String returnString = gson.toJson(alcoholResponseDto);
		 return returnString;
	 }
	 
	 // 랜덤 술 조회
	 @GetMapping("/api/random/alcohol") 
	 public String getRandomAlcohol() {
		 Gson gson = new Gson();
		 AlcoholResponseDto alcoholResponseDto = alcoholService.getRandomAlcohol();
		 String returnString = gson.toJson(alcoholResponseDto);
		 return returnString;
	 }
	 
	 // 오늘의 술 조회
	 @GetMapping("/api/today/alcohol") 
	 public String getTodyAlcohol() {
		 Gson gson = new Gson();
		 AlcoholResponseDto alcoholResponseDto = alcoholService.getTodayAlcohol();
		 String returnString = gson.toJson(alcoholResponseDto);
		 return returnString;
	 }
	 
	 // 술 저장
	 @PostMapping("/api/alcohol")
	 public String saveAlcohol() {
		 return "";
	 }
}
