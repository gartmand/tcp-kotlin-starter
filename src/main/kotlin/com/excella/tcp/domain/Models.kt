package com.excella.tcp.domain

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate
import javax.validation.Valid
import javax.validation.constraints.*

abstract class DomainModel {
    @Id
    var id: String? = null
}

@Document
@TypeAlias("employee")
data class Employee(
        @field:Valid
        val bio: Bio,
        @field:Valid
        val contact: Contact) : DomainModel()

data class Bio(
        @field:NotEmpty
        val firstName: String?,
        val middleName: String?,

        @field:NotEmpty
        val lastName: String?,

        @field:Past
        @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @field:JsonSerialize(using = LocalDateSerializer::class)
        @field:JsonDeserialize(using = LocalDateDeserializer::class)
        val birthDate: LocalDate?,

        @field:NotNull
        val gender: Gender?,

        @field:NotNull val ethnicity: Ethnicity?
)

data class Contact(
        @field:NotEmpty
        @field:Email
        val email: String?,

        @field:Pattern(regexp = "\\d{10}",
                message = "Phone number must consist of 10 digits")
        @field:NotEmpty
        val phoneNumber: String?,

        @field:Valid
        val address: Address?
)

data class Address(
        val line1: String?,
        val line2: String?,
        val city: String?,

        @field:Size(min = 2, max = 2)
        val stateCode: String?,

        val zipCode: String?
)

enum class Gender {
    MALE, FEMALE, OTHER
}

enum class Ethnicity {
    CAUCASIAN,
    HISPANIC,
    AMERICAN_INDIAN,
    ASIAN,
    BLACK,
    DECLINED,
    OTHER
}