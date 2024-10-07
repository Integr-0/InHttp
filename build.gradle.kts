/*
 * Copyright © 2024 Integr
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

repositories {
    mavenCentral()
    gradlePluginPortal()
}

plugins {
    kotlin("jvm") version "1.9.23"
    id("tech.yanand.maven-central-publish") version "1.2.0"
    `maven-publish`
    `java-library`
    signing

}

group = "io.github.integr-0"
version = "1.0.0"

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
}

java {
    withJavadocJar()
    withSourcesJar()
}

kotlin {
    jvmToolchain(17)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "in-http"
            from(components["java"])
            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }

                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
            pom {
                name = "InHttp"
                description = "Simple but effective HTTP Client for Kotlin"
                url = "https://github.com/Integr-0/InHttp"
                licenses {
                    license {
                        name = "The Apache License, Version 2.0"
                        url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                    }
                }
                developers {
                    developer {
                        id = "integr"
                        name = "Integr"
                        email = "-"
                    }
                }
                scm {
                    connection = "scm:git:git://github.com/Integr-0/InHttp.git"
                    developerConnection = "scm:git:ssh://github.com/Integr-0/InHttp.git"
                    url = "https://github.com/Integr-0/InHttp"
                }
            }
        }
    }

    repositories {
        maven {
            name = "Local"
            url = uri(layout.buildDirectory.dir("repos/bundles/$version"))
        }
    }
}

signing {
    sign(publishing.publications["mavenJava"])
}

mavenCentral {
    repoDir.set(layout.buildDirectory.dir("repos/bundles/$version"))
    val sonatypeToken = project.findProperty("sonatypeToken") as String?
    authToken.set(sonatypeToken)
    publishingType.set("AUTOMATIC")
    maxWait = 120
}