package taskmanager.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import taskmanager.dto.UserDto;
import taskmanager.entity.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-19T19:54:47+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public void updateUserFromDto(UserDto userDto, User user) {
        if ( userDto == null ) {
            return;
        }

        if ( userDto.getName() != null ) {
            user.setName( userDto.getName() );
        }
        if ( userDto.getSurname() != null ) {
            user.setSurname( userDto.getSurname() );
        }
        if ( userDto.getPassword() != null ) {
            user.setPassword( userDto.getPassword() );
        }
    }
}
