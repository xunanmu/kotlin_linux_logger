@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

import com.google.common.io.Files
import org.jetbrains.kotlin.utils.addToStdlib.ifFalse
import java.net.URL

plugins {
    kotlin("multiplatform") version "1.6.10"
}

group = "me.xunanmu"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}


("datetime.klib" in mkdir("lib").list()).ifFalse {
    URL("https://gitee.com/xunanmu/klib/raw/master/linuxX64/datetime.klib").apply {
        Files.write(readBytes(), file("lib/datetime.klib"))
    }
}
kotlin {

    linuxX64("native") {
        binaries {
            executable {
                entryPoint = "main"
            }
        }
        compilations.getByName("main"){
            cinterops{
                val C by creating{
                    defFile(project.file("src/nativeInterop/cinterop/linux.def"))
                    compilerOpts("-I/path")
                    includeDirs.allHeaders("path")
                }
            }
        }
    }
    sourceSets {
        val nativeMain by getting {
            dependencies {
                implementation(fileTree("lib"))
            }
        }
        val nativeTest by getting
    }
}
