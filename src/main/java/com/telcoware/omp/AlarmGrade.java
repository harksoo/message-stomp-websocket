package com.telcoware.omp;

import java.util.Random;

import org.apache.log4j.Logger;

public class AlarmGrade {
	
	static final Logger logger = Logger.getLogger(AlarmGrade.class);
	
	public static final String Critical = "CRITICAL";
	public static final String Major = "MAJOR";
	public static final String Minor = "MINOR";
	public static final String Normal = "NORMAL";
	
	// Testing 요망.
	public static String getRandomGrade(){
		
		String value = Normal;
		
		Random generator = new Random();   
		
		int random = generator.nextInt(4);
		
		//logger.info(random);
		
		if (random == 1){
			value = Major;
		} else if (random == 2) {
			value = Minor;
		} else if (random == 3) {
			value = Critical;
		}
		
		return value;
	}
}
