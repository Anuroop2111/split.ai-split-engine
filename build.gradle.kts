plugins {
  kotlin("multiplatform") version "2.0.21"
  `maven-publish`
  id("dev.petuska.npm.publish") version "3.4.1"
  kotlin("jvm")
}

group = "io.split"
version = "0.1.0"

kotlin {
  jvm()
  js(IR) {
    browser()
    nodejs()
    binaries.library()
    generateTypeScriptDefinitions()
  }
  sourceSets {
    val commonMain by getting
    val commonTest by getting
    val jvmMain by getting
    val jvmTest by getting
    val jsMain by getting
    val jsTest by getting
  }
  jvmToolchain(8)
}

// Publishing to Maven
publishing {
  publications.withType<MavenPublication> {
    pom {
      name.set("split-engine")
      description.set("Deterministic expense split engine")
    }
  }
  repositories {
    mavenLocal() // dev convenience
  }
}

// Publishing to NPM
npmPublishing {
  publications {
    publication("js") {
      packageJson {
        name.set("@anuroop/split-engine")
        version.set(project.version.toString())
        description.set("Deterministic expense split engine")
        main.set("index.js")
        types.set("index.d.ts")
        license.set("MIT")
      }
    }
  }

}
dependencies {
  implementation(kotlin("stdlib-jdk8"))
}
repositories {
  mavenCentral()
}