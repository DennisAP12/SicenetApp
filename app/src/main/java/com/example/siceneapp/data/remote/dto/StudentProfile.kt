package com.example.siceneapp.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * Este es el "molde" para los datos del alumno que vienen en formato JSON.
 * La anotación @Serializable le dice a la librería que puede convertir
 * un JSON a un objeto de esta clase automáticamente.
 */
@Serializable
data class StudentProfile(
    val fechaReins: String,
    val modEducativo: Int,
    val adeudo: Boolean,
    val urlFoto: String,
    val adeudoDescripcion: String,
    val inscrito: Boolean,
    val estatus: String,
    val semActual: Int,
    val cdtosAcumulados: Int,
    val cdtosActuales: Int,
    val especialidad: String,
    val carrera: String,
    val lineamiento: Int,
    val nombre: String,
    val matricula: String
)
