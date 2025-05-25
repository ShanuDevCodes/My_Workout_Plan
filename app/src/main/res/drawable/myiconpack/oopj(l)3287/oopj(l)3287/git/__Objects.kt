package myiconpack.`oopj(l)3287`.`oopj(l)3287`.git

import androidx.compose.ui.graphics.vector.ImageVector
import kotlin.String
import myiconpack.`oopj(l)3287`.`oopj(l)3287`.git.objects.AllIcons
import myiconpack.`oopj(l)3287`.`oopj(l)3287`.git.objects.AllIconsNamed
import myiconpack.`oopj(l)3287`.`oopj(l)3287`.git.objects.Info
import myiconpack.`oopj(l)3287`.`oopj(l)3287`.git.objects.Pack
import myiconpack.`oopj(l)3287`.`oopj(l)3287`.git.objects.groupName
import myiconpack.`oopj(l)3287`.`oopj(l)3287`.gitGroup
import kotlin.collections.List as ____KtList
import kotlin.collections.Map as ____KtMap

public object ObjectsGroup

public val gitGroup.Objects: ObjectsGroup
  get() = ObjectsGroup

public val ObjectsGroup.groupName: String
  get() = "objects"

private var __AllIcons: ____KtList<ImageVector>? = null

public val ObjectsGroup.AllIcons: ____KtList<ImageVector>
  get() {
    if (__AllIcons != null) {
      return __AllIcons!!
    }
    __AllIcons= Info.AllIcons + Pack.AllIcons + listOf()
    return __AllIcons!!
  }

private var __AllIconsNamed: ____KtMap<String, ImageVector>? = null

public val ObjectsGroup.AllIconsNamed: ____KtMap<String, ImageVector>
  get() {
    if (__AllIconsNamed != null) {
      return __AllIconsNamed!!
    }
    __AllIconsNamed= Info.AllIconsNamed.mapKeys { "${Info.groupName}.${it.key}"} +
        Pack.AllIconsNamed.mapKeys { "${Pack.groupName}.${it.key}"} + mapOf()
    return __AllIconsNamed!!
  }
