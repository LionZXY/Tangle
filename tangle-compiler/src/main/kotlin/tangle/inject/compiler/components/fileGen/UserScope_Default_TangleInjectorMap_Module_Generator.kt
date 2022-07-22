/*
 * Copyright (C) 2022 Rick Busarow
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

package tangle.inject.compiler.components.fileGen

import com.squareup.anvil.compiler.api.GeneratedFile
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import tangle.inject.compiler.ClassNames
import tangle.inject.compiler.FileGenerator
import tangle.inject.compiler.addContributesTo
import tangle.inject.compiler.addFunction
import tangle.inject.compiler.buildFile
import tangle.inject.compiler.components.MergeComponentParams
import tangle.inject.compiler.generateSimpleNameString
import tangle.inject.compiler.tangleInjectorMap
import java.io.File

/**
 * ```
 * @Module
 * @ContributesTo(UserScope::class)
 * public interface Default_TangleInjectorMap_Module {
 *   @Multibinds
 *   public fun bindTangleInjectorMap(): Map<Class<*>, @JvmSuppressWildcards TangleInjector<*>>
 * }
 * ```
 */
internal object UserScope_Default_TangleInjectorMap_Module_Generator : FileGenerator<MergeComponentParams> {
  override fun generate(
    codeGenDir: File,
    params: MergeComponentParams
  ): GeneratedFile {

    val packageName = params.packageName

    val scopeName = params.scopeClassName.generateSimpleNameString()

    val classNameString = "${scopeName}_Default_TangleInjectorMap_Module"
    val className = ClassName(packageName, classNameString)

    val content = FileSpec.buildFile(packageName, classNameString) {
      TypeSpec.interfaceBuilder(className)
        .addAnnotation(ClassNames.module)
        .addContributesTo(params.scopeClassName)
        .addFunction("bindTangleInjectorMap") {
          addAnnotation(ClassNames.multibinds)
          addModifiers(KModifier.ABSTRACT)
          returns(ClassNames.tangleInjectorMap)
        }
        .build()
        .let { addType(it) }
    }

    return createGeneratedFile(codeGenDir, packageName, classNameString, content)
  }
}
