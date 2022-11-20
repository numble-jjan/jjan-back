package numble.jjan.alcohol.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class AlcoholType {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

 	private String name;
 	
 	private String englishName;
 	
 	@Builder
    public AlcoholType(String name) {
    	this.name = name;
    }

    public void update(String name) {
    	this.name = name;
    }
}
