package oak.tests.models;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Asset {
	
	private String cusip;
	@Id
	private String ticker;
	
	private String description;
	private SecurityType securityType;
	private BigDecimal price;
	
	@ElementCollection(fetch=FetchType.EAGER)
	private Map<Timestamp,BigDecimal>historicalPrices;
	
	@ElementCollection(fetch = FetchType.LAZY)
	private Map<Exchange,String>tickerByExchange;

	
}
