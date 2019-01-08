##E-Mail System
System allows you to send Email to N-User on Periodically or based on Configure time with Status Update Jobs. 

##System Info
1. JDK 1.8 & Maven (3.3.9)
1. Spring Boot 2.1.1.RELEASE
1. BootStap (3.3.7) With DataTable (1.10.16) and JQuery(3.1.1)
1. Active MQ (5.11.1)
1. JUnit And Mokito

##System Configurations (application.properties)
1. SMTP Configurations
2. Status Update Job Config Params
3. Email Sender Job Params
```spring.h2.console.enabled=true
   hibernate.show_sql=true
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5Dialect
   # ==============================================================
   # = Spring Security / Queries for AuthenticationManagerBuilder
   # ==============================================================
   spring.queries.users-query=select email, password,active from user where email=?
   spring.queries.roles-query=select u.email, r.role from user u inner join user_roles ur on(u.id=ur.user_id) inner join role r on(ur.roles_role_id=r.role_id) where u.email=?
   #
   # LOGGING
   #
   logging.level.org.springframework.web=ERROR
   logging.level.com.hackerrank=INFO
   # Logging pattern for the console
   logging.pattern.console="%d{yyyy-MM-dd HH:mm:ss} - %msg%n"
   # Logging pattern for file
   logging.pattern.file="%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
   logging.file=application.log
   #
   # App Settings
   #
   #CRON SCHEDULING
   app.job.status.updator.cron=0 0 1 * * ?
   app.job.non-responsie.users.batch.size=100
   app.job.inactive.users.batch.size=100
   app.job.active.users.batch.size=100
   #Email Sender Job Config
   app.job.email-sender.cron=0 0 3 * * ?
   app.job.email-sender.non-responsie.users.batch.size=100
   app.job.email-sender.non-responsie.frequency=4320
   app.job.email-sender.active.users.batch.size=100
   app.job.email-sender.active_users.frequency=1440
   
   #STATUS UPDATE JOBS
   app.status.non-responsive.mins=5760
   app.status.inactive.mins=2880
   app.status.non-responsive_to_active.mins=2880
   #EMAIL CONTENT
   app.email.title=Welcome to Email System
   app.email.body=Test Body
   app.email.from=smsarraf@gmail.com
   #EMAIL SMTP AUTHENTICATION
   spring.mail.host=smtp.gmail.com
   spring.mail.port=587
   spring.mail.username=smsarraf@gmail.com
   spring.mail.password=xxxxxxx
   spring.mail.properties.mail.smtp.auth=true
   spring.mail.properties.mail.smtp.starttls.enable=true
   
   #
   # ACTIVEMQ Configurations
   #
   spring.activemq.broker-url=tcp://localhost:61616
   spring.activemq.user=admin
   spring.activemq.password=admin```

```
##how to run
1. mvn clean package
1. Run Jar File As Java -jar email-system-0.0.1-SNAPSHOT.jar or mvn spring-boot:run

##how to Use/Test
1. Go To URL http://localhost:8080
1. Click on Registration Link http://localhost:8080/registration and do registation
1. Go Back to Login Page http://localhost:8080/login or click on Login Page
1. Login As Admin and See Reports
```
Username: admin@gmail.com
Password: admin
```

##Documents
1. UX Document : EMailSystemUX.pdf
1. Java Doc {application_folder}/doc/index.html




