package cc.fastcv.ble.sdk

interface OTAUpgradeListener {
    fun upgradeFail()
    fun upgradeSuccessful()
    fun upgradeStart()
    fun upgradeProgress(progress: Int)
}