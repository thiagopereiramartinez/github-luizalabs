/**
 * Precompiled [jacoco-reports.gradle.kts][Jacoco_reports_gradle] script plugin.
 *
 * @see Jacoco_reports_gradle
 */
public
class JacocoReportsPlugin : org.gradle.api.Plugin<org.gradle.api.Project> {
    override fun apply(target: org.gradle.api.Project) {
        try {
            Class
                .forName("Jacoco_reports_gradle")
                .getDeclaredConstructor(org.gradle.api.Project::class.java, org.gradle.api.Project::class.java)
                .newInstance(target, target)
        } catch (e: java.lang.reflect.InvocationTargetException) {
            throw e.targetException
        }
    }
}
