
import C.CLONE_NEWUTS
import C.SIGCHLD
import C.pthread_tVar
import C.wait
import com.xunanmu.logger.*
import kotlinx.cinterop.*
import platform.zlib.voidp
import C.clone
import platform.posix.*

val a = "全局变量"
val logger = LinuxX64Logger("test")

fun child_p(voidp: voidp?):Int{
    println("child_p--------------------------------------------------------")
    logger.error("这是个错误信息")
    logger.warn("这是一个警告信息")
    logger.info("这是一个普通消息")
    logger.debug("这是debug信息")
    logger.trace("这是一个追踪信息")
    println("child_p--------------------------------------------------------")
    return 0
}

fun child_th(voidp: voidp?):COpaquePointer?{
    println("child_th--------------------------------------------------------")
    logger.error("这是个错误信息")
    logger.warn("这是一个警告信息")
    logger.info("这是一个普通消息")
    logger.debug("这是debug信息")
    logger.trace("这是一个追踪信息")
    println("child_th--------------------------------------------------------")
    return voidp
}

fun cmp(a:COpaquePointer?,b:COpaquePointer?):Int{
    println("cmp--------------------------------------------------------")
    logger.error("这是个错误信息")
    logger.warn("这是一个警告信息")
    logger.info("这是一个普通消息")
    logger.debug("这是debug信息")
    logger.trace("这是一个追踪信息")
    println("cmp--------------------------------------------------------")
    return 1
}

fun main() {
    println("main--------------------------------------------------------")
    logger.error("这是个错误信息")
    logger.warn("这是一个警告信息")
    logger.info("这是一个普通消息")
    logger.debug("这是debug信息")
    logger.trace("这是一个追踪信息")
    println("main--------------------------------------------------------")
    memScoped {
        val child_stack =allocArray<ByteVar>(1024*1024)
        clone(
            staticCFunction ( ::child_p),
            child_stack + 1024*1024,
            CLONE_NEWUTS or SIGCHLD,
            null
        )
        wait(null)

        qsort(child_stack,2,4 ,staticCFunction(::cmp))

        val pthreadT = alloc<pthread_tVar>()
        pthread_create(pthreadT.ptr,null, staticCFunction(::child_th) ,null)
        pthread_join(pthreadT.value,null)
    }
}