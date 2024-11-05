package sv.edu.itca.apployment.modelos;

public class Usuario {
        private String nombre;
        private String apellido;
        private String correo;
        private String telefono;
        private String ciudad;
        private String profesion;
        private String habilidad;
        private String fotoPerfil; // URL o ruta de la foto

        // Constructor
        public Usuario(String nombre, String apellido, String correo, String telefono, String ciudad, String profesion, String habilidad, String fotoPerfil) {
            this.nombre = nombre;
            this.apellido = apellido;
            this.correo = correo;
            this.telefono = telefono;
            this.ciudad = ciudad;
            this.profesion = profesion;
            this.habilidad = habilidad;
            this.fotoPerfil = fotoPerfil;
        }

        // Getters y Setters
        public String getNombre() {
            return nombre;
        }

        public String getApellido() {
            return apellido;
        }

        public String getCorreo() {
            return correo;
        }

        public String getTelefono() {
            return telefono;
        }

        public String getCiudad() {
            return ciudad;
        }

        public String getProfesion() {
            return profesion;
        }

        public String getHabilidad() {
            return habilidad;
        }


        public String getFotoPerfil() {
            return "http://192.168.1.59/storage/" + fotoPerfil;
        }
    }

