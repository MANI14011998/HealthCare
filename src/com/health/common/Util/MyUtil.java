package com.health.common.Util;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Logger;

public class MyUtil {
	
	private static final Logger log = Logger.getLogger(MyUtil.class.getName());
	
	private static String[] unitdo ={"", " ONE", " TWO", " THREE", " FOUR", " FIVE",
            " SIX", " SEVEN", " EIGHT", " NINE", " TEN", " ELEVEN", " TWELVE",
            " THIRTEEN", " FOURTEEN", " FIFTEEN",  " SIXTEEN", " SEVENTEEN", 
            " EIGHTEEN", " NINETEEN"};
	private static  String[] tens =  {"", "TEN", " TWENTY", " THIRTY", " FORTY", " FIFTY",
            " SIXTY", " SEVENTY", " EIGHTY", " NINETY"};
	private static String[] digit = {"", " HUNDRED", " THOUSAND", " LAKH", " CRORE"};

	public static Date getDateWithoutTimeComponent(Date date) {
		Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+530"));
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date dateWithOutTimeComponent = cal.getTime();
		return dateWithOutTimeComponent;
	}
	
	public static <T> List<T> getPartOfList(int size, int fromIndex, List<T> list) {
		if (list.size() <= 0) {
			return null;
		}
		
		// fromIndex is greater than the list size, so do nothing
		if (fromIndex >= list.size()) {
			return null;
		}
		
		List<T> partOfList = null;
		int toIndex = fromIndex + size;

		if (toIndex > list.size()) {
			toIndex = list.size();
		}
		
		partOfList = list.subList(fromIndex, toIndex);
		return partOfList;
	}
	
	public static String getAmountFormatedString(double amount) {
		MessageFormat mf = new MessageFormat("{0, number,#,##0.00}");
		Object[] objs = {new Double(amount)};
		String result = mf.format(objs);
		return result;
	}
	
	public static String getNumberToWordString(double amount) {
		int len, q = 0, r = 0;
		String word = " ";
		String numberToWordStr = "RUPEES";
		String paise [] = getAmountFormatedString(amount).split("[.]");
		   
		int num = (int) amount;
		if (num <= 0) return "";
		
		while (num > 0) {
			len = numberCount(num);
			//Take the length of the number and do letter conversion
			switch (len) {
				case 8:
					q = num / 10000000;
					r = num % 10000000;
					word = twonum(q);
					numberToWordStr = numberToWordStr + word + digit[4];
					num = r;
					break;
				
				case 7:
				case 6:
					q = num / 100000;
					r=num % 100000;
					word = twonum(q);
					numberToWordStr = numberToWordStr + word + digit[3];
					num = r;
					break;
				
				case 5:
				case 4:
					q = num / 1000;
					r = num % 1000;
					word = twonum(q);
					numberToWordStr= numberToWordStr + word + digit[2];
					num = r;
					break;
				
				case 3:
					if (len == 3) {
					r = num;
					}
					word = threenum(r);
					numberToWordStr = numberToWordStr + word;
					num = 0;
					break;
				
				case 2:
					word = twonum(num);
					numberToWordStr = numberToWordStr + word;
					num=0;
					break;
				
				case 1:
					numberToWordStr = numberToWordStr + unitdo[num];
					num=0;
					break;
					
				default:
					num=0;
					numberToWordStr = "";
					log.severe("Exceeding Crore....No conversion");
			}
			if (num == 0) {
				if (paise[1].equals("00")) {
					numberToWordStr = numberToWordStr + " ONLY";
				} else {
					numberToWordStr = numberToWordStr + " AND " +  paise[1] + " PAISA ONLY";
				}
				return numberToWordStr;
			}
		}
		return numberToWordStr;
	}
	
	//Count the number of digits in the input number
	private static int numberCount(int num) {
		int cnt = 0;
		while (num > 0) {
			cnt++;
			num = num / 10;
		}
		return cnt;
	}
	
