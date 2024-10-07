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
 package inhttp.request.response.impl

import inhttp.jsonTree
import inhttp.to
import kotlinx.serialization.json.JsonElement
import inhttp.request.response.InHttpResponse
import java.net.http.HttpResponse

class InHttpTextResponse(res: HttpResponse<*>) : InHttpResponse(res) {
    inline fun <reified T> json(): T {
        return res.body().jsonTree().to<T>()
    }

    fun jsonTree(): JsonElement {
        return res.body().jsonTree()
    }

    fun text(): String {
        return res.body() as String
    }
}