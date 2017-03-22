package com.company.insurance.utils;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.springframework.beans.BeanUtils.copyProperties;

/**
 * Generic class for mapping models to related DTOs and vice versa.
 * Request->Model->Response
 */

@Slf4j
@NoArgsConstructor
public abstract class GenericRequestResponseMapper<T, R, M> extends GenericResponseMapper<R, M> {

    private Class<M> modelClass;

    GenericRequestResponseMapper(Class<M> modelClass, Class<R> responseClass) {
        super(responseClass);
        this.modelClass = modelClass;
    }

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    M buildModel(T dto) {
        final M model = createModelInstance(modelClass)
                .orElseThrow(() -> new IllegalArgumentException(String.format("can't create instance of %s", dto)));

        copyProperties(dto, model);

        return model;
    }

    private Optional<M> createModelInstance(Class<M> clazz) {
        try {
            return Optional.of(clazz.newInstance());
        } catch (Exception e) {
            log.error("can't create instance of {} model class", clazz);
            e.printStackTrace();
        }

        return Optional.empty();
    }
}
