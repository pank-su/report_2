import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.kotest.core.project.ProjectContext
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.perProject
import io.kotest.extensions.testcontainers.perSpec
import io.kotest.extensions.testcontainers.perTest
import io.kotest.matchers.shouldBe
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait


/** Последовательные тесты MapHelper
 * @author Панков Вася
 */
class MapHelperTest : FunSpec({
    val url = "http://0.0.0.0:5000"
    val client = HttpClient() {
        install(ContentNegotiation) {
            json()
        }
    }

    val mapName = "testmap"
    val mapName2 = "mapName2"

    val correctParams = """resolution: 0.050000
origin: [2.000000, -10.300000, 2.280000]
negate: 0
occupied_thresh: 0.7
free_thresh: 0.4
"""

    val mapHelperContainer = GenericContainer("panksu/map_helper:latest").apply {
        withNetworkMode("host")
        startupAttempts = 1
        withCommand("./run.sh")
        waitingFor(Wait.forLogMessage(" \\* Running\\ on http:.*", 1))
    }
    listeners(mapHelperContainer.perSpec())



    context("correct data"){

        test("check port and other configuration and list clear") {
            val response = client.get("$url/all_maps")
            response shouldHaveStatus HttpStatusCode.OK
            response.bodyAsText() shouldBe "[]\n"

        }

        test("get selected map before adding map"){
            val selectedMapResponse = client.get("$url/selected_map")
            selectedMapResponse shouldHaveStatus HttpStatusCode.BadRequest
            selectedMapResponse.body<ErrorDTO>() shouldBe ErrorDTO("No one map selected")
        }

        test("add valid map and config") {
            val response = client.submitFormWithBinaryData(url = "$url/load_map", formData =
            formData {
                append("map_name", mapName)
                append(
                    "params", correctParams
                )
                MapHelperTest::class.java.getResource("polygon.png")?.let {
                    append("map", it.readBytes(), Headers.build {
                        append(HttpHeaders.ContentType, ContentType.Image.Any)
                        append(HttpHeaders.ContentDisposition, "filename=\"map.png\"")

                    })
                }
            })
            response shouldHaveStatus HttpStatusCode.OK
            response.bodyAsText() shouldBe "ok"
        }


        test("get image from helper") {
            val imageResponse = client.get("$url/image/${mapName}")

            imageResponse shouldHaveStatus HttpStatusCode.OK
            imageResponse.body() as ByteArray shouldBe MapHelperTest::class.java.getResource("polygon.png")!!.readBytes()

        }


        test("get yaml from helper") {
            val yamlResponse = client.get("$url/yaml/${mapName}")

            yamlResponse shouldHaveStatus HttpStatusCode.OK
            yamlResponse.bodyAsText().split("\n").takeLast(6) shouldBe correctParams.split("\n")
        }

        test("get selected map"){
            val selectedMapResponse = client.get("$url/selected_map")
            selectedMapResponse shouldHaveStatus HttpStatusCode.OK
            selectedMapResponse.body<MapDTO>().name shouldBe mapName
        }

        test("get correct map list") {
            val response = client.get("$url/all_maps")
            response shouldHaveStatus HttpStatusCode.OK
            val maps = response.body<List<MapDTO>>()
            maps.size shouldBe 1
            maps[0].name shouldBe mapName


            val response2 = client.submitFormWithBinaryData(url = "$url/load_map", formData =
            formData {
                append("map_name", mapName2)
                append(
                    "params", correctParams
                )
                MapHelperTest::class.java.getResource("polygon.png")?.let {
                    append("map", it.readBytes(), Headers.build {
                        append(HttpHeaders.ContentType, ContentType.Image.Any)
                        append(HttpHeaders.ContentDisposition, "filename=\"map.png\"")

                    })
                }
            })
            response2 shouldHaveStatus HttpStatusCode.OK
            response2.bodyAsText() shouldBe "ok"


            val response3 = client.get("$url/all_maps")
            response3 shouldHaveStatus HttpStatusCode.OK
            val maps2 = response3.body<List<MapDTO>>()
            maps2.size shouldBe 2
            maps2.map { it.name } shouldBe listOf(mapName, mapName2)
        }

        test("get selected map with two maps"){
            var selectedMapResponse = client.get("$url/selected_map")
            selectedMapResponse shouldHaveStatus HttpStatusCode.OK
            selectedMapResponse.body<MapDTO>().name shouldBe mapName2

            val selectMapResponse = client.get("$url/select_map/${mapName}")
            selectMapResponse shouldHaveStatus HttpStatusCode.OK
            selectMapResponse.body<MapDTO>().name shouldBe mapName

            selectedMapResponse = client.get("$url/selected_map")
            selectedMapResponse shouldHaveStatus HttpStatusCode.OK
            selectedMapResponse.body<MapDTO>().name shouldBe mapName
        }
    }




    context("invalid data") {

        test("add invalid config") {
            val response = client.submitFormWithBinaryData(url = "$url/load_map", formData =
            formData {
                append("map_name", "Cool map")
                append(
                    "params",
                    "origin: [-10.000000, -10.000000, 0.000000]\n" +
                            "negate: 0\n" +
                            "occupied_thresh: 0.65\n" +
                            "free_thresh: 0.196"
                )
                MapHelperTest::class.java.getResource("polygon.pgm")?.let {
                    append("map", it.readBytes(), Headers.build {
                        append(HttpHeaders.ContentType, ContentType.Image.Any)
                        append(HttpHeaders.ContentDisposition, "filename=\"map.pgm\"")

                    })
                }
            })
            response shouldHaveStatus HttpStatusCode.BadRequest
            response.body<ErrorDTO>() shouldBe ErrorDTO("Invalid map metadata")
        }



        test("get incorrect map") {
            val imageResponse = client.get("$url/image/bladsfd")
            imageResponse shouldHaveStatus HttpStatusCode.BadRequest
            imageResponse.body<ErrorDTO>() shouldBe ErrorDTO("Map not found")

            val yamlResponse = client.get("$url/yaml/bladsfd")
            yamlResponse shouldHaveStatus HttpStatusCode.BadRequest
            yamlResponse.body<ErrorDTO>() shouldBe ErrorDTO("Map not found")
        }

        test("select incorrect map") {
            val imageResponse = client.get("$url/select_map/bladsfd")
            imageResponse shouldHaveStatus HttpStatusCode.BadRequest
            imageResponse.body<ErrorDTO>() shouldBe ErrorDTO("Map not found")
        }
    }

},)
