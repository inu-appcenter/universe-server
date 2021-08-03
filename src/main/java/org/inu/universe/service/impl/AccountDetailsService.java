package org.inu.universe.service.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.inu.universe.domain.Account;
import org.inu.universe.repository.AccountRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Getter
public class AccountDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String accountPk) throws UsernameNotFoundException {

        Account account = accountRepository.findById(Long.parseLong(accountPk))
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 계정입니다."));

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(String.valueOf(account.getRole()));
        authorities.add(authority);

        return new User(String.valueOf(account.getId()), "", authorities);
    }
}
