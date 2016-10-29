package experiments;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import org.junit.Test;

public class DateFormatTests {

	@Test
	public void test() {
		String str = "03/06/2016 21:01";
//		LocalDate parse = LocalDate.parse(str);
		SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy hh:mm");
		try {
			Date parse = f.parse(str);
			System.out.println(parse);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

}
