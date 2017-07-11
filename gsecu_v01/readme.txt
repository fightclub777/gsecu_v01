#gsecu_v0.1

1. 개요.
	gsecu_v0.1은 가장 기본적인 Spring Security 설정을 하고 이를 통해 가장 간단한 인증을 제공한다.

2. 목표.
	1) Spring Security 설정(v0.0 테스트 시나리오 반영).
	2) HomeController에 Logout 실제 구현.
	3) v0.0 테스트 시나리오에 따른 테스트 진행.

3. 주요내용.
	3.1. Spring Security 설정.
		1) web.xml 설정.
			1-1) Filter 추가.
				<!-- Spring SecurityFilter -->
				<filter>
					<filter-name>springSecurityFilterChain</filter-name>
					<filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
				</filter>
				<filter-mapping>
					<filter-name>springSecurityFilterChain</filter-name>
					<url-pattern>/*</url-pattern>
				</filter-mapping>
				
			1-2) security-context.xml 추가.
				<!-- Root Context -->
				<context-param>
					<param-name>contextConfigLocation</param-name>
					<param-value>classpath:spring/root-context.xml, classpath:spring/security-context.xml</param-value>
			    </context-param>
				<listener>
					<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
				</listener>

		2) security-context.xml 작성.
			2-1) 사용자 역할 지정.
				: 사용자 역할의 시작은 'ROLE_'를 반드시 써줘야 한다.
				Spring Security에 관련 부분을 커스터마이징하지 않는 이상,
				Spring Security는 역할의 시작을 'ROLE_'로 시작하는지 체크하기 때문이다.
				 
				- 최고관리자 : ROLE_H_ADMIN
				- 중간관리자 : ROLE_H_ADMIN
				- 실무관리자 : ROLE_H_ADMIN
				- 사용자 : USER
			
			2-2) 사용자 역할 설정.
				<authentication-manager>
					<authentication-provider>
						<user-service>
							<user name="ha" password="1234" authorities="ROLE_H_ADMIN"/>
							<user name="ma" password="1234" authorities="ROLE_M_ADMIN"/>
							<user name="la" password="1234" authorities="ROLE_L_ADMIN"/>
							<user name="ur" password="1234" authorities="ROLE_USER"/>
							<user name="laur" password="1234" authorities="ROLE_L_ADMIN, ROLE_USER"/>
							<!-- 
							# 'authorities'의 개념.
								: 사용자에게 역할(권한)을 부여한다. 역할은 하나일수도 다수일수도 있다.
							※ 'authorities'에 역할을 정할 때 규칙이 하나 있는데,
								반드시 'ROLE_'로 시작해야 한다는 것이다.
								'ROLE_'로 시작하지 않으려면 특정한 커스터마이징을 해야하므로 
								커스터마이징을 하지 않고 사용한다면 'ROLE_'로 시작하는 규칙을 반드시 따른다.
							-->
						</user-service>
					</authentication-provider>
				</authentication-manager>

			2-3) URL 접속 설정.
				<http auto-config="true">
					<intercept-url pattern="/admin/h*" access="hasRole('ROLE_H_ADMIN')"/>
					<intercept-url pattern="/admin/m*" access="hasAnyRole('ROLE_M_ADMIN', 'ROLE_H_ADMIN')"/>
					<intercept-url pattern="/admin/l*" access="hasAnyRole('ROLE_L_ADMIN', 'ROLE_M_ADMIN', 'ROLE_H_ADMIN')"/>
					<intercept-url pattern="/user/**" access="hasAnyRole('ROLE_USER', 'ROLE_L_ADMIN', 'ROLE_M_ADMIN', 'ROLE_H_ADMIN')"/>
					<intercept-url pattern="/**" access="permitAll"/>
				</http>
		
	3-2. HomeController에 Logout 구현.
		: Logout을 하면, 루트(/)로 이동한다.
		Logout이 올바로 이루어졌는지 확인하려면 다시 메뉴를 눌러서 로그인이 활성화되는지를 체크한다.
		
		@RequestMapping(value = "/logout", method = RequestMethod.GET)
		public String signout(HttpServletRequest request, HttpServletResponse response) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if(auth != null) {
				new SecurityContextLogoutHandler().logout(request, response, auth);
			}
			return "redirect:/";
		}
		
	