package api.mappers;

import api.domains.User;
import api.domains.dtos.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    public static final UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", source = "userDTO.username")
    @Mapping(target = "password", source = "userDTO.password")
    @Mapping(target = "authorities", source = "userDTO.authorities")
    @Mapping(target = "accountNonLocked", source = "userDTO.accountNonLocked")
    @Mapping(target = "accountNonExpired", source = "userDTO.accountNonExpired")
    @Mapping(target = "credentialsNonExpired", source = "userDTO.credentialsNonExpired")
    @Mapping(target = "enabled", source = "userDTO.enabled")
    public abstract User toUser(UserDTO userDTO);

}
