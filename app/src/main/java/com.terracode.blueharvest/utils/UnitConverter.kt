package com.terracode.blueharvest.utils

class UnitConverter {
    companion object {
        fun convertToMetric(currentValue: Double?): Double? {
            val metricToImperial = Constants.MetricImperialConversionConstant.value
            if (currentValue != null) {
                return currentValue / metricToImperial
            } else {
                return null
            }
        }

        fun convertToImperial(currentValue: Double?): Double? {
            val metricToImperial = Constants.MetricImperialConversionConstant.value
            if (currentValue != null) {
                return currentValue * metricToImperial
            } else {
                return null
            }
        }
    }
}
