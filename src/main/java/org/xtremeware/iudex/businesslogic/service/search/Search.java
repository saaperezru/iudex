package org.xtremeware.iudex.businesslogic.service.search;

import java.util.List;

/**
 *
 * @author josebermeo
 */
public interface Search<E> {
    public List<E> search(String query);
}
