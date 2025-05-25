package myiconpack

import MyIconPack
import androidx.compose.ui.graphics.vector.ImageVector
import kotlin.String
import kotlin.collections.List as ____KtList
import kotlin.collections.Map as ____KtMap

public object DocumentsGroup

public val MyIconPack.Documents: DocumentsGroup
  get() = DocumentsGroup

public val DocumentsGroup.groupName: String
  get() = "documents"

private var __AllIcons: ____KtList<ImageVector>? = null

public val DocumentsGroup.AllIcons: ____KtList<ImageVector>
  get() {
    if (__AllIcons != null) {
      return __AllIcons!!
    }
    __AllIcons= listOf()
    return __AllIcons!!
  }

private var __AllIconsNamed: ____KtMap<String, ImageVector>? = null

public val DocumentsGroup.AllIconsNamed: ____KtMap<String, ImageVector>
  get() {
    if (__AllIconsNamed != null) {
      return __AllIconsNamed!!
    }
    __AllIconsNamed= mapOf()
    return __AllIconsNamed!!
  }
