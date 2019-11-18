package org.hibernate.type;


import org.w3c.dom.Document;

/**
 * A type that maps between {@link java.sql.Types#SQLXML SQLXML} and {@link Document}
 *
 * @author Lukasz Wos
 */
public class XMLType extends AbstractSingleColumnStandardBasicType<Document> {
    public static final XMLType INSTANCE = new XMLType();

    public XMLType() {
        super(  org.hibernate.type.descriptor.sql.XMLTypeDescriptor.INSTANCE,
                org.hibernate.type.descriptor.java.XMLTypeDescriptor.INSTANCE );
    }

    @Override
    public String[] getRegistrationKeys() {
        return new String[] { getName(), Document.class.getName() };
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
