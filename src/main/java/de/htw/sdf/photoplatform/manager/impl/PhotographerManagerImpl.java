/*
 *
 * Copyright (C) 2014
 *
 */
package de.htw.sdf.photoplatform.manager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.htw.sdf.photoplatform.exception.common.AbstractBaseException;
import de.htw.sdf.photoplatform.exception.common.ManagerException;
import de.htw.sdf.photoplatform.manager.PhotographerManager;
import de.htw.sdf.photoplatform.manager.common.DAOReferenceCollector;
import de.htw.sdf.photoplatform.persistence.model.Collection;
import de.htw.sdf.photoplatform.persistence.model.CollectionImage;
import de.htw.sdf.photoplatform.persistence.model.Image;
import de.htw.sdf.photoplatform.persistence.model.User;
import de.htw.sdf.photoplatform.persistence.model.UserImage;

/**
 * business methods for categories.
 *
 * @author <a href="mailto:s0541962@htw-berlin.de">Vincent Schwarzer</a>
 * @author <a href="mailto:s0521159@htw-berlin.de">Sergej Meister</a>
 */
@Service
@Transactional
public class PhotographerManagerImpl extends DAOReferenceCollector implements
        PhotographerManager {

    @Override
    public Collection update(Collection entity) {
        return collectionDAO.update(entity);
    }

    @Override
    public void delete(Collection entity) {
        collectionDAO.delete(entity);
    }

    @Override
    public Collection getCollectionById(long id) {
        return collectionDAO.findOne(id);
    }

    @Override
    public List<Collection> getAllCollection() {
        return collectionDAO.findAll();
    }

    @Override
    public void deleteAllCollections() {
        collectionDAO.findAll();
    }

    @Override
    public Collection getCollectionById(Long collectionId) {
        return collectionDAO.findById(collectionId);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Collection> getCollectionByUser(long userId, int start, int count) {
        return collectionDAO.findCollectionsByUser(userId, start, count);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserImage createPhotographImage(User photograph, Image image) throws ManagerException {
        List<Image> images = new ArrayList<>();
        images.add(image);
        return createPhotographImage(photograph, images).get(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserImage> createPhotographImage(User photograph, List<Image> images) throws ManagerException {
        List<UserImage> result = new ArrayList<>();
        for(Image image : images){
            imageDAO.create(image);

            UserImage userImage = new UserImage();
            userImage.setOwner(photograph);
            userImage.setUser(photograph);
            userImage.setImage(image);
            userImageDAO.create(userImage);
            result.add(userImage);
        }

        if(images.size() != result.size()){
            throw new RuntimeException("The size of image to create is different to size of created images. This should not happen");
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection createCollection(final Long userId, final String name, final String description) {
        User user = userDAO.findById(userId);

        Collection collection = new Collection();
        collection.setUser(user);
        collection.setName(name);
        collection.setDescription(description);
        // Not public, not in show case
        collection.setPublic(false);

        collectionDAO.create(collection);

        return collection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CollectionImage addImageToCollection(Long userId, Long collectionId, Long imageId) throws ManagerException {
        List<Long> imageIds = new ArrayList<>();
        imageIds.add(imageId);
        return addImagesToCollection( userId, collectionId, imageIds).get(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CollectionImage> addImagesToCollection(Long userId, Long collectionId, List<Long> imageIds) throws ManagerException {
        List<CollectionImage> result = new ArrayList<>();

        if(imageIds == null || imageIds.isEmpty()){
            throw new ManagerException(AbstractBaseException.NOT_FOUND);
        }

        Collection affectedCollection = getCollection(userId, collectionId);

        List<UserImage> imagesToAdd = userImageDAO.getUserImagesBy(userId, imageIds);
        if(imagesToAdd.isEmpty()){
            throw new ManagerException(AbstractBaseException.NOT_FOUND);
        }

        for (UserImage userImage : imagesToAdd) {
            CollectionImage collectionImage = new CollectionImage();
            collectionImage.setCollection(affectedCollection);
            collectionImage.setImage(userImage.getImage());
            collectionImageDAO.create(collectionImage);
            result.add(collectionImage);
        }

        if(result.size() != imageIds.size()){
            throw new RuntimeException("The size of image id's is different to size of founded images. This should not happen");
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean deleteImagesFromCollection(Long userId, Long collectionId, Long imageId) throws ManagerException {
        List<Long> imageIds = new ArrayList<>();
        imageIds.add(imageId);
        return deleteImagesFromCollection(userId, collectionId, imageIds);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean deleteImagesFromCollection(Long userId, Long collectionId, List<Long> imageIds) throws ManagerException {
        if (collectionId == null) {
            throw new ManagerException(AbstractBaseException.COLLECTION_ID_NOT_VALID);
        }

        if(imageIds == null || imageIds.isEmpty()) {
            throw new ManagerException(AbstractBaseException.NOT_FOUND);
        }

        Set<CollectionImage> collectionImages = collectionDAO.findCollectionImagesBy(userId, collectionId, imageIds);
        if(collectionImages.size() != imageIds.size()){
            throw new RuntimeException("The size of image id's is different to size of founded images. This should not happen");
        }

        for(CollectionImage collectionImage : collectionImages) {
            collectionImageDAO.delete(collectionImage);
        }

        return true ;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean deleteCollection(Long userId, Long collectionId) throws ManagerException {

        Collection affectedCollection = getCollection(userId, collectionId);

        for(CollectionImage collectionImage : affectedCollection.getCollectionImages()){
            //Hm, collection include images
            //what should happen with images by deleting a collection.
            //My Solution, delete reference between collection and image, but not the image!
            collectionImageDAO.delete(collectionImage);
        }

        //now we can remove the collection!
        collectionDAO.delete(affectedCollection);

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean addToShowCase(Long userId, Long collectionId) throws ManagerException {
        return updateCollectionsPublicValue(userId, collectionId, Boolean.TRUE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean removeFromShowCase(Long userId, Long collectionId) throws ManagerException {
        return updateCollectionsPublicValue(userId, collectionId, Boolean.FALSE);
    }

    private Boolean updateCollectionsPublicValue(Long userId, Long collectionId, Boolean value) throws ManagerException {
        Collection affectedCollection = getCollection(userId, collectionId);
        affectedCollection.setPublic(value);
        collectionDAO.update(affectedCollection);
        return true ;
    }

    private Collection getCollection(Long userId, Long collectionId) throws ManagerException {
        if (collectionId == null) {
            throw new ManagerException(AbstractBaseException.COLLECTION_ID_NOT_VALID);
        }

        Collection affectedCollection = collectionDAO.findByUserAndCollectionId(userId,collectionId);
        if(affectedCollection == null){
            throw new ManagerException(AbstractBaseException.COLLECTION_ID_NOT_VALID);
        }

        return affectedCollection;
    }
}
