This parser requires json-simple.jar

it can translate a string (the content of a html file) into 
Json or HTMLObjects

HTMLParser:
	List<String>	 ignoredObjects			 | all Strings which don't end with a </obj>

	List<HTMLObject> parse(String htmlString)	 | parse a string to a list of HTMLObjects
	String 		 toJsonString(String html)	 | translate a html string to Json string	 	


HTMLObject:
	List<HTMLObject> inner				 | all the HTMLObjects which are inside of this object
	String   	 data				 | all the text of this object (content between <>)
	
	const    	 HTMLObject(String data) 	 | creates a new HTMLObject
	void 	 	 setInner(List<HTMLObject inner) | set all the inner HTMLObjects
	void	 	 parseInner(String innerHTML)	 | parse a string and set it as the inner content
	String[] 	 getLines()			 | split the data into its lines (only available if the data contains <br>)
	String 	 	 toString()			 | returns the object as a string
	List<HTMLObject> getByData(String data) 	 | returns a List of inner object with a string as data
	JSONObject	 toJSONObject()			 | returns the HTMLObject as JSONObject
	