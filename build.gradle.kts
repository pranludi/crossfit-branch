import com.google.protobuf.gradle.id

plugins {
  java
  id("org.springframework.boot") version "3.4.4"
  id("io.spring.dependency-management") version "1.1.7"
  id("com.google.protobuf") version "0.9.4"
}

group = "io.pranludi.crossfit"
version = "0.0.1-SNAPSHOT"

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(21)
  }
}

configurations {
  compileOnly {
    extendsFrom(configurations.annotationProcessor.get())
  }
}

repositories {
  mavenCentral()
}

extra["springGrpcVersion"] = "0.6.0"

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("io.grpc:grpc-services")
  implementation("org.springframework.grpc:spring-grpc-spring-boot-starter")
  implementation("org.springframework.kafka:spring-kafka")
  runtimeOnly("com.h2database:h2") // local
  // runtimeOnly("org.postgresql:postgresql") // qa, prod env

  // mapstruct
  implementation("org.mapstruct:mapstruct:1.5.5.Final")
  annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")

  // jwt
  implementation("io.jsonwebtoken:jjwt-api:0.12.6")
  runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
  runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.springframework.grpc:spring-grpc-test")
  testImplementation("org.springframework.kafka:spring-kafka-test")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
  imports {
    mavenBom("org.springframework.grpc:spring-grpc-dependencies:${property("springGrpcVersion")}")
  }
}

protobuf {
  protoc {
    artifact = "com.google.protobuf:protoc"
  }
  plugins {
    id("grpc") {
      artifact = "io.grpc:protoc-gen-grpc-java"
    }
  }
  generateProtoTasks {
    all().forEach {
      it.plugins {
        id("grpc") {
          option("jakarta_omit")
          option("@generated=omit")
        }
      }
    }
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}
