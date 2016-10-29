package oak.tests.models;

import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class Exchange {
	private String shortName;
	private String description;
	private String extension;
}
