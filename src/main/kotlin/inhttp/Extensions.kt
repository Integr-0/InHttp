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
 package inhttp

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement

inline fun <reified T> T.jsonTree(): JsonElement {
    return Json.encodeToJsonElement<T>(this)
}

inline fun <reified T> T.jsonString(): String {
    return Json.encodeToString<T>(this)
}

inline fun <reified T> JsonElement.to(): T {
    return Json.decodeFromJsonElement<T>(this)
}

inline fun <reified T> String.jsonDecodeTo(): T {
    return Json.decodeFromString<T>(this)
}