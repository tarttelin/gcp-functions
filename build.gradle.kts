val invoker by configurations.creating

plugins {
    java
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

buildscript {
    repositories {
        mavenCentral()
    }
}

repositories {
    mavenCentral()
}


dependencies {
    implementation("com.google.cloud.functions:functions-framework-api:1.0.1")
}

dependencies {
    implementation(platform("com.google.cloud:libraries-bom:8.0.0"))
    implementation("com.google.cloud.functions:functions-framework-api:1.0.1")
    implementation("com.google.cloud:google-cloud-nio")
    implementation("com.google.cloud:google-cloud-pubsub")
    implementation("com.google.cloud:google-cloud-bigquery")
    implementation("com.google.cloud:google-cloud-firestore")
    compileOnly("org.projectlombok:lombok:1.18.12")
    annotationProcessor("org.projectlombok:lombok:1.18.12")

    testCompileOnly("org.projectlombok:lombok:1.18.12")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.12")
    testImplementation("org.junit.jupiter:junit-jupiter:5.6.2")
    testImplementation("org.mockito:mockito-core:3.3.3")
    testImplementation("org.assertj:assertj-core:3.15.0")

    invoker("com.google.cloud.functions.invoker:java-function-invoker:1.0.0-beta1")
}

// Run a function with the syntax ./gradlew runFunction -PrunFunction.target=com.pyruby.cloudfunc.HelloWorld -PrunFunction.port=8000
task<JavaExec>("runFunction") {
    main = "com.google.cloud.functions.invoker.runner.Invoker"
    classpath(invoker)
    inputs.files(configurations.runtimeClasspath, sourceSets["main"].output)
    args(
            "--target", project.findProperty("runFunction.target") ?: "",
            "--port", project.findProperty("runFunction.port") ?: 8080
    )
    doFirst {
        args("--classpath", files(configurations.runtimeClasspath, sourceSets["main"].output).asPath)
    }
}
