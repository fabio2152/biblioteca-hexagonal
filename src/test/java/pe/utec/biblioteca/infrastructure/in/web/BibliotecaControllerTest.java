package pe.utec.biblioteca.infrastructure.in.web;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/** Prueba de extremo a extremo del adaptador REST, con los datos de ejemplo cargados. */
@SpringBootTest
@AutoConfigureMockMvc
class BibliotecaControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("GET /api/libros lista los libros de ejemplo")
    void listaLibros() throws Exception {
        mvc.perform(get("/api/libros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(6));
    }

    @Test
    @DisplayName("POST /api/prestamos con libro disponible responde 201")
    void prestaLibro() throws Exception {
        mvc.perform(post("/api/prestamos").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"codigoSocio\":\"S002\",\"codigoLibro\":\"LIB-04\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estado").value("ACTIVO"));
    }

    @Test
    @DisplayName("Una regla de negocio violada responde 409")
    void socioConMulta() throws Exception {
        mvc.perform(post("/api/prestamos").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"codigoSocio\":\"S003\",\"codigoLibro\":\"LIB-05\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje").value(containsString("multa")));
    }

    @Test
    @DisplayName("Devolver el libro vencido de ejemplo genera S/ 6.00 de multa")
    void devolucionVencida() throws Exception {
        mvc.perform(post("/api/devoluciones").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"codigoLibro\":\"LIB-01\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.multa").value(6.0));
    }

    @Test
    @DisplayName("Petición sin datos obligatorios responde 400")
    void datosInvalidos() throws Exception {
        mvc.perform(post("/api/prestamos").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest());
    }
}
