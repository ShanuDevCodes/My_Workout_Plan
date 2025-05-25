package myiconpack.`oopj(l)3287`.`oopj(l)3287`.git

import androidx.compose.ui.graphics.vector.ImageVector
import kotlin.String
import myiconpack.`oopj(l)3287`.`oopj(l)3287`.git.refs.AllIcons
import myiconpack.`oopj(l)3287`.`oopj(l)3287`.git.refs.AllIconsNamed
import myiconpack.`oopj(l)3287`.`oopj(l)3287`.git.refs.Heads
import myiconpack.`oopj(l)3287`.`oopj(l)3287`.git.refs.Tags
import myiconpack.`oopj(l)3287`.`oopj(l)3287`.git.refs.groupName
import myiconpack.`oopj(l)3287`.`oopj(l)3287`.gitGroup
import kotlin.collections.List as ____KtList
import kotlin.collections.Map as ____KtMap

public object RefsGroup

public val gitGroup.Refs: RefsGroup
  get() = RefsGroup

public val RefsGroup.groupName: String
  get() = "refs"

private var __AllIcons: ____KtList<ImageVector>? = null

public val RefsGroup.AllIcons: ____KtList<ImageVector>
  get() {
    if (__AllIcons != null) {
      return __AllIcons!!
    }
    __AllIcons= Heads.AllIcons + Tags.AllIcons + listOf()
    return __AllIcons!!
  }

private var __AllIconsNamed: ____KtMap<String, ImageVector>? = null

public val RefsGroup.AllIconsNamed: ____KtMap<String, ImageVector>
  get() {
    if (__AllIconsNamed != null) {
      return __AllIconsNamed!!
    }
    __AllIconsNamed= Heads.AllIconsNamed.mapKeys { "${Heads.groupName}.${it.key}"} +
        Tags.AllIconsNamed.mapKeys { "${Tags.groupName}.${it.key}"} + mapOf()
    return __AllIconsNamed!!
  }
