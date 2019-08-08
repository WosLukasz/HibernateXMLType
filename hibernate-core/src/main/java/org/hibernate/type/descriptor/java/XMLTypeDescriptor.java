package org.hibernate.type.descriptor.java;

import org.hibernate.HibernateException;
import org.hibernate.internal.SessionImpl;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.spi.JdbcRecommendedSqlTypeMappingContext;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLXML;

public class XMLTypeDescriptor extends AbstractTypeDescriptor<Document> {
    public static final XMLTypeDescriptor INSTANCE = new XMLTypeDescriptor();

    public XMLTypeDescriptor() {
        super( Document.class );
    }

    @Override
    public String toString(Document value) {
        return null;
    }

    @Override
    public Document fromString(String string) {
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


    //TODO: Ogarnac o co chodzi z ta funkcja na przykladach z innych deskryptorow z java
    @Override
    public SqlTypeDescriptor getJdbcRecommendedSqlType(JdbcRecommendedSqlTypeMappingContext context) {
        System.out.println("getJdbcRecommendedJavaTypeMapping from java");
        return null;
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
                //TODO: Sprawdzic czy tutaj juz jest weryfikacja poprawnosci tego xml a jak nie to dorobic
//                                Schema schema = null;
//                                try {
//                                    String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
//                                    SchemaFactory factory = SchemaFactory.newInstance(language);
//                                    schema = factory.newSchema(new File(name));
//                                } catch (Exception e) {
//                                    e.printStackStrace();
//                                }
//                                Validator validator = schema.newValidator();
//                                validator.validate(new DOMSource(document));
                //System.out.println("document:" + document.getFirstChild().getNodeName());
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
