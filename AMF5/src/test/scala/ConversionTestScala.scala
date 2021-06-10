import amf.client.environment.WebAPIConfiguration
import amf.plugins.document.apicontract.resolution.pipelines.compatibility.{Oas20CompatibilityPipeline, Raml10CompatibilityPipeline}
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should

class ConversionTestScala extends AsyncFlatSpec with should.Matchers {

  "AMF" should "convert a RAML 1.0 API to OAS 2.0" in {
    val client = WebAPIConfiguration.WebAPI().createClient()
    client.parse("file://resources/examples/banking-api.raml") map { parseResult =>
      val transformResult = client.transform(parseResult.bu, Oas20CompatibilityPipeline.name)
      val renderResult = client.render(transformResult.bu, "application/oas20+json")
      val readApi = getStrFromFile("resources/expected/converted-banking-api.json")
      renderResult shouldEqual readApi
    }
  }

  "AMF" should "convert an OAS 2.0 API to RAML 1.0" in {
    val client = WebAPIConfiguration.WebAPI().createClient()
    client.parse("file://resources/examples/banking-api.json") map { parseResult =>
      val transformResult = client.transform(parseResult.bu, Raml10CompatibilityPipeline.name)
      val renderResult = client.render(transformResult.bu, "application/raml10+yaml")
      val readApi = getStrFromFile("resources/expected/converted-banking-api.raml")
      renderResult shouldEqual readApi
    }
  }

  private def getStrFromFile(path: String): String = {
    val source = scala.io.Source.fromFile(path)
    val read =
      try source.mkString
      finally source.close()
    read
  }
}