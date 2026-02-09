package com.example.siceneapp.data.remote.dto

import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

/**
 * Este es el "molde" para la respuesta XML de SOAP.
 * Las anotaciones le dicen a SimpleXML cómo navegar por el árbol de etiquetas
 * para extraer el contenido que nos interesa.
 */
@Root(name = "soap:Envelope", strict = false)
data class ProfileResponse(
    // Navega hasta la etiqueta <getAlumnoAcademicoWithLineamientoResult>
    @field:Path("soap:Body/getAlumnoAcademicoWithLineamientoResponse")
    // Extrae el contenido de esa etiqueta como un String
    @field:Element(name = "getAlumnoAcademicoWithLineamientoResult")
    var result: String? = null
)
