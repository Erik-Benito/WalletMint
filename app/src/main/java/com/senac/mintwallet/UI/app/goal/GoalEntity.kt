package com.senac.mintwallet.UI.app.goal

data class GoalEntity(
    val name: String,
    val iconResourceId: Int,
    var isSelected: Boolean = false
)
