package org.xtremeware.iudex.dao;

import java.util.List;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.vo.ProgramVo;

/**
 *
 * @author josebermeo
 */
public interface ProgramDao <E> extends CrudDao<ProgramVo, E> {
    
    public List<ProgramVo> getByNameLike(DataAccessAdapter<E> em, String name);
    
}
