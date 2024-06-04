package com.example.todo.security;

import com.example.todo.dto.UserDTO;
import com.example.todo.model.UserEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private final JwtUtil jwtUtil;

	public JwtAuthenticationFilter(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
		setFilterProcessesUrl("/auth/signin");
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		log.info("로그인 시도");
		try {
			UserDTO requestDto = new ObjectMapper().readValue(request.getInputStream(), UserDTO.class);

			return getAuthenticationManager().authenticate(
				new UsernamePasswordAuthenticationToken(
					requestDto.getEmail(),
					requestDto.getPassword(),
					null
				)
			);
		} catch (IOException e) {
			log.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
		log.info("로그인 성공 및 JWT 생성");
		UserEntity user = ((UserDetailsImpl) authResult.getPrincipal()).getUser();
		String email = user.getEmail();
		String token = jwtUtil.createToken(email);
		response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);

		UserDTO userDto = UserDTO.builder()
				.id(user.getId())
				.email(email)
				.token(token)
				.build();
		String userDtoJson = new ObjectMapper().writeValueAsString(userDto);

		response.getWriter().write(userDtoJson);
		response.getWriter().flush();
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
		log.info("로그인 실패");
		response.setStatus(400);
		response.getWriter().write("login failed");
	}
}