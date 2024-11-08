# InHttp 
## Description
InHttp is a simple but effective http client that is based on the `java.net.http` client.
It is designed to be simple to use and easy to understand. It is easier to configure, while still maintaining the full functionality of the `java.net.http` client.

## Features
- Simple to use
- Easy to understand
- Easier to configure
- Full functionality of the `java.net.http` client
- Built-in support for JSON serialization and deserialization `(kotlinx.serialization)`

## Installation
### Gradle
```kotlin
implementation("io.github.integr-0:in-http:$version")
```

### Maven
```xml
<dependency>
    <groupId>io.github.integr-0</groupId>
    <artifactId>in-http</artifactId>
    <version>${version}</version>
</dependency>
```

## Usage
### Simple GET request
```kotlin
fun main() {
    val response = InHttpClient.DEFAULT
        .get("https://www.example.com/api")
        .sendAndReceive()

    if (response.statusCode() == 200) {
        println("Response: ${response.json<SimpleResponse>()}")
    } else {
        println("Error: ${response.statusCode()}")
    }
}

@Serializable
data class SimpleObject(val message: String)
```

### Simple POST request
```kotlin
fun main() {
    val response = InHttpClient.DEFAULT
        .get("https://www.example.com/api")
        .withBodyJson(SimpleObject("Hello, World!"))
        .send()

    if (response.statusCode() == 200) {
        println("Success!")
    } else {
        println("Error: ${response.statusCode()}")
    }
}

@Serializable
data class SimpleObject(val message: String)
```

# Configuring the InHttpClient

### Example configuration
```kotlin
val client = InHttpClient()
    .withProxy("localhost", 8080)
    .withTimeout(Duration.ofSeconds(10))
    .withRedirect(false)
    .withAuth("user", "password")
    .build()
```

### Original configuration
```kotlin

val client = InHttpClient.ofOriginal(
    HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .connectTimeout(Duration.ofSeconds(10))
        .followRedirects(HttpClient.Redirect.NORMAL)
        .proxy(ProxySelector.of(InetSocketAddress.createUnresolved("localhost", 8080)))
        .authenticator(Authenticator.getDefault())
        .build()
)       
```

## License
```
Apache License 2.0
```

## Author
```
integr-0
```

## Dependencies
- kotlinx.serialization

