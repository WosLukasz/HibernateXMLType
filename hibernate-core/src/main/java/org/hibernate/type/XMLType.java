package org.hibernate.type;

import org.hibernate.MappingException;
import org.hibernate.engine.spi.Mapping;

import java.sql.SQLXML;

public class XMLType extends AbstractSingleColumnStandardBasicType<SQLXML> {
    public static final XMLType INSTANCE = new XMLType();

    public XMLType() {
        super(  org.hibernate.type.descriptor.sql.XMLTypeDescriptor.INSTANCE,
                org.hibernate.type.descriptor.java.XMLTypeDescriptor.INSTANCE );
    }

    @Override
    public String[] getRegistrationKeys() {
        return new String[] {getName(), SQLXML.class.getName()};
    }

    @Override
    public String getName() {
        return "XML";
    }

    @Override
    protected boolean registerUnderJavaType() {
        return true;
    }
}