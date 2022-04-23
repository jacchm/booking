package com.jacchm.project.mapper;

import com.jacchm.project.controller.dto.UserCreationDTO;
import com.jacchm.project.controller.dto.UserDTO;
import com.jacchm.project.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

  String GRAND_AUTHORITIES_SEPARATOR = ",";

  @Mapping(source = "userCreationDTO.roles", target = "authorities")
  User mapUserCreationDTOtoUser(final UserCreationDTO userCreationDTO);

  @Mapping(target = "authorities", expression = "java(mapAuthoritiesListToString(user.getAuthorities()))")
  UserDTO mapUserToUserDTO(final User user);

  default String mapAuthoritiesListToString(final Collection<? extends GrantedAuthority> authorities) {
    return authorities
        .stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(GRAND_AUTHORITIES_SEPARATOR));
  }

}
