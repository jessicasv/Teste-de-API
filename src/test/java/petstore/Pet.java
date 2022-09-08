package petstore;

import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

public class Pet {

    String uri = "https://petstore.swagger.io/v2/pet\n"; //endereco entidade pet

    public String lerJson(String caminhoJson) throws IOException {

        return new String(Files.readAllBytes(Paths.get(caminhoJson)));
    }

    @Test //identifica o metodo ou funcao como teste p/ o testng
    public void incluirPet() throws IOException {
        String jsonBody = lerJson("db/pet1.json");

        //sintaxe gherkin
        //dado - quando - entao
        // given - when - then

        given()
                .contentType("application/json") //comum em api rest - antigas usam text/xml
                .log().all()
                .body(jsonBody)

        .when()
                .post(uri)

        .then()
                .log().all()
                .statusCode(200)
                .body("name", is("Loki"))
                .body("status", is("available"))
        ; //toda essa estrutura esta em uma linha, por isso fecha aqui
    }

}
