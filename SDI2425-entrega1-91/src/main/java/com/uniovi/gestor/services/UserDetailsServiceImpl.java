package com.uniovi.gestor.services;
import com.uniovi.gestor.entities.Employee;
import org.springframework.stereotype.Service;
import com.uniovi.gestor.repositories.EmployeesRepository;
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
        if (employee == null) {
            throw new UsernameNotFoundException(dni);
        }
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        grantedAuthorities.add(new SimpleGrantedAuthority(employee.getRole()));

        return new org.springframework.security.core.userdetails.User(
                employee.getDni(), employee.getPassword(), grantedAuthorities);
    }
}
