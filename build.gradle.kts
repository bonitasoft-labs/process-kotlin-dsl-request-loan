import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.1.4.RELEASE"
    id("org.jetbrains.kotlin.jvm") version "1.3.21"
    id("org.jetbrains.kotlin.plugin.spring") version "1.3.21"
    id("io.spring.dependency-management") version "1.0.7.RELEASE"
}

repositories {
    mavenLocal()
    jcenter()
}

dependencies {
    implementation("com.bonitasoft.engine.dsl:process-kotlin-dsl:0.0.1")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("junit:junit:4.12")
}


tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.freeCompilerArgs = listOf("-Xjsr305=strict")
}