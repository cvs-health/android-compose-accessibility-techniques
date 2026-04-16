/*
 * Copyright 2026 CVS Health and/or one of its affiliates
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cvshealth.accessibility.plugin

import com.cvshealth.a11y.agent.core.A11yFix
import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile

class A11yQuickFix(private val fix: A11yFix) : IntentionAction {

    override fun getText(): String = "[a11y] ${fix.description}"

    override fun getFamilyName(): String = "Accessibility fix"

    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean = true

    override fun startInWriteAction(): Boolean = true

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
        val document = editor?.document ?: return
        if (fix.startOffset < 0 || fix.endOffset > document.textLength ||
            fix.startOffset > fix.endOffset) return
        document.replaceString(fix.startOffset, fix.endOffset, fix.replacementText)
    }
}
