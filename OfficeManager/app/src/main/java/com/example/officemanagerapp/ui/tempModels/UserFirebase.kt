package com.example.officemanagerapp.ui.tempModels

import android.net.Uri

data class UserFirebase (
    val email: String = "example@gmail.com",
    val uid: String =" ",
    val name: String = "Алексей",
    val surname: String = "Алексеев",
    val fathername: String = "Алексеевич",
    val number: String = "89999999999",
    val cardnumber: String = "***********",
    val room: String = "421",
    val floor: String = "4",
    var photoUrl: String = "empty"
)