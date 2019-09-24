package com.dev.validations;




import java.util.regex.Matcher;
import java.util.regex.Pattern;




public class Validate {

	public Boolean idValidation(String id) {
		Pattern pat = Pattern.compile("\\d+"); 
		Matcher mat = pat.matcher(id);
		if(mat.matches() && (Integer.parseInt(id)>=0)) {

			return true;
		}

		return false;


	}


	public Boolean dateValidation(String date)  {
		Pattern pat = Pattern.compile("([12]\\d{3}(\\/)(0[1-9]|1[0-2])(\\/)(0[1-9]|[12]\\d|3[01]))"); 
		
		Matcher mat = pat.matcher(date);
		if(mat.matches()) { 
			return true;
		}
		else
		{
			return false;
		}
	}




}



