package org.hibernate.type.descriptor.java;

import org.hibernate.HibernateException;
import org.hibernate.internal.SessionImpl;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.spi.JdbcRecommendedSqlTypeMappingContext;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLXML;

public class XMLTypeDescriptor extends AbstractTypeDescriptor<Document> {
    public static final XMLTypeDescriptor INSTANCE = new XMLTypeDescriptor();

    public XMLTypeDescriptor() {
        super( Document.class );
    }

    @Override
    public String toString(Document value) {

        if ( value == null ) {
            return null;
        }
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = tf.newTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        StringWriter writer = new StringWriter();
        try {
            transformer.transform(new DOMSource(value), new StreamResult(writer));
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        String output = writer.getBuffer().toString().replaceAll("\n|\r", "");
        return output;
    }

    @Override
    public Document fromString(String string) {

        if ( string == null ) {
            return null;
        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = (builder.parse(new InputSource(new StringReader(string))));
            return doc;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <X> X unwrap(Document value, Class<X> type, WrapperOptions options) {
        if ( value == null ) {
            return null;
        }
        if ( SQLXML.class.isAssignableFrom( type ) ) {
            return (X) SQLXMLParser.INSTANCE.DOMToXML(value);
        }
        throw unknownUnwrap( type );
    }

    @Override
    public <X> Document wrap(X value, WrapperOptions options) {
        if ( value == null ) {
            return null;
        }
        if ( SQLXML.class.isInstance( value ) ) {
            return SQLXMLParser.INSTANCE.XMLToDOM((SQLXML)value);
        }
        throw unknownWrap( value.getClass() );
    }

    public static class SQLXMLParser {
        public static final SQLXMLParser INSTANCE = new SQLXMLParser();

        private static PreparedStatement preparedStatement;

        public static void setPreparedStatement(PreparedStatement preparedStatement) {
            SQLXMLParser.preparedStatement = preparedStatement;
        }

        public Document XMLToDOM(SQLXML sqlxml) {
            try {
                InputStream binaryStream = sqlxml.getBinaryStream();
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document document = dBuilder.parse(binaryStream);
                document.getDocumentElement().normalize();
                return document;
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            throw new HibernateException("XMLToDOM failure");
        }

        public SQLXML DOMToXML(Document document) {
            try {
                SQLXML sqlxml = preparedStatement.getConnection().createSQLXML();
                SAXResult sax = sqlxml.setResult(SAXResult.class);
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.transform(new DOMSource(document), sax);
                return sqlxml;
            } catch (Exception e) {
                e.printStackTrace();
            }
            throw new HibernateException("DOMToXML failure");
        }
    }

}
