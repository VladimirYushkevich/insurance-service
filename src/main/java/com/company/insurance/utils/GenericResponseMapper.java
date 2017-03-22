package com.company.insurance.utils;

import com.company.insurance.dto.PageDTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.springframework.beans.BeanUtils.copyProperties;

/**
 * Request->Model->Response
 */

@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public abstract class GenericResponseMapper<R, M> {

    private Class<R> responseClass;

    R buildDTO(M model) {
        final R dto = createResponseInstance(responseClass)
                .orElseThrow(() -> new IllegalArgumentException(String.format("can't create instance of %s", model)));

        copyProperties(model, dto);

        return dto;
    }

    PageDTO<R> buildPageResponseDTO(Page<M> modelPage) {
        final List<R> entries = modelPage.getContent().parallelStream()
                .map(this::buildDTO)
                .collect(toList());

        return PageDTO.<R>builder()
                .totalPages(modelPage.getTotalPages())
                .totalEntries(modelPage.getTotalElements())
                .entries(entries)
                .build();
    }

    private Optional<R> createResponseInstance(Class<R> clazz) {
        try {
            return Optional.of(clazz.newInstance());
        } catch (Exception e) {
            log.error("can't create instance of {} response class", clazz);
            e.printStackTrace();
        }

        return Optional.empty();
    }
}
