package org.xtremeware.iudex.dao;

import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.vo.ConfirmationKeyVo;

/**
 *
 * @author josebermeo
 */
public interface ConfirmationKeyDao<E> extends CrudDao<ConfirmationKeyVo, E> {

    public ConfirmationKeyVo getByConfirmationKey(DataAccessAdapter<E> em, String confirmationKey);
}
