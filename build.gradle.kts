plugins {
	java
	id("org.springframework.boot") version "3.1.4"
	id("io.spring.dependency-management") version "1.1.3"
}

group = "ru.khokhlov"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.liquibase:liquibase-core")
	implementation("org.springframework.boot:spring-boot-starter-validation:3.1.2")
	implementation("org.springframework.boot:spring-boot-starter-mail:3.1.3")
	implementation("org.springframework.cloud:spring-cloud-starter-openfeign:4.0.4")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")
	implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.1.0")
	implementation("io.jsonwebtoken:jjwt:0.9.1")
	implementation("javax.xml.bind:jaxb-api:2.4.0-b180830.0359")

	implementation("org.mapstruct:mapstruct:1.5.5.Final")
	compileOnly ("org.projectlombok:lombok:1.18.22")
	annotationProcessor ("org.projectlombok:lombok-mapstruct-binding:0.2.0")
	annotationProcessor ("org.mapstruct:mapstruct-processor:1.5.5.Final")
	annotationProcessor ("org.projectlombok:lombok:1.18.22")


	//developmentOnly("org.springframework.boot:spring-boot-docker-compose")
	runtimeOnly("org.postgresql:postgresql")



	testImplementation("org.awaitility:awaitility:3.1.2")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	//testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
