package org.hibernate.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * To use on a field or class method. Extracts a specific value
 * from an XML type column in the database. Can only be used to
 * read values from the database.
 *
 * @author  Lukasz Wos
 * @see ReadOnly
 */
@Target({FIELD, METHOD})
@Retention(RUNTIME)
public @interface XPath {

    String path();

    Class type();
}
