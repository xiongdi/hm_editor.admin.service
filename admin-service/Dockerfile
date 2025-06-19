FROM sfrolvlad/alpine-oraclejdk8:slim

ADD ./target/*.jar app.jar
RUN apk --update add bash && bash -c 'touch /app.jar'
CMD ["java", "-Xmx200m", "-jar", "/app.jar"]

EXPOSE 13071