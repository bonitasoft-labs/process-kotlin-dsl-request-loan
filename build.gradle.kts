import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.1.4.RELEASE"
    id("org.jetbrains.kotlin.jvm") version "1.3.21"
    id("org.jetbrains.kotlin.plugin.spring") version "1.3.21"
}

repositories {
    mavenLocal()
    jcenter()
    maven("http://repositories.rd.lan/maven/all/")
}

dependencies {
    implementation("org.bonitasoft.engine.dsl:process-kotlin-dsl:0.0.1")
    implementation("org.bonitasoft.engine:bonita-engine-spring-boot-starter:0.0.1")
    implementation("org.bonitasoft.connectors:bonita-connector-email-dsl:6.1.0")
    implementation("org.springframework.boot:spring-boot-starter-web:2.1.4.RELEASE")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.github.microutils:kotlin-logging:1.6.24")

    testImplementation("org.springframework.boot:spring-boot-starter-test:2.1.4.RELEASE")
    testImplementation("junit:junit:4.12")
    testImplementation("org.assertj:assertj-core:3.12.2")
}


tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.freeCompilerArgs = listOf("-Xjsr305=strict")
}