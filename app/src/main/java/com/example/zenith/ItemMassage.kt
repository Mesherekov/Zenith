package com.example.zenith

import android.graphics.drawable.Drawable


open class ItemMassage(var textmassage: String, var ownmassage: Boolean, var key: String, var rescolor: Int, var sendImage: Drawable?, var uri: String) {
    var textistrigger: Boolean = false
    private var listener: ChangeListener? = null
    fun getTexttrigger(): Boolean {
            return textistrigger
        }
        fun setTexttrigger(value: Boolean) {
            textistrigger = value
        }
    fun getSendYourImage(): Drawable? {
        return sendImage
    }
    fun getUriImage():String{
        return uri
    }
    fun setUriImage(uri: String){
        this.uri = uri
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
    fun setResColor(rescol: Int){
        rescolor = rescol
        listener?.onChange()
    }
    fun getColorrelat(): Int{
        return rescolor
    }
    open fun getListener(): ChangeListener? {
        return listener
    }

    open fun setListener(listener: ChangeListener?) {
        this.listener = listener
    }
    interface ChangeListener {
        fun onChange()
    }

}