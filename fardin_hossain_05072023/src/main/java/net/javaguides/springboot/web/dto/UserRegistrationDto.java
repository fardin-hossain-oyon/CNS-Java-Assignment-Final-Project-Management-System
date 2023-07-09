package net.javaguides.springboot.web.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDto {
	private String username;
	private String email;
	private String password;
}
