package com.example.frndassignment.domain.servermodels

data class DateModel(
    var id: String? = null,
    var day: Int? = null,
    var month: Int? = null,
    var year: Int? = null,
    var isSelected: Boolean? = null
) {

    override fun equals(other: Any?): Boolean {
        if (other?.javaClass != this.javaClass) {
            return false
        }
        val date = other as DateModel
        return date.id == id
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}
