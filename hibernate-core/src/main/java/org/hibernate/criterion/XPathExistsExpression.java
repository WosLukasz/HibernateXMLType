package org.hibernate.criterion;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.dialect.*;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.TypedValue;

/**
 * Represents a strategy for checking if xpath expression is true
 *
 * @author Lukasz Wos
 */
public class XPathExistsExpression implements Criterion {

    private final String propertyName;
    private final String xPath;

    protected XPathExistsExpression(String propertyName, String xPath) {
        this.propertyName = propertyName;
        this.xPath = xPath;
    }

    @Override
    public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {

        final String[] columns = criteriaQuery.findColumns( propertyName, criteria );
        final SessionFactoryImplementor factory = criteriaQuery.getFactory();
        final Dialect dialect = factory.getDialect();

        if ( columns.length > 1 ) {
            throw new HibernateException("Only one column can be in xPath query");
        }

        String fragment = "";

        for ( int i = 0; i < columns.length; i++ ) {
            if(dialect instanceof SQLServerDialect || dialect instanceof SQLServer2012Dialect || dialect instanceof SQLServer2005Dialect || dialect instanceof SQLServer2008Dialect) {
                fragment = columns[i] +".exist('" + xPath + "') = 1";
            } else if(dialect instanceof OracleDialect || dialect instanceof Oracle8iDialect ||
                    dialect instanceof Oracle9Dialect || dialect instanceof Oracle9iDialect ||
                    dialect instanceof Oracle10gDialect || dialect instanceof Oracle12cDialect) {

                fragment = "EXISTSNODE(" + columns[i] + ", '" + xPath + "') = 1 ";

            } else if(dialect instanceof PostgreSQLDialect || dialect instanceof PostgreSQL81Dialect ||
                    dialect instanceof PostgreSQL9Dialect || dialect instanceof PostgreSQL10Dialect ||
                    dialect instanceof PostgreSQL82Dialect || dialect instanceof PostgreSQL91Dialect ||
                    dialect instanceof PostgreSQL92Dialect || dialect instanceof PostgreSQL93Dialect ||
                    dialect instanceof PostgreSQL94Dialect || dialect instanceof PostgreSQL95Dialect) {

                fragment = "xmlexists('" + xPath + "' PASSING BY REF " + columns[i] + ")";

            } else {
                throw new HibernateException("Could not guess dialect. Check if your dialect is supported.");
            }
        }

        return fragment;
    }

    @Override
    public TypedValue[] getTypedValues(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        return new TypedValue[0];
    }

}
