package com.watermelon.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.watermelon.model.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserDetails implements UserDetails{

	private static final long serialVersionUID = 1L;
		private Long id;
		private String username;
		private String password;
		private String firstName;
		private String lastName;
		private String email;
		private String phone;
		private String address;
		private String description;
		private String avatar;
		private boolean isActive;
		private Collection<? extends GrantedAuthority> authorities;
		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			return this.authorities;
		}
		@Override
		public String getPassword() {
			return this.password;
		}

		@Override
		public String getUsername() {
			return this.username;
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
		
		public CustomUserDetails mapUserToCustomUserDetail(User user) {
			List<GrantedAuthority> listAuthorities = new ArrayList<>();
			user.getListRoles().forEach(role -> {
						listAuthorities.add(new SimpleGrantedAuthority("ROLE_"+ role.getName()));
						role.getListPermissions().forEach( permission ->
						listAuthorities.add(new SimpleGrantedAuthority(permission.getName())));
					});
			return CustomUserDetails.builder()
					.id(user.getId())
					.username(user.getUsername())
					.password(user.getPassword())
					.email(user.getEmail())
					.firstName(user.getFirstName())
					.lastName(user.getLastName())
					.authorities(listAuthorities)
					.isActive(user.isActive())
					.build();
		}
		
}
