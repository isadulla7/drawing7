package dot.isadulla.dotpaint.di

import kotlin.reflect.KClass

object ServiceCenter {
    val services = mutableMapOf<KClass<*>, Any>()

    fun <T : Any> set(clazz: KClass<T>, instance: T) {
        services[clazz] = instance
    }

    inline fun <reified T : Any> get(): T {
        val instance = services[T::class] as? T
        return instance ?: throw IllegalStateException("Service not found: ${T::class.simpleName}")
    }
}