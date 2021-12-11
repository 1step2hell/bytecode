package com.step2hell.bytecode

open class BytecodeExtension {
    var excludeClass: MutableList<String> = mutableListOf()
    var foo: String? = null
    var bar: Int = 0
    var baz: Boolean = false

    override fun toString(): String {
        return "BytecodeExtension(excludeClass=$excludeClass, foo=$foo, bar=$bar, baz=$baz)"
    }
}
