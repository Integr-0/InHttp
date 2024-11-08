/*
 * Copyright © 2024 Integr
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package inhttp.request

import kotlinx.serialization.json.JsonElement
import inhttp.jsonString
import inhttp.request.response.impl.InHttpByteArrayResponse
import inhttp.request.response.impl.InHttpInputStreamResponse
import inhttp.request.response.impl.InHttpLinesResponse
import inhttp.request.response.impl.InHttpTextResponse
import net.integr.client.method.InHttpMethod
import net.integr.inhttp.request.response.impl.*
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse.BodyHandlers
import java.nio.file.Paths

class InHttpRequest(private val method: InHttpMethod, private val url: String, private val client: HttpClient) {
    val headers: MutableMap<String, String> = mutableMapOf()
    var body: HttpRequest.BodyPublisher = BodyPublishers.noBody()

    private var dontAddDefaults = false

    fun withHeader(key: String, value: String): InHttpRequest {
        headers[key] = value
        return this
    }

    fun withBearer(token: String): InHttpRequest {
        headers["Authorization"] = "Bearer $token"
        return this
    }

    fun withUserAgent(userAgent: String): InHttpRequest {
        headers["User-Agent"] = userAgent
        return this
    }

    fun withContentType(contentType: String): InHttpRequest {
        headers["Content-Type"] = contentType
        return this
    }

    fun withAccept(accept: String): InHttpRequest {
        headers["Accept"] = accept
        return this
    }

    fun withIncognitoUserAgent(): InHttpRequest {
        headers["User-Agent"] = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36"
        return this
    }

    fun withDefaultUserAgent(): InHttpRequest {
        headers["User-Agent"] = "InHttp/1.0.1"
        return this
    }

    fun withDefaultAccept(): InHttpRequest {
        headers["Accept"] = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"
        return this
    }

    private fun withDefaults(): InHttpRequest {
        withDefaultAccept()
        withDefaultUserAgent()
        return this
    }

    private fun withDefaultsIfNecessary() {
        if (!dontAddDefaults) withDefaults()
    }

    fun withoutDefaults(): InHttpRequest {
        dontAddDefaults = true
        return this
    }

    fun withBodyJson(body: JsonElement, autoSetContentType: Boolean = true): InHttpRequest {
        this.body = BodyPublishers.ofString(body.jsonString())
        if (autoSetContentType) headers["Content-Type"] = "application/json"
        return this
    }

    fun withBodyJson(body: String, autoSetContentType: Boolean = true): InHttpRequest {
        this.body = BodyPublishers.ofString(body)
        if (autoSetContentType) headers["Content-Type"] = "application/json"
        return this
    }

    inline fun <reified T> withBodyJson(element: T, autoSetContentType: Boolean = true): InHttpRequest {
        this.body = BodyPublishers.ofString(element.jsonString())
        if (autoSetContentType) headers["Content-Type"] = "application/json"
        return this
    }

    fun withBodyString(body: String, autoSetContentType: Boolean = true): InHttpRequest {
        this.body = BodyPublishers.ofString(body)
        if (autoSetContentType) headers["Content-Type"] = "text/plain"
        return this
    }

    fun withBodyForm(body: String, autoSetContentType: Boolean = true): InHttpRequest {
        this.body = BodyPublishers.ofString(body)
        if (autoSetContentType) headers["Content-Type"] = "application/x-www-form-urlencoded"
        return this
    }

    fun sendAndReceive(): InHttpTextResponse {
        withDefaultsIfNecessary()

        var req = HttpRequest.newBuilder()
            .uri(URI.create(url))

        headers.forEach { (key, value) ->
            req = req.header(key, value)
        }

        req.method(method.name, body)

        val res = client.send(req.build(), BodyHandlers.ofString())
        return InHttpTextResponse(res)
    }

    fun sendAndReceiveByteArray(): InHttpByteArrayResponse {
        withDefaultsIfNecessary()

        var req = HttpRequest.newBuilder()
            .uri(URI.create(url))

        headers.forEach { (key, value) ->
            req = req.header(key, value)
        }

        req.method(method.name, body)

        val res = client.send(req.build(), BodyHandlers.ofByteArray())
        return InHttpByteArrayResponse(res)
    }

    fun sendAndReceiveInputStream(): InHttpInputStreamResponse {
        withDefaultsIfNecessary()

        var req = HttpRequest.newBuilder()
            .uri(URI.create(url))

        headers.forEach { (key, value) ->
            req = req.header(key, value)
        }

        req.method(method.name, body)

        val res = client.send(req.build(), BodyHandlers.ofInputStream())
        return InHttpInputStreamResponse(res)
    }

    fun sendAndSaveFile(path: String): InHttpDiscardingResponse {
        withDefaultsIfNecessary()

        var req = HttpRequest.newBuilder()
            .uri(URI.create(url))

        headers.forEach { (key, value) ->
            req = req.header(key, value)
        }

        req.method(method.name, body)

        val res = client.send(req.build(), BodyHandlers.ofFile(Paths.get(path)))
        return InHttpDiscardingResponse(res)
    }

    fun sendAndReceiveLines(): InHttpLinesResponse {
        withDefaultsIfNecessary()

        var req = HttpRequest.newBuilder()
            .uri(URI.create(url))

        headers.forEach { (key, value) ->
            req = req.header(key, value)
        }

        req.method(method.name, body)

        val res = client.send(req.build(), BodyHandlers.ofLines())
        return InHttpLinesResponse(res)
    }

    fun send(): InHttpDiscardingResponse {
        withDefaultsIfNecessary()

        val req = HttpRequest.newBuilder()
            .uri(URI.create(url))

        headers.forEach { (key, value) ->
            req.header(key, value)
        }

        req.method(method.name, body)

        val res = client.send(req.build(), BodyHandlers.discarding())
        return InHttpDiscardingResponse(res)
    }
}