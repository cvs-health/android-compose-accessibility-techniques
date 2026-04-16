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

import com.cvshealth.a11y.agent.core.A11yDiagnostic
import com.cvshealth.a11y.agent.core.A11ySeverity
import com.cvshealth.a11y.agent.core.RuleRegistry
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.ExternalAnnotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile

class A11yExternalAnnotator : ExternalAnnotator<A11yAnnotatorInfo, List<A11yDiagnostic>>() {

    override fun collectInformation(file: PsiFile): A11yAnnotatorInfo? {
        if (file.fileType.name != "Kotlin") return null
        val virtualFile = file.virtualFile ?: return null
        return A11yAnnotatorInfo(
            fileContent = file.text,
            filePath = virtualFile.path
        )
    }

    override fun doAnnotate(info: A11yAnnotatorInfo): List<A11yDiagnostic> {
        val registry = RuleRegistry()
        return registry.analyze(info.fileContent, info.filePath)
    }

    override fun apply(file: PsiFile, diagnostics: List<A11yDiagnostic>, holder: AnnotationHolder) {
        val document: Document = file.viewProvider.document ?: return

        for (diag in diagnostics) {
            val lineIndex = diag.line - 1
            if (lineIndex < 0 || lineIndex >= document.lineCount) continue

            val lineStartOffset = document.getLineStartOffset(lineIndex)
            val lineEndOffset = document.getLineEndOffset(lineIndex)
            val textRange = TextRange(lineStartOffset, lineEndOffset)

            val severity = when (diag.severity) {
                A11ySeverity.ERROR -> HighlightSeverity.ERROR
                A11ySeverity.WARNING -> HighlightSeverity.WARNING
                A11ySeverity.INFO -> HighlightSeverity.WEAK_WARNING
            }

            val wcag = diag.wcagCriteria.joinToString(", ")

            // Short message shown on hover — fits without "toggle info"
            val message = buildString {
                append("[a11y] ${diag.message}")
                if (wcag.isNotEmpty()) append(" (WCAG $wcag)")
            }

            // Full HTML tooltip with fix suggestion shown in expanded view
            val tooltip = buildString {
                append("<html><b>[a11y]</b> ${diag.message}")
                if (wcag.isNotEmpty()) append("<br/><b>WCAG:</b> $wcag")
                if (diag.suggestion != null) append("<br/><b>Fix:</b> ${diag.suggestion}")
                append("</html>")
            }

            val builder = holder.newAnnotation(severity, message)
                .tooltip(tooltip)
                .range(textRange)

            if (diag.fix != null) {
                builder.withFix(A11yQuickFix(diag.fix!!))
            }

            builder.create()
        }
    }
}
