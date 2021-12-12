package com.step2hell.bytecode

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import java.io.File
import java.io.IOException

class BytecodeTransform constructor(private val extension: BytecodeExtension) : Transform() {
    override fun getName() = "BytecodeTransform"

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> =
        TransformManager.CONTENT_CLASS

    /**
     * 指Transform要操作内容的范围，官方文档Scope有7种类型：
     * <p>
     * EXTERNAL_LIBRARIES        只有外部库
     * PROJECT                   只有项目内容
     * PROJECT_LOCAL_DEPS        只有项目的本地依赖(本地jar)
     * PROVIDED_ONLY             只提供本地或远程依赖项
     * SUB_PROJECTS              只有子项目
     * SUB_PROJECTS_LOCAL_DEPS   只有子项目的本地依赖项(本地jar)
     * TESTED_CODE               由当前变量(包括依赖项)测试的代码
     * SCOPE_FULL_PROJECT        整个项目
     */
    override fun getScopes(): MutableSet<in QualifiedContent.Scope> =
        TransformManager.SCOPE_FULL_PROJECT

    override fun isIncremental() = true

    @Throws(TransformException::class, InterruptedException::class, IOException::class)
    override fun transform(invocation: TransformInvocation) {
        super.transform(invocation)
        println("====> transform: extension=$extension")
        println("====> transform: isIncremental=$isIncremental invocation.isIncremental=${invocation.isIncremental}")
        if (!invocation.isIncremental) invocation.outputProvider.deleteAll()
        invocation.inputs.forEach { transformInput ->
            transformInput.jarInputs.forEach { jarInput ->
                onJarInput(jarInput, invocation.outputProvider, invocation.isIncremental)
            }
            transformInput.directoryInputs.forEach { dirInput ->
                onDirInput(dirInput, invocation.outputProvider, invocation.isIncremental)
            }
        }
    }

    private fun onJarInput(
        jarInput: JarInput,
        outputProvider: TransformOutputProvider,
        incremental: Boolean
    ) {
        val dst = outputProvider.getContentLocation(
            jarInput.name,
            jarInput.contentTypes,
            jarInput.scopes,
            Format.JAR
        )
        println("\n====> onJarInput src=${jarInput.file.absolutePath} dst=$dst incremental=$incremental status=${jarInput.status}")
        if (incremental) {
            when (jarInput.status) {
                Status.NOTCHANGED -> {
                }
                Status.REMOVED -> if (dst.exists()) FileUtils.delete(dst)
                Status.ADDED -> doTransform(jarInput.file, dst)
                Status.CHANGED -> {
                    if (dst.exists()) FileUtils.delete(dst)
                    doTransform(jarInput.file, dst)
                }
            }
        } else {
            doTransform(jarInput.file, dst)
        }
    }

    private fun onDirInput(
        dirInput: DirectoryInput,
        outputProvider: TransformOutputProvider,
        incremental: Boolean
    ) {
        val dst = outputProvider.getContentLocation(
            dirInput.name,
            dirInput.contentTypes,
            dirInput.scopes,
            Format.DIRECTORY
        )
        println("\n====> onDirInput dst=$dst incremental=$incremental")
        if (incremental) {
            val srcDirPath = dirInput.file.absolutePath
            val dstDirPath = dst.absolutePath
            dirInput.changedFiles.forEach { (srcFile, status) ->
                val dstFile = File(srcFile.absolutePath.replace(srcDirPath, dstDirPath))
                println("\n====> onDirInput status=$status srcDirPath=$srcDirPath dstDirPath=$dstDirPath dstFile=${dstFile.absolutePath}")
                when (status) {
                    Status.NOTCHANGED -> {
                    }
                    Status.REMOVED -> if (dstFile.exists()) FileUtils.delete(dstFile)
                    Status.ADDED -> doTransform(srcFile, dstFile)
                    Status.CHANGED -> {
                        if (dstFile.exists()) FileUtils.delete(dstFile)
                        doTransform(srcFile, dstFile)
                    }
                }
            }
        } else {
            doTransform(dirInput.file, dst)
        }
    }

    private fun doTransform(src: File, dst: File) {
        if (src.isDirectory) {
            src.walk().forEach { file ->
                val name = file.name
                println("====> transformFile name=$name")
//                if (name.endsWith(".class") && !name.startsWith("R\$")
//                    && name != "R.class" && name != "BuildConfig.class"
//                ) {
//                    val classReader = ClassReader(file.readBytes())
//                    val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
//                    val className = name.split(".class")[0]
//                    val classVisitor = TraceVisitor(className, classWriter)
//                    classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
//                    val code = classWriter.toByteArray()
//                    val fos =
//                        FileOutputStream(file.parentFile.absoluteFile.toString() + File.separator + name)
//                    fos.write(code)
//                    fos.flush()
//                    fos.close()
//                }
            }
            FileUtils.copyDirectory(src, dst)
        } else {
            FileUtils.copyFile(src, dst)
        }
    }
}
