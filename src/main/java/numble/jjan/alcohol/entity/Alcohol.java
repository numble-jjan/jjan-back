package numble.jjan.alcohol.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.jjan.post.entity.Post;
import numble.jjan.util.BaseTimeEntity;


@Getter
@NoArgsConstructor
@Entity
public class Alcohol extends BaseTimeEntity {
	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	 	private String type;
	 	
	 	@Column(nullable = false)
	    private String name;

	    @Column(nullable = false)
	    private String image;

	    @Column(nullable = false)
	    private String description;

	    @Builder
	    public Alcohol(String type, String name, String image, String description) {
	        this.type = type;
	    	this.name = name;
	        this.image = image;
	        this.description = description;
	    }

	    public void update(String type, String name, String image, String description) {
	        this.type = type;
	    	this.name = name;
	        this.image = image;
	        this.description = description;
	    }
}
