package dot.isadulla.dotpaint.api.sync

interface Plugin {
    fun register()
    fun id(): String
}