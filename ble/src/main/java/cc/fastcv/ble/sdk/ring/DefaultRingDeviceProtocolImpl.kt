package cc.fastcv.ble.sdk.ring

import cc.fastcv.ble.sdk.OTAUpgradeListener
import java.io.File

class DefaultRingDeviceProtocolImpl(private val interaction: RingDeviceInteraction) :
    IRingDeviceProtocol {

    /**
     * 指令生成器
     */
    private val commandBuilder = RingCommandBuilder()

    override fun syncCurrentTimeAndTimeFormat() {
        interaction.writeCommand(commandBuilder.buildP01Command())
    }

    override fun getDeviceBatteryPower() {
        interaction.writeCommand(commandBuilder.buildP02Command())
    }

    override fun flipDeviceScreen() {
        interaction.writeCommand(commandBuilder.buildP03Command())
    }

    override fun startFindDevice() {
        interaction.writeCommand(commandBuilder.buildP04Command(true))
    }

    override fun stopFindDevice() {
        interaction.writeCommand(commandBuilder.buildP04Command(false))
    }

    override fun getDeviceState() {
        interaction.writeCommand(commandBuilder.buildP05Command())
    }

    override fun getPrayerCountInformation(tick: String) {
        interaction.writeCommand(commandBuilder.buildP06Command(tick))
    }

    override fun getRealTimePrayerCount(tick: String) {
        interaction.writeCommand(commandBuilder.buildP07Command(tick))
    }

    override fun shutdown() {
        interaction.writeCommand(commandBuilder.buildP08Command())
    }

    override fun setPrayerRemindersState(
        isOpen: Boolean,
        calculationMethodPosition: Int,
        juristicMethodPosition: Int,
        latitude: Double,
        longitude: Double,
        timeZone: Double
    ) {
        interaction.writeCommand(
            commandBuilder.buildP09Command(
                isOpen, calculationMethodPosition,
                juristicMethodPosition, latitude, longitude, timeZone
            )
        )
    }

    override fun reboot() {
        interaction.writeCommand(commandBuilder.buildP10Command())
    }

    override fun setPrayerReminders(
        isOpen: Boolean,
        startTime: String,
        endTime: String,
        intervalTime: Int
    ) {
        interaction.writeCommand(
            commandBuilder.buildP11Command(
                isOpen,
                startTime,
                endTime,
                intervalTime
            )
        )
    }

    override fun getDeviceCacheData() {
        interaction.writeCommand(commandBuilder.buildP12Command())
    }

    override fun getDeviceConfigInfo() {
        interaction.writeCommand(commandBuilder.buildP13Command())
    }

    override fun getDeviceLog() {
        interaction.writeCommand(commandBuilder.buildP14Command())
    }

    override fun setDeviceScreenBrightness(brightness: Int) {
        interaction.writeCommand(commandBuilder.buildP15Command(brightness))
    }

    override fun setDeviceLanguage(languageType: Int) {
        interaction.writeCommand(commandBuilder.buildP16Command(languageType))
    }

    override fun setCustomizeConvention(
        fajrAngle: Float,
        ishaIsMinutes: Boolean,
        ishaValue: Float
    ) {
        interaction.writeCommand(
            commandBuilder.buildP17Command(
                fajrAngle,
                ishaIsMinutes,
                ishaValue
            )
        )
    }

    override fun setStableConvention(switch: Boolean) {
        interaction.writeCommand(commandBuilder.buildP18Command(switch))
    }

    override fun startOTAUpgrade(file: File, listener: OTAUpgradeListener) {
        interaction.startOTAUpgrade(file, listener)
    }
}