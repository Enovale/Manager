package com.aliucord.manager.installer.steps.patch

import androidx.collection.mutableObjectListOf
import com.aliucord.manager.R
import com.aliucord.manager.installer.steps.StepGroup
import com.aliucord.manager.installer.steps.StepRunner
import com.aliucord.manager.installer.steps.base.Step
import com.aliucord.manager.installer.steps.download.*
import com.github.diamondminer88.zip.*
import org.koin.core.component.KoinComponent

class UncompressDexesStep : Step(), KoinComponent {
    override val group = StepGroup.Patch
    override val localizedName = R.string.install_step_uncompress_dexes

    override suspend fun execute(container: StepRunner) {
        val apk = container.getStep<CopyDependenciesStep>().patchedApk

        val (dexCount, dexes) = ZipReader(apk).use {
            val dexes = mutableObjectListOf<ByteArray>()
            val dexCount = it.entryNames.count { name -> name.endsWith(".dex") }
            for (i: Int in 1..dexCount) {
                val entry = it.openEntry("classes${(if (i == 1) "" else i)}.dex")?.read()
                if (entry != null) {
                    dexes.add(entry)
                }
            }

            Pair(dexCount, dexes)
        }

        ZipWriter(apk, /* append = */ true).use {
            for (i: Int in 1..dexCount) {
                it.deleteEntry("classes${(if (i == 1) "" else i)}.dex");
            }

            for (i: Int in 1..dexCount) {
                it.writeEntry("classes${(if (i == 1) "" else i)}.dex", dexes[i - 1], ZipCompression.NONE, 4096)
            }
        }
    }
}
