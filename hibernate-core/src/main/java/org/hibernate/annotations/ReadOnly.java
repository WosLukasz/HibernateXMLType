package org.hibernate.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * To use on a Type elements. Causes that you can only read data from the database.
 *
 * @author Lukasz Wos
 */
@java.lang.annotation.Target({ElementType.TYPE})
@Retention( RetentionPolicy.RUNTIME )
public @interface ReadOnly {
}
