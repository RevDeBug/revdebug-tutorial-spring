# RevDeBug recording of a Java Spring Boot application demo
The following application is an example spring boot application showing the tracing and code execution recording of RevDeBug.
A prerequisite for this follow along tutorial is a working RevDeBug Server instance running, please refer to user manual at: <https://revdebug.gitbook.io/revdebug/installing-revdebug-server#setting-up-revdebug-server-instance>
## Clone the project (Linux/MacOS X machine with Docker installed recommended)

```
git clone https://github.com/RevDeBug/revdebug-tutorial-spring
cd revdebug-tutorial-spring
```

## Add RevDeBug dependencies in pom.xml

```
		<!-- RevDeBug -->
		<dependency>
			<groupId>com.revdebug</groupId>
			<artifactId>compiler</artifactId>
			<version>6.0.19</version>
		</dependency>
		<dependency>
			<groupId>com.revdebug</groupId>
			<artifactId>RevDeBug</artifactId>
			<version>6.0.19</version>
		</dependency>
```

Dependency version will be updated however you can check if theres never version on https://nexus.revdebug.com/#browse/browse:maven:com%2Frevdebug%2FRevDeBug

## Add RevDeBug configuration in maven-compiler-plugin

```
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-compiler-plugin</artifactId>
				<version>3.8.1</version>
				<configuration>
					<compilerArgs>
						<arg>-ArecordServerAddress=${env.REVDEBUG_RECORD_SERVER_ADDRESS}</arg>
						<arg>-ArecordServerPort=42734</arg>
						<arg>-ArecordingMode=OnEvent</arg>
						<arg>-AapplicationName=InvoiceJava</arg>
						<arg>-AsolutionName=InvoiceJava</arg>
						<arg>-ArepositoryPath=target/Metadata</arg>
						<arg>-AuploadMetadata=true</arg>
						<arg>-AreleaseId=${git.commit.id}</arg>
					</compilerArgs>
					<source>1.8</source>
					<target>1.8</target>
				</configuration>
			</plugin>
```
Change -ArecordServerAddress to point to your devops monitor instance (just ip address or hostname without protocol)

Remember, if you already have this plugin defined for some reason in your application just provide the compilerArgs section

## Download and configure RevDeBug agent

### Download https://nexus.revdebug.com/repository/files/agent/revdebug-agent-6.0.15.tar.gz

### Extract it in location convenient for you (you will the use it on application startup)

### Modify config/agent.config

Change agent.service_name=${SW_AGENT_NAME:Your_ApplicationName} on line 21 e.g.
agent.service_name=${SW_AGENT_NAME:revdebug-tutorial-spring}

Change collector.backend_service=${SW_AGENT_COLLECTOR_BACKEND_SERVICES:127.0.0.1:11800} on line 74 to point to your devops monitor instance

## Build application

```
\revdebug-tutorial-spring> ./mvnw clean package
```

## run application

```
\revdebug-tutorial-spring\target>  java -javaagent:.\[path-to-agent]\skywalking-agent.jar -jar .\revdebug-tutorial-spring-0.0.1-SNAPSHOT.jar
```

Change path-to-agent to the directory you extracted agent to  

## Use the application to cause its error

Afterwards the application will be accessible through a web browser at: <http://localhost:8080/>

Navigate to Invoices web page.

![Demo application web page](./demo_screens_java/web-app.png)


Select the invoice that came from: Exceptional LLC, access the details of the invoice and press the "Reconcile" link.

![Demo application web page](./demo_screens_java/web-app-details.png)

It will result with an error - you may see a "blank" or an error 500 web page at this time.

# Use RevDeBug to access code execution recording

## Find the errorneous trace
Next, you may switch to a RevDeBug server web interface and navigate to the “Trace” tab, where you may filter the traces to show only errors and limit to application “InvoiceJava”. 

![Demo application web page](./demo_screens_java/trace.png)

There you’ll find the trace for the caused error and you can access the recording of code execution. Accessing the recording for the first time will ask to configure the source code repository address.

## Connect code repository (one time action)

Follow the “connect to repository” option and fill the “Repository address” to github repository location of  ```https://github.com/RevDeBug/revdebug-tutorial-spring``` and press “Save”.

![Demo application web page](./demo_screens_java/repository.png)

## Access the code execution for the error
Closing the inner window and reopening the code recording will present the recording on the source code and allow you to move around the recording timeline using the arrow buttons at top left.

![Demo application web page](./demo_screens_java/code-recording.png)

