package com.senac.mintwallet.model

data class UserEntity(
    val name: String?,
    val email: String?,
    val transfers: List<TransferEntity?>?,
) {
    //Note: this is needed to read the data from the firebase database
    //firebase database throws this exception: UserData does not define a no-argument constructor
    constructor() : this(null, null, null)
}