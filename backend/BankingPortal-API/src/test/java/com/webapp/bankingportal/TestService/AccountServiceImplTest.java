package com.webapp.bankingportal.TestService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.webapp.bankingportal.entity.Account;
import com.webapp.bankingportal.entity.User;
import com.webapp.bankingportal.exception.NotFoundException;
import com.webapp.bankingportal.exception.UnauthorizedException;
import com.webapp.bankingportal.repository.AccountRepository;
import com.webapp.bankingportal.service.AccountServiceImpl;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {
    
    @InjectMocks
    private AccountServiceImpl accountServiceImpl;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreatePIN_accountNotFound() {
        when(accountRepository.findByAccountNumber(anyString())).thenReturn(null);

        assertThrows(NotFoundException.class, () -> {
            accountServiceImpl.createPIN("123456", "password", "1234");
        });
    }

    @Test
    void testCreatePIN_invalidPassword() {
        Account account = new Account();
        User user = new User();
        user.setPassword("encodedPassword");
        account.setUser(user);

        when(accountRepository.findByAccountNumber(anyString())).thenReturn(account);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(UnauthorizedException.class, () -> {
            accountServiceImpl.createPIN("123456", "password", "1234");
        });
    }

    @Test
    void testCreatePIN_success() {
        Account account = new Account();
        User user = new User();
        user.setPassword("encodedPassword");
        account.setUser(user);

        when(accountRepository.findByAccountNumber(anyString())).thenReturn(account);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPin");

        accountServiceImpl.createPIN("123456", "password", "1234");

        verify(accountRepository, times(1)).save(account);
        assertEquals("encodedPin", account.getPin());
    }
}
