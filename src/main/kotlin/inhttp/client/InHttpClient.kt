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

@file:Suppress("unused")
 package inhttp.client

import net.integr.inhttp.request.InHttpRequest
import net.integr.client.method.InHttpMethod
import java.net.Authenticator
import java.net.InetSocketAddress
import java.net.PasswordAuthentication
import java.net.ProxySelector
import java.net.http.HttpClient
import java.time.Duration


class InHttpClient {
    private var proxySelector: ProxySelector? = null
    private var followRedirects: Boolean = true
    private var authenticator: Authenticator? = null
    private var timeout: Duration? = null
    private var isBuilt: Boolean = false
    private var version: HttpClient.Version = HttpClient.Version.HTTP_2

    private var internalClient: HttpClient? = null

    fun withProxy(hostname: String, port: Int): InHttpClient {
        proxySelector = ProxySelector.of(InetSocketAddress(hostname, port))
        return this
    }

    fun withDefaultProxy(): InHttpClient {
        proxySelector = ProxySelector.getDefault()
        return this
    }

    fun withRedirect(allow: Boolean): InHttpClient {
        followRedirects = allow
        return this
    }

    fun withAuth(username: String, password: String): InHttpClient {
        authenticator = object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication = PasswordAuthentication(username, password.toCharArray())
        }

        return this
    }

    fun withVersion(protocolVersion: HttpClient.Version): InHttpClient {
        version = protocolVersion
        return this
    }

    fun withAuth(authenticator: Authenticator): InHttpClient {
        this.authenticator = authenticator
        return this
    }

    fun withDefaultAuth(): InHttpClient {
        authenticator = Authenticator.getDefault()
        return this
    }

    fun withProxy(proxySelector: ProxySelector): InHttpClient {
        this.proxySelector = proxySelector
        return this
    }

    fun withTimeout(timeout: Duration): InHttpClient {
        this.timeout = timeout
        return this
    }

    private fun build(): HttpClient {
        if (isBuilt) {
            throw IllegalStateException("Already built")
        }

        isBuilt = true

        var curr = HttpClient.newBuilder().version(version)

        if (proxySelector != null) {
            curr = curr.proxy(proxySelector!!)
        }

        curr = if (followRedirects) {
            curr.followRedirects(HttpClient.Redirect.NORMAL)
        } else {
            curr.followRedirects(HttpClient.Redirect.NEVER)
        }

        if (authenticator != null) {
            curr.authenticator(authenticator)
        }

        if (timeout != null) {
            curr = curr.connectTimeout(timeout!!)
        }

        internalClient = curr.build()
        return internalClient!!
    }

    fun get(url: String): InHttpRequest {
        val preRequest = InHttpRequest(InHttpMethod.GET, url, build())
        return preRequest
    }

    fun post(url: String): InHttpRequest {
        val preRequest = InHttpRequest(InHttpMethod.POST, url, build())
        return preRequest
    }

    fun put(url: String): InHttpRequest {
        val preRequest = InHttpRequest(InHttpMethod.PUT, url, build())
        return preRequest
    }

    fun delete(url: String): InHttpRequest {
        val preRequest = InHttpRequest(InHttpMethod.DELETE, url, build())
        return preRequest
    }

    fun head(url: String): InHttpRequest {
        val preRequest = InHttpRequest(InHttpMethod.HEAD, url, build())
        return preRequest
    }

    fun options(url: String): InHttpRequest {
        val preRequest = InHttpRequest(InHttpMethod.OPTIONS, url, build())
        return preRequest
    }

    fun req(method: InHttpMethod, url: String): InHttpRequest {
        val preRequest = InHttpRequest(method, url, build())
        return preRequest
    }

    companion object {
        val DEFAULT = InHttpClient()
            .withDefaultProxy()
            .withRedirect(true)

        fun ofOriginal(client: HttpClient): InHttpClient {
            val clientI = InHttpClient()
            clientI.internalClient = client
            return clientI
        }
    }
}