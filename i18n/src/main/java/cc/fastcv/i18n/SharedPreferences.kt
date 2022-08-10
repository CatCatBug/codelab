package cc.fastcv.i18n

/**
 * 获取SharedPreferences中存储的Int值
 */
fun String.getSpIntValue(default: Int) = SharedPreferencesHelper.getInt(this, default)

/**
 * 存储Int值到SharedPreferences中
 */
fun String.saveSpIntValue(value: Int) = SharedPreferencesHelper.put(this, value)

/**
 * 获取SharedPreferences中存储的Float值
 */
fun String.getSpFloatValue(default: Float) = SharedPreferencesHelper.getFloat(this, default)

/**
 * 存储Float值到SharedPreferences中
 */
fun String.saveSpFloatValue(value: Float) = SharedPreferencesHelper.put(this, value)

/**
 * 获取SharedPreferences中存储的Boolean值
 */
fun String.getSpBooleanValue(default: Boolean) = SharedPreferencesHelper.getBoolean(this, default)

/**
 * 存储Boolean值到SharedPreferences中
 */
fun String.saveSpBooleanValue(value: Boolean) = SharedPreferencesHelper.put(this, value)

/**
 * 获取SharedPreferences中存储的Long值
 */
fun String.getSpLongValue(default: Long) = SharedPreferencesHelper.getLong(this, default)

/**
 * 存储Long值到SharedPreferences中
 */
fun String.saveSpLongValue(value: Long) = SharedPreferencesHelper.put(this, value)

/**
 * 获取SharedPreferences中存储的String值
 */
fun String.getSpStringValue(default: String) = SharedPreferencesHelper.getString(this, default)

/**
 * 存储String值到SharedPreferences中
 */
fun String.saveSpStringValue(value: String) = SharedPreferencesHelper.put(this, value)

/**
 * 获取SharedPreferences中存储的StringSet值
 */
fun String.getSpStringSetValue(default: Set<String?>) =
    SharedPreferencesHelper.getStringSet(this, default)

/**
 * 存储StringSet值到SharedPreferences中
 */
fun String.saveSpStringSetValue(value: Set<String?>) = SharedPreferencesHelper.put(this, value)

/**
 * 检查SharedPreferences中是否存在当前key值
 */
fun String.includedInSpKeys() = SharedPreferencesHelper.contains(this)

/**
 * 移除SharedPreferences中对应key的 key - value值
 */
fun String.removeInSp() = SharedPreferencesHelper.remove(this)

/**
 * 清空所有的sp的key-value
 */
fun Any.clearAllSp() = SharedPreferencesHelper.clear()