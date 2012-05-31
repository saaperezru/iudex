package org.xtremeware.iudex.vo;

/**
 *
 * @author josebermeo
 */
public abstract class IdentifiableValueObject<E> implements ValueObject {

    private E id;

    public E getId() {
        return id;
    }

    public void setId(E id) {
        this.id = id;
    }
}
