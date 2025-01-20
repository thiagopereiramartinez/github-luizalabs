import br.dev.thiagopereira.luizalabs.JacocoReportsExtension

plugins {
    id("jacoco")
}

jacoco {
    toolVersion = "0.8.12"
}

tasks.withType(Test::class.java) {
    extensions.configure<JacocoTaskExtension> {
        isIncludeNoLocationClasses = true
        excludes = listOf("jdk.internal.*")
    }
}

val fileFilter = setOf(
    "**/R.class",
    "**/R\$*.class",
    "**/proto/**",
    "**/ui/**",
    "**/di/**",
    "**/hilt_aggregated_deps/**",
    "**/*Test*.*",
)

project.extensions.create<JacocoReportsExtension>("jacoco-reports")

project.afterEvaluate {

    val buildTypes = listOf("debug")

    buildTypes.forEach { buildType ->

        val taskName = "jacocoTest${
            buildType
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        }Report"

        tasks.register<JacocoReport>(taskName) {
            dependsOn("test${
                buildType
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
            }UnitTest")
            group = "coverage"

            reports {
                xml.required.set(true)
                html.required.set(true)
            }

            val ext = project.extensions.getByType<JacocoReportsExtension>()

            val classTree = files (
                fileTree("${project.layout.buildDirectory.get()}/tmp/kotlin-classes/${buildType}") {
                    exclude(fileFilter)
                    exclude(ext.fileFilter)
                },
                fileTree("${project.layout.buildDirectory.get()}/intermediates/javac/${buildType}/classes") {
                    exclude(fileFilter)
                    exclude(ext.fileFilter)
                }
            )

            val executationTree = fileTree("${project.layout.buildDirectory.get()}") {
                include(
                    "jacoco/test${
                        buildType
                            .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                    }UnitTest.exec",
                    "outputs/code-coverage/connected/*.ec",
                    "outputs/code_coverage/${buildType}AndroidTest/connected/*coverage.ec",
                )
            }

            sourceDirectories.setFrom(files(
                "${project.projectDir}/src/main/java",
                "${project.projectDir}/src/main/kotlin",
                "${project.projectDir}/src/main/kotlin+java"
            ))
            classDirectories.setFrom(classTree)
            executionData.setFrom(executationTree)

            doLast {
                val reportDir = jacoco.reportsDirectory.get()
                val html = "${reportDir}/${taskName}/html/index.html"

                logger.quiet("=================== JACOCO REPORT ===================")
                logger.quiet(html)
                logger.quiet("=====================================================")
            }
        }
    }

}