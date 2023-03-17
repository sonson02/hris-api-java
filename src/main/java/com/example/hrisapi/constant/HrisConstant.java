package com.example.hrisapi.constant;

import com.example.hrisapi.api.base.PaginatedResponse;
import lombok.var;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HrisConstant {

    public static PaginatedResponse<?> extractPaginationList(int page, int size, final List<?> listMaster) {
        final var fromIndex = (page - 1) * size;
        final var paginatedList = new ArrayList<>();

        if (fromIndex < listMaster.size()) {
            paginatedList.addAll(
                    listMaster.subList(fromIndex, Math.min(fromIndex + size, listMaster.size()))
            );
        }

        return PaginatedResponse
                .builder()
                .page(page)
                .size(size)
                .totalRecord(listMaster.size())
                .data(paginatedList)
                .build();
    }

    public static final Double MANAJEMEN_FEE_PERSENT = 0.1;

    private static final String PATTERN_DATE_FORMAT = "yyyy-MM-dd";

    public static String formatDate(Date requestDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_DATE_FORMAT);
        String responseDate = "";
        if (requestDate != null) {
            responseDate = sdf.format(requestDate);
        }

        return responseDate;
    }
}
