package org.xtremeware.iudex.entity;

public interface Entity<E> {

    Long getId();

    void setId(Long id);

    E toVo();
}
