package com.amigoscode.customer;

import org.junit.jupiter.api.Test;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerRowMapperTest {

    @Test
    void mapRow() throws SQLException {
            //Given
            CustomerRowMapper customerRowMapper = new CustomerRowMapper();
            ResultSet resultSet = mock(ResultSet.class);
            when(resultSet.getInt("id")).thenReturn(1);
            when(resultSet.getInt("age")).thenReturn(19);
            when(resultSet.getString("name")).thenReturn("jamila");
            when(resultSet.getString("email")).thenReturn("jamila@gmail.com");

            //when
            Customer actual =  customerRowMapper.mapRow(resultSet,1);

            //then
            Customer expected = new Customer(1,"jamila" , "jamila@gmail.com",19);

        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
        assertThat(actual.getAge()).isEqualTo(expected.getAge());

    }
}