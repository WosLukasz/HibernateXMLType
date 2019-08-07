package org.hibernate.type;

import java.sql.SQLXML;

public class XMLType extends AbstractSingleColumnStandardBasicType<SQLXML> {
    public static final XMLType INSTANCE = new XMLType();

    public XMLType() {
        super(  org.hibernate.type.descriptor.sql.XMLTypeDescriptor.INSTANCE,
                org.hibernate.type.descriptor.java.XMLTypeDescriptor.INSTANCE );
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