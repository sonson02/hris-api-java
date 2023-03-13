package com.example.hrisapi.mapper;

import com.example.hrisapi.dto.request.TempatTugasMasterRequest;
import com.example.hrisapi.dto.response.TempatTugasMasterResponse;
import com.example.hrisapi.entity.TempatTugasMasterEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TempatTugasMasterMapper {
    TempatTugasMasterResponse map(TempatTugasMasterEntity tempatTugasMasterEntity);

    TempatTugasMasterEntity mapRequest(TempatTugasMasterRequest tempatTugasMasterRequest);

}
