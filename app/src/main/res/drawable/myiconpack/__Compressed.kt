package myiconpack

import MyIconPack
import androidx.compose.ui.graphics.vector.ImageVector
import kotlin.String
import kotlin.collections.List as ____KtList
import kotlin.collections.Map as ____KtMap

public object CompressedGroup

public val MyIconPack.Compressed: CompressedGroup
  get() = CompressedGroup

public val CompressedGroup.groupName: String
  get() = "compressed"

private var __AllIcons: ____KtList<ImageVector>? = null

public val CompressedGroup.AllIcons: ____KtList<ImageVector>
  get() {
    if (__AllIcons != null) {
      return __AllIcons!!
    }
    __AllIcons= listOf()
    return __AllIcons!!
  }

private var __AllIconsNamed: ____KtMap<String, ImageVector>? = null

public val CompressedGroup.AllIconsNamed: ____KtMap<String, ImageVector>
  get() {
    if (__AllIconsNamed != null) {
      return __AllIconsNamed!!
    }
    __AllIconsNamed= mapOf()
    return __AllIconsNamed!!
  }
