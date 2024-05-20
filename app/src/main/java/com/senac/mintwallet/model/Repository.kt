package com.senac.mintwallet.model

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

class Repository {
    private var user = FirebaseAuth.getInstance().currentUser

    public fun create(name: String?, email: String?) {
        val authenticatedUser = FirebaseAuth.getInstance().currentUser
        authenticatedUser?.run {
            val userIdReference = Firebase.database.reference
                .child("users").child(uid)
            val userData = UserEntity(
                name = name,
                email = email,
                transfers = null
            )
            userIdReference.setValue(userData)
        }
        this.user = authenticatedUser
    }

    public fun createNewTransfer(transferEntity: TransferEntity): TransferEntity {
        val databaseReference: DatabaseReference = FirebaseDatabase
            .getInstance()
            .getReference("users")
            .child(user!!.uid)
            .child("transfers")

        val transfer = databaseReference.push()
        transfer.setValue(transferEntity)

        this.user = FirebaseAuth.getInstance().currentUser
        transferEntity.uuid = transfer.key

        return transferEntity
    }

}