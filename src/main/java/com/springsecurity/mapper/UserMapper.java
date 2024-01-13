package com.springsecurity.mapper;

import com.springsecurity.domain.User;
import com.springsecurity.model.RegistrationRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "password", target = "password")
    User toUser(RegistrationRequestDto registrationRequestDto, String password);
}
