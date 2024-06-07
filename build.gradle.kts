// Gradle script to build the Macana project

import de.undercouch.gradle.tasks.download.Download
import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform

plugins {
    application // to build JVM applications
    alias(libs.plugins.download) // to retrieve files from URLs
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

// which BTF (buildType + flavor) of the native physics library to copy:
val btf = "ReleaseSp"
//val btf = "DebugSp"

val fs = System.getProperty("file.separator")
val downloadsDir = System.getProperty("user.home") + fs + "Downloads" + fs

val lbjVersion = libs.versions.libbulletjme.get()

// URL from which native physics libraries should be copied:
val libbulletjmeUrl = "https://github.com/stephengold/Libbulletjme/releases/download/$lbjVersion/"
//val libbulletjmeUrl = "file:///home/sgold/NetBeansProjects/Libbulletjme/dist/"

tasks.withType<JavaCompile>().all { // Java compile-time options:
    options.compilerArgs.add("-Xdiags:verbose")
    options.compilerArgs.add("-Xlint:unchecked")
    options.setDeprecation(true) // to provide detailed deprecation warnings
    options.encoding = "UTF-8"
}

// Register tasks to run specific applications:

tasks.register<JavaExec>("HelloObsidian") {
    description = "Runs the HelloObsidian app."
    mainClass = "com.github.stephengold.macana.HelloObsidian"
}
tasks.register<Exec>("rdHelloObsidian") {
    commandLine(
            "/usr/share/renderdoc_1.31/bin/renderdoccmd",
            "capture",
            "--wait-for-exit",
            "--working-dir", ".",
            "${System.getProperty("java.home")}/bin/java",
            "-classpath", sourceSets.main.get().getRuntimeClasspath().asPath,
            "com.github.stephengold.macana.HelloObsidian"
            )

    dependsOn("classes", "downloadNatives")
    description = "Runs HelloObsidian with a renderDoc overlay."
}

val os = DefaultNativePlatform.getCurrentOperatingSystem()

tasks.withType<JavaExec>().all { // Java runtime options:
    if (os.isMacOsX()) {
        jvmArgs("-XstartOnFirstThread") // required for GLFW on macOS
    }
    classpath = sourceSets.main.get().getRuntimeClasspath()
    dependsOn("downloadNatives")
    enableAssertions = true
}

val includeLinux = os.isLinux()
val includeMacOsX = os.isMacOsX()
val includeWindows = os.isWindows()
tasks.register("downloadNatives") {
    if (includeLinux) {
        dependsOn("downloadLinux64")
    }
    if (includeMacOsX) {
        dependsOn("downloadMacOSX64")
        dependsOn("downloadMacOSX_ARM64")
    }
    if (includeWindows) {
        dependsOn("downloadWindows64")
    }
}

application {
    mainClass = "com.github.stephengold.macana.HelloObsidian"
}

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(0, "seconds") // to disable caching of snapshots
}

dependencies {
    implementation(libs.sport)
    implementation(libs.libbulletjme)

    implementation(libs.obsidian)
    implementation(libs.skija.shared)

    implementation(libs.joml)
    implementation(libs.lwjgl)
    implementation(libs.lwjgl.assimp)
    implementation(libs.lwjgl.glfw)
    implementation(libs.lwjgl.opengl)
    implementation(libs.lwjgl.yoga)

    implementation(platform(libs.lwjgl.bom))

    if (includeLinux) {
        runtimeOnly(libs.skija.linux.x64)
        runtimeOnly(variantOf(libs.lwjgl){classifier("natives-linux")})
        runtimeOnly(variantOf(libs.lwjgl.assimp){classifier("natives-linux")})
        runtimeOnly(variantOf(libs.lwjgl.glfw){classifier("natives-linux")})
        runtimeOnly(variantOf(libs.lwjgl.opengl){classifier("natives-linux")})
        runtimeOnly(variantOf(libs.lwjgl.yoga){classifier("natives-linux")})
    }

    if (includeMacOsX) {
        runtimeOnly(libs.skija.macos.x64)
        runtimeOnly(variantOf(libs.lwjgl){classifier("natives-macos")})
        runtimeOnly(variantOf(libs.lwjgl.assimp){classifier("natives-macos")})
        runtimeOnly(variantOf(libs.lwjgl.glfw){classifier("natives-macos")})
        runtimeOnly(variantOf(libs.lwjgl.opengl){classifier("natives-macos")})
        runtimeOnly(variantOf(libs.lwjgl.yoga){classifier("natives-macos")})

        runtimeOnly(libs.skija.macos.arm64)
        runtimeOnly(variantOf(libs.lwjgl){classifier("natives-macos-arm64")})
        runtimeOnly(variantOf(libs.lwjgl.assimp){classifier("natives-macos-arm64")})
        runtimeOnly(variantOf(libs.lwjgl.glfw){classifier("natives-macos-arm64")})
        runtimeOnly(variantOf(libs.lwjgl.opengl){classifier("natives-macos-arm64")})
        runtimeOnly(variantOf(libs.lwjgl.yoga){classifier("natives-macos-arm64")})
    }

    if (includeWindows) {
        runtimeOnly(libs.skija.windows.x64)
        runtimeOnly(variantOf(libs.lwjgl){classifier("natives-windows")})
        runtimeOnly(variantOf(libs.lwjgl.assimp){classifier("natives-windows")})
        runtimeOnly(variantOf(libs.lwjgl.glfw){classifier("natives-windows")})
        runtimeOnly(variantOf(libs.lwjgl.opengl){classifier("natives-windows")})
        runtimeOnly(variantOf(libs.lwjgl.yoga){classifier("natives-windows")})
    }
}

// Register tasks to download/clean the native physics library for each platform:

registerPlatformTasks("Linux64",      "_libbulletjme.so")
registerPlatformTasks("MacOSX64",     "_libbulletjme.dylib")
registerPlatformTasks("MacOSX_ARM64", "_libbulletjme.dylib")
registerPlatformTasks("Windows64",    "_bulletjme.dll")

// helper method to register 'download' and 'clean' tasks:

fun registerPlatformTasks(platform : String, suffix : String) {
    val cleanTaskName = "clean" + platform
    val filename = platform + btf + suffix
    val filepath = "" + downloadsDir + filename

    tasks.named("clean") {
        dependsOn(cleanTaskName)
    }

    tasks.register<Delete>(cleanTaskName) {
        delete(filepath)
    }

    tasks.register<Download>("download" + platform) {
        src(libbulletjmeUrl + filename)
        dest(filepath)
        overwrite(false)
    }
}
