package com.n1nt3nd0.social_media_app.mapper;

import com.n1nt3nd0.social_media_app.dto.request.UserCreateEditDto;
import com.n1nt3nd0.social_media_app.dto.response.UserReadDto;
import com.n1nt3nd0.social_media_app.entity.UserSocialMedia;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UserInformationMapper.class)
public interface UserMapper {
//    @Mapping(source = "email", target = "userInformation.email")
//    @Mapping(source = "firstName", target = "userInformation.firstName")
//    @Mapping(source = "lastName", target = "userInformation.lastName")
//    @Mapping(source = "status", target = "userInformation.status")
    @Mapping(source = "userInformationCreateEditDto", target = "userInformation")
//    @Mapping(source = "birthday", target = "userInformation.birthday", dateFormat = "yyyy-MM-dd")
    UserSocialMedia toUser(UserCreateEditDto userCreateEditDto);
    @Mapping(source = "userInformation", target = "userInformationReadDto")
    UserReadDto toUserReadDto(UserSocialMedia userSocialMedia);
}

//@Mapper
//public interface EmployeeMapper {
//
//    @Mapping(target = "employeeId", source = "entity.id")
//    @Mapping(target = "employeeName", source = "entity.name")
//    EmployeeDTO employeeToEmployeeDTO(Employee entity);
//
//    @Mapping(target = "id", source = "dto.employeeId")
//    @Mapping(target = "name", source = "dto.employeeName")
//    Employee employeeDTOtoEmployee(EmployeeDTO dto);
//}