	//Function for Conversion of two digit
	private static String twonum(int numq) {
		int numr, nq;
		String word = "";
		nq = numq / 10;
		numr = numq % 10;
		if (numq > 19) {
			word=word + tens[nq] + unitdo[numr];
		}
		else {
			word = word + unitdo[numq];
		}
		return word;
	}
	
	//Function for Conversion of three digit
	private static String threenum(int numq) {
		int numr, nq;
		String word = "";
		nq = numq / 100;
		numr = numq % 100;
		if (numr == 0) {
			word = word + unitdo[nq] + digit[1];
		}
		else {
			word = word + unitdo[nq] + digit[1] + " AND" + twonum(numr);
		}
		return word;
	}
	
	/**
	 * returns the next word.
	 * 
	 * @param word get the next word for the given word
	 * @return
	 * <li> if the given word is abc, then it will return abd(increases the last char by one).
	 * <li> if the last char is z. i.e, if the given word is abz, then it will return the same given word.
	 * <li> if the last char of the given word is special char, then it will return the same given word.
	 *  i,e if the given word is *** or ab* it will return the same given word.
	 * <li> if the given word is empty, then it will return the same word(empty string) 
	 */
	public static String getNextWord(String word) {
		if (word.isEmpty()) {
			return word;
		}

		Character lastChar = word.charAt(word.length() - 1);

		if ((lastChar >= '0' && lastChar <= '9') || (lastChar >= 'a' && lastChar <= 'z') ||
				(lastChar >= 'A' && lastChar <= 'Z')) {

			if (!(lastChar.equals('z') || lastChar.equals('Z'))) {
				char nextLetterOfLastChar = (char) (lastChar + 1);
				String newWord = word.substring(0, word.length() - 1) + nextLetterOfLastChar;
				return newWord;
			} else {
				return word;
			}
		}
		return word;
	}

	/**
	 * for a given word it will change the case of the first character to other case
	 * i.e, upperCase to lowerCase or viceversa.
	 * 
	 * @return
	 * <li> for ex: if the given word is "abc", then it will returns "Abc"
	 * <li> for ex: if the given word is "Abc", then it will returns "abc"
	 */
	public static String getWordStartingWithOtherCase(String word) {
		if (word.isEmpty()) {
			return word;
		}

		Character firstChar = word.charAt(0);
		if ((firstChar >= 'a' && firstChar <= 'z') || (firstChar >= 'A' && firstChar <= 'Z')) {

			if (Character.isLowerCase(firstChar)) {
				word = Character.toUpperCase(firstChar) + word.substring(1, word.length());
			} else if (Character.isUpperCase(firstChar)) {
				word = Character.toLowerCase(firstChar) + word.substring(1, word.length());
			}
		}
		return word;
	}

	/**
	 * for a given word it will returns the word with lowerCase
	 * 
	 * @return
	 * <li> for ex: if the given word is "abc", then it will returns "abc"
	 * <li> for ex: if the given word is "ABC", then it will returns "abc"
	 */
	public static String getWordWithLowerCase(String word) {
		if (word.isEmpty()) {
			return word;
		}

		Character firstChar = word.charAt(0);
		if ((firstChar >= 'a' && firstChar <= 'z') || (firstChar >= 'A' && firstChar <= 'Z')) {

			String newWord = word.toLowerCase();
			return newWord;
		}
		return word;
	}

	/**
	 * for a given word it will returns the word with upperCase
	 * 
	 * @return
	 * <li> for ex: if the given word is "abc", then it will returns "ABC"
	 * <li> for ex: if the given word is "ABC", then it will returns "ABC"
	 */
	public static String getWordWithUpperCase(String word) {
		if (word.isEmpty()) {
			return word;
		}

		Character firstChar = word.charAt(0);
		if ((firstChar >= 'a' && firstChar <= 'z') || (firstChar >= 'A' && firstChar <= 'Z')) {

			String newWord = word.toUpperCase();
			return newWord;
		}
		return word;
	}

	public static Date getDateWith24hrTimeComponent(Date date) {
		Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+530"));
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 0);
		Date dateWith24hrTimeComponent = cal.getTime();
		return dateWith24hrTimeComponent;
	}
}