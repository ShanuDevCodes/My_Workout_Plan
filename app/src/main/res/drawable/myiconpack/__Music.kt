package myiconpack

import MyIconPack
import androidx.compose.ui.graphics.vector.ImageVector
import kotlin.String
import kotlin.collections.List as ____KtList
import kotlin.collections.Map as ____KtMap

public object MusicGroup

public val MyIconPack.Music: MusicGroup
  get() = MusicGroup

public val MusicGroup.groupName: String
  get() = "music"

private var __AllIcons: ____KtList<ImageVector>? = null

public val MusicGroup.AllIcons: ____KtList<ImageVector>
  get() {
    if (__AllIcons != null) {
      return __AllIcons!!
    }
    __AllIcons= listOf()
    return __AllIcons!!
  }

private var __AllIconsNamed: ____KtMap<String, ImageVector>? = null

public val MusicGroup.AllIconsNamed: ____KtMap<String, ImageVector>
  get() {
    if (__AllIconsNamed != null) {
      return __AllIconsNamed!!
    }
    __AllIconsNamed= mapOf()
    return __AllIconsNamed!!
  }
