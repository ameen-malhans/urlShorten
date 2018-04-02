
# URL Shorten Project

## About

This is a project which produces a REST API(Spring/Hibernate/Maven/Mysql/Tomcat) that allow the users to get short URl of the given URL and analytics of the URL.
To get desired output simply enter the type of the payment. 

For example if you enter payment type physicalProduct, you will see output :generate a packing slip for shipping  in the results.

## How To Run

### To Run in Live environment
#### For Shorten URL
1) From your Postman For shorten send POST request to http://espressoprojector.com/api/{type of approach you want to use}/shortenurl where {type of approach you want to use} = google, inmemory , db.
2) Enter the URL in the body you want to get shorten and send the request you will get the shorten url in response.
3) Hit the shorten url from Step 2.
#### For Analytics
3) From your Postman For analytics send POST request to http://espressoprojector.com/api/{type of approach you want to use}/analytics where {type of approach you want to use} = google, inmemory , db.
4) Enter the Shorten Url you got from Step 2  in the body  and  send the POST request you will get analytics for that website.

### To Run Locally
1) Import the project in IDE (Eclipse/Intellij)
2) Build project using maven "mvn clean install"
3) Launch Java Application using Tomcat Server by deploying war file
#### For Shorten URL
4) From your Postman send POST request to http://localhost:8080/urlshorten/api/{type of approach you want to use}/shortenurl where {type of approach you want to use} = google, inmemory , db.
5) Enter the URL in the body you want to get shorten and send the request you will get the shorten url in response.
6) Now hit the shorten Url from step5
#### For Analytics
7)From your Postman send POST request to http://localhost:8080/urlshorten/api/{type of approach you want to use}/analytics  where {type of approach you want to use} = google, inmemory , db.
8) Enter the Shorten Url you got from Step 5  in the body  and  send the POST request you will get analytics for that website.
    
## Explanation

### 1) The task has been done in 3 Approaches:
a) Approach 1: Using google APIs:

   a.1) To shorten the url , From your Postman For shorten send POST request to http://espressoprojector.com/			api/google/shortenurl
   		You will get following response:
   
							  {
									 "kind": "urlshortener#url",
									 "id": "https://goo.gl/5Bk1ch",
									 "longUrl": "https://beginnersbook.com/2013/12/java-string-indexof-method
									            -example/"
							   }
							

   a.2) To get analytics,From your Postman For analytics send POST request to http://espressoprojector.com/api/		google/analytics
        You will get following response:
        
                               {
								 "kind": "urlshortener#url",
								 "id": "https://goo.gl/5Bk1ch",
								 "longUrl": "https://beginnersbook.com/2013/12/java-string-indexof-method-													example/",
								 "status": "OK",
								 "analytics": {
								  "allTime": {
								   "shortUrlClicks": "5",
								   "longUrlClicks": "5"
								  },
								  "month": {
								   "shortUrlClicks": "5",
								   "longUrlClicks": "5"
								  },
								  "week": {
								   "shortUrlClicks": "5",
								   "longUrlClicks": "5"
								  },
								  "day": {
								   "shortUrlClicks": "5",
								   "longUrlClicks": "5"
								  },
								  "twoHours": {
								   "shortUrlClicks": "4",
								   "longUrlClicks": "4"
								  }
								 }
						      }
  							
 b) Approach 2: Using inmemory webservices :
   
   b.1) To shorten the url , From your Postman For shorten send POST request to http://espressoprojector.com/			api/inmemory/shortenurl
		You will get following response:
		
								{
								"url": "http://espressoprojector.com/api/7e4ae64c"
								}
							
   b.2) To get analytics,From your Postman For analytics send POST request to http://espressoprojector.com/api/		inmemory/analytics
 		You will get following response:

							{
                                "count": 3
                             }
		
 
 c) Approach 3: Using db webservices means url is firstly stored in database to specific id and then shorten 					service is called and again we store shorten url to get analytics:
   
   	c.1) To shorten the url , From your Postman For shorten send POST request to http://espressoprojector.com/			 api/db/shortenurl
		 You will get following response:

							{
                               "url": "http://espressoprojector.com/api/db/g"
                             }
							
   	c.2) To get analytics,From your Postman For analytics send POST request to http://espressoprojector.com/api/		 bd/analytics
 		 You will get following response:

							{
                                "count": 3
                             }
					
### 2) The service for task is  :
  
  ### To Run in Live environment
  http://espressoprojector.com/api/{type of approach you want to use}/{task} where {type of approach you want to use} = google, inmemory , db and task = shortenurl ,analytics
 
  ### To Run Locally
  http://localhost:8080/urlshorten/api/{type of approach you want to use}/{task} where {type of approach you want to use} = google, inmemory , db and task = shortenurl ,analytics
  

### 3) Improvements :
   a) More Exception handling cases can be added.
   b) Docker can be used.
