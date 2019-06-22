package org.hibernate.type.descriptor.sql;

import org.hibernate.type.descriptor.ValueBinder;
import org.hibernate.type.descriptor.ValueExtractor;
import org.hibernate.type.descriptor.java.BasicJavaDescriptor;
import org.hibernate.type.descriptor.java.JavaTypeDescriptor;
import org.hibernate.type.spi.TypeConfiguration;

import java.sql.Types;


//TODO: DOKONCZYC PISANIE
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
    public <T> BasicJavaDescriptor<T> getJdbcRecommendedJavaTypeMapping(TypeConfiguration typeConfiguration) {
        return null;
    }

    @Override
    public <X> ValueBinder<X> getBinder(JavaTypeDescriptor<X> javaTypeDescriptor) {
        return null;
    }

    @Override
    public <X> ValueExtractor<X> getExtractor(JavaTypeDescriptor<X> javaTypeDescriptor) {
        return null;
    }
}
