package org.hibernate.type.descriptor.sql;

import org.hibernate.type.descriptor.ValueBinder;
import org.hibernate.type.descriptor.ValueExtractor;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.BasicJavaDescriptor;
import org.hibernate.type.descriptor.java.JavaTypeDescriptor;
import org.hibernate.type.spi.TypeConfiguration;
import org.w3c.dom.Document;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import java.sql.*;

public class XMLTypeDescriptor implements SqlTypeDescriptor {
    public static final XMLTypeDescriptor INSTANCE = new XMLTypeDescriptor();

    public XMLTypeDescriptor() {
    }

    @Override
    public int getSqlType() {
        return Types.SQLXML;
    }

    @Override
    public boolean canBeRemapped() {
        return true;
    }

    @Override
    public <X> ValueBinder<X> getBinder(JavaTypeDescriptor<X> javaTypeDescriptor) {
        return new BasicBinder<X>( javaTypeDescriptor, this ) {
            @Override
            protected void doBind(PreparedStatement st, X value, int index, WrapperOptions options) throws SQLException {
                st.setSQLXML( index, javaTypeDescriptor.unwrap( value, SQLXML.class, options ) );
            }

            @Override
            protected void doBind(CallableStatement st, X value, String name, WrapperOptions options) throws SQLException {
                st.setSQLXML( name, javaTypeDescriptor.unwrap( value, SQLXML.class, options ) );
            }
        };
    }

    @Override
    public <X> ValueExtractor<X> getExtractor(JavaTypeDescriptor<X> javaTypeDescriptor) {
        return new BasicExtractor<X>( javaTypeDescriptor, this ) {
            @Override
            protected X doExtract(ResultSet rs, String name, WrapperOptions options) throws SQLException {
                return javaTypeDescriptor.wrap( rs.getSQLXML( name ), options );
            }

            @Override
            protected X doExtract(CallableStatement statement, int index, WrapperOptions options) throws SQLException {
                return javaTypeDescriptor.wrap( statement.getSQLXML( index ), options );
            }

            @Override
            protected X doExtract(CallableStatement statement, String name, WrapperOptions options) throws SQLException {
                return javaTypeDescriptor.wrap( statement.getSQLXML( name ), options );
            }
        };
    }

    //TODO: Ogarnac o co chodzi z ta funkcja na przykladach z innych deskryptorow z sql
    @Override
    public <T> BasicJavaDescriptor<T> getJdbcRecommendedJavaTypeMapping(TypeConfiguration typeConfiguration) {
        System.out.println("getJdbcRecommendedJavaTypeMapping from sql");
        return null;
    }
}