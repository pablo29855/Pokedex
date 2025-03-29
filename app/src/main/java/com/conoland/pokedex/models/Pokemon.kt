package com.conoland.pokedex.models

class Pokemon {
    var number: Int = 0
        get() {
            if (url != null) {
                val urlPartes =
                    url!!.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                return urlPartes[urlPartes.size - 1].toInt()
            }

            return id!!.toInt()
        }
    @JvmField
    var name: String? = null
    var url: String? = null
    @JvmField
    var weight: String? = null
    private val id: String? = null
}
