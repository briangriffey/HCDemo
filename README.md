# String features extraction service
This is a dropwizard application that will take the input of a string, and output an object that describes the emoticons,
mentions, links and link titles found within that text. The service consists of a single endpoint, /extractfeatures, that takes 
a POST of text/plain and returns a json object. 

An example curl is 
```
curl -X POST -H "Cache-Control: no-cache" -d 'Hey @brian how (now) (brown) (cow) http://www.twitter.com. Isn'"'"'t this just the best @tom. I think that it is (areyoukiddingme) http://www.google.com http://www.foxnews.com/index.html' "http://localhost:8080/extractfeatures"
```

With an example response of
```
{
  "mentions": [
    "brian",
    "tom"
  ],
  "emoticons": [
    "now",
    "brown",
    "cow",
    "areyoukiddingme"
  ],
  "links": [
    {
      "url": "http://www.google.com",
      "title": "Google"
    },
    {
      "url": "http://www.foxnews.com/index.html",
      "title": "Fox News - Breaking News Updates | Latest News Headlines | Photos & News Videos"
    },
    {
      "url": "http://www.twitter.com",
      "title": "Twitter - see what's happening"
    }
  ]
}
```

The response will always contain the mentions,emoticons, and links keys. If no values are found then the value for that key will
be an empty array [].

## Structure
The application is structure as a standard dropwizard project. It contains a single resource, the FeatureExtractionResource, that takes a string argument. The resource then hands the string off to a set of feature extractors. Each feature extractor is responsible with taking in a piece of information, and extracting a certain kind of information. Each feature extractor hands back an RxJava Observable, allowing any of the extractors to be composed in any number of ways, and on any set of threading constructs. 

The CompositeExtractor contains the 3 kinds of extractors needed for this exercise; the EmoticonExtractor, the MentionExtractor, and the StringHtmlTitleExtractor; which itself is a composite of the UrlHtmlTitleExtractor and the StringUrlExtractor.

The visitor pattern is then used to transform the various Extractions into the response given back to the client. This allows us to aggregate all of our Extractions into a single colletion under one interface, but still use the implementation specific convenience methods, without doing any casting to concrete types.

Gson was placed as the serialization/deserialization layer for two reasons: First is to keep our serialization logic separate from our actual objects that will be serialized. Second is to not require us to have default constructors on our objects, which keep them free to require arguments in their constructor and keep them package level protection.

## Building
The project can be build using the standard 

```
mvn package
```
command, this will compile the entire service into a single jar. To 
create a docker container with the included Dockerfile run the command 
```
docker build -t hipchatdemo/briangriffey .
```
after building with maven

## Running
The project can be ran either from the command line or as a docker container.
To run from the command line simply build with maven, using the instructions above then execute
```
java -jar target/hipchatdemo-1.0.jar server
```

Or if you have packaged the application into a docker container then run
```
docker run -d -p 8080:8080 hipchatdemo/briangriffey
```

#Deviations
1. There are some small deviations from teh isntructions below. Since no resources were present to start with the REST standard
was not used; instead a single POST was implemented.
2. Mentions were implemented slightly differently so that email addresses were not recognized as mentions. In order to be a mention
the string must start with @ and must be preceeded by either a whitespace character, or be at the beginning of a sentence

#Instructions
Here are the given instructions from the project

```
Hi [CANDIDATE],
We'd like you to complete a take-home coding exercise.  This exercise is not meant to be tricky or complex; however, it does represent a typical problem faced by the HipChat Engineering team.  Here are a few things to keep in mind as you work through it:
* The position is for a [POSITION].  As a platform team, we work in golang.  If you are not comfortable working in golang, we encourage you to code your solution using the language of your choice. We are much more interested in how you solve the problem than we are in how much of a new language you can learn in a few hours. 
* There's no time limit; take your time and write quality, production-ready code.  Treat this as if you're a member of the HipChat Engineering team and are solving it as part of your responsibilities there.
* Be thorough and take the opportunity to show the HipChat Engineering team that you've got technical chops.
* Using frameworks and libraries is acceptable. We are looking for how you would solve a problem like this on the job. If that involves bringing in libraries then do so, and even better, tell us why you made the choice.
  
When you think it's ready for prime time, push your work to a public repo on Bitbucket or Github and send us a link.
  
Now, for the coding exercise...
Please write a RESTful API that takes a chat message string as input and returns a JSON object containing information about its contents as described below.
  
Your service should parse the following data from the input:
1. mentions - A way to mention a user. Always starts with an '@' and ends when hitting a non-word character. (http://help.hipchat.com/knowledgebase/articles/64429-how-do-mentions-work-)
2. Emoticons - For this exercise, you only need to consider 'custom' emoticons which are alphanumeric strings, no longer than 15 characters, contained in parenthesis. You can assume that anything matching this format is an emoticon. (https://www.hipchat.com/emoticons)
3. Links - Any URLs contained in the message, along with the page's title.
  
The response should be a JSON object containing arrays of all matches parsed from the input string.
For example, calling your function with the following inputs should result in the corresponding return values.
Input: "@chris you around?"
Return:
{
  "mentions": [
    "chris"
  ]
}
 
Input: "Good morning! (megusta) (coffee)"
Return:
{
  "emoticons": [
    "megusta",
    "coffee"
  ]
}
 
Input: "Olympics are starting soon; http://www.nbcolympics.com"
Return:
{
  "links": [
    {
      "url": "http://www.nbcolympics.com",
      "title": "2016 Rio Olympic Games | NBC Olympics"
    }
  ]
}
 
Input: "@bob @john (success) such a cool feature; https://twitter.com/jdorfman/status/430511497475670016"
Return:
{
  "mentions": [
    "bob",
    "john"
  ],
  "emoticons": [
    "success"
  ],
  "links": [
    {
      "url": "https://twitter.com/jdorfman/status/430511497475670016",
      "title": "Justin Dorfman on Twitter: &quot;nice @littlebigdetail from @HipChat (shows hex colors when pasted in chat). http://t.co/7cI6Gjy5pq&quot;"
    }
  ]
}
 
Good luck!
```

