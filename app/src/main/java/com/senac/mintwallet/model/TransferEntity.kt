package com.senac.mintwallet.model

import java.io.Serializable

data class TransferEntity(
    var uuid: String?,
    var amount: Double?,
    var describe: String?,
    var typeTransfer: ETypeTransfer?,
    var createdAt: String?,
): Serializable {
    //Note: this is needed to read the data from the firebase database
    //firebase database throws this exception: UserData does not define a no-argument constructor
    constructor() : this(null, null, null, null, null)
}
