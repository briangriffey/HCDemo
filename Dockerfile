FROM java:8-jre

ADD target/hipchatdemo-1.0.jar hipchatdemo-1.0.jar

CMD java -jar hipchatdemo-1.0.jar  server

EXPOSE 8080