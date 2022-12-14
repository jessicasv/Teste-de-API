package petstore;

import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;

public class Pet {

    String uri = "https://petstore.swagger.io/v2/pet\n"; //endereco entidade pet

    public String lerJson(String caminhoJson) throws IOException {

        return new String(Files.readAllBytes(Paths.get(caminhoJson)));
    }

    @Test (priority = 1)//identifica o metodo ou funcao como teste p/ o testng
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
                .body("category.name", is("cat"))
                .body("tags.name", contains("sta"))
        ; //toda essa estrutura esta em uma linha, por isso fecha aqui
    }

    @Test (priority = 2)
    public void consultarPet(){

        String petId = "2022090801";

        String nomeCategoria = //variavel pra armazenar o nome da categoria no exemplo de extracao

        given()
                .contentType("application/json")
                .log().all()

        .when()
               // .get(uri + "/" + petId) assim nao funcionou...
                .get("https://petstore.swagger.io/v2/pet/2022090801\n")

        .then()
                .log().all()
                .statusCode(200)
                .body("name", is("Loki"))
                .body("category.name", is("cat"))
                .body("status", is("available"))
        .extract() //exemplo de como extrair algo especifico
                .path("category.name")
        ;

        System.out.println("Categoria: " + nomeCategoria); //exibir o dado extraido

    }

    @Test (priority = 3)
    public void alterarPet() throws IOException {

        String jsonBody = lerJson("db/pet2.json");

        given()
                .contentType("application/json")
                .log().all()
                .body(jsonBody)
        .when()
                .put(uri)
        .then()
                .log().all()
                .statusCode(200)
                .body("name", is("Loki"))
                .body("status", is("adopted"))
        ;

    }

    @Test(priority = 4)
    public void excluirPet(){

        String petId = "2022090801";

        given()
                .contentType("application/json")
                .log().all()
        .when()
                //.delete(uri + "/" + petId) nao consigo usar desse jeito :(
                .delete("https://petstore.swagger.io/v2/pet/2022090801\n")
        .then()
                .log().all()
                .statusCode(200)
                .body("code", is(200))
                .body("type", is("unknown"))
                .body("message", is(petId))
        ;


    }

}
