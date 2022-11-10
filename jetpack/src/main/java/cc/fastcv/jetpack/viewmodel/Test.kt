package cc.fastcv.jetpack.viewmodel

class Test(name: String?, age: String?) {
    @JvmOverloads
    constructor(name: String?, age: Int = 18) : this(name, age.toString() + "") {
    }
}