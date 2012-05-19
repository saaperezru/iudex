package org.xtremeware.iudex.entity;

public interface Entity<E> {

    public Long getId();

    public void setId(Long id);

    public E toVo();
}
