plugins {
	java
	id("org.springframework.boot") version "2.7.11"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
}

group = "com.sondy"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-web-services")
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")

	// https://mvnrepository.com/artifact/javax.websocket/javax.websocket-api
	//웹소켓
	compileOnly("javax.websocket:javax.websocket-api:1.1")
	// https://mvnrepository.com/artifact/com.google.code.gson/gson
	//gson
	implementation("com.google.code.gson:gson:2.10")


}

tasks.withType<Test> {
	useJUnitPlatform()
}
