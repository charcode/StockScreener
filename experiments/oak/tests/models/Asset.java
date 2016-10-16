package oak.tests.models;

import java.math.BigDecimal;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;

import com.sun.jna.platform.win32.Sspi.TimeStamp;

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
	private Map<TimeStamp,BigDecimal>historicalPrices;
	
//	@OneToMany
//	@MapKeyJoinColumn
	@ElementCollection(fetch = FetchType.LAZY)
	private Map<Exchange,String>tickerByExchange;

	
}
