package cc.fastcv.koin.model

class WeatherData(val normalData: NormalData) {
    fun printData(string: String) {
        normalData.printInfo(string)
    }
}