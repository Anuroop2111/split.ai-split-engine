plugins {
  kotlin("multiplatform") version "2.0.21"
}

group = "io.split"
version = "0.1.0"

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

  jvmToolchain(17)

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

/** Hard-disable all JS test tasks created by the plugin */
tasks.matching { it.name.contains("jsTest", ignoreCase = true) }.configureEach {
  enabled = false
}
