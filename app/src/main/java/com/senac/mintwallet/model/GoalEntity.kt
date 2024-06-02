package com.senac.mintwallet.model

import java.io.Serializable

data class GoalEntity(
    var uuid: String?,
    var type: String?,
    var amountMonthly: Double?,
    var dayIndicator: String?,
    var amountTotal: Double?,
): Serializable {
    //Note: this is needed to read the data from the firebase database
    //firebase database throws this exception: UserData does not define a no-argument constructor
    constructor() : this(null, null, null, null, null)
}
