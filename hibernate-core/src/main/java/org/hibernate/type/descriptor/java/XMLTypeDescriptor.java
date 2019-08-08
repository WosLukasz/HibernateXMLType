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
import java.sql.SQLException;
import java.sql.SQLXML;

public class XMLTypeDescriptor extends AbstractTypeDescriptor<SQLXML> {
    public static final XMLTypeDescriptor INSTANCE = new XMLTypeDescriptor();

    public XMLTypeDescriptor() {
        super( SQLXML.class );
    }

    @Override
    public String toString(SQLXML value) {
        try {
            return value.getString();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public SQLXML fromString(String string) {
        return null;
    }

    @Override
    public <X> X unwrap(SQLXML value, Class<X> type, WrapperOptions options) {
        if ( value == null ) {
            return null;
        }
        if ( SQLXML.class.isAssignableFrom( type ) ) {
            return (X) value;
        }
        throw unknownUnwrap( type );
    }

    @Override
    public <X> SQLXML wrap(X value, WrapperOptions options) {
        if ( value == null ) {
            return null;
        }
        if ( SQLXML.class.isInstance( value ) ) {
            return (SQLXML)value;
        }
        throw unknownWrap( value.getClass() );
    }
}