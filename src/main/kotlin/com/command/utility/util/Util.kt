package com.command.utility.util

import com.command.utility.model.AddressBook
import com.command.utility.model.Contact
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.SAXException
import java.io.File
import java.io.IOException
import javax.script.ScriptEngine.FILENAME
import javax.xml.XMLConstants
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException


class Util {
    companion object {
        fun readFile(fileName: String): AddressBook? {
            val dbf = DocumentBuilderFactory.newInstance()

            try {
                dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true)

                // parse XML file
                val db = dbf.newDocumentBuilder()
                val doc: Document = db.parse(File(fileName))

                doc.getDocumentElement().normalize()

                val list: NodeList = doc.getElementsByTagName("contact")
                val contactList: MutableList<Contact> = mutableListOf()
                for (temp in 0 until list.getLength()) {
                    val node: Node = list.item(temp)
                    if (node.getNodeType() === Node.ELEMENT_NODE) {
                        val element: Element = node as Element

                        val customerID: String = element.getElementsByTagName("CustomerID").item(0).getTextContent()
                        val companyName: String = element.getElementsByTagName("CompanyName").item(0).getTextContent()
                        val contactName: String = element.getElementsByTagName("ContactName").item(0).getTextContent()
                        val contactTitle: String = element.getElementsByTagName("ContactTitle").item(0).getTextContent()
                        val address: String = element.getElementsByTagName("Address").item(0).getTextContent()
                        val city: String = element.getElementsByTagName("City").item(0).getTextContent()
                        val email: String = element.getElementsByTagName("Email").item(0).getTextContent()
                        val postalCode: String = element.getElementsByTagName("PostalCode").item(0).getTextContent()
                        val country: String = element.getElementsByTagName("Country").item(0).getTextContent()
                        val phone: String = element.getElementsByTagName("Phone").item(0).getTextContent()
                        val fax: String = element.getElementsByTagName("Fax").item(0).getTextContent()

                        contactList.add(Contact(customerID, companyName, contactName, contactTitle, address, city, email, postalCode, country, phone, fax))
                    }
                }
                return AddressBook(contactList)
            } catch (e: ParserConfigurationException) {
                e.printStackTrace()
            } catch (e: SAXException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }
    }
}