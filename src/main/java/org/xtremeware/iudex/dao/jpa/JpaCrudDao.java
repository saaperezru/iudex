package org.xtremeware.iudex.dao.jpa;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.da.DataAccessException;
import org.xtremeware.iudex.dao.CrudDao;
import org.xtremeware.iudex.entity.Entity;
import org.xtremeware.iudex.vo.ValueObject;

public abstract class JpaCrudDao<E extends ValueObject, F extends Entity<E>> implements CrudDao<E, EntityManager> {

    /**
     * Returns the same received ValueObject after being persisted in the
     * EntityManager em
     *
     * @param em DataAccessAdapter with which the entity will be persisted
     * @param vo ValueObject that will be persisted
     * @return The received entity after being persisted
     */
    @Override
    public E persist(DataAccessAdapter<EntityManager> em, E vo) throws DataAccessException {
        checkDataAccessAdapter(em);
        em.getDataAccess().persist(voToEntity(em, vo));
        return vo;
    }

    /**
     * Returns the received ValueObject after being merged in the EntityManager
     * em
     *
     * @param em DataAccessAdapter with which the entity will be persisted
     * @param vo ValueObject that will be merged
     * @return The received ValueObject after being merged and persisted
     */
    @Override
    public E merge(DataAccessAdapter<EntityManager> em, E vo) throws DataAccessException {
        checkDataAccessAdapter(em);
        return (E) ((F) em.getDataAccess().merge(voToEntity(em, vo))).toVo();
    }

    private F getEntityById(DataAccessAdapter<EntityManager> em, long id) throws DataAccessException {
        checkDataAccessAdapter(em);
        return (F) em.getDataAccess().find(getEntityClass(), id);
    }

    /**
     * Deletes the ValueObject identified by id within the received
     * DataAccessAdapter
     *
     * @param em DataAccessAdapter with which the entity will be deleted
     */
    @Override
    public void remove(DataAccessAdapter<EntityManager> em, long id) throws DataAccessException {
        F entity = getEntityById(em, id);
        if (entity == null) {
            throw new NoResultException("No entity found for id " + String.valueOf(id) + "while triying to delete the associated record");
        }
        em.getDataAccess().remove(entity);
    }

    /**
     * Returns an ValueObject whose Id is equal to the received id.
     *
     * @param em EntityManager within which the entity will be searched
     * @param entity ValueObject that will be searched
     * @return The received entity after being merged and persisted
     */
    /**
     * Returns an ValueObject whose Id is equal to the received id.
     *
     * @param em DataAccessAdapter within which the entity will be searched
     * @param id of the ValueObject that will be searched
     * @return The received ValueObject after being merged and persisted
     */
    @Override
    public E getById(DataAccessAdapter<EntityManager> em, long id) throws DataAccessException {
        return (E) (getEntityById(em, id)).toVo();
    }

    @Override
    public void checkDataAccessAdapter(DataAccessAdapter em) throws DataAccessException {
        if (em == null) {
            throw new DataAccessException("DataAccessAdapter em cannot be null");
        }
        if (em.getDataAccess() == null) {
            throw new DataAccessException("DataAccess cannot be null");
        }
    }

    protected List<E> entitiesToVos(List<F> list) {
        ArrayList<E> arrayList = new ArrayList<E>();
        for (F entity : list) {
            arrayList.add(entity.toVo());
        }
        return arrayList;
    }

    protected abstract F voToEntity(DataAccessAdapter<EntityManager> em, E vo);

    protected abstract Class getEntityClass();

    /**
     * /**
     * Returns a list with all of the entities actually in the database
     *
     * @param em EntityManager within which the entity will be brought
     * @return List of all entities in the database
     *
     * public abstract List<E> list(EntityManager em);
     *
     * public abstract List<E> list(EntityManager em, int from, int max);
     *
     * public abstract int count(EntityManager em);
     *
     *
     */
    private Class returnedClass() {
        return getTypeArguments(JpaCrudDao.class, getClass()).get(0);
    }

    /**
     * Get the underlying class for a type, or null if the type is a variable
     * type. Taken from http://www.artima.com/weblogs/viewpost.jsp?thread=208860
     *
     * @param type the type
     * @return the underlying class
     */
    private static Class<?> getClass(Type type) {
        if (type instanceof Class) {
            return (Class) type;
        } else if (type instanceof ParameterizedType) {
            return getClass(((ParameterizedType) type).getRawType());
        } else if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            Class<?> componentClass = getClass(componentType);
            if (componentClass != null) {
                return Array.newInstance(componentClass, 0).getClass();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Get the actual type arguments a child class has used to extend a generic
     * base class. Taken from
     * http://www.artima.com/weblogs/viewpost.jsp?thread=208860
     *
     * @param baseClass the base class
     * @param childClass the child class
     * @return a list of the raw classes for the actual type arguments.
     */
    private static <T> List<Class<?>> getTypeArguments(
            Class<T> baseClass, Class<? extends T> childClass) {
        Map<Type, Type> resolvedTypes = new HashMap<Type, Type>();
        Type type = childClass;
        // start walking up the inheritance hierarchy until we hit baseClass
        while (!getClass(type).equals(baseClass)) {
            if (type instanceof Class) {
                // there is no useful information for us in raw types, so just keep going.
                type = ((Class) type).getGenericSuperclass();
            } else {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Class<?> rawType = (Class) parameterizedType.getRawType();

                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                TypeVariable<?>[] typeParameters = rawType.getTypeParameters();
                for (int i = 0; i < actualTypeArguments.length; i++) {
                    resolvedTypes.put(typeParameters[i], actualTypeArguments[i]);
                }

                if (!rawType.equals(baseClass)) {
                    type = rawType.getGenericSuperclass();
                }
            }
        }

        // finally, for each actual type argument provided to baseClass, determine (if possible)
        // the raw class for that type argument.
        Type[] actualTypeArguments;
        if (type instanceof Class) {
            actualTypeArguments = ((Class) type).getTypeParameters();
        } else {
            actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
        }
        List<Class<?>> typeArgumentsAsClasses = new ArrayList<Class<?>>();
        // resolve types by chasing down type variables.
        for (Type baseType : actualTypeArguments) {
            while (resolvedTypes.containsKey(baseType)) {
                baseType = resolvedTypes.get(baseType);
            }
            typeArgumentsAsClasses.add(getClass(baseType));
        }
        return typeArgumentsAsClasses;
    }
}
