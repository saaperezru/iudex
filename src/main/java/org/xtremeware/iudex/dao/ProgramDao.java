package org.xtremeware.iudex.dao;

import java.util.List;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.da.DataAccessException;
import org.xtremeware.iudex.vo.ProgramVo;

/**
 * DAO for ProgramVo.
 * 
 * @author josebermeo
 */
public interface ProgramDao <E> extends CrudDao<ProgramVo, E> {
    
    /**
     * Search a program which name contains the given parameter name
     *
     * @param em the DataAccessAdapter
     * @param name
     * @return Return a list of programEntity objects
     */
    public List<ProgramVo> getByNameLike(DataAccessAdapter<E> em, String name)throws DataAccessException;
    
}
