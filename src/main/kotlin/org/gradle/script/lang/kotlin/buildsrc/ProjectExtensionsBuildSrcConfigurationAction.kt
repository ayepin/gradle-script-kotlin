/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.script.lang.kotlin.buildsrc

import org.gradle.api.DefaultTask
import org.gradle.api.internal.project.ProjectInternal
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

import org.gradle.initialization.buildsrc.BuildSrcProjectConfigurationAction

import org.gradle.script.lang.kotlin.*

import java.io.File

/**
 * Prepares the buildSrc project for generating Kotlin code with accessors for available project extensions.
 *
 * The available project extensions are read from the project schema file found at
 * `src/gradle-script-kotlin/resources/project-schema.json`.
 */
class ProjectExtensionsBuildSrcConfigurationAction : BuildSrcProjectConfigurationAction {

    companion object {
        const val PROJECT_SCHEMA_RESOURCE_PATH = "src/gradle-script-kotlin/resources/project-schema.json"
    }

    override fun execute(project: ProjectInternal) {
        with (project) {
            val projectSchema = file(PROJECT_SCHEMA_RESOURCE_PATH)
            if (projectSchema.exists()) {
                tasks {
                    val gskProcessBuildSrcResources by creating(ProcessBuildSrcResources::class) {
                        inputSchema = projectSchema
                        destinationDir = File(buildDir, "generated-src/gradle-script-kotlin")
                    }
                    "build" {
                        dependsOn(gskProcessBuildSrcResources)
                    }
                }
            }
        }
    }
}

open class ProcessBuildSrcResources : DefaultTask() {

    @get:InputFile
    var inputSchema: File? = null

    @get:OutputDirectory
    var destinationDir: File? = null

    @TaskAction
    fun act() {
        println("Compiling ${project.relativeProjectPath(inputSchema!!.path)} into ${project.relativeProjectPath(destinationDir!!.path)}")
    }
}
