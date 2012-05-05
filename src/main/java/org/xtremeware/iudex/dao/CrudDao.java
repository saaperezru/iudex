package org.xtremeware.iudex.dao;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.xtremeware.iudex.entity.Entity;

public abstract class CrudDao<E extends Entity> implements CrudDaoInterface<E> {

    /**
     * Returns the same received Entity after being persisted in the
     * EntityManager em
     *
     * @param em EntityManager with which the entity will be persisted
     * @param entity Entity that will be persisted
     * @return The received entity after being persisted
     */
    @Override
    public E persist(EntityManager em, E entity) {
        checkEntityManager(em);
        em.persist(entity);
        return entity;
    }

    /**
     * Returns the received Entity after being merged in the EntityManager em
     *
     * @param em EntityManager with which the entity will be persisted
     * @param entity Entity that will be merged
     * @return The received entity after being merged and persisted
     */
    @Override
    public E merge(EntityManager em, E entity) {
        checkEntityManager(em);
        return em.merge(entity);
    }

    /**
     * Deletes the entity identified by id within the received EntityManager
     *
     * @param em EntityManager with which the entity will be deleted
     */
    @Override
    public void remove(EntityManager em, long id) {
        checkEntityManager(em);
        E entity = getById(em, id);
        if (entity == null) {
            throw new NoResultException("No entity found for id " + String.valueOf(id) + "while triying to delete the associated record");
        }
        em.remove(entity);
        
    }

    /**
     * Returns an Entity whose Id is equal to the received id.
     *
     * @param em EntityManager within which the entity will be searched
     * @param entity Entity that will be searched
     * @return The received entity after being merged and persisted
     */
    @Override
    public E getById(EntityManager em, long id) {
        checkEntityManager(em);
        return (E) em.find(returnedClass(), id);
        
        
    }
    
    @Override
    public void checkEntityManager(EntityManager em) {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }
    }

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
        return getTypeArguments(CrudDaoInterface.class, getClass()).get(0);
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
