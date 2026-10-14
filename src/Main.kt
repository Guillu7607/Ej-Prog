import java.util.Calendar
import java.util.Date

open class Usuario(
    loginParam: String,
    passwordParam: String,
    fechaNacParam: Date? = null,
    emailParam: String = "correo@example.com",
    activoParam: Boolean = true
) {
    var login: String = loginParam
        set(value) {
            field = if (value.isEmpty()) "desconocido" else value
        }

    var password: String = passwordParam

    var fechaNac: Date? = fechaNacParam
        set(value) {
            field = if (value != null && esMenorDeEdad(value)) null else value
        }

    var email: String = emailParam

    init {
        this.login = loginParam
        this.fechaNac = fechaNacParam
    }

    constructor() : this("Invitado", "")

    private fun esMenorDeEdad(fecha: Date): Boolean {
        val calNac = Calendar.getInstance().apply { time = fecha }
        val calActual = Calendar.getInstance()

        var edad = calActual.get(Calendar.YEAR) - calNac.get(Calendar.YEAR)
        if (calActual.get(Calendar.DAY_OF_YEAR) < calNac.get(Calendar.DAY_OF_YEAR)) {
            edad--
        }
        return edad < 18
    }

    open fun obtenerDescripcion(): String {
        return "Login: $login, Fecha Nacimiento: ${fechaNac ?: "No especificada"}, Email: $email"
    }
}

class UsuarioNormal(
    login: String,
    password: String,
    fechaNac: Date? = null,
    email: String = "correo@example.com",
    val nivel: Int
) : Usuario(login, password, fechaNac, email) {

    override fun obtenerDescripcion(): String {
        return "${super.obtenerDescripcion()}, Nivel de usuario: $nivel"
    }
}

class Administrador(
    login: String,
    password: String,
    fechaNac: Date? = null,
    email: String = "guillermo@hotmail.com",
    val area: String
) : Usuario(login, password, fechaNac, email) {

    override fun obtenerDescripcion(): String {
        return "${super.obtenerDescripcion()}, Administrador del área: $area"
    }
}
val listaUsuarios = mutableListOf<Usuario>()

fun procesarUsuario(nuevoUsuario: Usuario) {
    val usuarioExistente = listaUsuarios.find { it.login == nuevoUsuario.login }

    if (usuarioExistente == null) {
        listaUsuarios.add(nuevoUsuario)
        print("Insercion exitosa")
    } else {
        if (usuarioExistente.login == nuevoUsuario.login && usuarioExistente.password == nuevoUsuario.password) {
        print("Ya existe")
        }
    }
}
fun filtrarUsuarios(condicion: (Usuario) -> Boolean): List<Usuario> {
    return listaUsuarios.filter(condicion)
}

fun main() {
    val listaUsuarios = mutableListOf<Usuario>()

    fun procesarUsuario(nuevoUsuario: Usuario) {
        val usuarioExistente = listaUsuarios.find { it.login == nuevoUsuario.login }

        if (usuarioExistente == null) {
            listaUsuarios.add(nuevoUsuario)
            println("Usuario dado de alta correctamente: ${nuevoUsuario.obtenerDescripcion()}")
        } else {
            if (usuarioExistente.login == nuevoUsuario.login && usuarioExistente.password == nuevoUsuario.password) {
                println("¡Coincidencia! El usuario '${usuarioExistente.login}' ya existe y las credenciales son correctas.")
            } else {
                println("El usuario ya existe, pero la contraseña no coincide.")
            }
        }
    }

    fun filtrarUsuarios(condicion: (Usuario) -> Boolean): List<Usuario> {
        return listaUsuarios.filter(condicion)
    }

    println("--- PRUEBAS DE ALTA ---")
    val u1 = UsuarioNormal(login = "hola", password = "123", nivel = 1)
    val u2 = Administrador(login = "funcionando", password = "abc", area = "Sistemas")

    procesarUsuario(u1)
    procesarUsuario(u2)

    println("\n--- PRUEBA DE USUARIO EXISTENTE (Credenciales correctas) ---")
    val u1Repetido = UsuarioNormal(login = "carlos123", password = "123", nivel = 2)
    procesarUsuario(u1Repetido)

    println("\n--- PRUEBA DE FUNCIÓN DE ORDEN SUPERIOR (Filtrar administradores) ---")
    val administradores = filtrarUsuarios { it is Administrador }
    for (admin in administradores) {
        println(admin.obtenerDescripcion())
    }
}