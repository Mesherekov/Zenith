package com.example.zenith

open class ItemMassage(var textmassage: String, var ownmassage: Boolean, var key: String) {
    var textistrigger: Boolean = false
    fun getTexttrigger(): Boolean {
            return textistrigger
        }
        fun setTexttrigger(value: Boolean) {
            textistrigger = value
        }

    fun getTextMassage(): String {
        return textmassage
    }
    fun getOwnMassage(): Boolean{
        return ownmassage
    }
    fun setTextMassage(text: String){
        textmassage = text
    }
    fun setOwnMassage(massagebool: Boolean){
        ownmassage = massagebool
    }



}