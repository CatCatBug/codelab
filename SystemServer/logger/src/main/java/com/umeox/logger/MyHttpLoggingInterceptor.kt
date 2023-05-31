//package com.umeox.logger
//
//import okhttp3.Headers
//import okhttp3.Interceptor
//import okhttp3.Response
//import okhttp3.internal.http.promisesBody
//import okhttp3.logging.HttpLoggingInterceptor.Level
//import okhttp3.logging.HttpLoggingInterceptor.Logger
//import okio.Buffer
//import okio.GzipSource
//import java.io.EOFException
//import java.io.IOException
//import java.nio.charset.Charset
//import java.nio.charset.StandardCharsets
//import java.util.*
//import java.util.concurrent.TimeUnit
//
//class MyHttpLoggingInterceptor @JvmOverloads constructor(
//    private val logger: Logger = Logger.DEFAULT
//) : Interceptor {
//
//    @Volatile
//    private var headersToRedact = emptySet<String>()
//
//    @set:JvmName("level")
//    @Volatile
//    var level = Level.NONE
//
//    fun redactHeader(name: String) {
//        val newHeadersToRedact = TreeSet(String.CASE_INSENSITIVE_ORDER)
//        newHeadersToRedact += headersToRedact
//        newHeadersToRedact += name
//        headersToRedact = newHeadersToRedact
//    }
//
//    /**
//     * Sets the level and returns this.
//     *
//     * This was deprecated in OkHttp 4.0 in favor of the [level] val. In OkHttp 4.3 it is
//     * un-deprecated because Java callers can't chain when assigning Kotlin vals. (The getter remains
//     * deprecated).
//     */
//    fun setLevel(level: Level) = apply {
//        this.level = level
//    }
//
//    @JvmName("-deprecated_level")
//    @Deprecated(
//        message = "moved to var",
//        replaceWith = ReplaceWith(expression = "level"),
//        level = DeprecationLevel.ERROR
//    )
//    fun getLevel(): Level = level
//
//    @Throws(IOException::class)
//    override fun intercept(chain: Interceptor.Chain): Response {
//        val httpLogBody = HttpLogBody()
//        val level = this.level
//
//        val request = chain.request()
//        if (level == Level.NONE) {
//            return chain.proceed(request)
//        }
//
//        val logBody = level == Level.BODY
//        val logHeaders = logBody || level == Level.HEADERS
//
//        val requestBody = request.body
//
//        val connection = chain.connection()
//        var requestStartMessage =
//            ("--> ${request.method} ${request.url}${if (connection != null) " " + connection.protocol() else ""}")
//        if (!logHeaders && requestBody != null) {
//            requestStartMessage += " (${requestBody.contentLength()}-byte body)"
//        }
//        httpLogBody.log(requestStartMessage)
//        httpLogBody.log("请求头：")
//        if (logHeaders) {
//            val headers = request.headers
//
//            if (requestBody != null) {
//                // Request body headers are only present when installed as a network interceptor. When not
//                // already present, force them to be included (if available) so their values are known.
//                requestBody.contentType()?.let {
//                    if (headers["Content-Type"] == null) {
//                        httpLogBody.log("Content-Type: $it")
//                    }
//                }
//                if (requestBody.contentLength() != -1L) {
//                    if (headers["Content-Length"] == null) {
//                        httpLogBody.log("Content-Length: ${requestBody.contentLength()}")
//                    }
//                }
//            }
//
//            for (i in 0 until headers.size) {
//                logHeader(headers, i, httpLogBody)
//            }
//
//            if (!logBody || requestBody == null) {
//                httpLogBody.log("--> END ${request.method}")
//            } else if (bodyHasUnknownEncoding(request.headers)) {
//                httpLogBody.log("--> END ${request.method} (encoded body omitted)")
//            } else if (requestBody.isDuplex()) {
//                httpLogBody.log("--> END ${request.method} (duplex request body omitted)")
//            } else if (requestBody.isOneShot()) {
//                httpLogBody.log("--> END ${request.method} (one-shot body omitted)")
//            } else {
//                val buffer = Buffer()
//                requestBody.writeTo(buffer)
//
//                val contentType = requestBody.contentType()
//                val charset: Charset =
//                    contentType?.charset(StandardCharsets.UTF_8) ?: StandardCharsets.UTF_8
//
//                httpLogBody.log("请求内容：")
//                if (buffer.isProbablyUtf8()) {
//                    httpLogBody.log(buffer.readString(charset))
//                    httpLogBody.log("--> END ${request.method} (${requestBody.contentLength()}-byte body)")
//                } else {
//                    httpLogBody.log(
//                        "--> END ${request.method} (binary ${requestBody.contentLength()}-byte body omitted)"
//                    )
//                }
//            }
//        }
//
//        val startNs = System.nanoTime()
//        val response: Response
//        try {
//            response = chain.proceed(request)
//        } catch (e: Exception) {
//            httpLogBody.log("<-- HTTP FAILED: $e")
//            throw e
//        }
//
//        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
//
//        val responseBody = response.body!!
//        val contentLength = responseBody.contentLength()
//        val bodySize = if (contentLength != -1L) "$contentLength-byte" else "unknown-length"
//        httpLogBody.log("")
//        httpLogBody.log(
//            "<-- ${response.code}${if (response.message.isEmpty()) "" else ' ' + response.message} ${response.request.url} (${tookMs}ms${if (!logHeaders) ", $bodySize body" else ""})"
//        )
//
//        httpLogBody.log("响应内容：")
//        if (logHeaders) {
//            val headers = response.headers
////            for (i in 0 until headers.size) {
////                logHeader(headers, i, httpLogBody)
////            }
//
//            if (!logBody || !response.promisesBody()) {
//                httpLogBody.log("<-- END HTTP")
//            } else if (bodyHasUnknownEncoding(response.headers)) {
//                httpLogBody.log("<-- END HTTP (encoded body omitted)")
//            } else {
//                val source = responseBody.source()
//                source.request(Long.MAX_VALUE) // Buffer the entire body.
//                var buffer = source.buffer
//
//                var gzippedLength: Long? = null
//                if ("gzip".equals(headers["Content-Encoding"], ignoreCase = true)) {
//                    gzippedLength = buffer.size
//                    GzipSource(buffer.clone()).use { gzippedResponseBody ->
//                        buffer = Buffer()
//                        buffer.writeAll(gzippedResponseBody)
//                    }
//                }
//
//                val contentType = responseBody.contentType()
//                val charset: Charset =
//                    contentType?.charset(StandardCharsets.UTF_8) ?: StandardCharsets.UTF_8
//
//                if (!buffer.isProbablyUtf8()) {
//                    httpLogBody.log("<-- END HTTP (binary ${buffer.size}-byte body omitted)")
//                    return response
//                }
//
//                if (contentLength != 0L) {
//                    httpLogBody.log(buffer.clone().readString(charset))
//                }
//
//                if (gzippedLength != null) {
//                    httpLogBody.log("<-- END HTTP (${buffer.size}-byte, $gzippedLength-gzipped-byte body)")
//                } else {
//                    httpLogBody.log("<-- END HTTP (${buffer.size}-byte body)")
//                }
//            }
//        }
//        logger.log(httpLogBody.getLog())
//        return response
//    }
//
//    private fun logHeader(headers: Headers, i: Int, httpLogBody: HttpLogBody) {
//        val value = if (headers.name(i) in headersToRedact) "██" else headers.value(i)
//        httpLogBody.log(headers.name(i) + ": " + value)
//    }
//
//    private fun bodyHasUnknownEncoding(headers: Headers): Boolean {
//        val contentEncoding = headers["Content-Encoding"] ?: return false
//        return !contentEncoding.equals("identity", ignoreCase = true) &&
//                !contentEncoding.equals("gzip", ignoreCase = true)
//    }
//}
//
//internal fun Buffer.isProbablyUtf8(): Boolean {
//    try {
//        val prefix = Buffer()
//        val byteCount = size.coerceAtMost(64)
//        copyTo(prefix, 0, byteCount)
//        for (i in 0 until 16) {
//            if (prefix.exhausted()) {
//                break
//            }
//            val codePoint = prefix.readUtf8CodePoint()
//            if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
//                return false
//            }
//        }
//        return true
//    } catch (_: EOFException) {
//        return false // Truncated UTF-8 sequence.
//    }
//}
//
//class HttpLogBody {
//    private val logs = mutableListOf<String>()
//
//    fun log(message: String) {
//        logs.add(message + "\n")
//    }
//
//    fun getLog(): String {
//        return logs.joinToString(
//            separator = ""
//        )
//    }
//}