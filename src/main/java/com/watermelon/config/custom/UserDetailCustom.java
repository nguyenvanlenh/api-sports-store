package com.watermelon.config.custom;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.watermelon.model.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailCustom implements UserDetails{
	private String username;
	private String password;
	private String firtName;
	private String lastName;
	private String email;
	private String phone;
	private String address;
	private java.util.List<? extends GrantedAuthority> authorities;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	
	public UserDetailCustom mapUserToUserDetail(User user) {
		List<GrantedAuthority> listAuthorities = user.getListRoles()
				.stream().map(role -> new SimpleGrantedAuthority(role.getName()))
				.collect(Collectors.toList());
		UserDetailCustom userDetail = new UserDetailCustom(
				user.getUsername(),
				user.getPassword(),
				user.getFirstName(),
				user.getLastName(), 
				user.getEmail(), 
				user.getPhone(), 
				user.getAddress(), 
				listAuthorities);
		
		return userDetail;
				
	}
	

}
