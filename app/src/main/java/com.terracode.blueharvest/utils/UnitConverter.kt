package com.terracode.blueharvest.utils



class UnitConverter {
    companion object {
        fun convertHeightToImperial(currentValue: Double?): Double? {
            val heightToImperial = Constants.heightToImperialConstant.value
            if (currentValue != null) {
                return "%.2f".format(currentValue * heightToImperial).toDouble()
            } else {
                return null
            }
        }

        fun convertSpeedToImperial(currentValue: Double?): Double? {
            val speedToImperial = Constants.SpeedToImperialConstant.value
            if (currentValue != null) {
                return "%.2f".format(currentValue * speedToImperial).toDouble()
            } else {
                return null
            }
        }
    }
}
