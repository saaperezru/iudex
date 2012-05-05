package org.xtremeware.iudex.vo;

import java.io.Serializable;

/**
 *
 * @author jdbermeol
 */
public interface ValueObject extends Serializable {

    @Override
    public abstract boolean equals(Object ob);

    @Override
    public abstract int hashCode();

    @Override
    public abstract String toString();
}
