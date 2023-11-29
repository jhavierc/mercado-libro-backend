package com.mercadolibro.util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashSet;
import java.util.Set;

public class Util {
    /**
     * Gets the names of properties that have null values in the source object.
     *
     * @param source The source object from which to obtain the names of null properties.
     * @return An array of type String containing the names of null properties in the source object.
     */
    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = wrappedSource.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = wrappedSource.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * Merges the non-null properties from the source object into the target object.
     * Null properties in the source object will not be copied into the target object.
     *
     * @param src    The source object from which non-null properties will be copied.
     * @param target The target object into which non-null properties from the source object will be copied.
     */
    public static void mergeObjects(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }

    /**
     * Get user email from SecurityContextHolder
     * @see org.springframework.security.core.context.SecurityContextHolder
     * @return user email
     */
    public static String getUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
