package com.asyncapi.v2.model.channel.operation

import com.asyncapi.v2.ClasspathUtils
import com.asyncapi.v2.model.Tag
import com.asyncapi.v2.model.channel.message.Message
import com.google.gson.GsonBuilder
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

/**
 * @author Pavel Bodiachevskii
 */
class OperationTest {

    private val gson = GsonBuilder().setPrettyPrinting().create()

    private fun buildOperation(): Operation {
        return Operation.builder()
                .operationId("registerUser")
                .summary("Action to sign a user up.")
                .description("A longer description")
                .tags(listOf(
                        Tag("user", null, null),
                        Tag("signup", null, null),
                        Tag("register", null, null)
                ))
                .message(Message.builder()
                        .headers(mapOf(
                                Pair("type", "object"),
                                Pair("properties", mapOf(
                                        Pair("applicationInstanceId", mapOf(
                                                Pair("description", "Unique identifier for a given instance of the publishing application"),
                                                Pair("type", "string")
                                        ))
                                ))
                        ))
                        .payload(mapOf(
                                Pair("type", "object"),
                                Pair("properties", mapOf(
                                        Pair("user", mapOf(
                                                Pair("\$ref", "#/components/schemas/userCreate")
                                        )),
                                        Pair("signup", mapOf(
                                                Pair("\$ref", "#/components/schemas/signup")
                                        ))
                                ))
                        ))
                        .build()
                )
                .bindings(mapOf(
                        Pair("amqp", mapOf(
                                Pair("ack", false)
                        ))
                ))
                .traits(listOf(mapOf(Pair("\$ref", "#/components/operationTraits/kafka"))))
                .build()
    }

    @Test
    @DisplayName("Compare hand crafted model with parsed json")
    fun compareModelWithParsedJson() {
        val model = ClasspathUtils.readAsString("/json/model/channel/operation/operation.json")

        Assertions.assertEquals(
                gson.fromJson(model, Operation::class.java),
                buildOperation()
        )
    }

}