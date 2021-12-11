package com.step2hell.bytecode

import Dependencies
import Versions
import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import kotlin.reflect.full.memberProperties

class BytecodePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        createExtension(project)
        printVersions()
        registerTransform(project)
    }

    private fun createExtension(project: Project) {
        project.extensions.create("bytecode", BytecodeExtension::class.java)
    }

    private fun printVersions() {
        println("\n====> Print Versions:")
        for (prop in Versions::class.memberProperties) {
            println("${prop.name} = ${prop.get(Versions)}")
        }

        println("\n====> Print Dependencies:")
        for (prop in Dependencies::class.memberProperties) {
            println("${prop.name} = ${prop.get(Dependencies)}")
        }
    }

    private fun registerTransform(project: Project) {
        if (!project.plugins.hasPlugin(AppPlugin::class.java)) return

        val extension = project.extensions.getByType(BytecodeExtension::class.java)
        project.extensions.getByType(AppExtension::class.java)
            .registerTransform(BytecodeTransform(extension))
    }
}
