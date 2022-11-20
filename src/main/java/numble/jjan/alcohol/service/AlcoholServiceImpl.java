package numble.jjan.alcohol.service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import numble.jjan.alcohol.dto.AlcoholDetailResponseDto;
import numble.jjan.alcohol.dto.AlcoholResponseDto;
import numble.jjan.alcohol.dto.AlcoholTypeReponseDto;
import numble.jjan.alcohol.entity.Alcohol;
import numble.jjan.alcohol.repository.AlcoholMapper;
import numble.jjan.alcohol.repository.AlcoholRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class AlcoholServiceImpl  implements  AlcoholService{
	
	@Autowired
	private final AlcoholRepository alcoholRepository;
	
	@Autowired
	private final AlcoholMapper alcoholMapper;
	
	@Override
	public String saveAlcoholWithNaver(String search) {
	    HttpHeaders headers = new HttpHeaders();
	    String CLIENT_ID = "ymfsVXkzcriDjqBonWsF";
	    String SECRET_KEY = "hAV7WcEzOc";
	    String url = "https://openapi.naver.com/v1/search/encyc.json?";
	    String returnStr = "";
	    
	    headers.add("X-Naver-Client-Id", CLIENT_ID);
	    headers.add("X-Naver-Client-Secret", SECRET_KEY);
	    Charset utf8 = Charset.forName("UTF-8");
	    MediaType mediaType = new MediaType("application", "json", utf8);
	    headers.setContentType(mediaType);
	    
	    		
	    try {
	       HttpEntity<String> httpRequest = new HttpEntity<String>(headers);
	       UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).queryParam("query", search).build(false);
	      
	       RestTemplate restTemplate = new RestTemplate();
		   ResponseEntity<String> responseEntityStr = restTemplate.exchange(
				   uri.toString(), 
				   HttpMethod.GET,
				   httpRequest,
				   String.class);
	       
		   String resBody = responseEntityStr.getBody().toString();
		   
		   resBody.replaceAll("<b>", "");
		   resBody.replaceAll("</b>", "");
		   
		   
		   ObjectMapper objectMapper = new ObjectMapper();
	       JsonNode jsonNode = objectMapper.readTree(resBody);	       
	       ArrayNode arrayNode = (ArrayNode) jsonNode.get("items");
	       
	       for(int i = 0; i < arrayNode.size(); i++) {
	    	   String name = arrayNode.get(i).get("title").asText();
	    	   String image = arrayNode.get(i).get("thumbnail").asText();
	    	   String description = arrayNode.get(i).get("description").asText();
	    	   
	    	   if (!"".equals(image) && !"".equals(description) && !description.contains("...")) {
	    		   name = name.replaceAll("<b>", "").replaceAll("</b>", "");
	    		   description = description.replaceAll("<b>", "").replaceAll("</b>", "");
	    		   alcoholRepository.save(Alcohol.builder()
			                .name(name)
			                .description(description)
			                .image(image)
			                .type("1")
			                .build());
	    	   }
	       }
		   
	       returnStr = resBody;
		   
		   
	    } catch (IOException iOException) {
	       log.error(iOException.toString());
	    } catch (HttpClientErrorException httpClientErrorException) {
	       log.error(httpClientErrorException.toString());
	    } catch (Exception e) {
	    	log.error(e.toString());
	    }
	    
		return returnStr;
	}
	
	@Override
	public List<AlcoholResponseDto> getAlcoholList(String type) {
		return alcoholMapper.findAlcoholList(type);
	}

	@Override
	public AlcoholResponseDto getRandomAlcohol() {
		return alcoholMapper.findRandomAlcohol();
	}

	@Override
	public AlcoholResponseDto getTodayAlcohol() {
		return alcoholMapper.findTodayAlcohol();
	}

	@Override
	public AlcoholDetailResponseDto getAlcoholDetail(Long id) {
		return alcoholMapper.findAlcoholDetail(id);
	}

	@Override
	public List<AlcoholTypeReponseDto> getAlcoholType() {
		return alcoholMapper.findAlcoholType();
	}

}
