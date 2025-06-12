plugins {
    id("java")
    id("pmd")
    id("checkstyle")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation ("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.5")
    implementation ("org.json:json:20240303")
    implementation ("org.jfree:jfreechart:1.5.4")
}

tasks.test {
    useJUnitPlatform()
}

pmd {
    toolVersion = "6.55.0"
    isConsoleOutput = true
    ruleSets = listOf()
    ruleSetFiles = files("config/pmd/ruleset.xml")
}

checkstyle {
    toolVersion = "10.12.4"
    configFile = file("config/checkstyle/checkstyle.xml")
    isShowViolations = true
}

// NOTE: If you encounter InaccessibleObjectException with PMD on Java 17+, run Gradle with:
// export GRADLE_OPTS="--add-opens=java.base/java.io=ALL-UNNAMED --add-opens=java.base/java.lang=ALL-UNNAMED"
// before running './gradlew pmdMain'