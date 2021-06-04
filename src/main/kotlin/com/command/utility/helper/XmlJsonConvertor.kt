package com.command.utility.helper

import com.command.utility.model.AddressBook
import com.command.utility.util.Util.Companion.readFile
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import java.io.File
import java.util.concurrent.Callable
import javax.xml.XMLConstants
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.Schema
import javax.xml.validation.SchemaFactory
import javax.xml.validation.Validator
import kotlin.system.exitProcess


@Command(name = "xmlJsonConvertor", mixinStandardHelpOptions = true, version = ["XmlJsonConvertor 1.0"],
    description = ["Utility to convert xml to json"])
class XmlJsonConvertor : Callable<Int> {

    @Option(names = ["-j"], description = ["Convert xml to json"])
    var toJson = false

    @Option(names = ["-x"], description = ["Convert json to xml"])
    var toXml = false

    @Option(names = ["-v"], description = ["validate it against schema"])
    var validate = false

    override fun call(): Int {
        if (toJson){
            val addressBook: AddressBook? = readFile("ab.xml")
            val mapper = jacksonObjectMapper()
            val json = mapper.writeValueAsString(addressBook)
            println(json)
        }

        if (toXml){
            val objectMapper = ObjectMapper()
            val xmlMapper: ObjectMapper = XmlMapper()
            val tree = objectMapper.readTree("")//TODO json to read and convert to xml
            val jsonAsXml = xmlMapper.writer().withRootName("AddressBook").writeValueAsString(tree)
            println(jsonAsXml)
        }

        if(validate) {
            try {
                val factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
                val schema: Schema = factory.newSchema(File(""))//TODO path to the XSD
                val validator: Validator = schema.newValidator()
                validator.validate(StreamSource(File("")))//TODO path to the xml file
            } catch (e: Exception) {
                println(e.message)
            }
        }

        return 0
    }
}

fun main(args: Array<String>) : Unit = exitProcess(CommandLine(XmlJsonConvertor()).execute(*args))