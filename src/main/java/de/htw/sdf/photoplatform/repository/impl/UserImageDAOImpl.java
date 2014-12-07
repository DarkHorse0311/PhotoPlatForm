/*
 *
 * Copyright (C) 2014
 *
 */

package de.htw.sdf.photoplatform.repository.impl;

import java.util.List;

import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import de.htw.sdf.photoplatform.persistence.model.User;
import de.htw.sdf.photoplatform.persistence.model.UserImage;
import de.htw.sdf.photoplatform.repository.UserImageDAO;
import de.htw.sdf.photoplatform.repository.common.GenericDAOImpl;

/**
 * Repository methods for relation between user and image.
 *
 * @author Sergej Meister.
 */
@Repository
@Transactional
public class UserImageDAOImpl extends GenericDAOImpl<UserImage> implements
        UserImageDAO {

    /**
     * UserImage DAO constructor.
     */
    public UserImageDAOImpl() {
        super();
        setClazz(UserImage.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<UserImage> getUserImagesBy(User user) {
        StringBuilder queryBuilder = initSelectQuery();
        queryBuilder.append("WHERE user.id = ?1");
        Query query = createQuery(queryBuilder.toString());
        query.setParameter(1, user.getId());
        return (List<UserImage>) query.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<UserImage> getUserImagesBy(long ownerId, List<Long> imageIds) {
        StringBuilder queryBuilder = initSelectQuery();
        queryBuilder.append("WHERE owner.id = :ownerId AND image.id IN (:imageIds)");
        Query query = createQuery(queryBuilder.toString());
        query.setParameter("ownerId", ownerId);
        query.setParameter("imageIds", imageIds);
        return (List<UserImage>) query.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserImage> getPhotographImages(User owner) {
        Query query = initPhotographImageQuery(owner);

        return (List<UserImage>) query.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserImage> getPhotographImages(User owner, int start, int count) {
        Query query = initPhotographImageQuery(owner);
        query.setFirstResult(start);
        query.setMaxResults(count);
        return (List<UserImage>) query.getResultList();
    }

    private Query initPhotographImageQuery(User owner) {
        StringBuilder queryBuilder = initSelectQuery();
        queryBuilder.append("WHERE owner.id = :ownerId AND user.id = :userId");
        Query query = createQuery(queryBuilder.toString());
        query.setParameter("ownerId", owner.getId());
        query.setParameter("userId", owner.getId());
        return query;
    }

    private StringBuilder initSelectQuery() {
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT DISTINCT(userImage) FROM UserImage userImage ");
        queryBuilder.append("LEFT JOIN FETCH userImage.image image ");
        queryBuilder.append("LEFT JOIN FETCH userImage.owner owner ");
        queryBuilder.append("LEFT JOIN FETCH userImage.user user ");

        return queryBuilder;
    }
}
