package com.uniovi.notaneitor.services;
import com.uniovi.notaneitor.entities.Employee;
import org.springframework.stereotype.Service;
import com.uniovi.notaneitor.repositories.EmployeesRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.*;
@Service("employeeDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    private final EmployeesRepository employeesRepository;
    public UserDetailsServiceImpl(EmployeesRepository employeesRepository) {
        this.employeesRepository = employeesRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String dni) throws UsernameNotFoundException {
        Employee employee = employeesRepository.findByDni(dni);
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        //grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_STUDENT"));
        grantedAuthorities.add(new SimpleGrantedAuthority(employee.getRole()));
        if (employee == null) {
            throw new UsernameNotFoundException(dni);
        }
        return new org.springframework.security.core.userdetails.User(
                employee.getDni(), employee.getPassword(), grantedAuthorities);
    }
}
