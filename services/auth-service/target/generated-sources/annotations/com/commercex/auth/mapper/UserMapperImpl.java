package com.commercex.auth.mapper;

import com.commercex.auth.dto.request.RegisterRequest;
import com.commercex.auth.dto.response.UserResponse;
import com.commercex.auth.entity.Role;
import com.commercex.auth.entity.User;
import com.commercex.auth.entity.UserStatus;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-09-13T01:19:00+0530",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toEntity(RegisterRequest request) {
        if ( request == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.email( request.email() );
        user.firstName( request.firstName() );
        user.lastName( request.lastName() );
        user.phone( request.phone() );

        return user.build();
    }

    @Override
    public UserResponse toResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UUID id = null;
        String firstName = null;
        String lastName = null;
        String email = null;
        String phone = null;
        UserStatus status = null;
        Set<String> roles = null;

        id = user.getId();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        email = user.getEmail();
        phone = user.getPhone();
        status = user.getStatus();
        roles = roleSetToStringSet( user.getRoles() );

        UserResponse userResponse = new UserResponse( id, firstName, lastName, email, phone, status, roles );

        return userResponse;
    }

    protected Set<String> roleSetToStringSet(Set<Role> set) {
        if ( set == null ) {
            return null;
        }

        Set<String> set1 = LinkedHashSet.newLinkedHashSet( set.size() );
        for ( Role role : set ) {
            set1.add( map( role ) );
        }

        return set1;
    }
}
