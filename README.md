## Goal

The goal of this project is to provide a parseMessage API that parses a string and return a list of emoticons, mentions and links in the text.

## How to Run

This project is created in Intellij using Java 8, Maven and Tomcat.
I used the Jersey framework to implement the REST API in Java.
To run it simply import the project (HipChatService) into Intellij and run the tomcat plugin.

To make requests to the API you can make a GET request to the following URL :
http://localhost:8080/newjersey/hipchat/parse/message/

Include the query parameter *message*.

Sample Request:
```
http://localhost:8080/newjersey/hipchat/parse/message/?message=%40kaivan+%40wadia+Check+out+this+%28emoticon1%29+and+%28emoticon2%29+..+You+can+create+similar+ones+according+to+https%3A%2F%2Fwww.hipchat.com%2Femoticons+Check+out+another+link+here+https%3A%2F%2Fwww.google.com
```

You can use the [Postman Chrome Extension](https://chrome.google.com/webstore/detail/postman/fhbjgbiflinjbdggehcddcbncdddomop?hl=en) to make GET requests and encode the URL correctly.

The package HipChatServiceIntegTests contains integration tests for the service. You will have to run the service and then run the Integ tests package to test the service.

## Things to do

The following are things that can be done to make this API better:

1. Change the URL matching from relying on the Java URL class to a reliable regular expression
2. Investigate performance difference between regex matching on the whole string 3 times vs breaking up the string into words and checking individual words for a mention, emoticon, or link.
    1. Consider cases where one word might actually contain two or more emoticons.
3. Do load testing to see how many concurrent requests can be made without performance drop.
