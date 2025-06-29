FROM amazoncorretto:17
 
COPY target/spring-sse-server-1.0.0.jar /app/app.jar
COPY entrypoint.sh ./entrypoint.sh
COPY monitoring	monitoring

RUN chmod g+rx ./entrypoint.sh \
	&& chmod g+rx /app/app.jar	

EXPOSE 8080


CMD [ "./entrypoint.sh" ]