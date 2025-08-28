plugins {
    kotlin("multiplatform") version "2.0.21"
    `maven-publish`
}

group = "io.split"
version = "0.0.1"

repositories { mavenCentral() }

kotlin {
    jvm()

    js(IR) {
        nodejs {
            // We truly don't want any JS tests to run or be configured
            testTask {
                enabled = false
            }
        }
        binaries.library()
        // keep TS generation off for now (avoids typescript devDep)
        // generateTypeScriptDefinitions()
    }

    jvmToolchain(21)

    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies { implementation(kotlin("test")) }
        }

        val jvmMain by getting
        val jvmTest by getting {
            dependencies { implementation(kotlin("test-junit5")) }
        }

        val jsMain by getting
        // remove this to avoid pulling kotlin-test-js + npm chain
        // val jsTest by getting { dependencies { implementation(kotlin("test-js")) } }
    }
}

/* =========================
 * Maven publishing (local)
 * ========================= */
publishing {
    publications {
        // Publish the KMP component (metadata + jvm + js variants)
        create<MavenPublication>("mavenKotlin") {
            from(components["kotlin"])
            groupId = "io.split"
            artifactId = "split-engine"
            version = project.version.toString()

            pom {
                name.set("SplitEngine")
                description.set("Algorithms for expense splitting (Kotlin Multiplatform)")
                url.set("https://github.com/Anuroop2111/split-engine")
                licenses {
                    license {
                        name.set("Apache-2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0")
                    }
                }
                scm {
                    url.set("https://github.com/Anuroop2111/split-engine")
                }
                developers {
                    developer {
                        id.set("anuroop")
                        name.set("Anuroop L Sabu")
                    }
                }
            }
        }
    }
}

/** Hard-disable all JS test tasks created by the plugin */
tasks.matching { it.name.contains("jsTest", ignoreCase = true) }.configureEach {
    enabled = false
}
