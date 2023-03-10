package Base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import io.opentelemetry.exporter.logging.SystemOutLogExporter;

public class Hooks {
	public static final String VALUE = "value";

	public static String prop(String Key) {

		Properties pp = new Properties();
		FileInputStream fil = null;
		try {
			fil = new FileInputStream("src/test/java/data.properties");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			pp.load(fil);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pp.getProperty(Key);
	}

	// This methods converts the date from 2023-Nov-20 to 2023-11-20
	public String dateConvert(String D) {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MMM-dd");
		SimpleDateFormat format2 = new SimpleDateFormat("yyy-MM-dd");
		Date date = null;
		try {
			date = format1.parse(D);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		String dateString = format2.format(date);
		return dateString;
	}
}
